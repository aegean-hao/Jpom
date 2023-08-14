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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.common.BaseAgentController;
import org.dromara.jpom.common.Const;
import org.dromara.jpom.common.JsonMessage;
import org.dromara.jpom.common.commander.AbstractProjectCommander;
import org.dromara.jpom.model.RunMode;
import org.dromara.jpom.model.data.DslYmlDto;
import org.dromara.jpom.model.data.NodeProjectInfoModel;
import org.dromara.jpom.service.WhitelistDirectoryService;
import org.dromara.jpom.util.CommandUtil;
import org.dromara.jpom.util.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 编辑项目
 *
 * @author bwcx_jzy
 * @since 2019/4/16
 */
@RestController
@RequestMapping(value = "/manage/")
@Slf4j
public class ManageEditProjectController extends BaseAgentController {

    private final WhitelistDirectoryService whitelistDirectoryService;

    public ManageEditProjectController(WhitelistDirectoryService whitelistDirectoryService) {
        this.whitelistDirectoryService = whitelistDirectoryService;
    }

    /**
     * 基础检查
     *
     * @param projectInfo        项目实体
     * @param whitelistDirectory 白名单
     * @param previewData        预检查数据
     */
    private NodeProjectInfoModel checkParameter(NodeProjectInfoModel projectInfo,
                                                String whitelistDirectory,
                                                boolean previewData) {
        String id = projectInfo.getId();
        // 兼容 _
        String checkId = StrUtil.replace(id, StrUtil.DASHED, StrUtil.UNDERLINE);
        Validator.validateGeneral(checkId, 2, Const.ID_MAX_LEN, "项目id 长度范围2-20（英文字母 、数字和下划线）");
        Assert.state(!Const.SYSTEM_ID.equals(id), "项目id " + Const.SYSTEM_ID + " 关键词被系统占用");
        // 运行模式
        String runModeStr = getParameter("runMode");
        RunMode runMode1 = EnumUtil.fromString(RunMode.class, runModeStr, RunMode.ClassPath);
        projectInfo.setRunMode(runMode1);
        // 监测
        if (runMode1 == RunMode.ClassPath || runMode1 == RunMode.JavaExtDirsCp) {
            Assert.hasText(projectInfo.getMainClass(), "ClassPath、JavaExtDirsCp 模式 MainClass必填");
        } else if (runMode1 == RunMode.Jar || runMode1 == RunMode.JarWar) {
            projectInfo.setMainClass(StrUtil.EMPTY);
        }
        if (runMode1 == RunMode.JavaExtDirsCp) {
            Assert.hasText(projectInfo.getJavaExtDirsCp(), "JavaExtDirsCp 模式 javaExtDirsCp必填");
        }
        // 判断是否为分发添加
        String strOutGivingProject = getParameter("outGivingProject");
        boolean outGivingProject = Boolean.parseBoolean(strOutGivingProject);

        projectInfo.setOutGivingProject(outGivingProject);
        if (!previewData) {
            // 不是预检查数据才效验白名单
            if (!whitelistDirectoryService.checkProjectDirectory(whitelistDirectory)) {
                if (outGivingProject) {
                    whitelistDirectoryService.addProjectWhiteList(whitelistDirectory);
                } else {
                    throw new IllegalArgumentException("请选择正确的项目路径,或者还没有配置白名单");
                }
            }
            String logPath = projectInfo.getLogPath();
            if (StrUtil.isNotEmpty(logPath)) {
                if (!whitelistDirectoryService.checkProjectDirectory(logPath)) {
                    if (outGivingProject) {
                        whitelistDirectoryService.addProjectWhiteList(logPath);
                    } else {
                        throw new IllegalArgumentException("请填写的项目日志存储路径,或者还没有配置白名单");
                    }
                }
            }
        }
        //
        String lib = projectInfo.getLib();
        Assert.state(StrUtil.isNotEmpty(lib) && !StrUtil.SLASH.equals(lib) && !Validator.isChinese(lib), "项目路径不能为空,不能为顶级目录,不能包含中文");

        FileUtils.checkSlip(lib, e -> new IllegalArgumentException("项目路径存在提升目录问题"));
        NodeProjectInfoModel exits = projectInfoService.getItem(projectInfo.getId());

        // java 程序副本
        if (runMode1 == RunMode.ClassPath || runMode1 == RunMode.Jar || runMode1 == RunMode.JarWar || runMode1 == RunMode.JavaExtDirsCp) {
            String javaCopyIds = getParameter("javaCopyIds");
            // TODO 删除副本验证
            if (StrUtil.isEmpty(javaCopyIds)) {
                projectInfo.setJavaCopyItemList(null);
            } else {
                String[] split = StrUtil.splitToArray(javaCopyIds, StrUtil.COMMA);
                List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = new ArrayList<>(split.length);
                for (String copyId : split) {
                    String jvm = getParameter("jvm_" + copyId);
                    String name = getParameter("name_" + copyId);
                    String args = getParameter("args_" + copyId);
                    //
                    NodeProjectInfoModel.JavaCopyItem javaCopyItem = new NodeProjectInfoModel.JavaCopyItem();
                    javaCopyItem.setId(copyId);
                    javaCopyItem.setParentId(id);
                    javaCopyItem.setName(name);
                    javaCopyItem.setModifyTime(DateUtil.now());
                    javaCopyItem.setJvm(StrUtil.emptyToDefault(jvm, StrUtil.EMPTY));
                    javaCopyItem.setArgs(StrUtil.emptyToDefault(args, StrUtil.EMPTY));
                    javaCopyItemList.add(javaCopyItem);
                }
                projectInfo.setJavaCopyItemList(javaCopyItemList);
            }
        } else {
            projectInfo.setJavaCopyItemList(null);
        }
        return exits;
    }


