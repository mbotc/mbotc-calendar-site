import { createRouter, createWebHistory } from 'vue-router';

//page
import WelcomePage from '@/views/WelcomePage.vue';
import MainPage from '@/views/MainPage.vue';
import ErrorPage from '@/views/ErrorPage.vue';

//component
import CalendarBig from '@/components/calendar/CalendarBig.vue';
import DetailPage from '@/components/main/DetailPage.vue';
import NoticeEditor from '@/components/notice/NoticeEditor.vue';
import MyPage from '@/components/my/MyPage.vue';
import SearchPage from '@/components/main/SearchPage.vue';

const routes = [
    {
        path: '/',
        name: 'Welcome',
        component: WelcomePage,
        meta:{ loginRequired: false }
    },
    {
        path: '/main',
        name: 'Main',
        component: MainPage,
        children: [
            {path: "", component: CalendarBig, meta:{ loginRequired: true } },
            {path: "detail/:date", component: DetailPage , meta:{ loginRequired: true } },
            {path: "notice", component: NoticeEditor , meta:{ loginRequired: true } },
            {path: "myPage", component: MyPage , meta:{ loginRequired: true } },
            {path: "search", component: SearchPage , meta:{ loginRequired: true } },
        ],
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: "/404"
    },
    {
        path: '/404',
        name: 'Error',
        component: ErrorPage,  
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

const isLoggedIn = function(){
    let settings = JSON.parse(localStorage.getItem("settings"))
    if(settings.userData.token!='' && settings.userData.url!='' && settings.userData.userEmail!='' && settings.userData.userId!='' && settings.userData.userName!=''){
        return true
    }
    return false
}

router.beforeEach((to, from, next) => {
    if(to.meta.loginRequired){
        if(isLoggedIn()){
            next()
        }else{
            alert('Login Needed!')
            next("/")
        }
    }else{
        next()
    }
})

export default router;
