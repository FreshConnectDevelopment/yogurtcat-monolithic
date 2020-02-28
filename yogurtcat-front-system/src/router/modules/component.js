/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const componentRouter = {
  path: '/component',
  component: Layout,
  meta: { title: '基础组件', icon: 'chart' },
  children: [
    {
      path: 'tinymce',
      component: () => import('@/views/component/tinymce/index'),
      meta: { title: '富文本编辑器', icon: 'chart' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/component/tinymce/index'),
          name: '富文本编辑器',
          meta: { title: '富文本编辑器' }
        }
      ]
    },
    {
      path: 'storage',
      component: () => import('@/views/component/storage/index'),
      meta: { title: '上传组件', icon: 'chart' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/component/storage/index'),
          name: '上传组件',
          meta: { title: '上传组件' }
        }
      ]
    },
    {
      path: 'captcha',
      component: () => import('@/views/component/captcha/index'),
      meta: { title: '图形验证码', icon: 'fwb' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/component/captcha/index'),
          name: '图形验证码',
          meta: { title: '图形验证码' }
        }
      ]
    }
  ]
}
export default componentRouter
