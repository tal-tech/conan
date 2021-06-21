import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import ParentView from '@/components/ParentView';

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      },
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: false, affix: true }
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  },
  {
    path: '/task',
    component: Layout,
    hidden: true,
    // meta: { title: '任务管理', icon: '' },
    children: [
      {
        path: '/task/DetailSchedule',
        name: 'DetailSchedule',
        component: (resolve) => require(['@/views/task/DetailSchedule'], resolve),
        meta: { title: '定时任务关联接口', icon: '', noCache: true },
      },
      {
        path: '/task/ExecuteTheTaskDetails',
        name: 'ExecuteTheTaskDetails',
        component: (resolve) => require(['@/views/task/ExecuteTheTaskDetails'], resolve),
        meta: { title: '任务关联接口', icon: '', noCache: true },
      },
      {
        path: '/task/TaskViewDetails',
        name: 'TaskViewDetails',
        component: (resolve) => require(['@/views/task/TaskViewDetails'], resolve),
        meta: { title: '执行详情', icon: '', noCache: true },
      },
      {
        path: '/common/printRecordLog',
        name: 'printRecordLog',
        component: (resolve) => require(['@/views/execution/common/printRecordLog'], resolve),
        meta: { title: '录制日志详情', icon: '', noCache: true },
      },
      {
        path: '/common/printReplayLog',
        name: 'printReplayLog',
        component: (resolve) => require(['@/views/execution/common/printReplayLog'], resolve),
        meta: { title: '回放日志详情', icon: '', noCache: true },
      },
      {
        path: '/common/printDiffLog',
        name: 'printDiffLog',
        component: (resolve) => require(['@/views/execution/common/printDiffLog'], resolve),
        meta: { title: '比对日志详情', icon: '', noCache: true },
      },
      {
        path: '/common/DiffResult',
        name: 'DiffResult',
        component: (resolve) => require(['@/views/execution/common/DiffResult'], resolve),
        meta: { title: '单接口比对结果', icon: '', noCache: true },
      },
      {
        path: '/common/ReplayDetail',
        name: 'ReplayDetail',
        component: (resolve) => require(['@/views/execution/common/ReplayDetail'], resolve),
        meta: { title: '单接口回放结果', icon: '', noCache: true },
      },
    ]
  },
  {
    path: '/execution',
    component: Layout,
    hidden: true,
    // meta: { title: '执行记录', icon: '' },
    children: [
      {
        path: '/execution/list/ViewDetails',
        name: 'ViewDetails',
        component: (resolve) => require(['@/views/execution/ViewDetails'], resolve),
        meta: { title: '执行列表查看详情', icon: '', noCache: true },
      },

    ]
  },
  {
    path: '/conanConfig',
    component: Layout,
    hidden: true,
    // meta: { title: '配置中心', icon: '' },
    children: [
      {
        path: '/conanConfig/rule',
        name: 'ApiSchemaRule',
        component: (resolve) => require(['@/views/conanConfig/rule'], resolve),
        meta: { title: '接口Schema配置规则', icon: '', noCache: true },
      },
    ]
  },
  {
    path: '/admin',
    component: Layout,
    hidden: true,
    meta: { title: '业务管理', icon: '' },
    children: [
      {
        path: '/admin/domain/EsConditionSetting',
        name: 'EsConditionSetting',
        component: (resolve) => require(['@/views/admin/domain/EsConditionSetting'], resolve),
        meta: { title: '域名管理配置', icon: '', noCache: true },
      },
      {
        path: '/admin/domain/domainAuth',
        name: 'DomainAuth',
        component: (resolve) => require(['@/views/admin/domain/domainAuth'], resolve),
        meta: { title: '鉴权配置', icon: '', noCache: true },
      },
      {
        path: '/admin/domain/interface/exportExcel',
        name: 'exportExcel',
        component: (resolve) => require(['@/views/admin/interface/exportExcel'], resolve),
        meta: { title: '导出网关域名接口Excel', icon: '', noCache: true },
      },
    ]
  },
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
