<template>
    <div class="pb-4">
        <div class="flex items-end mt-8 mb-4 text-font">
            <p class="font-bold text-2xl">My Team</p><br/>
        </div>
        <div class="bg-panel w-full h-1/2 rounded-xl shadow-2xl p-8 border-l-8 border-label">
            <div class="grid grid-cols-2 gap-4 w-full">
                <perfect-scrollbar class="h-56 overflow-y-auto p-4" @scroll.stop="">
                    <div v-for="team in state.teams" :key="team.id" class="h-10 mb-1 rounded-lg shadow-md border-label cursor-pointer text-font" :style="{'background-color': team.color}"  @click="selectTeam(team.id)">
                        <div class="flex justify-between mr-0 ml-5 m-3 pl-2 h-10 rounded-r-lg " :class="{'bg-panel':(state.selectedTeam != team.id), 'text-black':(state.selectedTeam == team.id)}" >
                            <p class="text-xl overflow-x-hidden p-1">
                                {{team.teamName}}
                            </p>
                            <div class="flex justify-end items-center z-20">
                                <div class="w-5 h-5 cursor-pointer mr-2 border-2 border-gray-500" :style="{background: team.color}" @click.stop="changeColor(team.id)">
                                    <my-palette v-if="team.open" :color="team.color" :id="team.id" @saveColor="saveColor" @close="team.open=false, state.paletteOpen=false"/>
                                </div>
                                <div>
                                    <svg class="h-5 w-5 mr-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                                    </svg>	
                                </div>
                            </div>
                        </div>
                    </div>
                </perfect-scrollbar>
                <div class="col-span-1">
                    <div class="flex justify-between">
                        <div class="flex justify-between">
                            <p class="font-bold text-gray-600 pt-4 pl-2">{{state.teams[state.selectedTeam].teamName}}</p>
                        </div>
                        <div class="flex justify-end">
                            <button class="bg-back text-main font-bold border-2 border-label py-1 px-4 m-2 rounded-full hover:bg-main hover:text-back" @click="sync">&nbsp;Synchronization&nbsp;</button>
                            <button class="bg-back text-main font-bold border-2 border-label py-1 px-4 m-2 rounded-full hover:bg-main hover:text-back" @click="save">&nbsp;Save&nbsp;</button>
                        </div>
                    </div>
                    <div class="bg-back h-44 overflow-y-auto rounded-xl shadow-inner p-4">
                        <div v-for="channel in state.teams[state.selectedTeam].subscribe" :key="channel.channelId" class="m-2 p-2 bg-panel text-font rounded-xl">
                            <div class="flex justify-between">
                                <p class="text-xl overflow-x-hidden">
                                    {{channel.channelName}}
                                </p>
                                <div class="relative inline-block w-10 mr-2 align-middle select-none transition duration-200 ease-in">
                                    <div>
                                        <input type="checkbox" v-model="channel.show" name="show" :id="channel.channelId" :class="{'border-label':channel.show, 'right-0':channel.show}" class="absolute block w-5 h-5 rounded-full bg-back border-4 appearance-none cursor-pointer"/>
                                        <label :for="channel.channelId" :class="{'bg-main':channel.show}" class="block overflow-hidden h-5 rounded-full bg-gray-300 cursor-pointer"></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MyPalette from '@/components/my/MyPalette.vue'
import { notify } from '@kyvg/vue3-notification'
import { reactive, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
    name: 'MySubscribe',
    components: {
        MyPalette
    },
    props:{
        saveFlag:{
            type: Boolean,
            default: false,
        }
    },
    setup(props){
        const store = useStore()
        const router = useRouter()
        const state = reactive({
            teams:[{
                color: "#FFFFFF",
                id: 0,
                open: false,
                subscribe: [
                    {channelId: '123', channelName: ' ', show: true},
                ],
                teamId: "",
                teamName: "",
            }],
            selectedTeam: 0,
            paletteOpen: false,
        })
        const init = ()=>{
            let payload = store.getters['root/getUserData']
            store.dispatch('root/getUserSetting', payload)
            .then((result)=>{
                state.teams = []
                let index = 0;
                result.data.teams.forEach(data => {
                    let team = data
                    team.id = index
                    team.open = false,
                    index++
                    state.teams.push(team)
                }); 
            })
            .catch((err)=>{
            })
        }
        const selectTeam = (id)=>{
            state.selectedTeam = id
        }
        const changeColor = (id)=>{
            if(!state.paletteOpen){
                state.teams[id].open = true
                state.paletteOpen = true
            }
        }
        const saveColor = (data)=>{
            state.teams[data.id].color = '#' + data.color
            state.teams[data.id].open = false
            state.paletteOpen = false
            notify({
                title: "From MBOTC 😉",
                text: "Color has been changed temporarily, Please press SAVE before leave this page!",
                type: "warn"
            });
        }
        const save = ()=>{
            let payload = {
                token: store.getters['root/getToken'],
                teams: [],
                theme: store.getters['root/getTheme']
            }
            state.teams.forEach(team => {
                let data = {
                    color: team.color,
                    subscribe: team.subscribe,
                    teamId: team.teamId,
                    teamName: team.teamName,
                }
                payload.teams.push(data)
            }); 
            store.dispatch('root/setUserSetting', payload)
            .then((result)=>{
                notify({
                    title: "From MBOTC 😉",
                    text: "Setting saved successful!",
                    type: "success"
                });
            })
            .catch((err)=>{
                notify({
                    title: "From MBOTC 😅",
                    text: "Error has occurred. Please try again.",
                    type: "error"
                });
            })
        }
        const sync = ()=>{
            let payload = {
                token: store.getters['root/getToken'],
            }
            notify({
                    title: "From MBOTC 🧐",
                    text: "I'm checking your teams and chnnels!\nwait please..",
                    type: "warn"
            });
            store.dispatch('root/userSync', payload)
            .then((result)=>{
                notify({
                    title: "From MBOTC 😉",
                    text: "Synchronization complete!",
                    type: "success"
                });
                router.go()
            })
            .catch((err)=>{
                notify({
                    title: "From MBOTC 😅",
                    text: "Error has occurred. Please try again.",
                    type: "error"
                });
            })
        }
        watch(()=> props.saveFlag, ()=>{
            save()
        })
        init()
        return { state, selectTeam, changeColor, save, saveColor, sync }
    }
};

</script>

<style scoped>
</style>