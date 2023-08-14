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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.NioUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Lombok;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文件工具
 *
 * @author bwcx_jzy
 * @since 2019/4/28
 */
public class FileUtils {

    private static JSONObject fileToJson(File file) {
        JSONObject jsonObject = new JSONObject(6);
        jsonObject.put("isDirectory", file.isDirectory());
        long sizeFile = FileUtil.size(file);
        jsonObject.put("fileSizeLong", sizeFile);
        jsonObject.put("filename", file.getName());
        long mTime = file.lastModified();
        jsonObject.put("modifyTimeLong", mTime);
        return jsonObject;
    }

    /**
     * 对文件信息解析排序
     *
     * @param files     文件数组
     * @param time      是否安装时间排序
     * @param startPath 开始路径
     * @return 排序后的json
     */
    public static List<JSONObject> parseInfo(File[] files, boolean time, String startPath) {
        return parseInfo(CollUtil.newArrayList(files), time, startPath);
    }

    /**
     * 对文件信息解析排序
     *
     * @param files     文件数组
     * @param time      是否安装时间排序
     * @param startPath 开始路径
     * @return 排序后的json
     */
    public static List<JSONObject> parseInfo(Collection<File> files, boolean time, String startPath) {
        if (files == null) {
            return new ArrayList<>();
        }
        return files.stream().map(file -> {
            JSONObject jsonObject = FileUtils.fileToJson(file);
            //
            if (startPath != null) {
                String levelName = StringUtil.delStartPath(file, startPath, false);
                jsonObject.put("levelName", levelName);
            }
            return jsonObject;
        }).sorted((jsonObject1, jsonObject2) -> {
            if (time) {
                return jsonObject2.getLong("modifyTimeLong").compareTo(jsonObject1.getLong("modifyTimeLong"));
            }
            return jsonObject1.getString("filename").compareTo(jsonObject2.getString("filename"));
        }).collect(Collectors.toList());
    }

    /**
     * 读取 日志文件
     *
     * @param logFile 日志文件
     * @param line    开始行数
     * @return data
     */
    public static JSONObject readLogFile(File logFile, int line) {
        JSONObject data = new JSONObject();
        // 读取文件
        //int linesInt = Convert.toInt(line, 1);
        LimitQueue<String> lines = new LimitQueue<>(1000);
        final int[] readCount = {0};
        FileUtil.readLines(logFile, CharsetUtil.CHARSET_UTF_8, (LineHandler) line1 -> {
            readCount[0]++;
            if (readCount[0] < line) {
                return;
            }
            lines.add(line1);
        });
        // 下次应该获取的行数
        data.put("line", readCount[0] + 1);
        data.put("getLine", line);
        data.put("dataLines", lines);
        return data;
    }

    /**
     * 读取环境变量文件
     *
     * @param baseFile  基础文件夹
     * @param attachEnv 要读取的文件列表
     * @return map
     */
    public static Map<String, String> readEnvFile(File baseFile, String attachEnv) {
        HashMap<String, String> map = MapUtil.newHashMap(10);
        if (StrUtil.isEmpty(attachEnv)) {
            return map;
        }
        List<String> list2 = StrUtil.splitTrim(attachEnv, StrUtil.COMMA);
        for (String itemEnv : list2) {
            File envFile = FileUtil.file(baseFile, itemEnv);
            if (FileUtil.isFile(envFile)) {
                List<String> list = FileUtil.readLines(envFile, CharsetUtil.CHARSET_UTF_8);
                Map<String, String> envMap = StringUtil.parseEnvStr(list);
                // java.lang.UnsupportedOperationException
                map.putAll(envMap);
            }
        }
        return map;
    }

    /**
     * 判断目录是否有越级问题
     *
     * @param dir      目录
     * @param function 异常
     */
    public static void checkSlip(String dir, Function<Exception, Exception> function) {
        try {
            File tmpDir = FileUtil.getTmpDir();
            FileUtil.checkSlip(tmpDir, FileUtil.file(tmpDir, dir));
        } catch (IllegalArgumentException e) {
            throw Lombok.sneakyThrow(function.apply(e));
        }
    }

    /**
     * 判断目录是否有越级问题
     *
     * @param dir 目录
     */
    public static void checkSlip(String dir) {
        checkSlip(dir, e -> new IllegalArgumentException("目录不能越级：" + e.getMessage()));
    }

    /**
     * 文件追加
     *
     * @param file    被添加的文件
     * @param channel 需要添加的文件通道
     * @throws IOException io
     */
    public static void appendChannel(File file, FileChannel channel) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (FileChannel inChannel = fileInputStream.getChannel()) {
                ByteBuffer bb = ByteBuffer.allocate(IoUtil.DEFAULT_MIDDLE_BUFFER_SIZE);
                while (inChannel.read(bb) != NioUtil.EOF) {
                    bb.flip();
                    channel.write(bb);
                    bb.clear();
                }
            }
        }
    }

    /**
     * 使用当前系统的换行符写文件
     *
     * @param context    文件内容
     * @param scriptFile 文件路径
     * @param charset    编码格式
     */
    public static void writeScript(String context, File scriptFile, Charset charset) {
        // 替换换行符
        String replace = StrUtil.replace(context, StrUtil.LF, FileUtil.getLineSeparator());
        FileUtil.writeString(replace, scriptFile, charset);
    }

    /**
     * 安全的方式 move 文件夹内容
     *
     * @param src    源文件夹
     * @param target 目标文件夹
     */
    public static void tempMoveContent(File src, File target) {
        if (FileUtil.isSub(src, target)) {
            // 子目录
            // 将文件内容先复制到临时目录，避免递归出现自己 mv 自己的情况
            File tmpDir = FileUtil.getTmpDir();
            File tempMv = FileUtil.file(tmpDir, "mv", IdUtil.fastSimpleUUID());
            FileUtil.mkdir(tempMv);
            FileUtil.moveContent(src, tempMv, true);
            // 再将临时目录下的文件移动到目标路径
            FileUtil.mkdir(target);
            FileUtil.moveContent(tempMv, target, true);
            //
            FileUtil.del(tempMv);
            // 子目录不需要删除
        } else {
            FileUtil.moveContent(src, target, true);
            // 删除文件夹
            FileUtil.del(src);
        }
    }
}
