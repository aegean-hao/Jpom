import Vue from "vue";
import Router from "vue-router";

// NavigationDuplicated: Avoided redundant navigation to current location: "xxxx".
const originalPush = Router.prototype.push;

Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

Vue.use(Router);

const children = [
  {
    path: "/my-workspace",
    name: "my-workspace",
    component: () => import("../pages/layout/my-workspace"),
  },
  {
    path: "/node/list",
    name: "node-list",
    component: () => import("../pages/node/list"),
  },
  {
    path: "/docker/list",
    name: "docker-list",
    component: () => import("../pages/docker/list"),
  },
  {
    path: "/docker/swarm",
    name: "docker-swarm",
    component: () => import("../pages/docker/swarm/list"),
  },

  {
    path: "/node/search",
    name: "node-search",
    component: () => import("../pages/node/search"),
  },
  {
    path: "/node/script-all",
    name: "node-script-list-all",
    component: () => import("../pages/node/script-list"),
  },
  {
    path: "/script/script-list",
    name: "script-list-all",
    component: () => import("../pages/script/script-list"),
  },
  {
    path: "/script/script-log",
    name: "script-log",
    component: () => import("../pages/script/script-log"),
  },

  {
    path: "/ssh",
    name: "node-ssh",
    component: () => import("../pages/ssh/ssh"),
  },
  {
    path: "/ssh/command",
    name: "node-command",
    component: () => import("../pages/ssh/command"),
  },
  {
    path: "/ssh/command-log",
    name: "node-command-log",
    component: () => import("../pages/ssh/command-log"),
  },
  {
    path: "/dispatch/list",
    name: "dispatch-list",
    component: () => import("../pages/dispatch/list"),
  },
  {
    path: "/dispatch/log",
    name: "dispatch-log",
    component: () => import("../pages/dispatch/log"),
  },
  {
    path: "/dispatch/log-read",
    name: "dispatch-log-read",
    component: () => import("../pages/dispatch/logRead"),
  },

  {
    path: "/monitor/list",
    name: "monitor-list",
    component: () => import("../pages/monitor/list"),
  },
  {
    path: "/monitor/log",
    name: "monitor-log",
    component: () => import("../pages/monitor/log"),
  },
  {
    path: "/monitor/operate-log",
    name: "monitor-operate-log",
    component: () => import("../pages/monitor/operate-log"),
  },
  {
    path: "/repository/list",
    name: "repository-list",
    component: () => import("../pages/repository/list"),
  },
  {
    path: "/build/list-info",
    name: "build-list-info",
    component: () => import("../pages/build/list-info"),
  },
  {
    path: "/build/history",
    name: "build-history",
    component: () => import("../pages/build/history"),
  },
  {
    path: "/dispatch/white-list",
    name: "dispatch-white-list",
    component: () => import("../pages/dispatch/white-list"),
  },
  {
    path: "/script/env-list",
    name: "script-env-list",
    component: () => import("../pages/script/env"),
  },
  {
    path: "/tools/cron",
    name: "cron-tools",
    component: () => import("../pages/tools/cron"),
  },
  {
    path: "/file-manager/file-storage",
    name: "file-storage",
    component: () => import("../pages/file-manager/fileStorage/list"),
  },
  {
    path: "/file-manager/release-task",
    name: "file-storage-release-task",
    component: () => import("../pages/file-manager/release-task/list"),
  },
  {
    path: "/certificate/list",
    name: "/certificate-list",
    component: () => import("../pages/certificate/list"),
  },
];

const management = [
  {
    path: "/system/assets/machine-list",
    name: "system-machine-list",
    component: () => import("../pages/system/assets/machine/machine-list"),
  },
  {
    path: "/system/assets/ssh-list",
    name: "system-machine-ssh-list",
    component: () => import("../pages/system/assets/ssh/ssh-list"),
  },
  {
    path: "/system/assets/docker-list",
    name: "system-machine-docker-list",
    component: () => import("../pages/system/assets/docker/list"),
  },
  {
    path: "/system/assets/repository-list",
    name: "system-global-repository",
    component: () => import("../pages/repository/global-repository"),
  },
  {
    path: "/user/permission-group",
    name: "permission-group",
    component: () => import("../pages/user/permission-group"),
  },
  {
    path: "/user/list",
    name: "user-list",
    component: () => import("../pages/user"),
  },
  {
    path: "/operation/log",
    name: "operation-log",
    component: () => import("../pages/user/operation-log"),
  },
  {
    path: "/user/login-log",
    name: "user-login-log",
    component: () => import("../pages/user/user-login-log"),
  },
  // 工作空间
  {
    path: "/system/workspace",
    name: "system-workspace",
    component: () => import("../pages/system/workspace"),
  },
  {
    path: "/system/global-env",
    name: "global-env",
    component: () => import("../pages/system/global-env"),
  },
  {
    path: "/system/mail",
    name: "system-mail",
    component: () => import("../pages/system/mail"),
  },
  {
    path: "/system/oauth-config",
    name: "oauth-config",
    component: () => import("../pages/system/oauth-config"),
  },
  {
    path: "/system/cache",
    name: "system-cache",
    component: () => import("../pages/system/cache"),
  },
  {
    path: "/system/log",
    name: "system-log",
    component: () => import("../pages/system/log"),
  },
  {
    path: "/system/upgrade",
    name: "system-upgrade",
    component: () => import("../pages/system/upgrade"),
  },
  {
    path: "/system/config",
    name: "system-config",
    component: () => import("../pages/system/config"),
  },
  {
    path: "/system/ext-config",
    name: "ext-config",
    component: () => import("../pages/system/ext-config"),
  },
  // 数据库备份
  {
    path: "/system/backup",
    name: "system-backup",
    component: () => import("../pages/system/backup"),
  },
];

const router = new Router({
  mode: "hash",
  routes: [
    // {
    //   path: "/test",
    //   name: "test",
    //   component: () => import("../pages/test"),
    // },
    {
      path: "/login",
      name: "login",
      component: () => import("../pages/login"),
    },
    // 用于过渡页面（避免跳转到管理页面重复请求接口，oauth2）
    {
      path: "/",
      name: "home",
      component: () => import("../pages/layout/loading"),
    },
    {
      path: "/management",
      name: "management",
      component: () => import("../pages/layout"),
      redirect: "/node/list",
      children: children.map((item) => {
        const props = item.props || {};
        props.routerUrl = item.path;
        item.props = props;
        return item;
      }),
    },
    {
      path: "/install",
      name: "install",
      component: () => import("../pages/login/install"),
    },
    {
      path: "/full-terminal",
      name: "full-terminal",
      component: () => import("../pages/ssh/full-terminal"),
    },
    {
      path: "*",
      name: "404",
      component: () => import("../pages/404"),
    },
    {
      path: "/system/ipAccess",
      name: "ipAccess",
      component: () => import("../pages/system/ipAccess"),
    },
    {
      path: "/system/management",
      name: "sys-management",
      component: () => import("../pages/layout/management"),
      redirect: "/system/workspace",
      children: management.map((item) => {
        const props = item.props || {};
        props.routerUrl = item.path;
        props.mode = "management";
        item.props = props;
        //
        const meta = item.meta || {};
        meta.mode = props.mode;
        item.meta = meta;
        return item;
      }),
    },
  ],
});

export default router;
