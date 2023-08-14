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

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目录树
 *
 * @author bwcx_jzy
 * @since 2019/7/21
 */
public class DirTreeUtil {

    /**
     * 获取树的json
     *
     * @param path 文件名
     * @return jsonArray
     */
    public static List<JSONObject> getTreeData(String path) {
        File file = FileUtil.file(path);
        return readTree(file, path);
    }

    private static List<JSONObject> readTree(File file, String logFile) {
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        return Arrays.stream(files)
            .sorted((o1, o2) -> CompareUtil.compare(o2.lastModified(), o1.lastModified()))
            .map(file1 -> {
                JSONObject jsonObject = new JSONObject();
                String path = StringUtil.delStartPath(file1, logFile, true);
                jsonObject.put("title", file1.getName());
                jsonObject.put("path", path);
                if (file1.isDirectory()) {
                    List<JSONObject> children = readTree(file1, logFile);
                    jsonObject.put("children", children);
                }
                return jsonObject;
            })
            .collect(Collectors.toList());
    }
}
