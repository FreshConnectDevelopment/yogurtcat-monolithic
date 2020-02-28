import { constantRouterMap } from '@/router'
import Layout from '@/layout'

const state = {
  routes: constantRouterMap,
  addRoutes: []
}

const mutations = {
  SET_ROUTERS: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRouterMap.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, asyncRouter) {
    commit('SET_ROUTERS', asyncRouter)
  }
}

export const filterAsyncRouter = (routers) => {
  const accessedRouters = routers.filter(router => {
    if (router.component) {
      if (router.component === 'Layout') {
        router.component = Layout
      } else {
        const component = router.component
        router.component = loadView(component)
      }
    } else {
      // 防止空串报错
      router.component = Layout
    }
    if (router.children && router.children.length) {
      router.children = filterAsyncRouter(router.children)
    }
    return true
  })
  return accessedRouters
}

export const loadView = (view) => {
  return () => import(`@/views/${view}`)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
