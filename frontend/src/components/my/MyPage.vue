<template>
    <div class="w-full h-screen px-32 pt-12 overflow-y-auto no-scrollbar">
        <div class="w-11/12 h-full p-8">
            <my-info :url="state.myInfo.url" :id="state.myInfo.id" />
            <my-subscribe :saveFlag="state.saveFlag"/>
            <my-theme @saveTheme="state.saveFlag=!state.saveFlag" />
        </div>
    </div>
</template>
<script>
import MyInfo from '@/components/my/MyInfo.vue'
import MySubscribe from '@/components/my/MySubscribe.vue'
import MyTheme from '@/components/my/MyTheme.vue'
import { reactive } from 'vue'
import { getServerURL } from '../../common/lib/function.js'
import { useStore } from 'vuex'

export default {
    name: 'MyPage',
    components: {
        MyInfo,
        MySubscribe,
        MyTheme,
    },

    setup(){
        const store = useStore()
        const state = reactive({
            myInfo:{
                url: getServerURL(),
                id: "",
            },
            saveFlag: false,
        })
        const init = ()=>{
            state.myInfo.id = store.getters['root/getUserEmail']
        }
        init()
        return { state }
    }
};
</script>

<style scoped>
</style>