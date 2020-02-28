module.exports = {
  title: 'YogurtCat企业开发平台',
  description: 'Just playing around',
  base: '/yogurtcat/',
  themeConfig: {
    nav: [
      { text: '主页', link: '/' },
      { text: '开发规范', link: '/specification/overview.md' },
      {
        text: '开发指南',
        items: [
          { text: '前端', link: '/front/guide/overview.md' },
          { text: '服务端', link: '/server/guide/' }
        ]
      },
      {
        text: '功能',
        items: [
          {
            text: '前端功能',
            items: [
              { text: '富文本编辑器', link: '/front/editor/' }
            ]
          },
          {
            text: '服务端功能',
            items: [
              { text: 'word导入导出', link: '/server/word/' },
              { text: 'excel导入导出', link: '/server/excel/' },
              { text: 'pdf导入导出', link: '/server/pdf/' }
            ]
          }
        ]
      },
      {
        text: '技术框架',
        items: [
          {
            text: '前端框架',
            items: [
              { text: 'vue', link: '/front/vue/' },
              { text: 'vuex', link: '/front/vuex/' },
              { text: 'vue-router', link: '/front/vue-router/' },
              { text: 'element', link: '/front/element/' }
            ]
          },
          {
            text: '服务端框架',
            items: [
              { text: 'spring-boot-starter', link: '/server/spring-boot-starter/' },
              { text: 'spring-boot-starter-security', link: '/server/spring-boot-starter-security/' },
              { text: 'spring-boot-starter-data-jpa', link: '/server/spring-boot-starter-data-jpa/overview.md' },
              { text: 'spring-boot-starter-data-elasticsearch', link: '/server/spring-boot-starter-data-elasticsearch/' },
              { text: 'spring-boot-starter-amqp', link: '/server/spring-boot-starter-amqp/' },
              { text: 'spring-boot-starter-aop', link: '/server/spring-boot-starter-aop/' },
              { text: 'spring-boot-starter-logging', link: '/server/spring-boot-starter-logging/' },
              { text: 'spring-boot-starter-test', link: '/server/spring-boot-starter-test/' }
            ]
          }
        ]
      },
      {
        text: '中间件指南',
        items: [
          { text: 'nginx', link: '/middleware/nginx/' },
          { text: 'seaweedfs', link: '/middleware/seaweedfs/' },
          { text: 'elasticsearch', link: '/middleware/elasticsearch/' },
          { text: 'kibana', link: '/middleware/kibana/' },
          { text: 'beats', link: '/middleware/beats/' }
        ]
      }
    ],
    sidebar: {
      '/specification/': [
        {
          title: '开发规范',
          collapsable: true,
          children: [
            '/specification/overview.md',
            '/specification/front.md',
            '/specification/server.md',
            '/specification/mysql.md',
            '/specification/project.md'
          ]
        }
      ],
      '/front/guide/': [
        {
          title: '基础',
          collapsable: true,
          children: [
            '/front/guide/overview.md',
            '/front/guide/layout.md',
            '/front/guide/router-and-nav.md',
            '/front/guide/permission.md',
            '/front/guide/tags-view.md',
            '/front/guide/new-page.md',
            '/front/guide/style.md',
            '/front/guide/server.md',
            '/front/guide/mock-api.md',
            '/front/guide/import.md',
            '/front/guide/deploy.md',
            '/front/guide/env.md'
          ]
        },
        {
          title: '进阶',
          collapsable: true,
          children: [
            '/front/guide/cors.md',
            '/front/guide/eslint.md',
            '/front/guide/git-hook.md',
            '/front/guide/style-guide.md',
            '/front/guide/lazy-loading.md',
            '/front/guide/chart.md',
            '/front/guide/icon.md',
            '/front/guide/cdn.md',
            '/front/guide/theme.md',
            '/front/guide/i18n.md',
            '/front/guide/error.md',
            '/front/guide/webpack.md',
            '/front/guide/faq.md'
          ]
        }
      ],
      '/server/spring-boot-starter-data-jpa/': [
        {
          title: '开发规范',
          collapsable: true,
          children: [
            '/server/spring-boot-starter-data-jpa/overview.md'
          ]
        }
      ]
    }
  },
  extend: '@vuepress/theme-default'
}