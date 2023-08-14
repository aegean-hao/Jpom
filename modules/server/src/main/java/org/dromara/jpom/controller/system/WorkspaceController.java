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
package org.dromara.jpom.controller.system;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.Const;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.validator.ValidatorItem;
import org.dromara.jpom.common.validator.ValidatorRule;
import org.dromara.jpom.db.TableName;
import org.dromara.jpom.model.BaseWorkspaceModel;
import org.dromara.jpom.model.PageResultDto;
import org.dromara.jpom.model.data.WorkspaceModel;
import org.dromara.jpom.model.log.UserOperateLogV1;
import org.dromara.jpom.permission.ClassFeature;
import org.dromara.jpom.permission.Feature;
import org.dromara.jpom.permission.MethodFeature;
import org.dromara.jpom.permission.SystemPermission;
import org.dromara.jpom.service.system.SystemParametersServer;
import org.dromara.jpom.service.system.WorkspaceService;
import org.dromara.jpom.service.user.UserBindWorkspaceService;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * @author bwcx_jzy
 * @since 2021/12/3
 */
@RestController
@Feature(cls = ClassFeature.SYSTEM_WORKSPACE)
@RequestMapping(value = "/system/workspace/")
@SystemPermission
public class WorkspaceController extends BaseServerController {

    private final WorkspaceService workspaceService;
    private final UserBindWorkspaceService userBindWorkspaceService;
    private final SystemParametersServer systemParametersServer;

    public WorkspaceController(WorkspaceService workspaceService,
                               UserBindWorkspaceService userBindWorkspaceService,
                               SystemParametersServer systemParametersServer) {
        this.workspaceService = workspaceService;
        this.userBindWorkspaceService = userBindWorkspaceService;
        this.systemParametersServer = systemParametersServer;
    }

    /**
     * 编辑工作空间
     *
     * @param name        工作空间名称
     * @param description 描述
     * @return json
     */
    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public JsonMessage<Object> create(String id, @ValidatorItem String name, @ValidatorItem String description, String group) {
        this.checkInfo(id, name);
        //
        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setName(name);
        workspaceModel.setDescription(description);
        workspaceModel.setGroup(group);
        if (StrUtil.isEmpty(id)) {
            // 创建
            workspaceService.insert(workspaceModel);
        } else {
            workspaceModel.setId(id);
            workspaceService.updateById(workspaceModel);
        }
        return JsonMessage.success("操作成功");
    }

    private void checkInfo(String id, String name) {
        Entity entity = Entity.create();
        entity.set("name", name);
        if (StrUtil.isNotEmpty(id)) {
            entity.set("id", StrUtil.format(" <> {}", id));
        }
        boolean exists = workspaceService.exists(entity);
        Assert.state(!exists, "对应的工作空间名称已经存在啦");
    }

    /**
     * 工作空间分页列表
     *
     * @return json
     */
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public JsonMessage<PageResultDto<WorkspaceModel>> list() {
        PageResultDto<WorkspaceModel> listPage = workspaceService.listPage(getRequest());
        return JsonMessage.success("", listPage);
    }

    /**
     * 查询所有的分组
     *
     * @return list
     */
    @GetMapping(value = "list-group-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public JsonMessage<List<String>> listGroupAll() {
        List<String> listGroup = workspaceService.listGroup();
        return JsonMessage.success("", listGroup);
    }

    /**
     * 查询工作空间列表
     *
     * @return json
     */
    @GetMapping(value = "/list_all")
    @Feature(method = MethodFeature.LIST)
    public JsonMessage<List<WorkspaceModel>> listAll() {
        List<WorkspaceModel> list = workspaceService.list();
        return JsonMessage.success("", list);
    }

