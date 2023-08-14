/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Code Technology Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.dromara.jpom.system.init;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.model.data.NodeModel;
import org.dromara.jpom.model.log.UserOperateLogV1;
import org.dromara.jpom.model.user.UserModel;
import org.dromara.jpom.permission.ClassFeature;
import org.dromara.jpom.permission.Feature;
import org.dromara.jpom.permission.MethodFeature;
import org.dromara.jpom.service.dblog.DbUserOperateLogService;
import org.dromara.jpom.service.h2db.BaseWorkspaceService;
import org.dromara.jpom.system.AopLogInterface;
import org.dromara.jpom.util.StringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 操作记录控制器
 *
 * @author bwcx_jzy
 * @since 2019/4/19
 */
@Configuration
@Slf4j
public class OperateLogController implements AopLogInterface {
    private static final ThreadLocal<CacheInfo> CACHE_INFO_THREAD_LOCAL = new ThreadLocal<>();

    private final DbUserOperateLogService dbUserOperateLogService;

    private final String[] logFilterPar = new String[]{"pwd", "pass", "password"};


    public OperateLogController(DbUserOperateLogService dbUserOperateLogService) {
        this.dbUserOperateLogService = dbUserOperateLogService;
    }

    private ClassFeature findClassFeature(Class<?> declaringClass, Class<?> targetClass) {
        Feature feature1 = declaringClass.getAnnotation(Feature.class);
        if (feature1 == null || feature1.cls() == ClassFeature.NULL) {
            feature1 = targetClass.getAnnotation(Feature.class);
        }
        return Optional.ofNullable(feature1).map(Feature::cls).orElse(null);
    }

    private CacheInfo createCacheInfo(Class<?> targetClass, Method method, HttpServletRequest request) {
        Feature feature = method.getAnnotation(Feature.class);
        if (feature == null) {
            return null;
        }
        if (!feature.log()) {
            log.debug("忽略记录日志 {}", request.getRequestURI());
            return null;
        }
        Class<?> declaringClass = method.getDeclaringClass();
        MethodFeature methodFeature = feature.method();
        if (methodFeature == MethodFeature.NULL) {
            log.error("权限分发配置错误：{}  {}", declaringClass, method.getName());
            return null;
        }
        ClassFeature classFeature = feature.cls();
        if (classFeature == ClassFeature.NULL) {
            classFeature = this.findClassFeature(declaringClass, targetClass);
            if (classFeature == null || classFeature == ClassFeature.NULL) {
                log.error("权限分发配置错误：{}  {} class not find", declaringClass, method.getName());
                return null;
            }
        }
        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setClassFeature(classFeature);
        cacheInfo.setMethodFeature(methodFeature);
        cacheInfo.setOptTime(SystemClock.now());
        cacheInfo.setLogResponse(feature.logResponse());
        //
        return cacheInfo;
    }

