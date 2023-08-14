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
package org.dromara.jpom.controller.manage;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.common.BaseAgentController;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.commander.AbstractProjectCommander;
import org.dromara.jpom.common.commander.CommandOpResult;
import org.dromara.jpom.common.validator.ValidatorItem;
import org.dromara.jpom.controller.manage.vo.DiffFileVo;
import org.dromara.jpom.model.AfterOpt;
import org.dromara.jpom.model.BaseEnum;
import org.dromara.jpom.model.data.AgentWhitelist;
import org.dromara.jpom.model.data.NodeProjectInfoModel;
import org.dromara.jpom.script.ProjectFileBackupUtil;
import org.dromara.jpom.service.WhitelistDirectoryService;
import org.dromara.jpom.service.manage.ConsoleService;
import org.dromara.jpom.socket.ConsoleCommandOp;
import org.dromara.jpom.system.AgentConfig;
import org.dromara.jpom.util.CommandUtil;
import org.dromara.jpom.util.CompressionFileUtil;
import org.dromara.jpom.util.FileUtils;
import org.dromara.jpom.util.StringUtil;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 项目文件管理
 *
 * @author bwcx_jzy
 * @since 2019/4/17
 */
@RestController
@RequestMapping(value = "/manage/file/")
@Slf4j
public class ProjectFileControl extends BaseAgentController {

    private final ConsoleService consoleService;
    private final WhitelistDirectoryService whitelistDirectoryService;
    private final AgentConfig agentConfig;

    public ProjectFileControl(ConsoleService consoleService,
                              WhitelistDirectoryService whitelistDirectoryService,
                              AgentConfig agentConfig) {
        this.consoleService = consoleService;
        this.whitelistDirectoryService = whitelistDirectoryService;
        this.agentConfig = agentConfig;
    }

    @RequestMapping(value = "getFileList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<List<JSONObject>> getFileList(String id, String path) {
        // 查询项目路径
        NodeProjectInfoModel pim = projectInfoService.getItem(id);
        Assert.notNull(pim, "查询失败：项目不存在");
        String lib = pim.allLib();
        File fileDir = FileUtil.file(lib, StrUtil.emptyToDefault(path, FileUtil.FILE_SEPARATOR));
        boolean exist = FileUtil.exist(fileDir);
        if (!exist) {
            return JsonMessage.success("查询成功", Collections.emptyList());
        }
        //
        File[] filesAll = fileDir.listFiles();
        if (ArrayUtil.isEmpty(filesAll)) {
            return JsonMessage.success("查询成功", Collections.emptyList());
        }
        List<JSONObject> arrayFile = FileUtils.parseInfo(filesAll, false, lib);
        AgentWhitelist whitelist = whitelistDirectoryService.getWhitelist();
        for (JSONObject jsonObject : arrayFile) {
            String filename = jsonObject.getString("filename");
            jsonObject.put("textFileEdit", AgentWhitelist.checkSilentFileSuffix(whitelist.getAllowEditSuffix(), filename));
        }
        return JsonMessage.success("查询成功", arrayFile);
    }

