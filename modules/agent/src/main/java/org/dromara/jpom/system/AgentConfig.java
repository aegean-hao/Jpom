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
package org.dromara.jpom.system;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.system.SystemUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.jpom.JpomApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * 插件端配置信息
 *
 * @author bwcx_jzy
 * @since 2022/12/16
 */
@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties("jpom")
@Data
public class AgentConfig extends BaseExtConfig {

    private final JpomApplication jpomApplication;

    public AgentConfig(JpomApplication jpomApplication) {
        this.jpomApplication = jpomApplication;
    }

    /**
     * 白名单配置
     */
    private WhitelistDirectory whitelist;

    public WhitelistDirectory getWhitelist() {
        return Optional.ofNullable(this.whitelist).orElseGet(() -> {
            this.whitelist = new WhitelistDirectory();
            return this.whitelist;
        });
    }

    /**
     * 项目配置
     */
    private ProjectConfig project;

    public ProjectConfig getProject() {
        return Optional.ofNullable(this.project).orElseGet(() -> {
            this.project = new ProjectConfig();
            return this.project;
        });
    }


    /**
     * 系统配置参数
     */
    private SystemConfig system;


    public SystemConfig getSystem() {
        return Optional.ofNullable(this.system).orElseGet(() -> {
            this.system = new SystemConfig();
            return this.system;
        });
    }

    /**
     * 获取临时文件存储路径，并添加一个随机字符串
     *
     * @return 文件夹
     */
    public String getTempPathName() {
        File file = getTempPath();
        // 生成随机的一个文件夹、避免同一个节点分发同一个文件，mv 失败
        return FileUtil.getAbsolutePath(FileUtil.file(file, IdUtil.fastSimpleUUID()));
    }

    /**
     * 获取临时文件存储路径
     *
     * @return 文件夹
     */
    public String getFixedTempPathName() {
        File file = getTempPath();
        return FileUtil.getAbsolutePath(file);
    }


    /**
     * 获取临时文件存储路径
     *
     * @return file
     */
    public File getTempPath() {
        File file = jpomApplication.getTempPath();
        file = FileUtil.file(file, DateTime.now().toDateStr());
        FileUtil.mkdir(file);
        return file;
    }

    @Data
    public static class ProjectConfig {

        private LogConfig log;

        public LogConfig getLog() {
            return Optional.ofNullable(this.log).orElseGet(() -> {
                this.log = new LogConfig();
                return this.log;
            });
        }

        /**
         * 停止项目等待的时长 单位秒，最小为1秒
         */
        private int statusWaitTime = 10;

        /**
         * 项目状态检测间隔时间 单位毫秒，最小为1毫秒
         */
        private int statusDetectionInterval = 500;

        /**
         * 项目文件备份保留个数,大于 1 才会备份
         */
        private int fileBackupCount;

        /**
         * 限制备份指定文件后缀（支持正则）
         * [ '.jar','.html','^.+\\.(?i)(txt)$' ]
         */
        private String[] fileBackupSuffix;

        @Data
        public static class LogConfig {
            /**
             * 检测控制台日志周期，防止日志文件过大，目前暂只支持linux 不停服备份
             */
            private String autoBackupConsoleCron = "0 0/10 * * * ?";
            /**
             * 当文件多大时自动备份
             *
             * @see ch.qos.logback.core.util.FileSize
             */
            private DataSize autoBackupSize = DataSize.ofMegabytes(50);
            /**
             * 是否自动将控制台日志文件备份
             */
            private boolean autoBackupToFile = true;

            /**
             * 控制台日志保存时长单位天
             */
            private int saveDays = 7;

            public int getSaveDays() {
                return Math.max(saveDays, 0);
            }

            /**
             * 日志文件的编码格式
             */
            private Charset fileCharset;

            public Charset getFileCharset() {
                return Optional.ofNullable(this.fileCharset).orElseGet(() ->
                    SystemUtil.getOsInfo().isWindows() ?
                        CharsetUtil.CHARSET_GBK : CharsetUtil.CHARSET_UTF_8);
            }
        }


        public static ProjectConfig getInstance() {
            AgentConfig agentConfig = SpringUtil.getBean(AgentConfig.class);
            return agentConfig.getProject();
        }
    }

    @Data
    public static class WhitelistDirectory {
        /**
         * 白名单目录是否验证包含关系
         */
        private boolean checkStartsWith = true;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SystemConfig extends BaseSystemConfig {

    }
}
