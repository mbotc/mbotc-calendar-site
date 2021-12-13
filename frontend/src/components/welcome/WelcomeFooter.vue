<template>
    <div id="footer" class="w-full">
        <div class="w-full h-40">

        </div>
        <div class="p-8 w-full h-80 bg-blue-900 text-yellow-50 flex flex-wrap content-end justify-end">
            <div clsss="text-left whitespace-nowrap">       
            </div>
            <div clsss="text-right whitespace-nowrap">
                <div class="text-md text-right font-semibold">
                    <div class="transform transition duration-100 flex justify-end">
                        <img :src="state.logo" alt="mbotc" class="h-8 w-8 mr-1">
                        <p class="text-xl font-bold mt-1">MBotC</p>
                    </div>
                    <br>
                    <p>©Copyright. 2021. Team. GongMoJeon</p>
                </div>
                <div class="flex justify-end content-end mt-4 cursor-pointer">
                    <img :src="state.github" alt="git" class="h-8 w-8 mr-4" @click="clickLogo(0)">
                    <img :src="state.notion" alt="notion" class="h-7 w-7 mr-4" @click="clickLogo(1)">
                    <img :src="state.npm" alt="npm" class="h-6 w-12 mr-4 mt-1" @click="clickLogo(2)">
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { reactive } from 'vue'
import notion from '@/assets/logo/notion.png'
import github from '@/assets/logo/github.png'
import npm from '@/assets/logo/npm.png'
import logo from '@/assets/logo/logo_0.png'

export default {
    name: 'WelcomeFooter',
    components: {
    },

    setup(){
        const state = reactive({
            teamName : false,
            notion,
            github,
            npm,
            logo
        })
        const clickLogo = (id)=>{
            switch (id) {
                case 0:
                    window.open("https://github.com");
                    break;
                case 1:
                    window.open("https://notion.so");
                    break;
                case 2:
                    window.open("https://npmjs.com");
                    break;
            }
        }
        return { state, clickLogo }
    }
};
</script>

<style scoped>
</style>