    @RequestMapping(value = "saveProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> saveProject(NodeProjectInfoModel projectInfo) {
        // 预检查数据
        String strPreviewData = getParameter("previewData");
        boolean previewData = Convert.toBool(strPreviewData, false);
        String whitelistDirectory = projectInfo.getWhitelistDirectory();
        //
        NodeProjectInfoModel exits = this.checkParameter(projectInfo, whitelistDirectory, previewData);
        String id = projectInfo.getId();
        //
        String allLib = projectInfo.allLib();
        // 判断空格
        Assert.state(!id.contains(StrUtil.SPACE) && !allLib.contains(StrUtil.SPACE), "项目Id、项目路径不能包含空格");
        File checkFile = FileUtil.file(allLib);
        Assert.state(!FileUtil.exist(checkFile) || FileUtil.isDirectory(checkFile), "项目路径是一个已经存在的文件");
        // 重复lib
        List<NodeProjectInfoModel> list = projectInfoService.list();
        if (list != null) {
            for (NodeProjectInfoModel nodeProjectInfoModel : list) {
                File fileLib = FileUtil.file(nodeProjectInfoModel.allLib());
                if (!nodeProjectInfoModel.getId().equals(id) && FileUtil.equals(fileLib, checkFile)) {
                    return new JsonMessage<>(401, "当前项目路径已经被【" + nodeProjectInfoModel.getName() + "】占用,请检查");
                }
            }
        }
        // 自动生成log文件
        String log = projectInfo.getLog();
        Assert.hasText(log, "项目log解析读取失败");
        checkFile = FileUtil.file(log);
        Assert.state(!FileUtil.exist(checkFile) || FileUtil.isFile(checkFile), "项目log是一个已经存在的文件夹");
        //
        String token = projectInfo.getToken();
        if (StrUtil.isNotEmpty(token)) {
            Validator.validateMatchRegex(RegexPool.URL_HTTP, token, "WebHooks 地址不合法");
        }
        // 判断 yml
        this.checkDslYml(projectInfo);
        //
        return save(projectInfo, exits, previewData);
    }

    private void checkDslYml(NodeProjectInfoModel projectInfo) {
        if (projectInfo.getRunMode() == RunMode.Dsl) {
            String dslContent = projectInfo.getDslContent();
            Assert.hasText(dslContent, "请配置 dsl 内容");
            DslYmlDto build = DslYmlDto.build(dslContent);
            Assert.state(build.hasRunProcess("status"), "没有配置 run.status");
        }
    }

