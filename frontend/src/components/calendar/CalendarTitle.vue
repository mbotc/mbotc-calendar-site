<template>
    <div class="mx-4 text-font">
        <span class="text-6xl font-bold inline-block align-bottom mr-2 font-title text-main">{{state.day}}</span>
        <span class="text-4xl inline-block align-bottom mr-2 font-title">{{monthList[state.month]}}</span>
        <span class="inline-block align-bottom font-subtitle">{{state.year}}</span>
    </div>
</template>
<script>
import { reactive } from 'vue'

export default {
    name: 'CalendarTitle',
    components: {
    },
    props:{
        date:{
            type:String,
            default:20000000
        }
    },

    setup(props){
        const monthList = ["January","February","March","April","May","June","July","August","September","October","November","December"]
        const state = reactive({
            day:0,
            month:0,
            year:0
        })
        const init = ()=>{
            state.year = parseInt(props.date.substring(0,4))
            state.month = parseInt(props.date.substring(4,6))-1
            state.day = parseInt(props.date.substring(6,8))
        }
        init()
        return { state, monthList }
    }
};
</script>

<style scoped>
</style>