    /**
     * 对比文件
     *
     * @param diffFileVo 参数
     * @return json
     */
    @PostMapping(value = "diff_file", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<JSONObject> diffFile(@RequestBody DiffFileVo diffFileVo) {
        String id = diffFileVo.getId();
        NodeProjectInfoModel projectInfoModel = super.getProjectInfoModel(id);
        //
        List<DiffFileVo.DiffItem> data = diffFileVo.getData();
        Assert.notEmpty(data, "没有要对比的数据");
        // 扫描项目目录下面的所有文件
        String path = FileUtil.file(projectInfoModel.allLib(), Opt.ofBlankAble(diffFileVo.getDir()).orElse(StrUtil.SLASH)).getAbsolutePath();
        List<File> files = FileUtil.loopFiles(path);
        // 将所有的文件信息组装并签名
        List<JSONObject> collect = files.stream().map(file -> {
            //
            JSONObject item = new JSONObject();
            item.put("name", StringUtil.delStartPath(file, path, true));
            item.put("sha1", SecureUtil.sha1(file));
            return item;
        }).collect(Collectors.toList());
        // 得到 当前下面文件夹下面所有的文件信息 map
        Map<String, String> nowMap = CollStreamUtil.toMap(collect,
                jsonObject12 -> jsonObject12.getString("name"),
                jsonObject1 -> jsonObject1.getString("sha1"));
        // 将需要对应的信息转为 map
        Map<String, String> tryMap = CollStreamUtil.toMap(data, DiffFileVo.DiffItem::getName, DiffFileVo.DiffItem::getSha1);
        // 对应需要 当前项目文件夹下没有的和文件内容有变化的
        List<JSONObject> canSync = tryMap.entrySet()
                .stream()
                .filter(stringStringEntry -> {
                    String nowSha1 = nowMap.get(stringStringEntry.getKey());
                    if (StrUtil.isEmpty(nowSha1)) {
                        // 不存在
                        return true;
                    }
                    // 如果 文件信息一致 则过滤
                    return !StrUtil.equals(stringStringEntry.getValue(), nowSha1);
                })
                .map(stringStringEntry -> {
                    //
                    JSONObject item = new JSONObject();
                    item.put("name", stringStringEntry.getKey());
                    item.put("sha1", stringStringEntry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
        // 对比项目文件夹下有对，但是需要对应对信息里面没有对。此类文件需要删除
        List<JSONObject> delArray = nowMap.entrySet()
                .stream()
                .filter(stringStringEntry -> !tryMap.containsKey(stringStringEntry.getKey()))
                .map(stringStringEntry -> {
                    //
                    JSONObject item = new JSONObject();
                    item.put("name", stringStringEntry.getKey());
                    item.put("sha1", stringStringEntry.getValue());
                    return item;
                })
                .collect(Collectors.toList());
        //
        JSONObject result = new JSONObject();
        result.put("diff", canSync);
        result.put("del", delArray);
        return JsonMessage.success("", result);
    }


    private void saveProjectFileBefore(File lib, NodeProjectInfoModel projectInfoModel) throws Exception {
        String closeFirstStr = getParameter("closeFirst");
        // 判断是否需要先关闭项目
        boolean closeFirst = BooleanUtil.toBoolean(closeFirstStr);
        if (closeFirst) {
            CommandOpResult result = consoleService.execCommand(ConsoleCommandOp.stop, projectInfoModel, null);
            Assert.state(result.isSuccess(), "关闭项目失败：" + result.msgStr());
            List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = projectInfoModel.getJavaCopyItemList();
            Optional.ofNullable(javaCopyItemList).ifPresent(javaCopyItems -> {
                try {
                    for (NodeProjectInfoModel.JavaCopyItem javaCopyItem : javaCopyItems) {
                        CommandOpResult result1 = consoleService.execCommand(ConsoleCommandOp.stop, projectInfoModel, javaCopyItem);
                        Assert.state(result1.isSuccess(), "关闭项目副本" + javaCopyItem.getName() + " - " + javaCopyItem.getId() + "失败：" + result1.msgStr());
                    }
                } catch (Exception e) {
                    throw Lombok.sneakyThrow(e);
                }
            });
        }
        String clearType = getParameter("clearType");
        // 判断是否需要清空
        if ("clear".equalsIgnoreCase(clearType)) {
            CommandUtil.systemFastDel(lib);
        }
    }

    @RequestMapping(value = "upload-sharding", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<CommandOpResult> uploadSharding(MultipartFile file,
                                                       String sliceId,
                                                       Integer totalSlice,
                                                       Integer nowSlice,
                                                       String fileSumMd5) throws Exception {
        String tempPathName = agentConfig.getFixedTempPathName();
        this.uploadSharding(file, tempPathName, sliceId, totalSlice, nowSlice, fileSumMd5);

        return JsonMessage.success("上传成功");
    }

    @RequestMapping(value = "sharding-merge", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<CommandOpResult> shardingMerge(String type,
                                                      String levelName,
                                                      Integer stripComponents,
                                                      String sliceId,
                                                      Integer totalSlice,
                                                      String fileSumMd5,
                                                      String after) throws Exception {
        String tempPathName = agentConfig.getFixedTempPathName();
        File successFile = this.shardingTryMerge(tempPathName, sliceId, totalSlice, fileSumMd5);
        // 处理上传文件
        return this.upload(successFile, type, levelName, stripComponents, after);
    }

    /**
     * 处理上传文件
     *
     * @param file            上传的文件
     * @param type            上传类型
     * @param levelName       文件夹
     * @param stripComponents 剔除文件夹
     * @param after           上传之后
     * @return 结果
     * @throws Exception 异常
     */
    private JsonMessage<CommandOpResult> upload(File file, String type, String levelName, Integer stripComponents, String after) throws Exception {
        NodeProjectInfoModel pim = getProjectInfoModel();
        File lib = StrUtil.isEmpty(levelName) ? new File(pim.allLib()) : FileUtil.file(pim.allLib(), levelName);
        // 备份文件
        String backupId = ProjectFileBackupUtil.backup(pim);
        try {
            //
            this.saveProjectFileBefore(lib, pim);
            if ("unzip".equals(type)) {
                // 解压
                try {
                    int stripComponentsValue = Convert.toInt(stripComponents, 0);
                    CompressionFileUtil.unCompress(file, lib, stripComponentsValue);
                } finally {
                    if (!FileUtil.del(file)) {
                        log.error("删除文件失败：" + file.getPath());
                    }
                }
            } else {
                // 移动文件到对应目录
                FileUtil.mkdir(lib);
                FileUtil.move(file, lib, true);
            }
            AbstractProjectCommander.getInstance().asyncWebHooks(pim, null, "fileChange",
                    "changeEvent", "upload", "levelName", levelName, "fileType", type, "fileName", file.getName());
            //
            JsonMessage<CommandOpResult> resultJsonMessage = this.saveProjectFileAfter(after, pim);
            if (resultJsonMessage != null) {
                return resultJsonMessage;
            }
        } finally {
            ProjectFileBackupUtil.checkDiff(pim, backupId);
        }
        return JsonMessage.success("上传成功");
    }

    private JsonMessage<CommandOpResult> saveProjectFileAfter(String after, NodeProjectInfoModel pim) throws Exception {
        if (StrUtil.isEmpty(after)) {
            return null;
        }
        //
        List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = pim.getJavaCopyItemList();
        //
        AfterOpt afterOpt = BaseEnum.getEnum(AfterOpt.class, Convert.toInt(after, AfterOpt.No.getCode()));
        if ("restart".equalsIgnoreCase(after) || afterOpt == AfterOpt.Restart) {
            CommandOpResult result = consoleService.execCommand(ConsoleCommandOp.restart, pim, null);
            // 自动处理副本集
            if (javaCopyItemList != null) {
                ThreadUtil.execute(() -> javaCopyItemList.forEach(javaCopyItem -> {
                    try {
                        consoleService.execCommand(ConsoleCommandOp.restart, pim, javaCopyItem);
                    } catch (Exception e) {
                        log.error("重启副本集失败", e);
                    }
                }));
            }
            return new JsonMessage<>(result.isSuccess() ? 200 : 405, "上传成功并重启", result);
        } else if (afterOpt == AfterOpt.Order_Restart || afterOpt == AfterOpt.Order_Must_Restart) {
            CommandOpResult result = consoleService.execCommand(ConsoleCommandOp.restart, pim, null);
            if (javaCopyItemList != null) {
                int sleepTime = getParameterInt("sleepTime", 30);
                ThreadUtil.execute(() -> {
                    // 副本
                    for (NodeProjectInfoModel.JavaCopyItem javaCopyItem : javaCopyItemList) {
                        if (!this.restart(pim, javaCopyItem, afterOpt)) {
                            return;
                        }
                        // 休眠x秒 等待之前项目正常启动
                        try {
                            TimeUnit.SECONDS.sleep(sleepTime);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
            }
            return new JsonMessage<>(result.isSuccess() ? 200 : 405, "上传成功并重启", result);
        }
        return null;
    }


    private boolean restart(NodeProjectInfoModel nodeProjectInfoModel, NodeProjectInfoModel.JavaCopyItem javaCopyItem, AfterOpt afterOpt) {
        try {
            CommandOpResult result = consoleService.execCommand(ConsoleCommandOp.restart, nodeProjectInfoModel, javaCopyItem);
            if (result.isSuccess()) {
                return true;
            }
        } catch (Exception e) {
            log.error("重复失败", e);
        }
        // 完整重启，不再继续剩余的节点项目
        return afterOpt != AfterOpt.Order_Must_Restart;
    }

    @RequestMapping(value = "deleteFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> deleteFile(String filename, String type, String levelName) {
        NodeProjectInfoModel pim = getProjectInfoModel();
        File file = FileUtil.file(pim.allLib(), StrUtil.emptyToDefault(levelName, StrUtil.SLASH));
        // 备份文件
        String backupId = ProjectFileBackupUtil.backup(pim);
        try {
            if ("clear".equalsIgnoreCase(type)) {
                // 清空文件
                if (FileUtil.clean(file)) {
                    AbstractProjectCommander.getInstance().asyncWebHooks(pim, null, "fileChange",
                            "changeEvent", "delete", "levelName", levelName, "deleteType", type, "fileName", filename);
                    return JsonMessage.success("清除成功");
                }
                boolean run = AbstractProjectCommander.getInstance().isRun(pim, null);
                Assert.state(!run, "文件被占用，请先停止项目");
                return new JsonMessage<>(500, "删除失败：" + file.getAbsolutePath());
            } else {
                // 删除文件
                Assert.hasText(filename, "请选择要删除的文件");
                file = FileUtil.file(file, filename);
                if (FileUtil.del(file)) {
                    AbstractProjectCommander.getInstance().asyncWebHooks(pim, null, "fileChange",
                            "changeEvent", "delete", "levelName", levelName, "deleteType", type, "fileName", filename);
                    return JsonMessage.success("删除成功");
                }
                return new JsonMessage<>(500, "删除失败");
            }
        } finally {
            ProjectFileBackupUtil.checkDiff(pim, backupId);
        }
    }


    @RequestMapping(value = "batch_delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> batchDelete(@RequestBody DiffFileVo diffFileVo) {
        String id = diffFileVo.getId();
        String dir = diffFileVo.getDir();
        NodeProjectInfoModel projectInfoModel = super.getProjectInfoModel(id);
        // 备份文件
        String backupId = ProjectFileBackupUtil.backup(projectInfoModel);
        try {
            //
            List<DiffFileVo.DiffItem> data = diffFileVo.getData();
            Assert.notEmpty(data, "没有要对比的数据");
            //
            File path = FileUtil.file(projectInfoModel.allLib(), Opt.ofBlankAble(dir).orElse(StrUtil.SLASH));
            for (DiffFileVo.DiffItem datum : data) {
                File file = FileUtil.file(path, datum.getName());
                if (FileUtil.del(file)) {
                    continue;
                }
                return new JsonMessage<>(500, "删除失败：" + file.getAbsolutePath());
            }
            return JsonMessage.success("删除成功");
        } finally {
            ProjectFileBackupUtil.checkDiff(projectInfoModel, backupId);
        }

    }

    /**
     * 读取文件内容 （只能处理文本文件）
     *
     * @param filePath 相对项目文件的文件夹
     * @param filename 读取的文件名
     * @return json
     */
    @PostMapping(value = "read_file", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> readFile(String filePath, String filename) {
        NodeProjectInfoModel pim = getProjectInfoModel();
        filePath = StrUtil.emptyToDefault(filePath, File.separator);
        // 判断文件后缀
        AgentWhitelist whitelist = whitelistDirectoryService.getWhitelist();
        Charset charset = AgentWhitelist.checkFileSuffix(whitelist.getAllowEditSuffix(), filename);
        File file = FileUtil.file(pim.allLib(), filePath, filename);
        String ymlString = FileUtil.readString(file, charset);
        return JsonMessage.success("", ymlString);
    }

    /**
     * 保存文件内容 （只能处理文本文件）
     *
     * @param filePath 相对项目文件的文件夹
     * @param filename 读取的文件名
     * @param fileText 文件内容
     * @return json
     */
    @PostMapping(value = "update_config_file", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<Object> updateConfigFile(String filePath, String filename, String fileText) {
        NodeProjectInfoModel pim = getProjectInfoModel();
        filePath = StrUtil.emptyToDefault(filePath, File.separator);
        // 判断文件后缀
        AgentWhitelist whitelist = whitelistDirectoryService.getWhitelist();
        Charset charset = AgentWhitelist.checkFileSuffix(whitelist.getAllowEditSuffix(), filename);
        // 备份文件
        String backupId = ProjectFileBackupUtil.backup(pim);
        try {
            FileUtil.writeString(fileText, FileUtil.file(pim.allLib(), filePath, filename), charset);
            AbstractProjectCommander.getInstance().asyncWebHooks(pim, null, "fileChange",
                    "changeEvent", "edit", "levelName", filePath, "fileName", filename);
            return JsonMessage.success("文件写入成功");
        } finally {
            ProjectFileBackupUtil.checkDiff(pim, backupId);
        }
    }


    /**
     * 将执行文件下载到客户端 本地
     *
     * @param id        项目id
     * @param filename  文件名
     * @param levelName 文件夹名
     */
    @GetMapping(value = "download", produces = MediaType.APPLICATION_JSON_VALUE)
    public void download(String id, String filename, String levelName, HttpServletResponse response) {
        Assert.hasText(filename, "请选择文件");
//		String safeFileName = pathSafe(filename);
//		if (StrUtil.isEmpty(safeFileName)) {
//			return JsonMessage.getString(405, "非法操作");
//		}
        try {
            NodeProjectInfoModel pim = projectInfoService.getItem(id);
            File file = FileUtil.file(pim.allLib(), StrUtil.emptyToDefault(levelName, FileUtil.FILE_SEPARATOR), filename);
            if (file.isDirectory()) {
                ServletUtil.write(response, JsonMessage.getString(400, "暂不支持下载文件夹"), MediaType.APPLICATION_JSON_VALUE);
                return;
            }
            ServletUtil.write(response, file);
        } catch (Exception e) {
            log.error("下载文件异常", e);
            ServletUtil.write(response, JsonMessage.getString(400, "下载失败。请刷新页面后重试", e.getMessage()), MediaType.APPLICATION_JSON_VALUE);
        }
    }

    /**
     * 下载远程文件
     *
     * @param id              项目id
     * @param url             远程 url 地址
     * @param levelName       保存的文件夹
     * @param unzip           是否为压缩包、true 将自动解压
     * @param stripComponents 剔除层级
     * @return json
     */
    @PostMapping(value = "remote_download", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> remoteDownload(String id, String url, String levelName, String unzip, Integer stripComponents) {
        Assert.hasText(url, "请输入正确的远程地址");


        NodeProjectInfoModel pim = projectInfoService.getItem(id);
        String tempPathName = agentConfig.getTempPathName();
        //
        String backupId = null;
        try {
            File downloadFile = HttpUtil.downloadFileFromUrl(url, tempPathName);
            String fileSize = FileUtil.readableFileSize(downloadFile);
            // 备份文件
            backupId = ProjectFileBackupUtil.backup(pim);
            File file = FileUtil.file(pim.allLib(), StrUtil.emptyToDefault(levelName, FileUtil.FILE_SEPARATOR));
            FileUtil.mkdir(file);
            if (BooleanUtil.toBoolean(unzip)) {
                // 需要解压文件
                try {
                    int stripComponentsValue = Convert.toInt(stripComponents, 0);
                    CompressionFileUtil.unCompress(downloadFile, file, stripComponentsValue);
                } finally {
                    if (!FileUtil.del(downloadFile)) {
                        log.error("删除文件失败：" + file.getPath());
                    }
                }
            } else {
                // 移动文件到对应目录
                FileUtil.move(downloadFile, file, true);
            }
            AbstractProjectCommander.getInstance().asyncWebHooks(pim, null, "fileChange",
                    "changeEvent", "remoteDownload", "levelName", levelName, "fileName", file.getName(), "url", url);
            return JsonMessage.success("下载成功文件大小：" + fileSize);
        } catch (Exception e) {
            log.error("下载远程文件异常", e);
            return new JsonMessage<>(500, "下载远程文件失败:" + e.getMessage());
        } finally {
            ProjectFileBackupUtil.checkDiff(pim, backupId);
        }
    }

    /**
     * 创建文件夹/文件
     *
     * @param id        项目ID
     * @param levelName 二级文件夹名
     * @param filename  文件名
     * @param unFolder  true/1 为文件夹，false/2 为文件
     * @return json
     */
    @PostMapping(value = "new_file_folder.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<Object> newFileFolder(String id, String levelName, @ValidatorItem String filename, String unFolder) {
        NodeProjectInfoModel projectInfoModel = projectInfoService.getItem(id);
        Assert.notNull(projectInfoModel, "没有对应到项目");
        File file = FileUtil.file(projectInfoModel.allLib(), StrUtil.emptyToDefault(levelName, FileUtil.FILE_SEPARATOR), filename);
        //
        Assert.state(!FileUtil.exist(file), "文件夹或者文件已存在");
        boolean folder = !Convert.toBool(unFolder, false);
        if (folder) {
            FileUtil.mkdir(file);
        } else {
            FileUtil.touch(file);
        }
        AbstractProjectCommander.getInstance().asyncWebHooks(projectInfoModel, null, "fileChange",
                "changeEvent", "newFileOrFolder", "levelName", levelName, "fileName", filename, "folder", folder);
        return JsonMessage.success("操作成功");
    }

    /**
     * 修改文件夹/文件
     *
     * @param id        项目ID
     * @param levelName 二级文件夹名
     * @param filename  文件名
     * @param newname   新文件名
     * @return json
     */
    @PostMapping(value = "rename.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<Object> rename(String id, String levelName, @ValidatorItem String filename, String newname) {
        NodeProjectInfoModel projectInfoModel = getProjectInfoModel();
        File file = FileUtil.file(projectInfoModel.allLib(), StrUtil.emptyToDefault(levelName, FileUtil.FILE_SEPARATOR), filename);
        File newFile = FileUtil.file(projectInfoModel.allLib(), StrUtil.emptyToDefault(levelName, FileUtil.FILE_SEPARATOR), newname);

        Assert.state(FileUtil.exist(file), "文件不存在");
        Assert.state(!FileUtil.exist(newFile), "文件名已经存在拉");

        FileUtil.rename(file, newname, false);
        AbstractProjectCommander.getInstance().asyncWebHooks(projectInfoModel, null, "fileChange",
                "changeEvent", "rename", "levelName", levelName, "fileName", filename, "newname", newname);
        return JsonMessage.success("操作成功");
    }

}
