/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const systemRouter = {
  path: '/system',
  component: Layout,
  meta: { title: '系统管理', icon: 'system' },
  children: [
    {
      path: 'user',
      component: () => import('@/views/system/user/index'),
      meta: { title: '用户管理', icon: 'peoples' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/system/user/index'),
          name: '用户管理',
          meta: { title: '用户管理' }
        }
      ]
    },
    {
      path: 'role',
      component: () => import('@/views/system/role/index'),
      meta: { title: '角色管理', icon: 'role' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/system/user/index'),
          name: '角色管理',
          meta: { title: '角色管理' }
        }
      ]
    },
    {
      path: 'permission',
      component: () => import('@/views/system/permission/index'),
      meta: { title: '权限管理', icon: 'permission' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/system/permission/index'),
          name: '权限管理',
          meta: { title: '权限管理' }
        }
      ]
    },
    {
      path: 'menu',
      component: () => import('@/views/system/menu/index'),
      meta: { title: '菜单管理', icon: 'menu' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/system/menu/index'),
          name: '菜单管理',
          meta: { title: '菜单管理' }
        }
      ]
    },
    {
      path: 'resource',
      component: () => import('@/views/system/resource/index'),
      meta: { title: '资源管理', icon: 'excel' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/system/resource/index'),
          name: 'resource',
          meta: { title: '资源管理' }
        }
      ]
    }
  ]
}
export default systemRouter
