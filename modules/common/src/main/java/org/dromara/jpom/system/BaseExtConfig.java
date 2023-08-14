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

import lombok.Data;
import org.dromara.jpom.util.BaseFileTailWatcher;

/**
 * @author bwcx_jzy
 * @since 2022/12/17
 */
@Data
public abstract class BaseExtConfig {
    /**
     * 数据目录
     */
    private String path;

    /**
     * 初始读取日志文件行号
     */
    private int initReadLine = 10;

    public void setInitReadLine(int initReadLine) {
        this.initReadLine = initReadLine;
        BaseFileTailWatcher.setInitReadLine(this.getInitReadLine());
    }

    public void setPath(String path) {
        this.path = path;
        ExtConfigBean.setPath(path);
    }
}
