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
package org.dromara.jpom.controller.node.script;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import org.dromara.jpom.common.*;
import org.dromara.jpom.common.forward.NodeForward;
import org.dromara.jpom.common.forward.NodeUrl;
import org.dromara.jpom.common.validator.ValidatorItem;
import org.dromara.jpom.model.PageResultDto;
import org.dromara.jpom.model.data.NodeModel;
import org.dromara.jpom.model.node.NodeScriptCacheModel;
import org.dromara.jpom.model.user.UserModel;
import org.dromara.jpom.permission.*;
import org.dromara.jpom.service.node.script.NodeScriptExecuteLogServer;
import org.dromara.jpom.service.node.script.NodeScriptServer;
import org.dromara.jpom.service.user.TriggerTokenLogServer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 脚本管理
 *
 * @author bwcx_jzy
 * @since 2019/4/24
 */
@RestController
@RequestMapping(value = "/node/script")
@Feature(cls = ClassFeature.NODE_SCRIPT)
@NodeDataPermission(cls = NodeScriptServer.class)
public class NodeScriptController extends BaseServerController {

    private final NodeScriptServer nodeScriptServer;
    private final NodeScriptExecuteLogServer nodeScriptExecuteLogServer;
    private final TriggerTokenLogServer triggerTokenLogServer;

    public NodeScriptController(NodeScriptServer nodeScriptServer,
                                NodeScriptExecuteLogServer nodeScriptExecuteLogServer,
                                TriggerTokenLogServer triggerTokenLogServer) {
        this.nodeScriptServer = nodeScriptServer;
        this.nodeScriptExecuteLogServer = nodeScriptExecuteLogServer;
        this.triggerTokenLogServer = triggerTokenLogServer;
    }

    /**
     * get script list
     *
     * @return json
     * @author Hotstrip
     */
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<PageResultDto<NodeScriptCacheModel>> scriptList(HttpServletRequest request) {
        PageResultDto<NodeScriptCacheModel> pageResultDto = nodeScriptServer.listPageNode(request);
        return JsonMessage.success("success", pageResultDto);
    }

    /**
     * load node script list
     * 加载节点脚本列表
     *
     * @return json
     * @author Hotstrip
     */
    @PostMapping(value = "list_all", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<PageResultDto<NodeScriptCacheModel>> listAll(HttpServletRequest request) {
        PageResultDto<NodeScriptCacheModel> modelPageResultDto = nodeScriptServer.listPage(request);
        return JsonMessage.success("", modelPageResultDto);
    }


    @GetMapping(value = "item.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public String item(HttpServletRequest request) {
        return NodeForward.request(getNode(), request, NodeUrl.Script_Item).toString();
    }

    /**
     * 保存脚本
     *
     * @return json
     */
    @RequestMapping(value = "save.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public JsonMessage<Object> save(String autoExecCron, HttpServletRequest request) {
        NodeModel node = getNode();
        this.checkCron(autoExecCron);
        JsonMessage<Object> jsonMessage = NodeForward.request(node, request, NodeUrl.Script_Save, new String[]{}, "nodeId", node.getId());
        if (jsonMessage.success()) {
            nodeScriptServer.syncNode(node);
        }
        return jsonMessage;
    }

    @RequestMapping(value = "del.json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.DEL)
    public JsonMessage<Object> del(@ValidatorItem String id, HttpServletRequest request) {
        NodeModel node = getNode();
        JsonMessage<Object> requestData = NodeForward.request(node, request, NodeUrl.Script_Del);
        if (requestData.success()) {
            nodeScriptServer.syncNode(node);
            // 删除日志
            nodeScriptExecuteLogServer.delCache(id, node.getId(), request);
        }
        return requestData;
    }

    /**
     * 同步脚本模版
     *
     * @return json
     */
    @GetMapping(value = "sync", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.DEL)
    public JsonMessage<Object> syncProject(HttpServletRequest request) {
        //
        NodeModel node = getNode();
        int cache = nodeScriptServer.delCache(node.getId(), request);
        String msg = nodeScriptServer.syncExecuteNode(node);
        return JsonMessage.success("主动清除 " + cache + StrUtil.SPACE + msg);
    }

    /**
     * 删除节点缓存的所有脚本模版
     *
     * @return json
     */
    @GetMapping(value = "clear_all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SystemPermission(superUser = true)
    @Feature(method = MethodFeature.DEL)
    public JsonMessage<Object> clearAll() {
        Entity where = Entity.create();
        where.set("id", " <> id");
        int del = nodeScriptServer.del(where);
        return JsonMessage.success("成功删除" + del + "条脚本模版缓存");
    }

    /**
     * get a trigger url
     *
     * @param id id
     * @return json
     */
    @RequestMapping(value = "trigger-url", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.EDIT)
    public JsonMessage<Map<String, String>> getTriggerUrl(String id, String rest, HttpServletRequest request) {
        NodeScriptCacheModel item = nodeScriptServer.getByKeyAndGlobal(id, request);
        UserModel user = getUser();
        NodeScriptCacheModel updateInfo;
        if (StrUtil.isEmpty(item.getTriggerToken()) || StrUtil.isNotEmpty(rest)) {
            updateInfo = new NodeScriptCacheModel();
            updateInfo.setId(id);
            updateInfo.setTriggerToken(triggerTokenLogServer.restToken(item.getTriggerToken(), nodeScriptServer.typeName(),
                item.getId(), user.getId()));
            nodeScriptServer.updateById(updateInfo);
        } else {
            updateInfo = item;
        }
        Map<String, String> map = this.getBuildToken(updateInfo);
        return JsonMessage.success(StrUtil.isEmpty(rest) ? "ok" : "重置成功", map);
    }

    private Map<String, String> getBuildToken(NodeScriptCacheModel item) {
        String contextPath = UrlRedirectUtil.getHeaderProxyPath(getRequest(), ServerConst.PROXY_PATH);
        String url = ServerOpenApi.NODE_SCRIPT_TRIGGER_URL.
            replace("{id}", item.getId()).
            replace("{token}", item.getTriggerToken());
        String triggerBuildUrl = String.format("/%s/%s", contextPath, url);
        Map<String, String> map = new HashMap<>(10);
        map.put("triggerUrl", FileUtil.normalize(triggerBuildUrl));
        String batchTriggerBuildUrl = String.format("/%s/%s", contextPath, ServerOpenApi.NODE_SCRIPT_TRIGGER_BATCH);
        map.put("batchTriggerUrl", FileUtil.normalize(batchTriggerBuildUrl));

        map.put("id", item.getId());
        map.put("token", item.getTriggerToken());
        return map;
    }
}