    @Override
    public void before(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            //
            Class<?> targetClass = joinPoint.getTarget().getClass();
            ServletRequestAttributes servletRequestAttributes = BaseServerController.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            CacheInfo cacheInfo = this.createCacheInfo(targetClass, method, request);
            if (cacheInfo == null) {
                return;
            }
            // 获取ip地址
            cacheInfo.ip = ServletUtil.getClientIP(request);
            // 获取节点
            cacheInfo.nodeModel = (NodeModel) request.getAttribute("node");
            //
            cacheInfo.userAgent = ServletUtil.getHeaderIgnoreCase(request, HttpHeaders.USER_AGENT);
            cacheInfo.workspaceId = BaseWorkspaceService.getWorkspaceId(request);
            //ServletUtil.getHeaderIgnoreCase(request, Const.WORKSPACE_ID_REQ_HEADER);
            //
            Map<String, Object> allData = this.buildRequestParam(request);
            //
            cacheInfo.dataId = StrUtil.toStringOrNull(allData.get("id"));
            allData.put("request_url", request.getRequestURI());
            //
            cacheInfo.reqData = JSONObject.toJSONString(allData);
            //
            if (cacheInfo.methodFeature == MethodFeature.DEL) {
                // 删除数据 提前查询出操作到数据相关信息
                cacheInfo.optDataNameMap = dbUserOperateLogService.buildDataMsg(cacheInfo.classFeature, cacheInfo.dataId, cacheInfo.nodeModel == null ? null : cacheInfo.nodeModel.getId());
            }
            CACHE_INFO_THREAD_LOCAL.set(cacheInfo);
        }
    }

    private Map<String, Object> buildRequestParam(HttpServletRequest request) {
        Map<String, String> map = ServletUtil.getParamMap(request);
        // 过滤密码字段
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            if (StrUtil.containsAnyIgnoreCase(key, logFilterPar)) {
                entry.setValue("***");
            }
        }
        //
        Map<String, Object> allData = new HashMap<>(30);
        String body = ServletFileUpload.isMultipartContent(request) ? null : ServletUtil.getBody(request);
        if (StrUtil.isNotEmpty(body)) {
            JSONValidator.Type type = StringUtil.validatorJson(body);
            if (type == null || type == JSONValidator.Type.Value) {
                allData.put("bodyData", body);
            } else if (type == JSONValidator.Type.Object) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                allData.putAll(jsonObject);
                //
            } else if (type == JSONValidator.Type.Array) {
                allData.put("bodyData", JSON.toJSON(body));
            }
        }
        allData.putAll(map);
        return allData;
    }

    @Override
    public void afterReturning(Object value) {
        try {
            CacheInfo cacheInfo = CACHE_INFO_THREAD_LOCAL.get();
            if (cacheInfo == null || cacheInfo.methodFeature == MethodFeature.LIST) {
                return;
            }
            if (cacheInfo.classFeature == null || cacheInfo.methodFeature == null) {
                log.warn("权限功能没有配置正确 {}", cacheInfo);
                return;
            }
            UserModel userModel = BaseServerController.getUserByThreadLocal();
            // 没有对应的用户
            if (userModel == null) {
                return;
            }
            this.log(userModel, value, cacheInfo);
        } finally {
            CACHE_INFO_THREAD_LOCAL.remove();
        }
    }

    /**
     * 记录操作日志
     *
     * @param userModel 用户
     * @param value     返回执行
     * @param cacheInfo 请求信息
     */
    public void log(UserModel userModel, Object value, CacheInfo cacheInfo) {
        UserOperateLogV1 userOperateLogV1 = new UserOperateLogV1();
        userOperateLogV1.setWorkspaceId(cacheInfo.workspaceId);
        userOperateLogV1.setClassFeature(cacheInfo.classFeature.name());
        userOperateLogV1.setMethodFeature(cacheInfo.methodFeature.name());
        userOperateLogV1.setDataId(cacheInfo.dataId);
        userOperateLogV1.setUserId(userModel.getId());
        userOperateLogV1.setIp(cacheInfo.ip);
        userOperateLogV1.setUserAgent(cacheInfo.userAgent);
        userOperateLogV1.setReqData(cacheInfo.reqData);
        userOperateLogV1.setOptTime(ObjectUtil.defaultIfNull(cacheInfo.optTime, SystemClock.now()));
        if (value != null) {
            // 解析结果
            if (value instanceof Throwable) {
                // 发生异常
                Throwable throwable = (Throwable) value;
                userOperateLogV1.setResultMsg(ExceptionUtil.stacktraceToString(throwable));
                userOperateLogV1.setOptStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else {
                String json = value.toString();
                userOperateLogV1.setResultMsg(json);
                try {
                    JsonMessage<?> jsonMessage = JSONObject.parseObject(json, JsonMessage.class);
                    int code = jsonMessage.getCode();
                    userOperateLogV1.setOptStatus(code);
                } catch (Exception ignored) {
                }
            }
            // 判断是否记录响应日志
            Boolean logResponse = cacheInfo.getLogResponse();
            if (logResponse != null && !logResponse) {
                userOperateLogV1.setResultMsg(new cn.hutool.json.JSONObject().putOpt("hide", "*****").toString());
            }
        }
        //
        if (cacheInfo.nodeModel != null) {
            userOperateLogV1.setNodeId(cacheInfo.nodeModel.getId());
            if (StrUtil.isEmpty(cacheInfo.workspaceId)) {
                userOperateLogV1.setWorkspaceId(cacheInfo.nodeModel.getWorkspaceId());
            }
        }
        //
        try {
            BaseServerController.resetInfo(UserModel.EMPTY);
            dbUserOperateLogService.insert(userOperateLogV1, cacheInfo);
        } finally {
            BaseServerController.removeEmpty();
        }
    }


    /**
     * 修改执行结果
     *
     * @param reqId 请求id
     * @param val   结果
     */
    public void updateLog(String reqId, String val) {
        Entity entity = new Entity();
        entity.set("resultMsg", val);
        try {
            JsonMessage<?> jsonMessage = JSONObject.parseObject(val, JsonMessage.class);
            entity.set("optStatus", jsonMessage.getCode());
        } catch (Exception ignored) {
        }
        //
        Entity where = new Entity();
        where.set("reqId", reqId);
        dbUserOperateLogService.update(entity, where);
    }

    /**
     * 临时缓存
     */
    @Data
    public static class CacheInfo {
        private Long optTime;
        private String workspaceId;
        private ClassFeature classFeature;
        private MethodFeature methodFeature;
        private String ip;
        private NodeModel nodeModel;
        private String dataId;
        private String userAgent;
        private String reqData;
        private Boolean logResponse;
        /**
         * 操作到数据到名称相关 map
         */
        private Map<String, Object> optDataNameMap;
    }
}
