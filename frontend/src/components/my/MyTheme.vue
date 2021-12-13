<template>
    <div class="pb-4">
        <div class="flex items-end mt-8 mb-4 text-font">
            <p class="font-bold text-2xl">My Theme</p><br/>
        </div>
        <div class="flex justify-between text-font bg-panel w-full h-64 rounded-xl shadow-2xl mb-2 p-8 border-l-8 border-label">
            <perfect-scrollbar class="my-4 overflow-x-scroll w-5/6 whitespace-nowrap content-end">
                <div v-for="theme in state.themes" :key="theme.id" class="relative inline-block pb-2 w-48">
                    <div class="h-32 w-48 text-font rounded-lg p-2 mr-2" @click="clickTheme(theme.id)">
                        <div class="h-24 w-40 border-green-600" :class="{'border-4':(state.selected==theme.id)}">
                            <img :src="state.preview[theme.id]">
                        </div>
                        <div class="h-4 text-xs p-2">
                            {{theme.theme}}
                        </div>
                    </div>
                </div>
            </perfect-scrollbar>
            <button class="bg-back text-main font-bold border-2 border-label h-10 py-1 px-4 m-2 rounded-full hover:bg-main hover:text-back" @click="save">&nbsp;Save&nbsp;</button>
        </div>
    </div>
</template>
<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { notify } from '@kyvg/vue3-notification'

import preview_0 from '@/assets/bg/light_preview.png'
import preview_1 from '@/assets/bg/dark_preview.png'
import preview_2 from '@/assets/bg/ssuk_night_preview.png'
import preview_3 from '@/assets/bg/crayon_preview.png'
import preview_4 from '@/assets/bg/art_preview.png'
import preview_5 from '@/assets/bg/lilac_preview.png'

export default {
    name: 'MyTheme',
    components: {
    },
    setup(props, {emit}){
        const store = useStore()
        const state = reactive({
            selected: store.getters['root/getThemeId'],
            preview: [preview_0, preview_1, preview_2, preview_3, preview_4, preview_5],
            themes:[
                {
                    id: 0,
                    theme: "light",
                },
                {
                    id: 1,
                    theme: "dark",
                },
                {
                    id: 2,
                    theme: "ssuk",
                },
                {
                    id: 3,
                    theme: "crayon",
                },
                {
                    id: 4,
                    theme: "art",
                },
                {
                    id: 5,
                    theme: "lilac",
                },
            ],
        })
        const clickTheme = (id)=>{
            state.selected = id
            store.commit('root/setTheme', state.themes[state.selected].theme)
            notify({
                title: "From MBOTC 😉",
                text: "Theme has been changed temporarily, Please press SAVE before leave this page!",
                type: "warn"
            });
        }
        const save = ()=>{
            emit("saveTheme")
        }
        return { state, clickTheme, save }
    }
};
</script>

<style scoped>
.no-scrollbar::-webkit-scrollbar{
    display: none;
}
.no-scrollbar {
    -ms-overflow-style: none;  /* IE and Edge */
    scrollbar-width: none;  /* Firefox */
}
</style>