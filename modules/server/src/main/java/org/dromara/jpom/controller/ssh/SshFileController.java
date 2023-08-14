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
package org.dromara.jpom.controller.ssh;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.jpom.func.assets.controller.BaseSshFileController;
import org.dromara.jpom.func.assets.model.MachineSshModel;
import org.dromara.jpom.model.data.SshModel;
import org.dromara.jpom.permission.ClassFeature;
import org.dromara.jpom.permission.Feature;
import org.dromara.jpom.util.FileUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.BiFunction;

/**
 * ssh 文件管理
 *
 * @author bwcx_jzy
 * @since 2019/8/10
 */
@RestController
@RequestMapping("node/ssh")
@Feature(cls = ClassFeature.SSH_FILE)
@Slf4j
public class SshFileController extends BaseSshFileController {


    @Override
    protected <T> T checkConfigPath(String id, BiFunction<MachineSshModel, ItemConfig, T> function) {
        SshModel sshModel = sshService.getByKey(id);
        Assert.notNull(sshModel, "没有对应的ssh");
        MachineSshModel machineSshModel = machineSshServer.getByKey(sshModel.getMachineSshId(), false);
        return function.apply(machineSshModel, sshModel);
    }

    @Override
    protected <T> T checkConfigPathChildren(String id, String path, String children, BiFunction<MachineSshModel, ItemConfig, T> function) {
        FileUtils.checkSlip(path);
        Opt.ofBlankAble(children).ifPresent(FileUtils::checkSlip);

        SshModel sshModel = sshService.getByKey(id);
        Assert.notNull(sshModel, "没有对应的ssh");
        List<String> fileDirs = sshModel.fileDirs();
        String normalize = FileUtil.normalize(StrUtil.SLASH + path + StrUtil.SLASH);
        //
        Assert.state(CollUtil.contains(fileDirs, normalize), "不能操作当前目录");
        MachineSshModel machineSshModel = machineSshServer.getByKey(sshModel.getMachineSshId(), false);
        return function.apply(machineSshModel, sshModel);
    }
}
