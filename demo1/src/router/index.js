//定义路由转发器
import {createRouter, createWebHistory} from "vue-router"
import {getJwt} from "@/api/jwt.js";

//定义路由
const routes = [
    {
        name: "main",
        path: "/main",
        component: () => import("@/components/view/Main.vue"),
        children: [
            {
                name: "course",
                path: "/main/course",
                component: () => import("@/components/view/Course.vue")
            },{
                name: "dashboard",
                path: "/main/dashboard",
                component: () => import("@/components/view/Dashboard.vue")

            },{
                name: "member",
                path: "/main/member",
                component: () => import("@/components/view/Member.vue")
            },{
                name: "coach",
                path: "/main/coach",
                component: () => import("@/components/view/Coach.vue")
            },{
                name: "reservation",
                path: "/main/reservation",
                component: () => import("@/components/view/Reservation.vue")

            },{
                name: "admin",
                path: "/main/admin",
                component: () => import("@/components/view/Admin.vue")
            },{
                name: "calendar",
                path: "/main/calendar",
                component: () => import("@/components/view/Calendar.vue")
            },{
                name: "ai-chat",
                path: "/main/ai-chat",
                component: () => import("@/components/view/AIChat.vue")
            }
        ]
    }, {
        name: "index",
        path: "/",
        redirect: "/main"
    },
    {
        name: "login",
        path: "/login",
        component: () => import("@/components/view/Login.vue")
    },
    {
        name: "register",
        path: "/register",
        component: () => import("@/components/view/Register.vue")
    },{
        name: "information",
        path: "/information",
        component: () => import("@/components/view/Information.vue")
    },{
        name: "emailRegister",
        path: "/emailRegister",
        component: () => import("@/components/view/EmailRegister.vue")
    },{
        name: "emailLogin",
        path: "/emailLogin",
        component: () => import("@/components/view/EmailLogin.vue")
    }
];

//定义路由转发器
const router = createRouter({
    routes,
    history: createWebHistory()
});

router.beforeEach((to, from, next) => {
    let jwt = getJwt();
    if(jwt){
        if(to.name === "login"){
            next("/main");
        }
        next();
    }
    else{
        if(to.name !== "login" && to.name !== "register"&& to.name !== "emailRegister"&& to.name !== "emailLogin"){
            next("/login");
        }else {
            next();
        }
    }
})

export default router;
