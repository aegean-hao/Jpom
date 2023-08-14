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
package org.dromara.jpom.common.commander.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.StrUtil;
import org.dromara.jpom.common.commander.AbstractProjectCommander;
import org.dromara.jpom.common.commander.AbstractSystemCommander;
import org.dromara.jpom.common.commander.CommandOpResult;
import org.dromara.jpom.model.data.NodeProjectInfoModel;
import org.dromara.jpom.model.system.NetstatModel;
import org.dromara.jpom.util.CommandUtil;
import org.dromara.jpom.util.JvmUtil;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * windows 版
 *
 * @author Administrator
 */
public class WindowsProjectCommander extends AbstractProjectCommander {


    public WindowsProjectCommander(Charset fileCharset) {
        super(fileCharset);
    }

    @Override
    public String buildJavaCommand(NodeProjectInfoModel nodeProjectInfoModel, NodeProjectInfoModel.JavaCopyItem javaCopyItem) {
        String classPath = NodeProjectInfoModel.getClassPathLib(nodeProjectInfoModel);
        if (StrUtil.isBlank(classPath)) {
            return null;
        }
        // 拼接命令
        String jvm = javaCopyItem == null ? nodeProjectInfoModel.getJvm() : javaCopyItem.getJvm();
        String tag = javaCopyItem == null ? nodeProjectInfoModel.getId() : javaCopyItem.getTagId();
        String mainClass = nodeProjectInfoModel.getMainClass();
        String args = javaCopyItem == null ? nodeProjectInfoModel.getArgs() : javaCopyItem.getArgs();
        return StrUtil.format("{} {} {} {} {} {} >> {} &",
            getRunJavaPath(nodeProjectInfoModel, true),
            Optional.ofNullable(jvm).orElse(StrUtil.EMPTY),
            JvmUtil.getJpomPidTag(tag, nodeProjectInfoModel.allLib()),
            classPath,
            Optional.ofNullable(mainClass).orElse(StrUtil.EMPTY),
            Optional.ofNullable(args).orElse(StrUtil.EMPTY),
            nodeProjectInfoModel.getAbsoluteLog(javaCopyItem));
    }

    @Override
    public CommandOpResult stopJava(NodeProjectInfoModel nodeProjectInfoModel, NodeProjectInfoModel.JavaCopyItem javaCopyItem, int pid) throws Exception {
        String tag = javaCopyItem == null ? nodeProjectInfoModel.getId() : javaCopyItem.getTagId();
        List<String> result = new ArrayList<>();
        boolean success = false;
        // 如果正在运行，则执行杀进程命令
        String kill = AbstractSystemCommander.getInstance().kill(FileUtil.file(nodeProjectInfoModel.allLib()), pid);
        result.add(kill);
        if (this.loopCheckRun(nodeProjectInfoModel, javaCopyItem, false)) {
            success = true;
        } else {
            result.add("Kill not completed");
        }
        return CommandOpResult.of(success, status(tag)).appendMsg(result);
        // return status(tag) + StrUtil.SPACE + kill;
    }

    @Override
    public List<NetstatModel> listNetstat(int pId, boolean listening) {
        String cmd;
        if (listening) {
            cmd = "netstat -nao -p tcp | findstr \"LISTENING\" | findstr " + pId;
        } else {
            cmd = "netstat -nao -p tcp | findstr /V \"CLOSE_WAIT\" | findstr " + pId;
        }
        String result = CommandUtil.execSystemCommand(cmd);
        List<String> netList = StrSplitter.splitTrim(result, StrUtil.LF, true);
        if (netList == null || netList.size() <= 0) {
            return null;
        }
        List<NetstatModel> array = new ArrayList<>();
        for (String str : netList) {
            List<String> list = StrSplitter.splitTrim(str, " ", true);
            if (list.size() < 5) {
                continue;
            }
            NetstatModel netstatModel = new NetstatModel();
            netstatModel.setProtocol(list.get(0));
            netstatModel.setLocal(list.get(1));
            netstatModel.setForeign(list.get(2));
            netstatModel.setStatus(list.get(3));
            netstatModel.setName(list.get(4));
            array.add(netstatModel);
        }
        return array;
    }
}