    /**
     * 删除工作空间
     *
     * @param id 工作空间 ID
     * @return json
     */
    @GetMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.DEL)
    @SystemPermission(superUser = true)
    public JsonMessage<String> delete(@ValidatorItem(value = ValidatorRule.NOT_BLANK, msg = "数据 id 不能为空") String id) {
        //
        Assert.state(!StrUtil.equals(id, Const.WORKSPACE_DEFAULT_ID), "不能删除默认工作空间");
        // 判断是否存在关联数据
        Set<Class<?>> classes = BaseWorkspaceModel.allClass();
        StringBuilder autoDelete = new StringBuilder(StrUtil.EMPTY);
        for (Class<?> aClass : classes) {
            TableName tableName = aClass.getAnnotation(TableName.class);
            if (tableName == null) {
                continue;
            }
            if (aClass == UserOperateLogV1.class) {
                // 用户操作日志
                String sql = "delete from " + tableName.value() + " where workspaceId=?";
                int execute = workspaceService.execute(sql, id);
                if (execute > 0) {
                    autoDelete.append(StrUtil.format(" 自动删除 {} 表中数据 {} 条数据", tableName.value(), execute));
                }
                continue;
            }
            String sql = "select  count(1) as cnt from " + tableName.value() + " where workspaceId=?";
            List<Entity> query = workspaceService.query(sql, id);
            Entity first = CollUtil.getFirst(query);
            if (first != null) {
                Assert.notEmpty(first, "没有对应的信息");
                Integer cnt = first.getInt("cnt");
                Assert.state(cnt == null || cnt <= 0, "当前工作空间下还存在关联数据：" + tableName.name());
            }
        }
        // 判断用户绑定关系
        boolean workspace = userBindWorkspaceService.existsWorkspace(id);
        Assert.state(!workspace, "当前工作空间下还绑定着用户信息");
        // 删除缓存
        String menusConfigKey = StrUtil.format("menus_config_{}", id);
        systemParametersServer.delByKey(menusConfigKey);
        String whitelistConfigKey = StrUtil.format("node_whitelist_{}", id);
        systemParametersServer.delByKey(whitelistConfigKey);
        systemParametersServer.delByKey(StrUtil.format("node_config_{}", id));
        // 删除信息
        workspaceService.delByKey(id);
        return new JsonMessage<>(200, "删除成功 " + autoDelete);
    }

    /**
     * 加载菜单配置
     *
     * @return json
     */
    @RequestMapping(value = "get_menus_config", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public JsonMessage<JSONObject> getMenusConfig(String workspaceId) {
        WorkspaceModel workspaceModel = workspaceService.getByKey(workspaceId);
        Assert.notNull(workspaceModel, "不存在对应的工作空间");
        JSONObject config = systemParametersServer.getConfigDefNewInstance(StrUtil.format("menus_config_{}", workspaceId), JSONObject.class);
        //"classpath:/menus/index.json"
        //"classpath:/menus/node-index.json"
        config.put("serverMenus", this.readMenusJson("classpath:/menus/index.json"));
        config.put("nodeMenus", this.readMenusJson("classpath:/menus/node-index.json"));
        return JsonMessage.success("", config);
    }

    @PostMapping(value = "save_menus_config.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public JsonMessage<Object> saveMenusConfig(String serverMenuKeys, String nodeMenuKeys, String workspaceId) {
        WorkspaceModel workspaceModel = workspaceService.getByKey(workspaceId);
        Assert.notNull(workspaceModel, "不存在对应的工作空间");
        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nodeMenuKeys", StrUtil.splitTrim(nodeMenuKeys, StrUtil.COMMA));
        jsonObject.put("serverMenuKeys", StrUtil.splitTrim(serverMenuKeys, StrUtil.COMMA));
        String format = StrUtil.format("menus_config_{}", workspaceId);
        systemParametersServer.upsert(format, jsonObject, format);
        //
        return JsonMessage.success("修改成功");
    }

    private JSONArray readMenusJson(String path) {
        // 菜单
        InputStream inputStream = ResourceUtil.getStream(path);
        String json = IoUtil.read(inputStream, CharsetUtil.CHARSET_UTF_8);
        return JSONArray.parseArray(json);
    }
}
