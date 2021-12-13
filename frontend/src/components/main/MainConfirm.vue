<template>
    <div class="fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 z-50">
        <div class="bg-back rounded-lg border-2 border-label md:max-w-md md:mx-auto p-4 fixed inset-x-0 bottom-0 z-50 mb-4 mx-4 md:relative">
            <div class="md:flex items-center">
                <div class="rounded-full border border-label flex items-center justify-center w-16 h-16 flex-shrink-0 mx-auto">
                    <img :src="icon" alt="warning" class="h-12 w-12">
                </div>
                <div class="mt-4 md:mt-0 md:ml-6 text-center md:text-left">
                    <p class="font-bold">{{modalData.title}}</p>
                    <p class="text-sm text-font mt-1">{{modalData.message}}</p>
                </div>
            </div>
            <div class="text-center md:text-right mt-4 md:flex md:justify-end">
                <button class="block w-full md:inline-block md:w-auto px-4 py-3 md:py-2 bg-red-200 text-red-700 rounded-lg font-semibold text-sm md:ml-2 md:order-2" @click="action">{{modalData.action}}</button>
                <button class="block w-full md:inline-block md:w-auto px-4 py-3 md:py-2 bg-gray-200 rounded-lg font-semibold text-sm mt-4 md:mt-0 md:order-1" @click="cancel">Cancel</button>
            </div>
        </div>
    </div>
</template>
<script>
import warn from '@/assets/warning.png'

export default {
    name: 'MainConfirm',
    components: {
    },
    props:{
        modalData:{
            title:{
                type:String,
                default:" "
            },
            message:{
                type:String,
                default:" "
            },
            action:{
                type:String,
                default:" "
            }
        }
    },
    setup(props, {emit}){
        const icon = warn
        const action = ()=>{
            emit("action")
        }
        const cancel = ()=>{
            emit("cancel")
        }
        return { icon, action, cancel }
    }
};
</script>

<style scoped>
</style>