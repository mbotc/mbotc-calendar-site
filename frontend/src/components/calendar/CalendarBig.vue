<template>
    <div class="w-full overflow-y-auto no-scrollbar">
        <div class="w-full px-32 py-12">
            <div class="bg-panel rounded-xl shadow-2xl p-8">
                <div class="header flex justify-between pb-8">
                    <div class="text-font">
                        <span class="text-5xl font-title">{{monthList[state.month]}}</span>
                        <span class="text-5xl p-8 font-subtitle">{{state.year}}</span>
                    </div>
                    <div class="inline-block mr-6 mt-2">
                        <button class="mr-6" @click="beforeMonth">							
                            <svg class="h-8 w-8 text-font inline-flex leading-none"  fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                            </svg> 
                        </button>
                        <button @click="nextMonth">
                            <svg class="h-8 w-8 text-font inline-flex leading-none"  fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                            </svg>	
                        </button>
                    </div>
                </div>
                <table class="w-full">
                    <thead>
                        <tr class="mx-auto text-3xl text-font">
                            <th class="h-8 font-light text-red-600" style="width:12%">
                                <span>Sun</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Mon</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Tue</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Wed</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Thu</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Fri</span>
                            </th>
                            <th class="h-8 font-light" style="width:12%">
                                <span>Sat</span>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="week in state.weeks" :key="week.id" class="text-center border-collapse border-0">
                            <td v-for="(day, index) in week" :key="day.num" class="w-10 transition cursor-pointer duration-500 hover:bg-back">
                                <div class="flex flex-col h-32" @click="goDetail(day.num)">
                                    <div class="top h-4 mb-2">
                                        <span v-if="state.nowFlag && state.today == day.num" class="text-blue-700 font-title">{{day.num}}</span>
                                        <span v-else-if="index==0" class="text-red-500 font-bold">{{day.num}}</span>
                                        <span v-else class="text-gray-400">{{day.num}}</span>
                                    </div>
                                    <div class="bottom flex-grow p-1 cursor-pointer overflow-auto no-scrollbar">
                                        <div v-for="node in day.notice" :key="node.token" class="mb-1 text-sm h-7 text-left text-black opacity-100 rounded-md overflow-hidden" :style="{'background':node.color}">
                                            <!-- <p v-if="(node.startDay == day.num)" class="ml-2 font-bold opacity-100">{{node.title}}</p> -->
                                            <p class="ml-2 my-auto text-gray-800 font-semibold text-sm pt-1 opacity-100 overflow-clip">{{node.title}}</p>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div>
                <main-addon class="fixed right-8 bottom-8" @click="goEdit"/>
            </div>
        </div>
    </div>
</template>
<script>
import MainAddon from '@/components/main/MainAddon.vue'
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { getDayPicker, getTitle } from '../../common/lib/function.js';

export default {
    name: 'CalendarBig',
    components: {
        MainAddon
    },
    setup(props, {emit}){
        const store = useStore()
        const router = useRouter()
        const monthList = ["January","February","March","April","May","June","July","August","September","October","November","December"]
        const state = reactive({
            nowFlag: false,
            year:0,
            month:0,
            today:0,
            weeks: [[],[],[],[],[],[]],
            teamColor: []
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
                        title: getTitle(node.content),
                        color: "#808080",
                        startDay: getDayPicker(node.startTime, payload.month),
                        endDay: getDayPicker(node.endTime, payload.month),
                        token: node.channel.team.token
                    }

                    state.teamColor.forEach(team => {
                        if(team.id == notice.token){
                            notice.color = team.color
                            return
                        }
                    });
                    noticeList.push(notice)
                });
                noticeList.sort(function(a,b){
                    return a.startDay - b.startDay
                })
                setNotice(noticeList)
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        const setNotice = (noticeList)=>{
            state.weeks = [[],[],[],[],[],[]]
            let startDay = new Date(state.year, state.month, 1).getDay()
            let dayCount = new Date(state.year, state.month+1, 0).getDate()
            let target = 0
            for (let i = 0; i < startDay; i++) {
                let day = {
                    num : " ",
                }
                state.weeks[0].push(day)
            }
            for (let i = 1; i <= dayCount; i++) {
                let day = {
                    num : i,
                    notice : [], 
                }
                noticeList.forEach(notice => {
                    if(notice.startDay <= day.num && day.num <= notice.endDay){
                        day.notice.push(notice)
                    }
                });
                state.weeks[target].push(day)
                startDay++
                if(startDay == 7){
                    startDay = 0
                    target++
                }
            }
            let today = new Date()
            state.nowFlag = (state.year == today.getFullYear() && state.month == today.getMonth())? true : false
        }
        const init = ()=>{
            let today = new Date()
            state.year = today.getFullYear()
            state.month = today.getMonth()
            state.today = today.getDate()
            let payload = {
                year: state.year,
                month: state.month+1,
                token: store.getters['root/getToken']
            }
            store.dispatch('root/getUserSetting', payload)
            .then((result)=>{
                store.commit('root/setTheme', result.data.theme)
                result.data.teams.forEach(team=> {
                    let color = {
                        color: team.color,
                        id: team.teamId
                    }
                    state.teamColor.push(color)
                });
                initCalendar()
            })
            .catch((err)=>{
                console.log(err)
            })
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
        const goNotice = ()=>{
            router.push("notice")
        }
        const goEdit = ()=>{
            router.push("/main/notice")
        }
        init()
        return { state, monthList, nextMonth, beforeMonth, goDetail, goNotice, goEdit }
    }
};
</script>

<style scoped>
</style>