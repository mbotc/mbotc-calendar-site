<template>
    <div class="flex justify-start myPage" id="socket">
        <notifications/>
        <div class="w-full ml-20">
            <router-view :key="$route.fullPath" class="w-full h-screen pt-12"
            @clickDetail="onClickDetail"/>
        </div>
        <main-sidebar
        :detailFlag = "state.sideDetailFlag"
        @clickOther="onClickOther"/>
    </div>
</template>
<script>
import MainSidebar from '@/components/main/MainSidebar.vue'
import { reactive } from 'vue'
import { notify } from '@kyvg/vue3-notification'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
    name: 'MainPage',
    components: {
        MainSidebar
    },

    setup(){
        const router = useRouter()
        const store = useStore()
        const state = reactive({
            sideDetailFlag: false,
            stompClient: null,
            userToken: store.getters['root/getToken']
        })
        const onClickDetail = ()=>{
            state.sideDetailFlag = true
        }
        const onClickOther = ()=>{
            state.sideDetailFlag = false
        }
        const connect = () => {
            const serverURL = "/api/v1/websocket"
            let socket = new SockJS(serverURL);
            state.stompClient = Stomp.over(socket);
            state.stompClient.connect(
                {},
                frame =>{
                    state.stompClient.subscribe('/sub/notification/'+ state.userToken, function(notice){
                        let notification = JSON.parse(notice.body);
                        onClickTop(notification.content.substr(0,20));
                    })
                }
            )
        }
        const onClickTop = (message) => {
            notify(
                {
                title: "New Notification",
                text: message,
                });
                router.push("/main")
        }
        connect()
        return { state, onClickDetail, onClickOther, connect, onClickTop }
    }
};
</script>

<style scoped>
.myPage{
    background-color: rgba(0, 0, 0, 0);
}
</style>
