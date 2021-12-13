<template>
    <div class="bg-back w-5/6 px-2 py-4 text-font rounded-2xl" :class="{'shadow-lg':(state.themeId==4||state.themeId==5)}">
        <div class="w-full rounded-xl p-2">
            <div class="header flex justify-between">
                <div class="mb-2">
                    <span class="text-xl font-bold p-2 pr-0">{{monthList[state.month]}}</span>
                    <span class="text-xl p-2">{{state.year}}</span>
                </div>
                <div class="inline-block">
                    <button @click="beforeMonth" class="mr-2">							
                        <svg class="h-6 w-6 text-main inline-flex leading-none"  fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                        </svg> 
                    </button>
                    <button @click="nextMonth">
                        <svg class="h-6 w-6 text-main inline-flex leading-none"  fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                        </svg>	
                    </button>
                </div>
            </div>
            <table class="w-full">
                <thead>
                    <tr class="mx-auto text-md">
                        <th class="h-5 font-light text-red-600" style="width:12%">
                            <span>Sun</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Mon</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Tue</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Wed</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Thu</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Fri</span>
                        </th>
                        <th class="h-5 font-light" style="width:12%">
                            <span>Sat</span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="week in state.weeks" :key="week.id" class="text-center">
                        <td v-for="(day, index) in week" :key="day.num" class="p-1 w-6 overflow-auto transition duration-500 cursor-pointer ease hover:bg-panel">
                            <div class="flex flex-col h-12 w-full mx-auto overflow-hidden" @click="goDetail(day.num)">
                                <div class="top h-2 w-full">
                                    <span v-if="index==0" class="text-red-500 font-bold">{{day.num}}</span>
                                    <span v-else class="text-main">{{day.num}}</span>
                                </div>
                                <div class="flex-grow h-10 py-1 w-full cursor-pointer">
                                    <div class="w-4 h-4 rounded-full mx-auto mt-4"
                                        :class="{'bg-first':(day.count==1),'bg-second':(day.count>1 && day.count<4),'bg-third':(day.count>3 && day.count<6),'bg-most':(day.count>=6),}">
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { getDayPicker } from '../../common/lib/function.js'

export default {
    name: 'CalendarSmall',
    components: {
    },
    props:{
        date:{
            type:String,
            default:20000000,
        }
    },
    setup(props, {emit}){
        const store = useStore()
        const router = useRouter()
        const monthList = ["January","February","March","April","May","June","July","August","September","October","November","December"]
        const state = reactive({
            year:0,
            month:0,
            today:0,
            weeks: [[],[],[],[],[],[]],
            themeId: computed(() => store.getters['root/getThemeId']),
        })
        const initCalendar = ()=>{
            let payload = {
                year: state.year,
                month: state.month+1,
                token: store.getters['root/getToken']
            }
            let noticeList = []
            store.dispatch('root/getMonthNotice', payload)
            .then((result)=>{
                result.data.notifications.forEach(node => {
                    let notice = {
                        startDay: getDayPicker(node.startTime, payload.month),
                        endDay: getDayPicker(node.endTime, payload.month),
                    }
                    noticeList.push(notice)
                });
                state.weeks = [[],[],[],[],[],[]]
                let startDay = new Date(state.year, state.month, 1).getDay()
                let dayCount = new Date(state.year, state.month+1, 0).getDate()
                let target = 0
                for (let i = 0; i < startDay; i++) {
                    let day = {
                        num : " ",
                        count : 0
                    }
                    state.weeks[0].push(day)
                }
                for (let i = 1; i <= dayCount; i++) {
                    let day = {
                        num : i,
                        count : 0, 
                    }
                    noticeList.forEach(notice => {
                        if(notice.startDay <= day.num && day.num <= notice.endDay){
                            day.count++;
                        }
                    });

                    state.weeks[target].push(day)
                    startDay++
                    if(startDay == 7){
                        startDay = 0
                        target++
                    }
                }
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        const init = ()=>{
            state.year = parseInt(props.date.substring(0,4))
            state.month = parseInt(props.date.substring(4,6))-1
            state.today = parseInt(props.date.substring(6,8))
            initCalendar()
        }
        const nextMonth = ()=>{
            state.month++
            if(state.month == 12){
                state.month = 0
                state.year++
            }
            initCalendar()
        }
        const beforeMonth = ()=>{
            state.month--
            if(state.month == -1){
                state.month = 11
                state.year--
            }
            initCalendar()
        }
        const goDetail = (day)=>{
            emit('clickDetail')
            let target =  state.year.toString()
            if(state.month+1 < 10){
                target+="0"
            }
            target += (state.month + 1).toString()
            if(day < 10){
                target+="0"
            }
            target += day.toString()
            router.push("/main/detail/"+target)
        }
        init()
        return { state, monthList, nextMonth, beforeMonth, goDetail }
    }
};
</script>

<style scoped>
</style>