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
package org.dromara.jpom.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.keepbx.jpom.log.ILogRecorder;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * 日志记录
 *
 * @author bwcx_jzy
 * @since 2022/1/26
 */
@Builder
@Slf4j
@Getter
public class LogRecorder implements ILogRecorder {
    /**
     * 文件
     */
    private File file;
    /**
     * 文件编码
     */
    private Charset charset;

    public Charset getCharset() {
        return ObjectUtil.defaultIfNull(this.charset, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 记录错误信息
     *
     * @param title     错误描述
     * @param throwable 堆栈信息
     */
    public void error(String title, Throwable throwable) {
        log.error(title, throwable);
        FileUtil.appendLines(CollectionUtil.toList(title), this.file, this.getCharset());
        String s = ExceptionUtil.stacktraceToString(throwable);
        FileUtil.appendLines(CollectionUtil.toList(s), this.file, this.getCharset());
    }

    /**
     * 记录单行日志
     *
     * @param info 日志
     */
    public String info(String info, Object... vals) {
        String format = StrUtil.format(info, vals);
        FileUtil.appendLines(CollectionUtil.toList(format), this.file, this.getCharset());
        return format;
    }

    /**
     * 记录单行日志
     *
     * @param info 日志
     */
    public String system(String info, Object... vals) {
        return this.info("[SYSTEM-INFO] " + info, vals);
    }

    /**
     * 记录单行日志
     *
     * @param info 日志
     */
    public String systemError(String info, Object... vals) {
        return this.info("[SYSTEM-ERROR] " + info, vals);
    }

    /**
     * 记录单行日志
     *
     * @param info 日志
     */
    public String systemWarning(String info, Object... vals) {
        return this.info("[SYSTEM-WARNING] " + info, vals);
    }

    /**
     * 记录单行日志 (不还行)
     *
     * @param info 日志
     */
    public void append(String info, Object... vals) {
        String format = StrUtil.format(info, vals);
        FileUtil.appendString(format, this.file, this.getCharset());
    }

    /**
     * 获取 文件输出流
     *
     * @return Writer
     */
    public PrintWriter getPrintWriter() {
        return FileWriter.create(this.file, this.getCharset()).getPrintWriter(true);
    }
}
