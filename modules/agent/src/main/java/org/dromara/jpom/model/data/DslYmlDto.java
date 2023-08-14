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
package org.dromara.jpom.model.data;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.jpom.model.BaseJsonModel;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * dsl yml 配置
 *
 * @author bwcx_jzy
 * @since 2022/1/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DslYmlDto extends BaseJsonModel {

    /**
     * 描述
     */
    private String description;

    /**
     * 运行
     */
    private Run run;

    /**
     * 文件相关配置
     */
    private FileConfig file;
    /**
     * 配置
     */
    private Config config;

    public DslYmlDto.BaseProcess runProcess(String opt) {
        DslYmlDto.Run run = this.getRun();
        Assert.notNull(run, "yml 未配置 运行管理");
        DslYmlDto.BaseProcess baseProcess = (DslYmlDto.BaseProcess) ReflectUtil.getFieldValue(run, opt);
        Assert.notNull(baseProcess, "未找到对应的类型或者未配置 " + opt);
        return baseProcess;
    }

    public boolean hasRunProcess(String opt) {
        DslYmlDto.Run run = this.getRun();
        if (run == null) {
            return false;
        }
        DslYmlDto.BaseProcess baseProcess = (DslYmlDto.BaseProcess) ReflectUtil.getFieldValue(run, opt);
        return baseProcess != null;
    }

    /**
     * 构建对象
     *
     * @param yml yml 内容
     * @return DslYmlDto
     */
    public static DslYmlDto build(String yml) {
        InputStream inputStream = new ByteArrayInputStream(yml.getBytes());
        return YamlUtil.load(inputStream, DslYmlDto.class);
    }

    /**
     * 运行管理
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Run extends BaseJsonModel {
        private Start start;
        private Status status;
        private Stop stop;
        private Restart restart;
    }


    /**
     * 启动流程
     */
    public static class Start extends BaseProcess {

    }

    /**
     * 获取状态流程
     */
    public static class Status extends BaseProcess {

    }

    /**
     * 停止流程
     */
    public static class Stop extends BaseProcess {

    }

    /**
     * 重启流程
     */
    public static class Restart extends BaseProcess {

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class BaseProcess extends BaseJsonModel {
        /**
         * 脚本 ID
         */
        private String scriptId;
        /**
         * 执行参数
         */
        private String scriptArgs;
        /**
         * 执行脚本的环境变量
         */
        private Map<String, String> scriptEnv;

        /**
         * 通过 脚本模版运行
         *
         * @return true
         */
        public boolean runByScript() {
            return StrUtil.isNotEmpty(this.getScriptId());
        }
    }

    @Data
    public static class FileConfig {
        /**
         * 保留文件备份数量
         */
        private Integer backupCount;

        /**
         * 指定备份文件后缀，如果未指定则备份所有类型文件
         */
        private String[] backupSuffix;

        /**
         * 项目文件备份路径
         */
        private String backupPath;
    }

    @Data
    public static class Config {
        /**
         * 是否自动将控制台日志文件备份
         */
        private Boolean autoBackToFile;
    }
}
