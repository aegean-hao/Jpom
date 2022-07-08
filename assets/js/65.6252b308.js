(window.webpackJsonp=window.webpackJsonp||[]).push([[65],{559:function(s,a,t){"use strict";t.r(a);var n=t(31),e=Object(n.a)({},(function(){var s=this,a=s.$createElement,t=s._self._c||a;return t("ContentSlotsDistributor",{attrs:{"slot-key":s.$parent.slotKey}},[t("h1",{attrs:{id:"jpom-中的白名单说明"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#jpom-中的白名单说明"}},[s._v("#")]),s._v(" Jpom 中的白名单说明")]),s._v(" "),t("blockquote",[t("p",[s._v("Jpom 中的白名单的由来，由于项目管理都需要对项目的文件进行管理。在创建项目的时候需要确定项目的相关文件存放的路径。\n那么此时由用户决定存放到哪里，显得有点冒然（不安全，有点随意）。因为服务器中有些路径已经存放重要配置文件，此时项目路径相同那么必然没有任何安全性")])]),s._v(" "),t("h5",{attrs:{id:"假设设置黑名单"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#假设设置黑名单"}},[s._v("#")]),s._v(" 假设设置黑名单")]),s._v(" "),t("blockquote",[t("p",[s._v("如果设置黑名单那么没有办法最快速收集用户不同服务器中重要文件路径，此方法也显得不合适")])]),s._v(" "),t("h3",{attrs:{id:"综上jpom-就使用白名单来管理项目相关的文件"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#综上jpom-就使用白名单来管理项目相关的文件"}},[s._v("#")]),s._v(" 综上Jpom 就使用白名单来管理项目相关的文件")]),s._v(" "),t("blockquote",[t("p",[s._v("那么在Jpom 中有那些地方需要用到白名单呢")])]),s._v(" "),t("h2",{attrs:{id:"_1-项目路径"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_1-项目路径"}},[s._v("#")]),s._v(" 1. 项目路径")]),s._v(" "),t("blockquote",[t("p",[s._v("项目路径白名单主要是决定不同项目存放的位置，【但是项目路径选择的白名单不是项目文件存放的实际位置】")])]),s._v(" "),t("blockquote",[t("p",[s._v("项目文件存放的实际路径是由【选择的项目路径白名单+项目Jar包】 组合而成的")])]),s._v(" "),t("div",{staticClass:"language- line-numbers-mode"},[t("pre",{pre:!0,attrs:{class:"language-text"}},[t("code",[s._v("# 举例说明项目白名单如何使用\n\n如果有4个项目需要部署到服务器中，但是4个项目又可以分为两大类型\n\n项目1、项目2、项目3、项目4\n\n可以分为：后台、接口\n\n【后台】：项目1、项目3\n\n【接口】：项目2、项目4\n\n那么推荐配置白名单：\n\n/project/admin/\n/project/api/\n\n那么在创建项目1、项目3的时候选择路径：/project/admin/\n\n那么在创建项目2、项目4的时候选择路径：/project/api/\n\n")])]),s._v(" "),t("div",{staticClass:"line-numbers-wrapper"},[t("span",{staticClass:"line-number"},[s._v("1")]),t("br"),t("span",{staticClass:"line-number"},[s._v("2")]),t("br"),t("span",{staticClass:"line-number"},[s._v("3")]),t("br"),t("span",{staticClass:"line-number"},[s._v("4")]),t("br"),t("span",{staticClass:"line-number"},[s._v("5")]),t("br"),t("span",{staticClass:"line-number"},[s._v("6")]),t("br"),t("span",{staticClass:"line-number"},[s._v("7")]),t("br"),t("span",{staticClass:"line-number"},[s._v("8")]),t("br"),t("span",{staticClass:"line-number"},[s._v("9")]),t("br"),t("span",{staticClass:"line-number"},[s._v("10")]),t("br"),t("span",{staticClass:"line-number"},[s._v("11")]),t("br"),t("span",{staticClass:"line-number"},[s._v("12")]),t("br"),t("span",{staticClass:"line-number"},[s._v("13")]),t("br"),t("span",{staticClass:"line-number"},[s._v("14")]),t("br"),t("span",{staticClass:"line-number"},[s._v("15")]),t("br"),t("span",{staticClass:"line-number"},[s._v("16")]),t("br"),t("span",{staticClass:"line-number"},[s._v("17")]),t("br"),t("span",{staticClass:"line-number"},[s._v("18")]),t("br"),t("span",{staticClass:"line-number"},[s._v("19")]),t("br"),t("span",{staticClass:"line-number"},[s._v("20")]),t("br"),t("span",{staticClass:"line-number"},[s._v("21")]),t("br")])]),t("h2",{attrs:{id:"_2-证书路径"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_2-证书路径"}},[s._v("#")]),s._v(" 2. 证书路径")]),s._v(" "),t("blockquote",[t("p",[s._v("证书路径白名单是决定用户上次的ssl 证书存放的路径")])]),s._v(" "),t("blockquote",[t("p",[s._v("证书文件实际存放的路径是由【选择的证书路径白名单+证书id+id.key(id.pem)】 组合而成的")])]),s._v(" "),t("h2",{attrs:{id:"_3-nginx路径"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_3-nginx路径"}},[s._v("#")]),s._v(" 3. Nginx路径")]),s._v(" "),t("blockquote",[t("p",[s._v("Nginx路径白名单是决定Jpom 程序会自动扫描对应目录下的 *.conf 文件还展示配置文件")])]),s._v(" "),t("h2",{attrs:{id:"_4-节点分发"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#_4-节点分发"}},[s._v("#")]),s._v(" 4. 节点分发")]),s._v(" "),t("blockquote",[t("p",[s._v("节点分发白名单是决定创建节点分发项目时候，项目的白名单路径（此处规则和项目路径白名单一致）")])]),s._v(" "),t("blockquote",[t("p",[s._v("单独管理节点分发白名单的目的是为了多节点的白名单信息同步")])]),s._v(" "),t("h1",{attrs:{id:"注意-为了系统安全白名单只允许系统管理配置-在节点第一次使用时候为了系统能正常使用需要添加一个项目的白名单路径"}},[t("a",{staticClass:"header-anchor",attrs:{href:"#注意-为了系统安全白名单只允许系统管理配置-在节点第一次使用时候为了系统能正常使用需要添加一个项目的白名单路径"}},[s._v("#")]),s._v(" 注意：为了系统安全白名单只允许系统管理配置，在节点第一次使用时候为了系统能正常使用需要添加一个项目的白名单路径")])])}),[],!1,null,null,null);a.default=e.exports}}]);