// 该文件专门用于创建整个应用的路由器
import VueRouter from 'vue-router'

// 引入axios依赖并全局注册
import axios from 'axios'

// 修复vue项目在点击重复路由时报错
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

import ForgetPsw from "@/pages/login/ForgetPsw";
import Login from "@/pages/login/Login";
import AdminLogin from "@/pages/login/AdminLogin";
import Register from "@/pages/login/Register";
import PrivacyPolicy from "@/pages/login/PrivacyPolicy";
import AdminPage from "@/pages/main/AdminPage";
// 教师端组件
import TeaMainPage from "@/pages/main/TeaMainPage";
import TeaClassPage from "@/pages/main/TeaClassPage";
import TeaCodingPage from "@/pages/main/teaComponents/TeaCodingPage";
import TeaNoticePage from "@/pages/main/teaComponents/TeaNoticePage";
import TeaSectionPage from "@/pages/main/teaComponents/TeaSectionPage";
import TeaConPane from "@/pages/main/teaComponents/TeaConPane";
import TeaReportPage from "@/pages/main/teaComponents/TeaReportPage";
//学生端组件
import StuMainPage from "@/pages/main/StuMainPage";
import StuClassPage from "@/pages/main/StuClassPage";
import StuNoticePage from "@/pages/main/stuComponents/StuNoticePage";
import StuSectionPage from "@/pages/main/stuComponents/StuSectionPage";
import StuReportPage from "@/pages/main/stuComponents/StuReportPage";
import StuCodingPage from "@/pages/main/stuComponents/StuCodingPage";


//创建并暴露一个路由器 根级路由
const router =  new VueRouter({
    routes:[
        {   // 登录
            name:"Login",
            path:'/',
            component:Login,
            meta:{
                title:'登录'
            }
        },
        {   // 忘记密码
            name: "ForgetPsw",
            path:'/ForgetPsw',
            component:ForgetPsw,
            meta:{
                title:'找回密码'
            }
        },
        {   // 注册账号
            name: "Register",
            path:'/Register',
            component:Register,
            meta:{
                title:'注册账号'
            }
        },
        {   // 隐私政策
            name: "PrivacyPolicy",
            path:'/PrivacyPolicy',
            component:PrivacyPolicy,
            meta:{
                title:'隐私政策'
            }
        },
        {   // 班级管理
            name: "TeaClassPage",
            path:'/TeaClassPage',
            component:TeaClassPage,
            meta:{
                title:'班级管理'
            }
        },
        {   // 老师登录主页
            name: "TeaMainPage",
            path:'/TeaMainPage',
            component:TeaMainPage,
            meta:{
                title:'教师主页'
            },
            children:[
                {   // 实验中心
                    name:"TeaSectionPage",
                    path:'TeaSectionPage',
                    component: TeaSectionPage,
                    meat: {
                        title:'实验中心'
                    }
                },
                {   // 管理面板
                    name:"TeaConPane",
                    path:'TeaConPane',
                    component: TeaConPane,
                    meat: {
                        title:'管理面板'
                    }
                },
                {   // 班级通知
                    name: "TeaNoticePage",
                    path:'TeaNoticePage',
                    component:TeaNoticePage,
                    meta:{
                        title:'班级通知'
                    }
                },
                {
                    name: "TeaReportPage",
                    path: "TeaReportPage",
                    component: TeaReportPage,
                    meta:{
                        title: "报告管理"
                    }
                },
                {   // 在线编程
                    name: "TeaCodingPage",
                    path:'TeaCodingPage',
                    component:TeaCodingPage,
                    meta:{
                        title:'在线编程'
                    }
                },
            ]
        },
        {   // 班级列表
            name: "StuClassPage",
            path:'/StuClassPage',
            component:StuClassPage,
            meta:{
                title:'班级列表'
            }
        },
        {   // 学生主页
            name: "StuMainPage",
            path:'/StuMainPage',
            component:StuMainPage,
            meta:{
                title:'学生主页'
            },
            children: [
                {   // 实验中心
                    name:"StuSectionPage",
                    path:'StuSectionPage',
                    component: StuSectionPage,
                    meat: {
                        title:'实验中心'
                    }
                },
                {   // 实验记录
                    name: "StuReportPage",
                    path:'StuReportPage',
                    component:StuReportPage,
                    meta:{
                        title:'实验记录'
                    }
                },
                {   // 班级通知
                    name: "StuNoticePage",
                    path:'StuNoticePage',
                    component:StuNoticePage,
                    meta:{
                        title:'班级通知'
                    }
                },
                {   // 在线编程
                    name: "StuCodingPage",
                    path:'StuCodingPage',
                    component:StuCodingPage,
                    meta:{
                        title:'在线编程'
                    }
                },
            ]

        },
        {   // 管理员登录
            name:"AdminLogin",
            path:'/AdminLogin',
            component:AdminLogin,
            meta:{
                title:'管理员登录'
            }
        },
        {   // 管理员页面
            name:"AdminPage",
            path:'/AdminPage',
            component:AdminPage,
            meta:{
                title:'用户管理'
            }
        },

    ]
})

//全局前置路由守卫————初始化的时候被调用、每次路由切换之前被调用
router.beforeEach((to,from,next)=>{
  /*  console.log('前置路由守卫',to,from)*/
    if(to.name !== 'Login'
        && to.name !== 'Register'
        && to.name !== 'AdminLogin'
        && to.name !== 'ForgetPsw'
        && to.name !== 'PrivacyPolicy'){ //判断是否需要拦截
       axios.get("/UserCon/getUserInfo").then((res)=>{
            if(res.data.flag){
                // 登陆成功
                next();
            }else{
                // 1秒后跳转登陆界面
                setTimeout(()=> {next({ path: '/' })}, 1000);
            }
        });
    }else{  // 公开页面-无需拦截
        next()
    }
})

//全局后置路由守卫————标题切换
router.afterEach((to,from)=>{
    if(to.meta.title !== undefined) {
        document.title = to.meta.title;
    }
    else{
        document.title = 'Python Online';
    }
})
export default router;