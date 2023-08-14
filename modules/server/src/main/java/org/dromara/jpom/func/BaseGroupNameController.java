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
package org.dromara.jpom.func;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import org.dromara.jpom.common.BaseServerController;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.model.BaseGroupNameModel;
import org.dromara.jpom.permission.Feature;
import org.dromara.jpom.permission.MethodFeature;
import org.dromara.jpom.service.h2db.BaseDbService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bwcx_jzy
 * @since 2023/2/25
 */
public abstract class BaseGroupNameController extends BaseServerController {

    protected final BaseDbService<? extends BaseGroupNameModel> dbService;

    protected BaseGroupNameController(BaseDbService<? extends BaseGroupNameModel> dbService) {
        this.dbService = dbService;
    }

    @GetMapping(value = "list-group", produces = MediaType.APPLICATION_JSON_VALUE)
    @Feature(method = MethodFeature.LIST)
    public JsonMessage<List<String>> listGroup() {
        String sql = "select `groupName` from " + dbService.getTableName() + " group by `groupName`";
        List<Entity> list = dbService.query(sql);
        // 筛选字段
        List<String> collect = list.stream()
            .map(entity -> {
                Object obj = entity.get("groupName");
                return StrUtil.toStringOrNull(obj);
            })
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
        return JsonMessage.success("", collect);
    }
}
