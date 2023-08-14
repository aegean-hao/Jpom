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
package org.dromara.jpom.model.data;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.jpom.build.BuildUtil;
import org.dromara.jpom.common.Const;
import org.dromara.jpom.db.TableName;
import org.dromara.jpom.model.BaseEnum;
import org.dromara.jpom.model.BaseWorkspaceModel;
import org.dromara.jpom.model.enums.GitProtocolEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hotstrip
 * 仓库地址实体类
 */
@TableName(value = "REPOSITORY", name = "仓库信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class RepositoryModel extends BaseWorkspaceModel {
    /**
     * 名称
     */
    private String name;
    /**
     * 仓库地址
     */
    private String gitUrl;
    /**
     * 仓库类型{0: GIT, 1: SVN}
     */
    private Integer repoType;
    /**
     * 拉取代码的协议{0: http, 1: ssh}
     *
     * @see GitProtocolEnum
     */
    private Integer protocol;
    /**
     * 登录用户
     */
    private String userName;
    /**
     * 登录密码
     */
    private String password;
    /**
     * SSH RSA 公钥
     */
    @Deprecated
    private String rsaPub;
    /**
     * SSH RSA 私钥
     */
    private String rsaPrv;
    /**
     * 排序
     */
    private Float sortValue;
    /**
     * 仓库连接超时时间
     */
    private Integer timeout;

    /**
     * 返回协议类型，如果为 null 会尝试识别 http
     *
     * @return 枚举的值（1/0）
     * @see GitProtocolEnum
     */
    public Integer getProtocol() {
        if (protocol != null) {
            return protocol;
        }
        String gitUrl = this.getGitUrl();
        if (StrUtil.isEmpty(gitUrl)) {
            return null;
        }
        if (HttpUtil.isHttps(gitUrl) || HttpUtil.isHttp(gitUrl)) {
            return GitProtocolEnum.HTTP.getCode();
        }
        return null;
    }

    /**
     * 转换为 map
     *
     * @return map
     */
    public Map<String, Object> toMap() {
        //
        Map<String, Object> map = new HashMap<>(10);
        map.put("url", this.getGitUrl());
        map.put("protocol", this.getProtocol());
        Integer protocolCode = this.getProtocol();
        GitProtocolEnum protocol = EnumUtil.likeValueOf(GitProtocolEnum.class, protocolCode);
        if (protocol != null) {
            map.put("protocolStr", protocol.name());
        }
        map.put("username", this.getUserName());
        map.put("password", this.getPassword());
        map.put(Const.WORKSPACE_ID_REQ_HEADER, this.getWorkspaceId());
        map.put("rsaFile", BuildUtil.getRepositoryRsaFile(this));
        map.put("timeout", this.getTimeout());
        return map;
    }

    /**
     * 仓库类型
     */
    @Getter
    public enum RepoType implements BaseEnum {
        /**
         * git
         */
        Git(0, "Git"),
        Svn(1, "Svn"),
        ;
        private final int code;
        private final String desc;

        RepoType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Override
    protected boolean hasCreateUser() {
        return true;
    }
}
