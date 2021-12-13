<template>
    <div id="welcome-page">
        <notifications position="top right"/>
        <welcome-content/>
        <welcome-introduction/>
        <welcome-footer/>
        <div class="fixed h-40 w-8 right-3 p-1 bottom-1/2 rounded-full opacity-30 bg-gray-500 flex flex-col justify-between">
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[0], 'bg-gray-400':!state.marker[0]}"></div>
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[1], 'bg-gray-400':!state.marker[1]}"></div>
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[2], 'bg-gray-400':!state.marker[2]}"></div>
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[3], 'bg-gray-400':!state.marker[3]}"></div>
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[4], 'bg-gray-400':!state.marker[4]}"></div>
            <div class="h-4 w-4 rounded-full mx-auto" :class="{'bg-white': state.marker[5], 'bg-gray-400':!state.marker[5]}"></div>
        </div>
        <div class="transform fixed bottom-0 mx-auto inset-x-0" :class="{'rotate-180':state.reverseFlag}">
            <svg version="1.0" xmlns="http://www.w3.org/2000/svg" width="512.000000pt" height="512.000000pt" viewBox="0 0 512.000000 512.000000" preserveAspectRatio="xMidYMid meet"
            class="animate-bounce transform ml-auto mr-auto w-14 h-14 text-gray-400 fill-current opacity-50 hover:opacity-80 hover:scale-x-125 cursor-pointer"  @click="nextMarker">
                <g transform="translate(0.000000,512.000000) scale(0.100000,-0.100000)" stroke="none">
                    <path d="M352 3837 l-352 -352 1280 -1280 1280 -1280 1280 1280 1280 1280 -352 352 -353 353 -927 -927 -928 -928 -928 928 -927 927 -353 -353z"/>
                </g>
            </svg>
        </div>
    </div>
</template>
<script>
import WelcomeContent from '@/components/welcome/WelcomeContent.vue'
import WelcomeFooter from '@/components/welcome/WelcomeFooter.vue'
import WelcomeIntroduction from '@/components/welcome/WelcomeIntroduction.vue'
import { notify } from '@kyvg/vue3-notification'
import { reactive } from 'vue'

export default {
    name: 'WelcomePage',
    components: {
        WelcomeContent,
        WelcomeFooter,
        WelcomeIntroduction,
    },

    setup(){
        const state = reactive({
            marker:[true,false,false,false,false],
            offset:["top","intro1","intro2","intro3","intro4","footer"],
            now:0,
            reverseFlag:false,
        })

        const checkMarker = () =>{
            state.reverseFlag = (state.now==5)?true:false
        }

        const jumpScroll = (target)=>{
            state.marker[state.now] = false
            state.now = target
            state.marker[state.now] = true
            location.href = "#"+ state.offset[target]
        }
        const markerChange = ()=>{
            let nowScroll = window.pageYOffset;
            let list = []

            state.offset.forEach(item => {
                const target = document.getElementById(item)
                const clientRect = target.getBoundingClientRect()
                const relativeTop = clientRect.top
                list.push(relativeTop + nowScroll)
            })
            list.push(9999)
            nowScroll += 5
            for (let index = 0; index <= 5; index++) {
                if(nowScroll >= list[index] && nowScroll <= list[index+1]){
                    state.marker[state.now] = false
                    state.now = index
                    state.marker[state.now] = true
                    break
                }
            }
            checkMarker()
        }

        const nextMarker = ()=>{
            state.marker[state.now] = false
            state.now++
            if(state.now == 6){
                state.now = 0
            }
            location.href = "#"+ state.offset[state.now]
            state.marker[state.now] = true
            checkMarker()
        }

        window.addEventListener('scroll',markerChange)

        return {state , jumpScroll, nextMarker}
    }
};
</script>

<style scoped>
#welcome-page{
        background-image: url('../assets/bg/landingpage.png');
        background-repeat: no-repeat;
        background-size: cover;
}
</style>