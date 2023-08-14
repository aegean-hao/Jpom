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


import cn.hutool.core.util.StrUtil;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.forward.NodeForward;
import org.dromara.jpom.common.forward.NodeUrl;
import org.dromara.jpom.common.validator.ValidatorItem;
import org.dromara.jpom.model.PageResultDto;
import org.dromara.jpom.model.data.NodeModel;
import org.dromara.jpom.model.node.NodeScriptExecuteLogCacheModel;
import org.dromara.jpom.permission.ClassFeature;
import org.dromara.jpom.permission.Feature;
import org.dromara.jpom.permission.MethodFeature;
import org.dromara.jpom.service.node.script.NodeScriptExecuteLogServer;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bwcx_jzy
 * @since 2021/12/24
 */
@RestController
@RequestMapping(value = "/node/script_log")
@Feature(cls = ClassFeature.NODE_SCRIPT_LOG)
public class NodeScriptLogController extends BaseServerController {

    private final NodeScriptExecuteLogServer nodeScriptExecuteLogServer;

    public NodeScriptLogController(NodeScriptExecuteLogServer nodeScriptExecuteLogServer) {
        this.nodeScriptExecuteLogServer = nodeScriptExecuteLogServer;
    }

    /**
     * get script log list
     *
     * @return json
     */
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<PageResultDto<NodeScriptExecuteLogCacheModel>> scriptList() {
        PageResultDto<NodeScriptExecuteLogCacheModel> pageResultDto = nodeScriptExecuteLogServer.listPageNode(getRequest());
        return JsonMessage.success("", pageResultDto);
    }

    /**
     * 查日志
     *
     * @return json
     */
    @RequestMapping(value = "log", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public String log() {
        NodeModel node = getNode();
        return NodeForward.request(node, getRequest(), NodeUrl.SCRIPT_LOG).toString();
    }

    /**
     * 删除日志
     *
     * @param id        模版ID
     * @param executeId 日志ID
     * @return json
     */
    @RequestMapping(value = "del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.DEL)
    public JsonMessage<Object> del(@ValidatorItem String id, String executeId, HttpServletRequest request) {
        NodeModel node = getNode();
        NodeScriptExecuteLogCacheModel executeLogModel = nodeScriptExecuteLogServer.getByKey(executeId, request);
        Assert.notNull(executeLogModel, "没有对应的执行日志");
        Assert.state(StrUtil.equals(id, executeLogModel.getScriptId()), "数据关联的id 不一致");
//        NodeScriptExecuteLogCacheModel nodeScriptExecuteLogCacheModel = new NodeScriptExecuteLogCacheModel();
//        nodeScriptExecuteLogCacheModel.setId(executeId);
//        nodeScriptExecuteLogCacheModel.setScriptId(id);
//        nodeScriptExecuteLogCacheModel.setNodeId(node.getId());
//        NodeScriptExecuteLogCacheModel executeLogModel = nodeScriptExecuteLogServer.queryByBean(nodeScriptExecuteLogCacheModel);

        JsonMessage<Object> jsonMessage = NodeForward.request(node, request, NodeUrl.SCRIPT_DEL_LOG);
        if (jsonMessage.success()) {
            nodeScriptExecuteLogServer.delByKey(executeId);
        }
        return jsonMessage;
    }
}
