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
package org.dromara.jpom.func.openapi.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.JpomManifest;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.ServerOpenApi;
import org.dromara.jpom.common.interceptor.NotLogin;
import org.dromara.jpom.common.validator.ValidatorItem;
import org.dromara.jpom.common.validator.ValidatorRule;
import org.dromara.jpom.func.assets.model.MachineNodeModel;
import org.dromara.jpom.model.data.WorkspaceModel;
import org.dromara.jpom.model.user.UserModel;
import org.dromara.jpom.service.node.NodeService;
import org.dromara.jpom.service.system.WorkspaceService;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点管理
 *
 * @author bwcx_jzy
 * @since 2019/8/5
 */
@RestController
@Slf4j
public class NodeInfoController extends BaseServerController {

    private static final Map<String, JSONObject> CACHE_RECEIVE_PUSH = new HashMap<>();

    private final NodeService nodeService;
    private final WorkspaceService workspaceService;

    public NodeInfoController(NodeService nodeService,
                              WorkspaceService workspaceService) {
        this.nodeService = nodeService;
        this.workspaceService = workspaceService;
    }

    /**
     * 接收节点推送的信息
     * <p>
     * yum install -y wget && wget -O install.sh https://jpom.top/docs/install.sh && bash install.sh Agent jdk
     * --auto-push-to-server http://127.0.0.1:3000/api/node/receive_push?token=462a47b8fba8da1f824370bb9fcdc01aa1a0fe20&workspaceId=DEFAULT
     *
     * @return json
     */
    @RequestMapping(value = ServerOpenApi.RECEIVE_PUSH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @NotLogin
    public JsonMessage<JSONObject> receivePush(@ValidatorItem(msg = "token empty") String token,
                                               @ValidatorItem(msg = "ips empty") String ips,
                                               @ValidatorItem(msg = "loginName empty") String loginName,
                                               @ValidatorItem(msg = "loginPwd empty") String loginPwd,
                                               @ValidatorItem(msg = "workspaceId empty") String workspaceId,
                                               @ValidatorItem(value = ValidatorRule.NUMBERS, msg = "port error") int port,
                                               String ping) {
        Assert.state(StrUtil.equals(token, JpomManifest.getInstance().randomIdSign()), "token error");
        boolean exists = workspaceService.exists(new WorkspaceModel(workspaceId));
        Assert.state(exists, "workspaceId error");
        String sha1Id = SecureUtil.sha1(ips);
        //
        List<String> ipsList = StrUtil.split(ips, StrUtil.COMMA);
        String clientIp = getClientIP();
        if (!ipsList.contains(clientIp)) {
            ipsList.add(clientIp);
        }
        List<String> canUseIps = ipsList.stream()
            .filter(s -> this.testIpPort(s, ping))
            .collect(Collectors.toList());
        List<MachineNodeModel> canUseNode = canUseIps.stream().map(s -> {
            MachineNodeModel model = this.createMachineNodeModel(s, loginName, loginPwd, port);
            try {
                machineNodeServer.testNode(model);
            } catch (Exception e) {
                log.warn("测试结果：{} {}", model.getJpomUrl(), e.getMessage());
                return null;
            }
            return model;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // 只返回能通的 IP
        canUseIps = canUseNode.stream().map(MachineNodeModel::getName).collect(Collectors.toList());
        // 标记为系统操作
        BaseServerController.resetInfo(UserModel.EMPTY);
        //
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allIp", ipsList);
        jsonObject.put("canUseIp", canUseIps);
        jsonObject.put("port", port);
        jsonObject.put("id", sha1Id);
        jsonObject.put("canUseNode", canUseNode);
        //
        for (MachineNodeModel nodeModel : canUseNode) {
            MachineNodeModel existsMachine = machineNodeServer.getByUrl(nodeModel.getJpomUrl());
            if (existsMachine != null) {
                if (nodeService.existsNode2(workspaceId, existsMachine.getId())) {
                    // 存在
                    jsonObject.put("type", "exists");
                } else {
                    // 自动同步
                    jsonObject.put("type", "success");
                    machineNodeServer.insertNode(existsMachine, workspaceId);
                }
                break;
            }
        }
        if (!jsonObject.containsKey("type")) {
            int size1 = CollUtil.size(canUseNode);
            if (size1 == 1) {
                // 只有一个 ip 可以使用,添加插件端
                BaseServerController.resetInfo(UserModel.EMPTY);
                MachineNodeModel first = CollUtil.getFirst(canUseNode);
                machineNodeServer.insertAndNode(first, workspaceId);
                jsonObject.put("type", "success");
            } else {
                jsonObject.put("type", size1 == 0 ? "canUseIpEmpty" : "multiIp");
            }
        }
        CACHE_RECEIVE_PUSH.put(sha1Id, jsonObject);
        return JsonMessage.success("done", jsonObject);
    }

    /**
     * 查询所有缓存
     *
     * @return list
     */
    public static Collection<JSONObject> listReceiveCache(String removeId) {
        if (StrUtil.isNotEmpty(removeId)) {
            CACHE_RECEIVE_PUSH.remove(removeId);
        }
        return CACHE_RECEIVE_PUSH.values();
    }

    public static JSONObject getReceiveCache(String id) {
        return CACHE_RECEIVE_PUSH.get(id);
    }

    /**
     * 尝试 ping
     *
     * @param ip   ip 地址
     * @param ping ping 时间
     * @return true
     */
    private boolean testIpPort(String ip, String ping) {
        int pingTime = Convert.toInt(ping, 5);
        if (pingTime <= 0) {
            return true;
        }
        return NetUtil.ping(ip, pingTime * 1000);
    }

    private MachineNodeModel createMachineNodeModel(String ip, String loginName, String loginPwd, int port) {
        MachineNodeModel machineNodeModel = new MachineNodeModel();
        machineNodeModel.setName(ip);
        machineNodeModel.setStatus(1);
        machineNodeModel.setJpomUsername(loginName);
        machineNodeModel.setJpomPassword(loginPwd);
        machineNodeModel.setJpomUrl(ip + StrUtil.COLON + port);
        machineNodeModel.setJpomProtocol("http");
        return machineNodeModel;
    }
}
