(window.webpackJsonp=window.webpackJsonp||[]).push([[22],{520:function(v,_,r){"use strict";r.r(_);var e=r(31),t=Object(e.a)({},(function(){var v=this,_=v.$createElement,r=v._self._c||_;return r("ContentSlotsDistributor",{attrs:{"slot-key":v.$parent.slotKey}},[r("h1",{attrs:{id:"🚀-2-4-0-2-4-9-版本日志"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🚀-2-4-0-2-4-9-版本日志"}},[v._v("#")]),v._v(" 🚀  2.4.0 ~ 2.4.9 版本日志")]),v._v(" "),r("h2",{attrs:{id:"_2-4-9-3-0-0-beta"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-9-3-0-0-beta"}},[v._v("#")]),v._v(" 2.4.9 - 3.0.0(beta)")]),v._v(" "),r("blockquote",[r("p",[v._v("当前版本为重构页面后的预览版本")])]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】新增监控用户操作记录")]),v._v(" "),r("li",[v._v("【Agent】新增配置是否禁用根据jmx获取项目状态（默认启用）")]),v._v(" "),r("li",[v._v("项目文件管理支持在线修改文件（感谢@Chen 贡献）")]),v._v(" "),r("li",[v._v("3.0.0bata版本的页面重构[采用vue项目编写]（感谢@Hotstrip）")]),v._v(" "),r("li",[v._v("新增项目启动banner输出（感谢@Hotstrip）")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】 优化判断构建命令中的删除命令关键词")]),v._v(" "),r("li",[v._v("【Server】 优化删除构建历史、构建代码（避免不能删除情况）")]),v._v(" "),r("li",[v._v("【Agent】 调整项目的jvm 和 args参数支持url编码。避免xss后冲突")]),v._v(" "),r("li",[v._v("优化获取项目当前运行路径问题")]),v._v(" "),r("li",[v._v("【Server】开始构建时输出代码目录")]),v._v(" "),r("li",[v._v("【Server】编辑构建类型为SVN没有分组bug（感谢@JAVA-落泪归枫）")]),v._v(" "),r("li",[v._v("更新文档Jpom 的JDK要为1.8.0_40+（感谢@JAVA 企鹅）")]),v._v(" "),r("li",[v._v("【Server】数据库初始化时间前置,打印成功日志，未初始化结束数据库相关操作都忽略")]),v._v(" "),r("li",[v._v("【Server】修复报警恢复后，报警列表中的报警状态显示报警中的错误（感谢@南有乔木）")]),v._v(" "),r("li",[v._v("更新hutool 版本至5.4.x （能避免系统缓存页面里面获取文件大小卡死）")]),v._v(" "),r("li",[v._v("调整Jpom启动输出日志,启动消息采用控制台输出不再打印error级别的启动消息")])]),v._v(" "),r("blockquote",[r("p",[v._v("🙏 特别感谢：@Hotstrip 对Jpom的前端页面采用vue重构编写")]),v._v(" "),r("p",[v._v("当前版本为3.x版本前的过渡版本")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-8"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-8"}},[v._v("#")]),v._v(" 2.4.8")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-2"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-2"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Agent】读取进程新增 "),r("code",[v._v("ps -ef | grep xxx")]),v._v(" 方式（感谢@JAVA-落泪归枫）")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-2"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-2"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】构建历史中记录字段不全问题（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("【Server】Java-WebSocket 模块漏洞版本更新 来源 "),r("a",{attrs:{href:"https://github.com/advisories/GHSA-gw55-jm4h-x339",target:"_blank",rel:"noopener noreferrer"}},[v._v("Github GHSA-gw55-jm4h-x339"),r("OutboundLink")],1)]),v._v(" "),r("li",[v._v("【Server】节点分发列表点击控制台、文件管理404")]),v._v(" "),r("li",[v._v("【Server】节点分发顺序重启休眠时间取构建名称最后的时间（测试构建:10 则睡眠时间为10秒）")]),v._v(" "),r("li",[v._v("【Agent】启动完成打印授权信息日志级别调至error")]),v._v(" "),r("li",[v._v("CommandUtil.asyncExeLocalCommand 方法格式化命令中的换行")]),v._v(" "),r("li",[v._v("优化启动读取进程文件目录避免包含node_modules 目录卡死")]),v._v(" "),r("li",[v._v("【Server】修复构建命令中判断是否包含【rm、del、rd】bug （感谢@落泪归枫）")]),v._v(" "),r("li",[v._v("【Server】修改删除节点会修改掉非管理员的账号密码bug")]),v._v(" "),r("li",[v._v("【Server】 构建历史根据权限查询")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-7"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-7"}},[v._v("#")]),v._v(" 2.4.7")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-3"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-3"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[r("a",{attrs:{href:"https://gitee.com/keepbx/Jpom-Plugin/tree/master/jpom-maven-plugin",target:"_blank",rel:"noopener noreferrer"}},[v._v("支持maven快速编辑节点项目"),r("OutboundLink")],1),v._v(" （配合"),r("code",[v._v("jpom-maven-plugin")]),v._v("使用）(\n感谢@夜空中最亮的星)")]),v._v(" "),r("li",[v._v("【Agent】 新增jdk 管理，不同项目选择不同的jdk （GITEE@IV8ZZ）")]),v._v(" "),r("li",[v._v("【Server】构建新增分组属性，方便快速选择")]),v._v(" "),r("li",[v._v("【Agent】 新增[JavaExtDirsCp] 运行模式 (感谢@TXpcmgr（Geiger）)")]),v._v(" "),r("li",[v._v("【Server】 ssh 连接方式新增私钥证书连接")]),v._v(" "),r("li",[v._v("【Server】 ssh文件管理新增解压操作（感谢@TXpcmgr（Geiger）贡献）")]),v._v(" "),r("li",[v._v("【Agent】 项目新建副本集，方便单机快速运行多个副本")]),v._v(" "),r("li",[v._v("【Server】构建发布后操作支持副本集相关操作")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-3"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-3"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("完善使用nginx之类代理二级目录，指定端口路径跳转问题（感谢@😯😨😰😱 ）")]),v._v(" "),r("li",[v._v("解决菜单路径不正确问题（GITEE@I15O46）")]),v._v(" "),r("li",[v._v("【Agent】 windows中Agent关闭，Agent中所有项目跟随关闭（感谢@java gods）")]),v._v(" "),r("li",[v._v("【Server】构建命令包含删除命令误判断（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("【Server】构建历史支持配置单个构建最多保存多少个历史")]),v._v(" "),r("li",[v._v("【Server】解决节点分组筛选bug(感谢gitee@I17XEH)")]),v._v(" "),r("li",[v._v("【Server】角色权限动态数据，单个节点异常不影响所有节点配置（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("【Server】关联节点分发项目支持修改发布后操作")]),v._v(" "),r("li",[v._v("补充说明文档："),r("a",{attrs:{href:"https://jpom-docs.keepbx.cn/docs/index.html#/",target:"_blank",rel:"noopener noreferrer"}},[v._v("详情"),r("OutboundLink")],1),v._v(" (感谢@TXpcmgr（Geiger）)")]),v._v(" "),r("li",[v._v("更新部分插件依赖版本【hutool、fast-boot、fastjson】")])]),v._v(" "),r("blockquote",[r("p",[v._v("⚠️ 注意：如果在2.4.7以下项目运行方式中使用过【War】模式的由于【War】更名为【JarWar】 所有在升级后请重新修改运行方式后再运行对应项目")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-6"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-6"}},[v._v("#")]),v._v(" 2.4.6")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-4"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-4"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Agent】 nginx管理支持自定义编译运行，管理方式变更")]),v._v(" "),r("li",[v._v("【Server】 监控通知新增企业微信（感谢@TinyBao。）")]),v._v(" "),r("li",[v._v("管理脚本支持自动识别环境变量和java路径（linux环境）")]),v._v(" "),r("li",[v._v("项目类型新增File(快速管理纯静态文件)")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-4"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-4"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】解决分发列表项目状态显示不正确（感谢@群友）")]),v._v(" "),r("li",[v._v("【Server】修复权限选择错乱和无法正确过滤问题【注意此版本的角色动态权限不兼容旧数据，需要重新授权动态数据权限】（感谢@Java-OutMan）")]),v._v(" "),r("li",[v._v("调整项目日志输出")]),v._v(" "),r("li",[v._v("更新【commons-compress】依赖版本[漏洞升级]")]),v._v(" "),r("li",[v._v("【Server】构建弹窗条件构建名称（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("json文件读取异常提示（感谢@Taller）")]),v._v(" "),r("li",[v._v("【Server】 优化ssh上传文件、删除文件")]),v._v(" "),r("li",[v._v("InternalError 异常捕捉")]),v._v(" "),r("li",[v._v("【Server】优化Nginx 非80、443端口 二级路径代理重定向问题（感谢@😯😨😰😱 ）")])]),v._v(" "),r("h3",{attrs:{id:"升级注意"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#升级注意"}},[v._v("#")]),v._v(" 升级注意")]),v._v(" "),r("ol",[r("li",[v._v("此版本更新控制台日志级别有调整，如果使用管理命令方式运行日志级别将不再打印info级别，如果需要打印info级别的请调整管理命令中的"),r("code",[v._v("--spring.profiles.active=pro")]),v._v("\n为 "),r("code",[v._v("--spring.profiles.active=dev")])]),v._v(" "),r("li",[v._v("使用Nginx 二级路径代理请一定使用Jpom 推荐nginx配置"),r("a",{attrs:{href:"https://jpom-docs.keepbx.cn/docs/index.html#/%E8%BE%85%E5%8A%A9%E9%85%8D%E7%BD%AE/nginx-config",target:"_blank",rel:"noopener noreferrer"}},[v._v("查看配置"),r("OutboundLink")],1)])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-5"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-5"}},[v._v("#")]),v._v(" 2.4.5")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-5"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-5"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】节点列表支持筛选（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("【Server】新增构建触发器（感谢@java 麦田英雄）")]),v._v(" "),r("li",[v._v("【Server】新增自动清理过量的构建历史记录和文件（感谢@Sawyer、@Jvmlz）")]),v._v(" "),r("li",[v._v("【Server】构建支持ssh发布（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("【Server】节点新增分组属性，方便多节点快速筛选（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("新增windows快速升级")]),v._v(" "),r("li",[v._v("【Server】layui升级到最新版，文件上传支持进度条")]),v._v(" "),r("li",[v._v("新增节点内存、cpu、硬盘使用情况采集报表（感谢@￡天空之城～龙）")]),v._v(" "),r("li",[v._v("节点首页新增快速结束进程方式")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-5"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-5"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】节点分发需要节点数大于二（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("修复未加载到tools.jar判断（感谢@java-磊）")]),v._v(" "),r("li",[v._v("【Server】控制台新增自动清屏开关（感谢@Jvmlz）")]),v._v(" "),r("li",[v._v("上传文件大小限制，配置化")]),v._v(" "),r("li",[v._v("【Server】构建文件copy忽略隐藏文件")]),v._v(" "),r("li",[v._v("【Server】不能清除错误进程缓存（感谢@java 李道甫）")]),v._v(" "),r("li",[v._v("【Agent】长时间运行jpom无法监控到项目运行状态（感谢@java 李道甫、@洋芋）")]),v._v(" "),r("li",[v._v("【Server】节点分发编辑支持修改分发后的操作")]),v._v(" "),r("li",[v._v("【Agent】脚本模板跟随系统编码")]),v._v(" "),r("li",[v._v("【Server】tomcat控制台删除日志文件错误（感谢@Java-iwen）")]),v._v(" "),r("li",[v._v("【Agent】自动备份控制台日志表达式为none,不生成日志备份")]),v._v(" "),r("li",[v._v("【Server】角色授权编辑权限不能创建数据（感谢@Lostshadow）")]),v._v(" "),r("li",[v._v("【Server】tomcat动态权限配置不正确（感谢@Lostshadow）")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-4"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-4"}},[v._v("#")]),v._v(" 2.4.4")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-6"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-6"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Agent】添加对SpringBoot war包支持")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-6"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-6"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】新项目打开项目控制台页面报错（感谢@黄战虎）")]),v._v(" "),r("li",[v._v("【Server】修改邮箱不及时生效问题（感谢@WeChat）")]),v._v(" "),r("li",[v._v("【Server】修复发布构建产物路径bug（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("优化执行命令方式")]),v._v(" "),r("li",[v._v("脚本模板在linux 不添加权限（采用sh 方式执行）")]),v._v(" "),r("li",[v._v("【Server】修复添加节点分发项目报错的数据异常（感谢@WeChat）")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-3"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-3"}},[v._v("#")]),v._v(" 2.4.3")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-7"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-7"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("SpringBoot 升级到2.1.x")]),v._v(" "),r("li",[v._v("【Server】velocity模板引擎升级为thymeleaf")]),v._v(" "),r("li",[v._v("【Server】构建支持svn类型仓库（感谢@群友 .）")]),v._v(" "),r("li",[v._v("插件端自动注册到服务端（感谢@群友 .）")]),v._v(" "),r("li",[v._v("新增在线修改配置并可及时重启")]),v._v(" "),r("li",[v._v("新增WebSSH 管理功能")]),v._v(" "),r("li",[v._v("【Server】用户新增邮箱和钉钉群webhook 属性")]),v._v(" "),r("li",[v._v("【Server】监控报警通知改为联系人")]),v._v(" "),r("li",[v._v("【Server】引人netty插件（感谢@夜空中最亮的星）")]),v._v(" "),r("li",[v._v("支持docker 容器运行（感谢@24k）")]),v._v(" "),r("li",[v._v("【Server】 新增清空构建代码（解决代码冲突）(感谢@xieyue200810)")]),v._v(" "),r("li",[v._v("搭建插件化基础架构")]),v._v(" "),r("li",[v._v("用户权限重构，使用角色支持更细粒的权限控制")]),v._v(" "),r("li",[v._v("新增ssh快速部署插件端")]),v._v(" "),r("li",[v._v("新增一键安装脚本"),r("a",{attrs:{href:"https://gitee.com/dromara/Jpom/#%E4%B8%80%E9%94%AE%E5%AE%89%E8%A3%85",target:"_blank",rel:"noopener noreferrer"}},[v._v("详情"),r("OutboundLink")],1)])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-7"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-7"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】未登录重定向带入参数")]),v._v(" "),r("li",[v._v("【Server】页面登录方法调整支持自定义事件登录")]),v._v(" "),r("li",[v._v("【Server】删除节点、分发验证是否存在关联数据,分发释放分发关系")]),v._v(" "),r("li",[v._v("项目白名单目录调整为属性")]),v._v(" "),r("li",[v._v("【Server】编辑用户回显节点选中错乱问题")]),v._v(" "),r("li",[v._v("调整linux管理命令脚本防止在线升级产生tail 进程")]),v._v(" "),r("li",[v._v("【Agent】插件端的脚本模板路径切换到数据目录下")]),v._v(" "),r("li",[v._v("【Agent】Windows异步执行命令调整不使用[INHERIT]（防止插件端进程阻塞）")]),v._v(" "),r("li",[v._v("【Server】分页查询会存在字段not found")]),v._v(" "),r("li",[v._v("【Server】构建命令不能包含删除命令（del,rd,rm）")]),v._v(" "),r("li",[v._v("支持配置初始读取日志文件最后多少行【log.intiReadLine】(感谢@夜空中最亮的星)")]),v._v(" "),r("li",[v._v("优化节点首页饼状图统计")]),v._v(" "),r("li",[v._v("取消用户输入脚本模板id")]),v._v(" "),r("li",[v._v("重定向支持自动识别 Proto（解决http-> https iframe报错）")]),v._v(" "),r("li",[v._v("构建执行命令存在错误只是提示，不取消执行（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("构建打包目录没有文件名异常（感谢@Sawyer）")]),v._v(" "),r("li",[v._v("修改为专属包名【io.jpom】")])]),v._v(" "),r("h3",{attrs:{id:"升级注意事项"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#升级注意事项"}},[v._v("#")]),v._v(" 升级注意事项")]),v._v(" "),r("ol",[r("li",[v._v("由于修改包名引起：如果在旧版本中使用过在线升级，本次升级需要手动上传jar到到服务器中执行命令升级，并且删除旧包并且覆盖管理命令文件")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-2"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-2"}},[v._v("#")]),v._v(" 2.4.2")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-8"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-8"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("新增实时查看tomcat日志")]),v._v(" "),r("li",[v._v("【Server】分发包支持更多压缩格式")]),v._v(" "),r("li",[v._v("页面菜单采用json文件配置(支持二级菜单)")]),v._v(" "),r("li",[v._v("【Server】分发包支持更多类型的压缩格式")]),v._v(" "),r("li",[v._v("【Server】节点支持配置请求超时时间")]),v._v(" "),r("li",[v._v("支持配置是否记录请求、响应日志【consoleLog.reqXss、consoleLog.reqResponse】")]),v._v(" "),r("li",[v._v("新增日志记录最大记录条数【默认100000】")]),v._v(" "),r("li",[v._v("【Server】layui 升级到2.5.4")]),v._v(" "),r("li",[v._v("【Server】新增项目监控功能")]),v._v(" "),r("li",[v._v("【Server】新增在线构建项目功能")]),v._v(" "),r("li",[v._v("【Server】新增查看项目实际执行的命令行")]),v._v(" "),r("li",[v._v("【Server】新增分发日志")]),v._v(" "),r("li",[v._v("新增清空文件缓存、临时数据缓存")]),v._v(" "),r("li",[v._v("在线查看、下载Jpom运行日志(windows不能实时查看)")]),v._v(" "),r("li",[v._v("新增linux在线升级")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-8"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-8"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Agent】logBack页面最后修改时间不能正确显示（感谢@JAVA jesion）")]),v._v(" "),r("li",[v._v("【Agent】nginx修改内容截断，不正确情况（感谢@JAVA jesion）")]),v._v(" "),r("li",[v._v("【Agent】nginx、脚本模板保存内容xss标签还原")]),v._v(" "),r("li",[v._v("【Server】节点分发页面的交互方式")]),v._v(" "),r("li",[v._v("【Server】页面菜单分类整理")]),v._v(" "),r("li",[v._v("【Agent】修复SpringBoot相对文件夹下无法读取配置问题")]),v._v(" "),r("li",[v._v("【Agent】缓存异常的jvm进程，避免卡死状态（感谢@java 李道甫）")]),v._v(" "),r("li",[v._v("【Server】节点分发状态更新到所有节点状态")]),v._v(" "),r("li",[v._v("【Server】节点分发白名单独立页面配置")]),v._v(" "),r("li",[v._v("【Server】项目控制台未运行能查看已经存在的最后的日志")]),v._v(" "),r("li",[v._v("【Agent】删除阿里云oss构建，已经有在线构建功能代替")]),v._v(" "),r("li",[v._v("【Server】修改证书名称和导出证书问题")]),v._v(" "),r("li",[v._v("打包方式改为一个可执行的jar")]),v._v(" "),r("li",[v._v("【Server】解决编辑用户页面json转换异常（感谢@JAVA jesion）")]),v._v(" "),r("li",[v._v("分发项目新增清空发布防止新旧jar冲突")]),v._v(" "),r("li",[v._v("【Server】优化节点列表页面加载速度[不显示运行的项目数]（感谢@java 李道甫）")]),v._v(" "),r("li",[v._v("【Agent】调整启动，关闭进程命令执行方式[解决重启不能监控项目状态]（感谢@java 李道甫）")]),v._v(" "),r("li",[v._v("【Agent】调整进程标识传入参数到JVM参数中，避免和部分框架冲突（感谢@java-杨侨）")])]),v._v(" "),r("h3",{attrs:{id:"升级注意事项-2"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#升级注意事项-2"}},[v._v("#")]),v._v(" 升级注意事项")]),v._v(" "),r("ol",[r("li",[v._v("需要删除旧lib目录所有文件")]),v._v(" "),r("li",[v._v("覆盖旧版管理命令文件")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-1"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-1"}},[v._v("#")]),v._v(" 2.4.1")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-9"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-9"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("【Agent】新增线程列表监控(感谢@其锋)")]),v._v(" "),r("li",[v._v("【Agent】新增节点脚本模板(感谢@其锋)")]),v._v(" "),r("li",[v._v("【Server】新增所有页面添加公共Html代码")]),v._v(" "),r("li",[v._v("新增Tomcat管理")]),v._v(" "),r("li",[v._v("【Agent】导入证书文件新增对cer、crt文件支持")]),v._v(" "),r("li",[v._v("【Agent】导入项目包时指出多压缩包[tar|bz2|gz|zip|tar.bz2|tar.gz] (感谢@群友)")]),v._v(" "),r("li",[v._v("【Agent】新增配置控制台日志文件编码格式（详情查看extConfig.yml）")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-9"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-9"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("【Server】节点首页，右上角管理路径错误(感谢@其锋)")]),v._v(" "),r("li",[v._v("【Server】查看用户操作日志支持筛选用户")]),v._v(" "),r("li",[v._v("【Server】页面数据路径权限判断修复(感谢@Will)")]),v._v(" "),r("li",[v._v("【Agent】优化获取进程监听端口的，防止卡死")]),v._v(" "),r("li",[v._v("文件的读写锁不使用 synchronized关键字提高效率")]),v._v(" "),r("li",[v._v("优化数据id字段的输入限制，数字+字母+中划线+下划线（感谢@JAVA jesion）")]),v._v(" "),r("li",[v._v("【Agent】连接JVM失败则跳过（感谢@JAVA jesion）")]),v._v(" "),r("li",[v._v("【Server】编辑用户页面优化选择授权项目")]),v._v(" "),r("li",[v._v("【Agent】项目Jvm参数和Args参数兼容回车符（感谢@牛旺）")])]),v._v(" "),r("hr"),v._v(" "),r("h2",{attrs:{id:"_2-4-0"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#_2-4-0"}},[v._v("#")]),v._v(" 2.4.0")]),v._v(" "),r("h3",{attrs:{id:"🐣-新增功能-10"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐣-新增功能-10"}},[v._v("#")]),v._v(" 🐣 新增功能")]),v._v(" "),r("ol",[r("li",[v._v("首页进程列表显示属于Jpom项目名称(感谢@〓下页)")]),v._v(" "),r("li",[v._v("多节点统一管理（插件模式）")]),v._v(" "),r("li",[v._v("证书解析支持cer 证书(感谢@JAVA jesion)")]),v._v(" "),r("li",[v._v("新增记录用户操作日志[采用H2数据库]（感谢@〓下页）")]),v._v(" "),r("li",[v._v("节点分发功能、合并管理项目(感谢@其锋)")])]),v._v(" "),r("h3",{attrs:{id:"🐞-解决bug、优化功能-10"}},[r("a",{staticClass:"header-anchor",attrs:{href:"#🐞-解决bug、优化功能-10"}},[v._v("#")]),v._v(" 🐞 解决BUG、优化功能")]),v._v(" "),r("ol",[r("li",[v._v("解析端口信息兼容"),r("code",[v._v(":::8084")]),v._v("(感谢@Agoni 、)")]),v._v(" "),r("li",[v._v("进程id解析端口、解析项目名称带缓存")]),v._v(" "),r("li",[v._v("项目分组变更，项目列表及时刷新(感谢@〓下页)")]),v._v(" "),r("li",[v._v("批量上传文件数量进度显示(感谢@群友)")]),v._v(" "),r("li",[v._v("linux udp端口信息解析失败(感谢@Ruby)")]),v._v(" "),r("li",[v._v("jar模式读取主jar包错误(感谢@其锋)")])])])}),[],!1,null,null,null);_.default=t.exports}}]);