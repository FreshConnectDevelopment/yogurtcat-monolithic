/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const toolsRouter = {
  path: '/systemtool',
  component: Layout,
  meta: { title: '系统工具', icon: 'tools' },
  children: [
    {
      path: 'pay',
      component: () => import('@/views/tools/payment/index'),
      meta: { title: '支付工具', icon: 'alipay' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/tools/payment/index'),
          name: '支付工具',
          meta: { title: '支付工具' }
        }
      ]
    },
    {
      path: 'storage',
      component: () => import('@/views/tools/filestorage/index'),
      meta: { title: '文件存储', icon: 'chain' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/tools/filestorage/index'),
          name: '文件存储',
          meta: { title: '文件存储' }
        }
      ]
    },
    {
      path: 'message',
      component: () => import('@/views/tools/message/index'),
      meta: { title: '消息工具', icon: 'message' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/tools/message/index'),
          name: '消息工具',
          meta: { title: '消息工具' }
        }
      ]
    },
    {
      path: 'rabbitMQ',
      component: () => import('@/views/tools/rabbitMQ/index'),
      meta: { title: 'rabbitMQ配置', icon: 'codeConsole' },
      children: [
        {
          path: 'index',
          component: () => import('@/views/tools/rabbitMQ/index'),
          name: 'rabbitMQ配置',
          meta: { title: 'rabbitMQ配置' }
        }
      ]
    }
  ]
}
export default toolsRouter
