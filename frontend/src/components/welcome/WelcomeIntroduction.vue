<template>
    <div>
        <div id="intro1" class="grid grid-cols-2 w-full h-screen items-center relative">
            <div class="w-full h-2/3 my-auto relative">
                <p class="z-50 absolute top-10 left-20 text-sm text-gray-700" style="animation: ping 6s cubic-bezier(0, 0, 0.3, 1) infinite;">Hello I'm MBOTC</p>
                <p class="z-50 absolute top-96 left-32 ttext-sm text-gray-500" style="animation: ping 7s cubic-bezier(0, 0, 0.2, 1) infinite;">What are you looking for?</p>
                <p class="z-50 absolute top-72 left-80 ttext-sm text-gray-700" style="animation: ping 3s cubic-bezier(0, 0, 0.4, 1) infinite;">Hello</p>
                <p class="z-50 absolute top-96 left-0 ttext-sm text-gray-700" style="animation: ping 5s cubic-bezier(0, 0, 0.4, 1) infinite;">Hello</p>
                <p class="z-50 absolute top-24 right-48 ttext-sm text-black" style="animation: ping 2s cubic-bezier(0, 0, 0.2, 1) infinite;">안녕하세요</p>
                <p class="z-50 absolute top-36 left-72 ttext-sm text-gray-400" style="animation: ping 7s cubic-bezier(0, 0, 0.3, 1) infinite;">Hi</p>
                <p class="z-50 absolute top-28 right-72 ttext-sm text-gray-700" style="animation: ping 6s cubic-bezier(0, 0, 0.2, 1) infinite;">Nice to meet you!</p>
                <div class="absolute top-12 left-20">
                    <img :src="mbotc[0]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 1s infinite;">
                </div>
                <div class="absolute top-30 right-52">
                    <img :src="mbotc[5]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 0.3s infinite;">
                </div>
                <div class="absolute top-80 left-52 animate-spin">
                    <img :src="mbotc[1]" alt="mbotc" class="h-20 w-20 transform">
                </div>
                <div class="absolute top-96 left-12">
                    <img :src="mbotc[0]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 0.8s infinite;">
                </div>
                <div class="absolute top-96 right-36">
                    <img :src="mbotc[0]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 0.8s infinite;">
                </div>
                <div class="absolute top-44 left-36">
                    <img :src="mbotc[2]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 1.5s infinite;">
                </div>
                <div class="absolute top-32 left-96">
                    <img :src="mbotc[2]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 1.3s infinite;">
                </div>
                <div class="absolute top-11 left-72">
                    <img :src="mbotc[3]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 2s infinite;">
                </div>
                <div class="absolute top-52 right-24">
                    <img :src="mbotc[3]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 1.4s infinite;">
                </div>
                <div class="absolute top-64 right-96">
                    <img :src="mbotc[4]" alt="mbotc" class="h-20 w-20 transform" style="animation: bounce 0.6s infinite;">
                </div>
            </div>
            <div class="w-full h-1/2 my-auto">
                <div class="mx-auto w-4/5">
                    <p class="text-7xl md:text-4xl font-bold text-left mb-4">Download</p>
                    <p class="text-xl md:text-lg text-left mb-20">Our MBotC is opended for every developers who can develop our services passionately. <br> 
                        Welcome for every interest and any of questions about out service. <br>
                        You could check our contributing convention documents in Github.</p>
                    <a href="https://github.com/mbotc">
                        <img :src="pluginLink" alt="plugin" class="h-20 w-auto">
                    </a>
                </div>
            </div>
        </div>
        <div id="intro2" class="grid grid-cols-2 w-full h-screen items-center relative">
            <div class="absolute right-16 top-0 h-56 w-56" style="animation: spin 3s linear infinite;">
                <img :src="mbotc[3]" alt="mbotc" class="h-24 w-24 my-10">
            </div>
            <div class="w-full h-full">
                <div class="h-3/4 w-5/6 m-auto relative">
                    <img :src="mbotc[0]" alt="mbotc" class="h-24 w-24 my-10">
                    <p class="text-7xl md:text-4xl font-bold text-left mb-10">Daily notifications</p>
                    <p class="text-xl md:text-lg text-left mb-20">Check your daily notification. <br>
                        You can check your every detail contents of daily notices. <br>
                        Also, it’s intuitive to recognize about tracking progress. <br>
                        Our service provide  such a cute version of calendar that shows how many <br>
                        announcements there are in a day.</p>
                    <p class="text-3xl font-bold text-left mb-4">Features</p>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Check daily notifications</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Tracking progress</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Simple monthly calendar</p>
                    </div>
                </div>
            </div>
            <div class="w-full h-full flex justify-end">
                <div class="h-1/2 w-auto my-auto">
                    <img :src="page[0]" alt="page-0" class="h-full w-full rounded-2xl shadow-lg">
                </div>
            </div>
        </div>
        <div id="intro3" class="grid grid-cols-2 w-full h-screen items-center relative">
            <div class="w-full h-full flex justify-start">
                <div class="h-1/2 w-auto my-auto">
                    <img :src="page[1]" alt="page-1" class="h-full w-full rounded-2xl shadow-lg">
                </div>
            </div>
            <div class="w-full h-full">
                <div class="h-3/4 w-5/6 m-auto relative">
                    <img :src="mbotc[3]" alt="mbotc" class="h-24 w-24 my-10"  style="animation: bounce 2s infinite;">
                    <p class="text-7xl md:text-4xl font-bold text-left mb-10">Create notification</p>
                    <p class="text-xl md:text-lg text-left mb-20">Easy to create new notification with MBotC. <br>
                        In Right Side, you can check your result of applying Markdown at a glance. <br> 
                        More of details, we could provide ‘Start Date’ and ‘End Date’ to effecitively let you see <br>
                        how many days your schedule can be processed. <br>
                        You could set Channels and Teams anyway, whatever you want. As you belong to.</p>
                    <p class="text-3xl font-bold text-left mb-4">Features</p>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Easy to create new notifiaction</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Write with markdown</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Set deadline</p>
                    </div>
                </div>
            </div>
        </div>
        <div id="intro4" class="grid grid-cols-2 w-full h-screen items-center relative">
            <div class="w-full h-full">
                <div class="h-3/4 w-5/6 m-auto relative">
                    <div class="h-24 w-24">
                        <img :src="mbotc[4]" alt="mbotc" class="h-24 w-24 my-10" style="animation: spin 3s linear infinite;">
                    </div>
                    <p class="text-7xl md:text-4xl font-bold text-left mb-4">Search Mode</p>
                    <p class="text-xl md:text-lg text-left mb-20">Don't you ever had a hard time searching for what you wanted 
                        while using Classic MatterMost? <br>
                        Here, MBotC offers a attractive solution just for you. <br>
                        Just type in a word, what you want to search from your all notices you’ve got. <br>
                        You can check the notice with a neat and easy-to-understand design. <br>
                        Now I'm ready not to hesitate to search in Metamost! Isn’t excited?</p>
                    <p class="text-3xl font-bold text-left mb-4">Features</p>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Keyword search</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">All Teams, and Channels</p>
                    </div>
                    <div class="flex justify-start p-4">
                        <img :src="checkBox" alt="checkBox" class="h-8 w-8">
                        <p class="text-xl ml-10 text-left">Check detail content</p>
                    </div>
                </div>
            </div>
            <div class="w-full h-full flex justify-end">
                <div class="h-1/2 w-auto my-auto">
                    <img :src="page[2]" alt="page-2" class="h-full w-full rounded-2xl shadow-lg">
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import pluginLink_0 from '@/assets/plugin.png'
import checkBox_0 from '@/assets/checkBox.png'
import page_1 from '@/assets/page_1.gif'
import page_2 from '@/assets/page_2.gif'
import page_3 from '@/assets/page_3.gif'
import logo_0 from '@/assets/logo/logo_0.png'
import logo_1 from '@/assets/logo/logo_1.png'
import logo_2 from '@/assets/logo/logo_2.png'
import logo_3 from '@/assets/logo/logo_3.png'
import logo_4 from '@/assets/logo/logo_4.png'
import logo_5 from '@/assets/logo/logo_5.png'

export default {
    name: 'WelcomeIntroduction',
    components: {
    },

    setup(){
        const mbotc = [logo_0,logo_1,logo_2,logo_3,logo_4,logo_5]
        const page = [page_1, page_2, page_3]
        const pluginLink = pluginLink_0
        const checkBox = checkBox_0
        return { pluginLink, checkBox,  page, mbotc }
    }
};
</script>

<style scoped>
</style>