    /**
     * 保存项目
     *
     * @param projectInfo 项目
     * @param previewData 是否是预检查
     * @return 错误信息
     */
    private JsonMessage<String> save(NodeProjectInfoModel projectInfo, NodeProjectInfoModel exits, boolean previewData) {
        projectInfo.setWorkspaceId(getWorkspaceId());

        this.checkPath(projectInfo);
        RunMode runMode = projectInfo.getRunMode();
        if (exits == null) {
            // 检查运行中的tag 是否被占用
            if (runMode != RunMode.File && runMode != RunMode.Dsl) {
                Assert.state(!AbstractProjectCommander.getInstance().isRun(projectInfo, null), "当前项目id已经被正在运行的程序占用");
            }
            if (previewData) {
                // 预检查数据
                return JsonMessage.success("检查通过");
            } else {
                projectInfoService.addItem(projectInfo);
                return JsonMessage.success("新增成功！");
            }
        }
        if (previewData) {
            // 预检查数据
            return JsonMessage.success("检查通过");
        } else {
            exits.setName(projectInfo.getName());
            exits.setGroup(projectInfo.getGroup());
            exits.setAutoStart(projectInfo.getAutoStart());
            exits.setMainClass(projectInfo.getMainClass());
            exits.setJvm(projectInfo.getJvm());
            exits.setArgs(projectInfo.getArgs());
            exits.setWorkspaceId(this.getWorkspaceId());
            exits.setOutGivingProject(projectInfo.isOutGivingProject());
            exits.setRunMode(runMode);
            exits.setToken(projectInfo.getToken());
            exits.setDslContent(projectInfo.getDslContent());
            // 检查是否非法删除副本集
            List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = exits.getJavaCopyItemList();
            List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList1 = projectInfo.getJavaCopyItemList();
            if (CollUtil.isNotEmpty(javaCopyItemList) && !CollUtil.containsAll(javaCopyItemList1, javaCopyItemList)) {
                // 重写了 equals
                return new JsonMessage<>(405, "修改中不能删除副本集、请到副本集中删除");
            }
            exits.setJavaCopyItemList(javaCopyItemList1);
            exits.setJavaExtDirsCp(projectInfo.getJavaExtDirsCp());
            // 移动到新路径
            this.moveTo(exits, projectInfo);
            // 最后才设置新的路径
            exits.setLib(projectInfo.getLib());
            exits.setWhitelistDirectory(projectInfo.getWhitelistDirectory());
            //
            exits.setLog(projectInfo.getLog());
            exits.setLogPath(projectInfo.getLogPath());
            projectInfoService.updateItem(exits);
            return JsonMessage.success("修改成功");
        }
    }

    private void moveTo(NodeProjectInfoModel old, NodeProjectInfoModel news) {
        {
            // 移动目录
            File oldLib = FileUtil.file(old.allLib());
            File newLib = FileUtil.file(news.allLib());
            if (!FileUtil.equals(oldLib, newLib)) {
                // 正在运行的项目不能修改路径
                this.projectMustNotRun(old, "正在运行的项目不能修改路径");
                if (oldLib.exists()) {
                    FileUtils.tempMoveContent(oldLib, newLib);
                }
            }
        }
        {
            // log
            File oldLog = FileUtil.file(old.getLog());
            File newLog = FileUtil.file(news.getLog());
            if (!FileUtil.equals(oldLog, newLog)) {
                // 正在运行的项目不能修改路径
                this.projectMustNotRun(old, "正在运行的项目不能修改路径");
                if (oldLog.exists()) {
                    FileUtil.mkParentDirs(newLog);
                    FileUtil.move(oldLog, newLog, true);
                }
                // logBack
                File oldLogBack = old.getLogBack();
                if (oldLogBack.exists()) {
                    File logBack = news.getLogBack();
                    FileUtils.tempMoveContent(oldLogBack, logBack);
                }
            }
        }
    }

    private void projectMustNotRun(NodeProjectInfoModel projectInfoModel, String msg) {
        boolean run = AbstractProjectCommander.getInstance().isRun(projectInfoModel, null);
        Assert.state(!run, msg);
        List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = projectInfoModel.getJavaCopyItemList();
        Optional.ofNullable(javaCopyItemList).ifPresent(javaCopyItems -> {
            for (NodeProjectInfoModel.JavaCopyItem javaCopyItem : javaCopyItems) {
                boolean run1 = AbstractProjectCommander.getInstance().isRun(projectInfoModel, javaCopyItem);
                Assert.state(!run1, msg + " 副本 " + javaCopyItem.getName());
            }
        });
    }

