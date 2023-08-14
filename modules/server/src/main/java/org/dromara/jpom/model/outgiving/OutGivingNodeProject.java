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
package org.dromara.jpom.model.outgiving;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.jpom.model.BaseEnum;

/**
 * 节点项目
 *
 * @author bwcx_jzy
 * @since 2019/4/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutGivingNodeProject extends BaseNodeProject {

    /**
     * 排序值
     */
    private Integer sortValue;

    /**
     * 是否禁用
     */
    private Boolean disabled;

    public Boolean getDisabled() {
        return ObjectUtil.defaultIfNull(disabled, false);
    }

    /**
     * 状态
     */
    @Getter
    public enum Status implements BaseEnum {
        /**
         *
         */
        No(0, "未分发"),
        Ing(1, "分发中"),
        Ok(2, "分发成功"),
        Fail(3, "分发失败"),
        Cancel(4, "系统取消分发"),
        Prepare(5, "准备分发"),
        ArtificialCancel(6, "手动取消分发"),
        ;
        private final int code;
        private final String desc;

        Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
