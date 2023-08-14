import axios from './config'

/**
 *
 * @param params
 */
export function editWorkSpace(params: any) {
  return axios({
    url: '/system/workspace/edit',
    method: 'post',
    data: params
  })
}

/*
 * 工作空间列表
 * @param {*}
 * } params
 */
export function getWorkSpaceList(params: any) {
  return axios({
    url: '/system/workspace/list',
    method: 'post',
    data: params
  })
}

/*
 * 工作空间列表（查询所有)
 * @param {*}
 * } params
 */
export function getWorkSpaceListAll() {
  return axios({
    url: '/system/workspace/list_all',
    method: 'get',
    data: {}
  })
}

/*
 * 删除工作空间
 * @param {String} id
 * } params
 */
export function deleteWorkspace(id: string) {
  return axios({
    url: '/system/workspace/delete',
    method: 'get',
    params: { id: id }
  })
}

/*
 * 工作空间环境变量列表
 * @param {*}
 * } params
 */
export function getWorkspaceEnvList(params: any) {
  return axios({
    url: '/system/workspace_env/list',
    method: 'post',
    data: params
  })
}

/**
 *
 * @param data
 */
export function editWorkspaceEnv(data: any) {
  return axios({
    url: '/system/workspace_env/edit',
    method: 'post',
    data: data
  })
}

/*
 * 删除工作空间变量
 * @param {String} id
 * } params
 */
export function deleteWorkspaceEnv(params: any) {
  return axios({
    url: '/system/workspace_env/delete',
    method: 'get',
    params: params
  })
}

/**
 * 加载 菜单配置信息
 */
export function getMenusConfig(data: any) {
  return axios({
    url: '/system/workspace/get_menus_config',
    method: 'post',
    data
  })
}

/**
 * 保存菜单配置信息
 */
export function saveMenusConfig(data: any) {
  return axios({
    url: '/system/workspace/save_menus_config.json',
    method: 'post',
    data: data
  })
}
