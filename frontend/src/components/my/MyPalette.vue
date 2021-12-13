<template>
    <div class="w-40 h-40 p-2 bg-panel border-2 border-label rounded-lg fixed left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2">
        <div class="w-32 grid grid-cols-4 gap-2">
            <div v-for="color in palette" :key="color.id" class="w-5 h-5 cursor-pointer rounded-sm" :style="{ 'background-color': `#${ color.hex }` }" @click="changeColor(color.hex)"></div>
        </div>
        <div class="flex justify-between mt-2">
            #<input type="text" v-model="state.color" :style="{ 'background-color': `#${ state.color }`}" class="w-16">
            <button class="text-main font-bold mr-2" @click.stop="save">select</button>
            <button class="text-main" @click.stop="close">x</button>
        </div>
    </div>
</template>
<script>
import { reactive } from 'vue'

export default {
    name: 'MyPalette',
    components: {
    },
    props:{
        color : {                
            type: String,
            default: "#FFFFFF",
        },
        id : {                
            type: Number,
            default: 0,
        },
    },
    setup(props, {emit}){
        const palette = [
            {hex : "F9FBE7", id : 0},
            {hex : "E1F5FE", id : 1},
            {hex : "FFEBEE", id : 2},
            {hex : "E8F5E9", id : 3},
            {hex : "FFF3E0", id : 4},
            {hex : "E8EAF6", id : 5},
            {hex : "F3E5F5", id : 6},
            {hex : "E0F2F1", id : 7},
            {hex : "B9F6CA", id : 8},
            {hex : "FF9E80", id : 9},
            {hex : "FF8A80", id : 10},
            {hex : "8C9EFF", id : 11},
            {hex : "FFFF8D", id : 12},
            {hex : "A7FFEB", id : 13},
            {hex : "CCFF90", id : 14},
            {hex : "FFD180", id : 15},
        ]
        const state = reactive({
            color: props.color.substring(1,8)
        })
        const changeColor = (hex)=>{
            state.color = hex
        }
        const save = ()=>{
            emit("saveColor", {color:state.color, id:props.id})
        }
        const close = ()=>{
            emit("close")
        }
        return { palette, state, changeColor, save, close }
    }
};
</script>

<style scoped>
</style>