<template>
    <div class="relative">
        <DoughnutChart ref="chartRef" :chartData="state" :options="options"></DoughnutChart>
        <div class="absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 text-4xl text-font font-bold align-bottom">
            {{progress}}%
        </div>
    </div>
</template>
<script>
// import abc from '@/components/'
import { computed, defineComponent, reactive, ref } from 'vue'
import { useStore } from 'vuex'
// import { useRouter } from 'vue-router'
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);
import { DoughnutChart } from 'vue-chart-3'

export default defineComponent({
    name: 'NoticeProgress',
    components: {
        DoughnutChart,
    },
    props:{
        data : {                
            type: Array,
            default: [3,1]
        },
        progress : {
            type: Number,
            default : 0,
        }
    },
    setup(props){
        const store = useStore()
        const colorSet = [
            ["#163172","rgba(255,255,255,0.2)"],["#000000","rgba(0,0,0,0.2)"],["#6F8788","rgba(0,0,0,0.2)"],["#019EE2","rgba(255,255,255,0.2)"],["#1D889C","rgba(0,0,0,0.2)"],["#FDD397","rgba(0,0,0,0.2)"],["#163172","rgba(0,0,0,0.2)"]
        ]
        const chartRef = ref()
        const state = reactive({
            labels: ['Complete', 'Task'],
            datasets: [
                {
                    data: props.data,
                    backgroundColor: colorSet[store.getters['root/getThemeId']],
                },
            ],
        })

        const options = ref({
            responsive: true,
            plugins: {
                legend: { display: false, position: 'bottom', },
                title: { display: false, text: 'progress', },
            },
        });

        return { chartRef, options, state };
    },
})
</script>

<style scoped>
</style>