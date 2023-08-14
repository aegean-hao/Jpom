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
package org.dromara.jpom.service.node.script;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.ServerConst;
import org.dromara.jpom.common.forward.NodeForward;
import org.dromara.jpom.common.forward.NodeUrl;
import org.dromara.jpom.model.BaseDbModel;
import org.dromara.jpom.model.data.NodeModel;
import org.dromara.jpom.model.data.WorkspaceModel;
import org.dromara.jpom.model.node.NodeScriptExecuteLogCacheModel;
import org.dromara.jpom.model.user.UserModel;
import org.dromara.jpom.service.h2db.BaseNodeService;
import org.dromara.jpom.service.node.NodeService;
import org.dromara.jpom.service.system.WorkspaceService;
import org.dromara.jpom.system.AgentException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 脚本默认执行记录
 *
 * @author bwcx_jzy
 * @since 2021/12/12
 */
@Service
@Slf4j
public class NodeScriptExecuteLogServer extends BaseNodeService<NodeScriptExecuteLogCacheModel> {

    public NodeScriptExecuteLogServer(NodeService nodeService,
                                      WorkspaceService workspaceService) {
        super(nodeService, workspaceService, "脚本模版日志");
    }

    @Override
    protected String[] clearTimeColumns() {
        return new String[]{"createTimeMillis"};
    }

    @Override
    public JSONObject getItem(NodeModel nodeModel, String id) {
        return null;
    }

    @Override
    public JSONArray getLitDataArray(NodeModel nodeModel) {
        JsonMessage<?> jsonMessage = NodeForward.request(nodeModel, NodeUrl.SCRIPT_PULL_EXEC_LOG, "pullCount", 100);
        if (!jsonMessage.success()) {
            throw new AgentException(jsonMessage.toString());
        }
        Object data = jsonMessage.getData();
        //
        JSONArray jsonArray = (JSONArray) JSON.toJSON(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            jsonObject.put("nodeId", nodeModel.getId());
            // 自动
            if (!jsonObject.containsKey("triggerExecType")) {
                jsonObject.put("triggerExecType", 1);
            }
        }
        return jsonArray;

    }

    @Override
    public void syncAllNode() {
        //
    }

    /**
     * 同步执行 同步节点信息(增量)
     *
     * @param nodeModel 节点信息
     * @return json
     */
    public Collection<String> syncExecuteNodeInc(NodeModel nodeModel) {
        String nodeModelName = nodeModel.getName();
        if (!nodeModel.isOpenStatus()) {
            log.debug("{} 节点未启用", nodeModelName);
            return null;
        }
        try {
            JSONArray jsonArray = this.getLitDataArray(nodeModel);
            if (CollUtil.isEmpty(jsonArray)) {
                //
                return null;
            }
            //
            List<NodeScriptExecuteLogCacheModel> models = jsonArray.toJavaList(this.tClass).stream()
                .filter(item -> {
                    if (StrUtil.equals(item.getWorkspaceId(), ServerConst.WORKSPACE_GLOBAL)) {
                        return true;
                    }
                    // 检查对应的工作空间 是否存在
                    return workspaceService.exists(new WorkspaceModel(item.getWorkspaceId()));
                })
                .filter(item -> {
                    if (StrUtil.equals(item.getWorkspaceId(), ServerConst.WORKSPACE_GLOBAL)) {
                        return true;
                    }
                    // 避免重复同步
                    return StrUtil.equals(nodeModel.getWorkspaceId(), item.getWorkspaceId());
                })
                .collect(Collectors.toList());
            // 设置 临时缓存，便于放行检查
            BaseServerController.resetInfo(UserModel.EMPTY);
            //
            models.forEach(NodeScriptExecuteLogServer.super::upsert);
            String format = StrUtil.format(
                "{} 节点拉取到 {} 个执行记录,更新 {} 个执行记录",
                nodeModelName, CollUtil.size(jsonArray),
                CollUtil.size(models));
            log.debug(format);
            return models.stream().map(BaseDbModel::getId).collect(Collectors.toList());
        } catch (Exception e) {
            this.checkException(e, nodeModelName);
            return null;
        } finally {
            BaseServerController.removeEmpty();
        }
    }

    @Override
    protected void executeClearImpl(int h2DbLogStorageCount) {
        super.autoLoopClear("createTimeMillis", h2DbLogStorageCount,
            null,
            executeLogModel -> {
                try {
                    NodeModel nodeModel = nodeService.getByKey(executeLogModel.getNodeId());
                    JsonMessage<Object> jsonMessage = NodeForward.request(nodeModel, NodeUrl.SCRIPT_DEL_LOG,
                        "id", executeLogModel.getScriptId(), "executeId", executeLogModel.getId());
                    if (!jsonMessage.success()) {
                        log.warn("{} {} {}", executeLogModel.getNodeId(), executeLogModel.getScriptName(), jsonMessage);
                        return false;
                    }
                    return true;
                } catch (Exception e) {
                    log.error("自动清除数据错误 {} {}", executeLogModel.getNodeId(), executeLogModel.getScriptName(), e);
                    return false;
                }
            });
    }
}
