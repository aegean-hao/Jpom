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
package org.dromara.jpom.socket;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.util.BaseFileTailWatcher;
import org.dromara.jpom.util.SocketSessionUtil;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件跟随器
 *
 * @author bwcx_jzy
 * @since 2019/3/16
 */
@Slf4j
public class AgentFileTailWatcher<T> extends BaseFileTailWatcher<T> {
    private static final ConcurrentHashMap<File, AgentFileTailWatcher<Session>> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();


    private AgentFileTailWatcher(File logFile, Charset charset) {
        super(logFile, charset);
    }

    public static int getOneLineCount() {
        return CONCURRENT_HASH_MAP.size();
    }

    /**
     * 添加文件监听
     *
     * @param file    文件
     * @param charset 编码格式
     * @param session 会话
     * @throws IOException 异常
     */
    public static boolean addWatcher(File file, Charset charset, Session session) throws IOException {
        if (!FileUtil.isFile(file)) {
            log.warn("文件不存在或者是目录:" + file.getPath());
            return false;
        }
        AgentFileTailWatcher<Session> agentFileTailWatcher = CONCURRENT_HASH_MAP.computeIfAbsent(file, s -> {
            try {
                return new AgentFileTailWatcher<>(file, charset);
            } catch (Exception e) {
                log.error("创建文件监听失败", e);
                return null;
            }
        });
        if (agentFileTailWatcher == null) {
            throw new IOException("加载文件失败:" + file.getPath());
        }
        if (agentFileTailWatcher.add(session, FileUtil.getName(file))) {
            agentFileTailWatcher.start();
        }
        return true;
    }

    /**
     * 有客户端离线
     *
     * @param session 会话
     */
    public static void offline(Session session) {
        Collection<AgentFileTailWatcher<Session>> collection = CONCURRENT_HASH_MAP.values();
        for (AgentFileTailWatcher<Session> agentFileTailWatcher : collection) {
            agentFileTailWatcher.socketSessions.removeIf(session::equals);
            if (agentFileTailWatcher.socketSessions.isEmpty()) {
                agentFileTailWatcher.close();
            }
        }
    }

    /**
     * 关闭文件读取流
     *
     * @param fileName 文件名
     */
    public static void offlineFile(File fileName) {
        AgentFileTailWatcher<Session> agentFileTailWatcher = CONCURRENT_HASH_MAP.get(fileName);
        if (null == agentFileTailWatcher) {
            return;
        }
        Set<Session> socketSessions = agentFileTailWatcher.socketSessions;
        for (Session socketSession : socketSessions) {
            offline(socketSession);
        }
        agentFileTailWatcher.close();
    }

    /**
     * 重新监听
     *
     * @param fileName 文件名
     */
    public static void reWatcher(File fileName) {
        AgentFileTailWatcher<Session> agentFileTailWatcher = CONCURRENT_HASH_MAP.get(fileName);
        if (null == agentFileTailWatcher) {
            return;
        }
        agentFileTailWatcher.restart();
    }

    /**
     * 关闭文件读取流
     *
     * @param fileName 文件名
     */
    static void offlineFile(File fileName, Session session) {
        AgentFileTailWatcher<Session> agentFileTailWatcher = CONCURRENT_HASH_MAP.get(fileName);
        if (null == agentFileTailWatcher) {
            return;
        }
        Set<Session> socketSessions = agentFileTailWatcher.socketSessions;
        for (Session socketSession : socketSessions) {
            if (socketSession.equals(session)) {
                offline(socketSession);
                break;
            }
        }
        if (agentFileTailWatcher.socketSessions.isEmpty()) {
            agentFileTailWatcher.close();
        }

    }

    @Override
    protected void send(T session, String msg) throws IOException {
//        try {
        SocketSessionUtil.send((Session) session, msg);
//        } catch (Exception e) {
//            log.error("发送消息异常", e);
//        }
    }

    /**
     * 关闭
     */
    @Override
    protected void close() {
        super.close();
        // 清理线程记录
        CONCURRENT_HASH_MAP.remove(this.logFile);
    }
}
