<template>
    <div>
        <div class="header pb-4">
            <span class="text-5xl font-title text-main ">Settings</span>
        </div>
        <div class="text-font bold flex justify-between mb-2">
            <div class="flex items-end">
                <span class="font-bold text-2xl align-bottom">My account</span>
            </div>
            <div class="flex justify-end items-end">
                <p class="mr-5 italic text-main text-xl">with mattermost</p>
                <img class="w-16 h-16" src="@/assets/mattermost.png" alt="mattermost">
            </div>
        </div>
        <div class="text-font bg-panel w-full h-1/5 rounded-xl shadow-2xl mb-2 p-8 border-l-8 border-label">
            <div class="flex justify-start">
                <div class="font-bold text-2xl w-20 mr-2">URL</div>
                <div class="text-2xl">{{url}}</div>
            </div>
            <div class="flex justify-between items-end">
                <div class="flex justify-start">
                    <div class="font-bold text-2xl w-20 mr-2">ID</div>
                    <div class="text-2xl">{{id}}</div>
                </div>
                <div>
                    <button class="bg-red-500 text-white font-bold border-2 border-red-600 py-1 px-4 m-2 rounded-full hover:bg-red-700" @click="state.openModal=true">Leave</button>
                </div>
            </div>
        </div>
        <main-confirm :modalData="modalData" v-if="state.openModal" @cancel="state.openModal=false" @action="leave"/>
    </div>
</template>
<script>
import MainConfirm from '@/components/main/MainConfirm.vue'
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
    name: 'MyInfo',
    components: {
        MainConfirm
    },
    props:{
        url:{
            type: String,
            default: "",
        },
        id:{
            type: String,
            default: "",
        }
    },
    setup(){
        const modalData = {
            title: "Leave MBotC",
            message: "You will lose all of your data by leaving MBotC. This action cannot be undone.",
            action: "Delete Account",
        }
        const store = useStore()
        const router = useRouter()
        const state = reactive({
            openModal: false,
        })
        const leave = ()=>{
            let payload = store.getters['root/getUserData']

            store.dispatch('root/deleteUser', payload)
            .then((result)=>{
                if(result.status == 200){
                    store.commit('root/logout')
                    router.push("/")
                }
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        return { modalData, state, leave }
    }
};
</script>

<style scoped>
</style>