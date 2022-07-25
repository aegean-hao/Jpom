package io.jpom.controller.openapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.jiangzeyin.common.JsonMessage;
import cn.jiangzeyin.common.spring.SpringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.jpom.common.BaseJpomController;
import io.jpom.common.BaseServerController;
import io.jpom.common.ServerOpenApi;
import io.jpom.common.interceptor.NotLogin;
import io.jpom.model.data.CommandModel;
import io.jpom.model.data.UserModel;
import io.jpom.model.script.ScriptExecuteLogModel;
import io.jpom.model.script.ScriptModel;
import io.jpom.service.script.ScriptExecuteLogServer;
import io.jpom.service.script.ScriptServer;
import io.jpom.service.user.TriggerTokenLogServer;
import io.jpom.socket.ScriptProcessBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bwcx_jzy
 * @since 2022/7/25
 */
@RestController
@NotLogin
@Slf4j
public class ServerScriptTriggerApiController extends BaseJpomController {

    private final ScriptServer scriptServer;
    private final ScriptExecuteLogServer scriptExecuteLogServer;
    private final TriggerTokenLogServer triggerTokenLogServer;

    public ServerScriptTriggerApiController(ScriptServer scriptServer,
                                            ScriptExecuteLogServer scriptExecuteLogServer,
                                            TriggerTokenLogServer triggerTokenLogServer) {
        this.scriptServer = scriptServer;
        this.scriptExecuteLogServer = scriptExecuteLogServer;
        this.triggerTokenLogServer = triggerTokenLogServer;
    }

    /**
     * 执行脚本
     *
     * @param id    构建ID
     * @param token 构建的token
     * @return json
     */
    @RequestMapping(value = ServerOpenApi.SERVER_SCRIPT_TRIGGER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String trigger2(@PathVariable String id, @PathVariable String token) {
        ScriptModel item = scriptServer.getByKey(id);
        Assert.notNull(item, "没有对应数据");
        Assert.state(StrUtil.equals(token, item.getTriggerToken()), "触发token错误,或者已经失效");
        //
        UserModel userModel = triggerTokenLogServer.getUserByToken(token, scriptServer.typeName());
        //
        Assert.notNull(userModel, "触发token错误,或者已经失效:-1");

        try {
            BaseServerController.resetInfo(userModel);
            // 创建记录
            ScriptExecuteLogModel nodeScriptExecLogModel = scriptExecuteLogServer.create(item, 2);
            // 执行
            ScriptProcessBuilder.create(item, nodeScriptExecLogModel.getId(), item.getDefArgs());
            //
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("logId", nodeScriptExecLogModel.getId());
            return JsonMessage.getString(200, "开始执行", jsonObject);
        } catch (Exception e) {
            log.error("触发自动执行服务器脚本异常", e);
            return JsonMessage.getString(500, "执行异常：" + e.getMessage());
        }
    }


    /**
     * 构建触发器
     * <p>
     * 参数 <code>[
     * {
     * "id":"1",
     * "token":"a"
     * }
     * ]</code>
     * <p>
     * 响应 <code>[
     * {
     * "id":"1",
     * "token":"a",
     * "logId":"1",
     * "msg":"没有对应数据",
     * }
     * ]</code>
     *
     * @return json
     */
    @PostMapping(value = ServerOpenApi.SERVER_SCRIPT_TRIGGER_BATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public String triggerBatch() {
        try {
            String body = ServletUtil.getBody(getRequest());
            JSONArray jsonArray = JSONArray.parseArray(body);
            List<Object> collect = jsonArray.stream().peek(o -> {
                JSONObject jsonObject = (JSONObject) o;
                String id = jsonObject.getString("id");
                String token = jsonObject.getString("token");
                ScriptModel item = scriptServer.getByKey(id);
                if (item == null) {
                    jsonObject.put("msg", "没有对应数据");
                    return;
                }
                UserModel userModel = triggerTokenLogServer.getUserByToken(token, scriptServer.typeName());
                if (userModel == null) {
                    jsonObject.put("msg", "对应的用户不存在,触发器已失效");
                    return;
                }
                //
                if (!StrUtil.equals(token, item.getTriggerToken())) {
                    jsonObject.put("msg", "触发token错误,或者已经失效");
                    return;
                }
                BaseServerController.resetInfo(userModel);
                try {
                    // 创建记录
                    ScriptExecuteLogModel nodeScriptExecLogModel = scriptExecuteLogServer.create(item, 2);
                    // 执行
                    ScriptProcessBuilder.create(item, nodeScriptExecLogModel.getId(), item.getDefArgs());
                    jsonObject.put("logId", nodeScriptExecLogModel.getId());
                } catch (Exception e) {
                    log.error("触发自动执行命令模版异常", e);
                    jsonObject.put("msg", "执行异常：" + e.getMessage());
                }
                //
            }).collect(Collectors.toList());
            return JsonMessage.getString(200, "触发成功", collect);
        } catch (Exception e) {
            log.error("服务端脚本批量触发异常", e);
            return JsonMessage.getString(500, "触发异常", e.getMessage());
        }
    }
}