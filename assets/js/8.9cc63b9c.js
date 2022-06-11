(window.webpackJsonp=window.webpackJsonp||[]).push([[8],{505:function(v,_,a){"use strict";a.r(_);var s=a(31),t=Object(s.a)({},(function(){var v=this,_=v.$createElement,a=v._self._c||_;return a("ContentSlotsDistributor",{attrs:{"slot-key":v.$parent.slotKey}},[a("h2",{attrs:{id:"服务端-server"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#服务端-server"}},[v._v("#")]),v._v(" 服务端（Server）")]),v._v(" "),a("p",[v._v("服务端可以理解为 Jpom 的一个总控系统，用户直接访问对应系统管理所有节点。一般情况服务端只需要安装一次即可，服务端可以管理多个插件端")]),v._v(" "),a("p",[v._v("以下功能位于服务端（和插件端没有直接关系）：")]),v._v(" "),a("ol",[a("li",[v._v("仓库管理")]),v._v(" "),a("li",[v._v("构建管理")]),v._v(" "),a("li",[v._v("SSH 管理")]),v._v(" "),a("li",[v._v("监控管理")]),v._v(" "),a("li",[v._v("账号管理")])]),v._v(" "),a("p",[v._v("温馨提示：在一定使用场景下只需要安装服务器即可，不一定需要搭配插件端才能使用 Jpom，如果使用 ssh 管理、构建发布到 ssh 中")]),v._v(" "),a("h2",{attrs:{id:"插件端-agent"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#插件端-agent"}},[v._v("#")]),v._v(" 插件端（Agent）")]),v._v(" "),a("p",[v._v("插件端可以理解为需要在 Jpom 被管理到服务器的守护程序，插件端主要是去监控对应服务器一些基本信息，保存一些项目信息已经对应项目的相关文件等。")]),v._v(" "),a("p",[v._v("已经安装插件端的服务器在 Jpom 中也可以称为节点")]),v._v(" "),a("p",[v._v("温馨提示：插件端存在账号密码，插件端端账号密码和服务端端管理员账号密码是不同的奥！")]),v._v(" "),a("h2",{attrs:{id:"物理节点"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#物理节点"}},[v._v("#")]),v._v(" 物理节点")]),v._v(" "),a("p",[v._v("安装过 Jpom 插件端的服务器可以称为物理节点")]),v._v(" "),a("h2",{attrs:{id:"逻辑节点"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#逻辑节点"}},[v._v("#")]),v._v(" 逻辑节点")]),v._v(" "),a("p",[v._v("安装过 Jpom 插件端的服务器添加在 Jpom 服务端中的节点列表里面称为逻辑节点")]),v._v(" "),a("p",[v._v("温馨提示：一个物理节点可以添加在多个工作空间")]),v._v(" "),a("h2",{attrs:{id:"工作空间"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#工作空间"}},[v._v("#")]),v._v(" 工作空间")]),v._v(" "),a("ul",[a("li",[v._v("Jpom 中使用工作空间代替角色来控制数据权限，一个账号可以绑定多个工作空间，并且可以设置单个工作空间的功能权限")]),v._v(" "),a("li",[v._v("Jpom 中主要使用工作空间隔离数据权限，建议在实际部署中使用工作空间来隔离权限。")])]),v._v(" "),a("p",[v._v("温馨提示：一个物理节点可以添加在多个工作空间")]),v._v(" "),a("p",[v._v("如：")]),v._v(" "),a("ol",[a("li",[v._v("测试环境")]),v._v(" "),a("li",[v._v("线上环境")]),v._v(" "),a("li",[v._v("...")])]),v._v(" "),a("p",[v._v("如：")]),v._v(" "),a("ol",[a("li",[v._v("前端开发")]),v._v(" "),a("li",[v._v("后端开发")]),v._v(" "),a("li",[v._v("...")])]),v._v(" "),a("h2",{attrs:{id:"端口-2122、2123"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#端口-2122、2123"}},[v._v("#")]),v._v(" 端口：2122、2123")]),v._v(" "),a("p",[v._v("Jpom 中默认使用到端口有 2122、2123")]),v._v(" "),a("ul",[a("li",[v._v("2122 是服务端默认端口")]),v._v(" "),a("li",[v._v("2123 是插件端默认端口（添加节点地址时候需要填写对应到 IP:PORT）")])]),v._v(" "),a("blockquote",[a("p",[v._v("特别声明：不建议将插件端端口开放到外网可以访问、建议服务端和插件端通信都使用内网")])]),v._v(" "),a("p",[v._v("如何修改程序运行端口:")]),v._v(" "),a("ul",[a("li",[v._v("修改管理程序命令文件中 "),a("code",[v._v("--server.port=2122")]),v._v("、"),a("code",[v._v("--server.port=2123")])])]),v._v(" "),a("h2",{attrs:{id:"数据目录"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#数据目录"}},[v._v("#")]),v._v(" 数据目录")]),v._v(" "),a("p",[v._v("Jpom 中数据目录是指存储数据的文件,一般会存储一些程序相关文件,运行中产生的临时文件。")]),v._v(" "),a("ul",[a("li",[v._v("服务端还会存储构建相关文件（构建仓库文件、构建产物文件等）")]),v._v(" "),a("li",[v._v("插件端还会存储项目文件备份相关文件")])]),v._v(" "),a("p",[v._v("jpom 数据存储路径：")]),v._v(" "),a("ul",[a("li",[v._v("如果调试模式运行默认路径为【${user.home}/jpom/】")]),v._v(" "),a("li",[v._v("安装运行默认为jar包文件的父级")]),v._v(" "),a("li",[v._v("当然用户还可以自定义数据目录，修改数据目录位置请参考："),a("code",[v._v("extConfig.yml")])])]),v._v(" "),a("h2",{attrs:{id:"服务端数据目录文件夹说明"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#服务端数据目录文件夹说明"}},[v._v("#")]),v._v(" 服务端数据目录文件夹说明")]),v._v(" "),a("div",{staticClass:"language- line-numbers-mode"},[a("pre",{pre:!0,attrs:{class:"language-text"}},[a("code",[v._v(".\n├── data\n├────── build =>                                      构建相关文件夹\n├──────────── a7edf3cff8d542d3bf5758f9ab9b9a32 =>     单个构建存储相关文件夹\n├──────────────── history =>                          需要执行的脚本\n├───────────────────── #1 =>                          单次执行记录相关文件夹\n├─────────────────────── info.log =>                  构建日志\n├─────────────────────── result =>                    构建产物存储文件夹\n├──────────────── source =>                           构建的仓库文件夹\n├────── script =>                                     服务端脚本文件夹\n├──────────── a7edf3cff8d542d3bf5758f9ab9b9a32 =>     脚本对应相关文件夹\n├──────────────── script.sh =>                        需要执行的脚本\n├──────────────── log =>                              执行日志\n├──────────── xxxx =>                                 其他脚本信息\n├────── command_log => ssh                            脚本执行记录\n├──────────── a7edf3cff8d542d3bf5758f9ab9b9a32 =>     单个脚本相关文件夹\n├──────────────── 004d320e00794d63831d63e3a5ed4344 => 脚本批量执行日志\n├──────────── ....... =>                              更多脚本文件\n├────── backup_old_data =>                            2.7.x 升级 2.8.x 迁移数据备份文件夹\n├────── agent =>                                      服务端保留插件的 jar 包文件夹\n├────── remote_version.json =>                        远程更新，获取信息版本缓存文件信息\n├────── temp =>                                       临时文件，上传文件缓存目录，运行中产生\n├── db =>                                             服务端数据存储文件夹\n├────── backup =>                                     备份数据文件夹\n├──────────── 20220609120000.sql =>                   sql 文件\n├──────────── .......sql =>                           更多 sql 文件\n├────── recover_backup =>                             执行修复数据后的原数据库备份文件夹\n├──────────── 2022-06-10 09/16/38 =>                  单次备份文件夹\n├──────────────── Server.mv.db =>                     数据库文件\n├──────────────── Server.trace.db =>                  数据库文件\n├──────────── ...... =>                               单次备份文件夹\n├────── execute.init.sql.log  =>                      记录已经初始化过 sql hash,避免重复初始化数据库\n├────── Server.mv.db =>                               数据库文件\n└────── Server.trace.db =>                            数据库文件\n")])]),v._v(" "),a("div",{staticClass:"line-numbers-wrapper"},[a("span",{staticClass:"line-number"},[v._v("1")]),a("br"),a("span",{staticClass:"line-number"},[v._v("2")]),a("br"),a("span",{staticClass:"line-number"},[v._v("3")]),a("br"),a("span",{staticClass:"line-number"},[v._v("4")]),a("br"),a("span",{staticClass:"line-number"},[v._v("5")]),a("br"),a("span",{staticClass:"line-number"},[v._v("6")]),a("br"),a("span",{staticClass:"line-number"},[v._v("7")]),a("br"),a("span",{staticClass:"line-number"},[v._v("8")]),a("br"),a("span",{staticClass:"line-number"},[v._v("9")]),a("br"),a("span",{staticClass:"line-number"},[v._v("10")]),a("br"),a("span",{staticClass:"line-number"},[v._v("11")]),a("br"),a("span",{staticClass:"line-number"},[v._v("12")]),a("br"),a("span",{staticClass:"line-number"},[v._v("13")]),a("br"),a("span",{staticClass:"line-number"},[v._v("14")]),a("br"),a("span",{staticClass:"line-number"},[v._v("15")]),a("br"),a("span",{staticClass:"line-number"},[v._v("16")]),a("br"),a("span",{staticClass:"line-number"},[v._v("17")]),a("br"),a("span",{staticClass:"line-number"},[v._v("18")]),a("br"),a("span",{staticClass:"line-number"},[v._v("19")]),a("br"),a("span",{staticClass:"line-number"},[v._v("20")]),a("br"),a("span",{staticClass:"line-number"},[v._v("21")]),a("br"),a("span",{staticClass:"line-number"},[v._v("22")]),a("br"),a("span",{staticClass:"line-number"},[v._v("23")]),a("br"),a("span",{staticClass:"line-number"},[v._v("24")]),a("br"),a("span",{staticClass:"line-number"},[v._v("25")]),a("br"),a("span",{staticClass:"line-number"},[v._v("26")]),a("br"),a("span",{staticClass:"line-number"},[v._v("27")]),a("br"),a("span",{staticClass:"line-number"},[v._v("28")]),a("br"),a("span",{staticClass:"line-number"},[v._v("29")]),a("br"),a("span",{staticClass:"line-number"},[v._v("30")]),a("br"),a("span",{staticClass:"line-number"},[v._v("31")]),a("br"),a("span",{staticClass:"line-number"},[v._v("32")]),a("br"),a("span",{staticClass:"line-number"},[v._v("33")]),a("br"),a("span",{staticClass:"line-number"},[v._v("34")]),a("br")])]),a("h2",{attrs:{id:"jps-命令异常"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#jps-命令异常"}},[v._v("#")]),v._v(" Jps 命令异常")]),v._v(" "),a("p",[v._v("Jpom 针对 Java 程序状态监控采用 jps 命令，jps 在 jdk 中都默认包含一般不需要额外配置，如果出现插件端中提示 jps 命令异常等相关错误提示。")]),v._v(" "),a("ul",[a("li",[v._v("需要检查一下在服务器中能正常执行 jps 命令,不能正常执行需要检查 jdk 是否完整")]),v._v(" "),a("li",[v._v("需要检查插件端是否能正常加载环境变量")]),v._v(" "),a("li",[v._v("需要检查服务器中是否存在多个 jdk 环境，如果存在多个需要确认使用的 jdk 是否正确（特殊情况下可以在 agent.sh、agent.bat 中指定使用的 jdk）")]),v._v(" "),a("li",[v._v("linux 服务器建议使用绝对路径启动插件端 如：sh /xxxx/xxxx/Agent.sh")]),v._v(" "),a("li",[v._v("windows 服务器需要检查 Agent.bat 编码格式是否匹配，windows 环境中建议使用 GB2312 编码格式")])]),v._v(" "),a("h2",{attrs:{id:"防火墙、安全组"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#防火墙、安全组"}},[v._v("#")]),v._v(" 防火墙、安全组")]),v._v(" "),a("p",[v._v("当出现服务都正常，但是无法访问都时候需要检查一下 当前网络环境下和对应都 IP(插件端、服务端) 是否畅通。")]),v._v(" "),a("p",[v._v("如果 IP 都是畅通情况下需要检查一下 服务器是否配置了防火墙规则没有开放对应端口（2122）、或者服务端的服务器中的网络环境是否能访问您配置的插件端 IP 和 端口")]),v._v(" "),a("p",[v._v("注意 linux 系统中防火墙可能存在多种：Firewall、Iptables，再检查防火墙配置时候需要都检查一下")]),v._v(" "),a("h2",{attrs:{id:"在线构建"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#在线构建"}},[v._v("#")]),v._v(" 在线构建")]),v._v(" "),a("p",[v._v("在线构建是通过 Jpom 自动拉取仓库代码（支持 git、svn 仓库）到服务端本地目录，执行对应到命令（"),a("code",[v._v("构建命令")]),v._v("）后将仓库目录下面到指定文件或者文件夹（"),a("code",[v._v("构建产物")]),v._v("）执行发布操作")]),v._v(" "),a("p",[v._v("构建也将产生构建历史，构建历史可以用于回滚、查看对应构建日志或者下载构建产物功能")]),v._v(" "),a("p",[v._v("构建可以搭配 "),a("code",[v._v("webhook")]),v._v(" 来实现提交代码后自动触发构建、同时也支持定时（"),a("code",[v._v("cron")]),v._v("）构建来实现自动触发构建")]),v._v(" "),a("ul",[a("li",[v._v("在使用在线构建提示相关命令未找到或者不能执行 ("),a("code",[v._v("command not found: xxxx")]),v._v("、"),a("code",[v._v("xxxx command not found")]),v._v(")，请检查服务端所在的服务器中是否存在对应环境")]),v._v(" "),a("li",[v._v("构建依赖的是系统环境，如果需要 maven 或者 node 需要服务端所在的服务器中有对应插件，如果已经启动服务端再安装的对应环境需要通过命令行重启服务端后才生效。")]),v._v(" "),a("li",[v._v("构建是在服务端执行，依赖的服务端环境和插件端没有直接关联。")]),v._v(" "),a("li",[v._v("构建需要占用的服务器资源可能较高，需要根据实际情况提前预留配置对应的 CPU 和内存空间，反则可能出现 OOM 情况")]),v._v(" "),a("li",[v._v("构建日志提示："),a("code",[v._v("xxx 不存在、处理构建产物失败")]),v._v(" 请优先检查构建命令执行结果是否如预期执行结果一致产生对应的文件,再检查构建产物目录填写是否正常，请注意构建产物目录是填写仓库下面的相对路径：如果 "),a("code",[v._v("dist")]),v._v("\n、"),a("code",[v._v("/target/xxxx.jar")])])]),v._v(" "),a("h2",{attrs:{id:"产物目录"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#产物目录"}},[v._v("#")]),v._v(" 产物目录")]),v._v(" "),a("p",[v._v("在添加构建时候需要填写产物目录。")]),v._v(" "),a("p",[v._v("这里产物目录是指：执行构建命令后执行发布操作时发布指定文件或者文件夹到对应位置的目录")]),v._v(" "),a("p",[v._v("这里的目录注意一定是仓库路径下面的相对目录")]),v._v(" "),a("p",[v._v("比如：")]),v._v(" "),a("ul",[a("li",[v._v("构建 java 项目成功后会生成 target 目录，一般场景都是需要 target 目录下面的某个文件，那么填写："),a("code",[v._v("/target/xxx.jar")])]),v._v(" "),a("li",[v._v("如果是多层级目录则需要填写完整路径如果："),a("code",[v._v("/xxxx/target/xxxx.jar")])]),v._v(" "),a("li",[v._v("构建 vue 项目成功后会生成 dist 目录，那么直接填写 "),a("code",[v._v("dist")]),v._v(" 即可")])]),v._v(" "),a("p",[v._v("这里可以添加文件夹或者文件，如果填写文件夹则会自动发布整个文件到对应位置，如果填写文件则只发布对应到文件")]),v._v(" "),a("p",[v._v("产物目录目前支持模糊匹配： 支持通配符(AntPathMatcher)")]),v._v(" "),a("ul",[a("li",[a("code",[v._v("? 匹配一个字符")])]),v._v(" "),a("li",[a("code",[v._v("* 匹配零个或多个字符")])]),v._v(" "),a("li",[a("code",[v._v("** 匹配路径中的零个或多个目录")])])]),v._v(" "),a("p",[v._v("模糊匹配示例："),a("code",[v._v("/target/xxx-1.0.?.jar")]),v._v("、"),a("code",[v._v("/target/*.jar")])]),v._v(" "),a("p",[v._v("注意：目前模糊匹配只会匹配到一个结果，如果匹配到多个结果将取第一个结果")]),v._v(" "),a("h2",{attrs:{id:"非阻塞命令"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#非阻塞命令"}},[v._v("#")]),v._v(" 非阻塞命令")]),v._v(" "),a("p",[v._v("在 Jpom 中大部分命令都需要使用非阻塞：")]),v._v(" "),a("ul",[a("li",[v._v("构建命令")]),v._v(" "),a("li",[v._v("构建本地发布命令")]),v._v(" "),a("li",[v._v("构建 SSH 发布命令")]),v._v(" "),a("li",[v._v("SSH 模版命令")]),v._v(" "),a("li",[v._v("脚本模版命令")])]),v._v(" "),a("p",[v._v("非阻塞命令指执行命令后会自动退出而不是一直阻塞窗口需要按 ctrl+c 才能退出命令窗口、或者需要输入交互都命令")]),v._v(" "),a("p",[v._v("阻塞命令举例：")]),v._v(" "),a("ol",[a("li",[a("code",[v._v("yum intall xxxxx")]),v._v(", 执行这样的命令需要用户确认输入才能继续执行，这样的命令就不能在 jpom 上述功能相关命令中使用，需要修改为 "),a("code",[v._v("yum install -y xxxxx")])]),v._v(" "),a("li",[a("code",[v._v("java -jar xxx.jar")]),v._v(", 执行这样的命令会阻塞窗口，那么在 linux 中可以使用 "),a("code",[v._v("nohup")]),v._v(" 来解决，修改为："),a("code",[v._v("nohup java -jar xxx.jar >>xxx.log 2>&1 &")])]),v._v(" "),a("li",[a("code",[v._v("npm run serve")]),v._v(",执行这样的命令窗口将一直阻塞，那么需要修改为 "),a("code",[v._v("npm run build")])])]),v._v(" "),a("h2",{attrs:{id:"java、javaw-命令"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#java、javaw-命令"}},[v._v("#")]),v._v(" java、javaw 命令")]),v._v(" "),a("p",[v._v("通常情况都是使用 java 命令来启动 java 项目,如: "),a("code",[v._v("java -jar xxxx.jar")])]),v._v(" "),a("p",[v._v("Jpom 中在 windows 环境来启动 java 项目（包括 Server.bat、Agent.bat） 因为 windows 没有 nohup 命令，使用 javaw 可以实现类似 nohup 效果并且能将控制台日志输出到指定文件")]),v._v(" "),a("h2",{attrs:{id:"jpom-中多种脚本对比"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#jpom-中多种脚本对比"}},[v._v("#")]),v._v(" Jpom 中多种脚本对比")]),v._v(" "),a("table",[a("thead",[a("tr",[a("th",[v._v("脚本类型")]),v._v(" "),a("th",[v._v("存储方式")]),v._v(" "),a("th",[v._v("执行方式")]),v._v(" "),a("th",[v._v("是否支持批量执行")]),v._v(" "),a("th",[v._v("是否支持批量编辑")]),v._v(" "),a("th",[v._v("是否支持定时执行")])])]),v._v(" "),a("tbody",[a("tr",[a("td",[v._v("脚本列表")]),v._v(" "),a("td",[v._v("服务端")]),v._v(" "),a("td",[v._v("在服务端执行")]),v._v(" "),a("td",[v._v("否")]),v._v(" "),a("td",[v._v("否")]),v._v(" "),a("td",[v._v("支持 cron")])]),v._v(" "),a("tr",[a("td",[v._v("ssh 命令脚本")]),v._v(" "),a("td",[v._v("服务端")]),v._v(" "),a("td",[v._v("ssh 远程执行")]),v._v(" "),a("td",[v._v("是")]),v._v(" "),a("td",[v._v("否")]),v._v(" "),a("td",[v._v("支持 cron")])]),v._v(" "),a("tr",[a("td",[v._v("节点脚本")]),v._v(" "),a("td",[v._v("节点(插件端)")]),v._v(" "),a("td",[v._v("在节点执行")]),v._v(" "),a("td",[v._v("否")]),v._v(" "),a("td",[v._v("是 （使用服务端脚本分发）")]),v._v(" "),a("td",[v._v("支持 cron")])])])]),v._v(" "),a("h2",{attrs:{id:"jpom-中的白名单说明"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#jpom-中的白名单说明"}},[v._v("#")]),v._v(" Jpom 中的白名单说明")]),v._v(" "),a("blockquote",[a("p",[v._v("Jpom 中的白名单的由来，由于项目管理都需要对项目的文件进行管理。在创建项目的时候需要确定项目的相关文件存放的路径。\n那么此时由用户决定存放到哪里，显得有点冒然（不安全，有点随意）。因为服务器中有些路径已经存放重要配置文件，此时项目路径相同那么必然没有任何安全性")])]),v._v(" "),a("blockquote",[a("p",[v._v("为什么是白名单而不是配置黑名单（禁止名单）：假设设置黑名单、如果设置黑名单那么没有办法最快速收集用户不同服务器中重要文件路径，此方法也显得不合适")])]),v._v(" "),a("p",[v._v("需要配置白名单的地方有如下：")]),v._v(" "),a("ul",[a("li",[v._v("节点项目目录白名单")]),v._v(" "),a("li",[v._v("节点 nginx 配置文件白名单")]),v._v(" "),a("li",[v._v("节点 ssl 证书目录白名单")]),v._v(" "),a("li",[v._v("节点分发白名单（和节点项目白名单类似）")]),v._v(" "),a("li",[v._v("SSH 授权管理目录白名单")])]),v._v(" "),a("h2",{attrs:{id:"jpom-的参数配置"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#jpom-的参数配置"}},[v._v("#")]),v._v(" Jpom 的参数配置")]),v._v(" "),a("blockquote",[a("p",[v._v("Jpom 中配置文件使用 SpringBoot 加载配置文件机制实现，配置文件中可以配置一些遵循 SpringBoot 的属性")])]),v._v(" "),a("p",[v._v("在项目运行的根路径下的"),a("code",[v._v("extConfig.yml")]),v._v("文件")]),v._v(" "),a("ol",[a("li",[v._v("插件端示例："),a("code",[v._v("extConfig.yml")])]),v._v(" "),a("li",[v._v("服务端示例："),a("code",[v._v("extConfig.yml")])])]),v._v(" "),a("h2",{attrs:{id:"demo-账号"}},[a("a",{staticClass:"header-anchor",attrs:{href:"#demo-账号"}},[v._v("#")]),v._v(" demo 账号")]),v._v(" "),a("p",[v._v("Jpom 系统中内置了 "),a("code",[v._v("demo")]),v._v(" 用户名为演示账号，演示账号存在很多限制，在自己部署后请非必要不要创建 "),a("code",[v._v("demo")]),v._v(" 用户")])])}),[],!1,null,null,null);_.default=t.exports}}]);