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
package org.dromara.jpom.model.enums;

import lombok.Getter;
import org.dromara.jpom.model.BaseEnum;

/**
 * @author bwcx_jzy
 * @since 2021/8/27
 */
@Getter
public enum BuildStatus implements BaseEnum {
    /**
     *
     */
    No(0, "未构建"),
    Ing(1, "构建中", true),
    Success(2, "构建结束"),
    Error(3, "构建失败"),
    PubIng(4, "发布中", true),
    PubSuccess(5, "发布成功"),
    PubError(6, "发布失败"),
    Cancel(7, "取消构建"),
    Interrupt(8, "构建中断"),
    WaitExec(9, "队列等待", true),
    AbnormalShutdown(10, "异常关闭"),
    ;

    private final int code;
    private final String desc;
    private final boolean progress;

    BuildStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
        this.progress = false;
    }

    BuildStatus(int code, String desc, boolean progress) {
        this.code = code;
        this.desc = desc;
        this.progress = progress;
    }

}
