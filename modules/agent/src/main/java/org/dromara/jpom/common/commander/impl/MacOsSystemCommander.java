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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.common.commander.AbstractSystemCommander;
import org.dromara.jpom.util.CommandUtil;

import java.io.File;
import java.util.List;

/**
 * @author User
 */
@Slf4j
public class MacOsSystemCommander extends AbstractSystemCommander {


    @Override
    public String emptyLogFile(File file) {
        return CommandUtil.execSystemCommand("cp /dev/null " + file.getAbsolutePath());
    }


    @Override
    public boolean getServiceStatus(String serviceName) {
        if (StrUtil.startWith(serviceName, StrUtil.SLASH)) {
            String ps = getPs(serviceName);
            return StrUtil.isNotEmpty(ps);
        }
        /**
         * Mac OS 里面查询服务的命令是 launchctl list | grep serverName
         * 第一个数字是进程的 PID，如果进程正在运行，如果它不在运行，则显示 "-"
         * 第二个数字是进程的退出代码（如果已完成）。如果为负，则为终止信号的数量
         * 第三列进程名称
         */
        String format = StrUtil.format("service {} status", serviceName);
        String result = CommandUtil.execSystemCommand(format);
        return StrUtil.containsIgnoreCase(result, "RUNNING");
    }

    private String getPs(final String serviceName) {
        String ps = StrUtil.format(" ps -ef |grep -w {} | grep -v grep", serviceName);
        return CommandUtil.execSystemCommand(ps);
    }

    @Override
    public String startService(String serviceName) {
        if (StrUtil.startWith(serviceName, StrUtil.SLASH)) {
            try {
                CommandUtil.asyncExeLocalCommand(FileUtil.file(SystemUtil.getUserInfo().getHomeDir()), serviceName);
                return "ok";
            } catch (Exception e) {
                log.error("执行异常", e);
                return "执行异常：" + e.getMessage();
            }
        }
        /**
         * Mac OS 里面启动服务命令是 launchctl start serverName
         */
        String format = StrUtil.format("service {} start", serviceName);
        return CommandUtil.execSystemCommand(format);
    }

    @Override
    public String stopService(String serviceName) {
        if (StrUtil.startWith(serviceName, StrUtil.SLASH)) {
            String ps = getPs(serviceName);
            List<String> list = StrUtil.splitTrim(ps, StrUtil.LF);
            if (list == null || list.isEmpty()) {
                return "stop";
            }
            String s = list.get(0);
            list = StrUtil.splitTrim(s, StrUtil.SPACE);
            if (list == null || list.size() < 2) {
                return "stop";
            }
            File file = new File(SystemUtil.getUserInfo().getHomeDir());
            int pid = Convert.toInt(list.get(1), 0);
            if (pid <= 0) {
                return "error stop";
            }
            return kill(file, pid);
        }
        /**
         * Mac OS 里面启动服务命令是 launchctl stop serverName
         */
        String format = StrUtil.format("service {} stop", serviceName);
        return CommandUtil.execSystemCommand(format);
    }

    @Override
    public String buildKill(int pid) {
        return String.format("kill  %s", pid);
    }
}