    /**
     * 路径存在包含关系
     *
     * @param nodeProjectInfoModel 比较的项目
     */
    private void checkPath(NodeProjectInfoModel nodeProjectInfoModel) {
        List<NodeProjectInfoModel> nodeProjectInfoModelList = projectInfoService.list();
        if (nodeProjectInfoModelList == null) {
            return;
        }
        NodeProjectInfoModel nodeProjectInfoModel1 = null;
        for (NodeProjectInfoModel model : nodeProjectInfoModelList) {
            if (!model.getId().equals(nodeProjectInfoModel.getId())) {
                File file1 = new File(model.allLib());
                File file2 = new File(nodeProjectInfoModel.allLib());
                if (FileUtil.pathEquals(file1, file2)) {
                    nodeProjectInfoModel1 = model;
                    break;
                }
                // 包含关系
                if (FileUtil.isSub(file1, file2) || FileUtil.isSub(file2, file1)) {
                    nodeProjectInfoModel1 = model;
                    break;
                }
            }
        }
        if (nodeProjectInfoModel1 != null) {
            throw new IllegalArgumentException("项目路径和【" + nodeProjectInfoModel1.getName() + "】项目冲突:" + nodeProjectInfoModel1.allLib());
        }
    }

    /**
     * 删除项目
     *
     * @return json
     */
    @RequestMapping(value = "deleteProject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<String> deleteProject(String copyId, String thorough) {
        NodeProjectInfoModel nodeProjectInfoModel = tryGetProjectInfoModel();
        if (nodeProjectInfoModel == null) {
            // 返回正常 200 状态码，考虑节点分发重复操作
            return JsonMessage.success("项目不存在");
        }
        NodeProjectInfoModel.JavaCopyItem copyItem = nodeProjectInfoModel.findCopyItem(copyId);

        if (copyItem == null) {
            List<NodeProjectInfoModel.JavaCopyItem> javaCopyItemList = nodeProjectInfoModel.getJavaCopyItemList();
            Assert.state(CollUtil.isEmpty(javaCopyItemList), "当前项目存在副本不能直接删除");
            // 运行判断
            boolean run = AbstractProjectCommander.getInstance().isRun(nodeProjectInfoModel, null);
            Assert.state(!run, "不能删除正在运行的项目");
            this.thorough(thorough, nodeProjectInfoModel, null);
            //
            projectInfoService.deleteItem(nodeProjectInfoModel.getId());
        } else {
            boolean run = AbstractProjectCommander.getInstance().isRun(nodeProjectInfoModel, copyItem);
            Assert.state(!run, "不能删除正在运行的项目副本");
            //
            this.thorough(thorough, nodeProjectInfoModel, copyItem);
            //
            boolean removeCopyItem = nodeProjectInfoModel.removeCopyItem(copyId);
            Assert.state(removeCopyItem, "删除对应副本集不存在");
            projectInfoService.updateItem(nodeProjectInfoModel);
        }
        return JsonMessage.success("删除成功！");

    }

    /**
     * 彻底删除项目文件
     *
     * @param thorough             是否彻底
     * @param nodeProjectInfoModel 项目
     * @param copyItem             副本
     */
    private void thorough(String thorough, NodeProjectInfoModel nodeProjectInfoModel, NodeProjectInfoModel.JavaCopyItem copyItem) {
        if (StrUtil.isEmpty(thorough)) {
            return;
        }
        if (copyItem != null) {
            File logBack = nodeProjectInfoModel.getLogBack(copyItem);
            boolean fastDel = CommandUtil.systemFastDel(logBack);
            Assert.state(!fastDel, "删除日志文件失败:" + logBack.getAbsolutePath());
            File log = nodeProjectInfoModel.getLog(copyItem);
            fastDel = CommandUtil.systemFastDel(log);
            Assert.state(!fastDel, "删除日志文件失败:" + log.getAbsolutePath());
        } else {
            File logBack = nodeProjectInfoModel.getLogBack();
            boolean fastDel = CommandUtil.systemFastDel(logBack);
            Assert.state(!fastDel, "删除日志文件失败:" + logBack.getAbsolutePath());
            File log = FileUtil.file(nodeProjectInfoModel.getAbsoluteLog(null));
            fastDel = CommandUtil.systemFastDel(log);
            Assert.state(!fastDel, "删除日志文件失败:" + log.getAbsolutePath());
            //
            File allLib = FileUtil.file(nodeProjectInfoModel.allLib());
            fastDel = CommandUtil.systemFastDel(allLib);
            Assert.state(!fastDel, "删除项目文件失败:" + allLib.getAbsolutePath());
        }
    }

    @RequestMapping(value = "releaseOutGiving", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage<Object> releaseOutGiving() {
        NodeProjectInfoModel nodeProjectInfoModel = tryGetProjectInfoModel();
        if (nodeProjectInfoModel != null) {
            nodeProjectInfoModel.setOutGivingProject(false);
            projectInfoService.updateItem(nodeProjectInfoModel);
        }
        return JsonMessage.success("释放成功");
    }
}
