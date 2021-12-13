<template>
    <div id="top" class="grid grid-cols-2 w-full h-screen items-center relative">
        <welcome-login></welcome-login>
        <div class="w-full h-full flex justify-end">
            <div class="h-2/3 w-full my-auto">
                <img :src="page" alt="page" class="my-12 rounded-2xl shadow-lg">
            </div>
        </div>
        <div class="absolute top-5 left-1/3 overflow-hidden w-2/3">
            <p class="text-9xl text-opacity-50 text-white overflow-clip whitespace-nowrap">Mattermost Bot Calendar</p>
        </div>
    </div>
</template>
<script>
import WelcomeLogin from '@/components/welcome/WelcomeLogin.vue'
import page_0 from '@/assets/page_0.gif'

export default {
    name: 'WelcomeContent',
    components: {
        WelcomeLogin
    },

    setup(){
        const page = page_0
        return { page }
    }
};
</script>

<style scoped>
</style>