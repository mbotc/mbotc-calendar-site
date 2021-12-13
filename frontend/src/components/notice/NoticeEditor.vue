<template>
    <div class="h-screen px-32 pt-12 overflow-y-auto no-scrollbar">
        <div class="bg-panel rounded-xl shadow-2xl p-12">
            <div class="header pb-8">
                <span class="text-5xl font-title text-main ">Create notification</span>
            </div>
            <div class="grid grid-cols-3 gap-4 w-11/12 mx-auto text-font">
                <div class="col-span-1">
                    <div>
                        <p class="text-xl font-bold">Destination</p>
                    </div>
                    <div class="w-5/6 flex justify-start items-center pt-3">
                        <select class="form-select block w-full mr-3 border-b-2 p-1 bg-back text-font"  v-model="state.teamId" @change="state.channelId=0">
                            <option v-for="team in state.teams" :key="team.id" :value="team.id">{{team.teamName}}</option>
                        </select>
                        <select class="form-select block w-full mr-3 border-b-2 p-1 bg-back text-font"  v-model="state.channelId">
                            <option v-for="channel in state.teams[state.teamId].subscribe" :key="channel.channelId" :value="channel.channelId">{{channel.channelName}}</option>
                        </select>
                    </div> 
                </div>
                <div class="col-span-1">
                    <div class="flex justify-start items-center">
                        <div>
                            <p class="text-xl font-bold mr-4">Date</p>
                        </div>
                        <div class="relative inline-block w-10 mr-2 align-middle select-none transition duration-200 ease-in">
                            <div>
                                <input type="checkbox" v-model="state.termToggle" name="termToggle" id="termToggle" :class="{'border-label':state.termToggle, 'right-0':state.termToggle}" class="absolute block w-5 h-5 rounded-full bg-back border-4 appearance-none cursor-pointer"/>
                                <label for="termToggle" :class="{'bg-main':state.termToggle}" class="block overflow-hidden h-5 rounded-full bg-gray-300 cursor-pointer"></label>
                            </div>
                        </div>
                        <p class="text-font text-sm">term</p>
                    </div>
                    <div class="w-5/6 flex justify-start items-center pt-3">
                        <input type="datetime-local" class="border-2 h-8 bg-back text-font"  v-model="state.startTime" :min="state.today">
                            <p v-if="state.termToggle" class="text-sm mx-4 text-font">-</p>
                        <input v-if="state.termToggle" type="datetime-local" class="border-2 h-8 bg-back text-font" v-model="state.endTime">
                    </div>
                </div>
                <div class="col-span-1">
                    <div class="flex justify-start items-center">
                        <p class="text-xl font-bold mr-4">File upload</p>
                        <label for="fileInput" class="bg-back hover:bg-panel text-font font-bold px-4 rounded cursor-pointer">upload</label>
                        <input ref="fileRoot" id="fileInput" type="file" name="file" accept="*" class="hidden" @input="uploadFile">
                    </div>
                    <div class="flex justify-start items-center pt-3 relative">
                        <select class="form-select block w-1/2 mr-3 border-b-2 p-1 bg-back text-font" v-model="state.listAnchor" @change="deleteFile">
                            <option value="0">{{state.fileList.length}} files in List</option>
                            <option v-for="file in state.fileList" :key="file.lastModified" :value="file.name">remove {{file.name}}</option>
                        </select>
                        <!-- <button class="bg-back text-main px-4 rounded" @click="showList">{{state.fileList.length}} files in List</button> -->
                    </div>
                </div>
            </div>
            <div ref="mdEditorWraper" class="w-11/12 bg-back text-font mx-auto mt-5" style="height:60vh">
                <div id="editor" ref="mdEditor"></div>
            </div>
            <div class="w-full flex justify-center mt-5">
                <button class="mt-1 w-11/12 h-12 px-4 py-2 my-2 inset-x-0 rounded-3xl bg-back text-main font-bold border-2 border-label hover:bg-main hover:text-back" :class="{'bg-back':state.clickable, 'hover:bg-main':state.clickable}" @click="submit">
                    Create notification to Mattermost
                </button>
            </div>
        </div>
    </div>
</template>
<script>
import "@toast-ui/editor/dist/toastui-editor.css"; 
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import Editor from "@toast-ui/editor";
import 'tui-color-picker/dist/tui-color-picker.css';
import '@toast-ui/editor-plugin-color-syntax/dist/toastui-editor-plugin-color-syntax.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

import { getTime } from '../../common/lib/function.js';
// import abc from '@/components/'
import { reactive, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { notify } from '@kyvg/vue3-notification'
// import { useRouter } from 'vue-router'

export default {
    name: 'NoticeEditor',
    components: {
        Editor
    },

    setup(){
        const store = useStore()
        const mdEditor = ref(null)
        const mdEditorWraper = ref(null)
        const fileRoot = ref(null)
        const state = reactive({
            fileList: [],
            termToggle: false,
            oldToggle: false,
            clickable: false,
            mountEditor: null,
            startTime: "",
            endTime: "",
            teamId: 0,
            channelId: "0",
            teamList: "",
            channelList: "",
            today: "",
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
            listAnchor: "0",
        })
        const uploadFile = ()=>{
            //console.log("upload")
            let file = fileRoot.value.files[0]
            if((file.size / (1024*1024)) > 50){
                //console.log("------------>",file.size / (1024*1024))
                notify({
                    title: "From MBOTC 😥",
                    text: "Sorry, File is too big. We can accept files less than 50MB",
                    type: "warn"
                });
            }else{
                if(state.fileList.indexOf(file.name) == -1){
                    state.fileList.push(file)
                }
                //console.log(state.fileList)
            }
        }
        const deleteFile = ()=>{
            if(state.fileList.length > 1){
                let index = state.fileList.indexOf(state.listAnchor)
                state.fileList.splice(index, 1);
                state.listAnchor = "0"
            }else{
                state.fileList = []
                state.listAnchor = "0"
            }
            //console.log(state.fileList)
        }
        const validation = ()=>{
            if(!state.startTime){
                return false
            }else if(state.channelId == 0){
                return false
            }else if(state.mountEditor.getMarkdown().length == 0){
                return false
            }
            return true
        }
        const submit = ()=>{
            //console.log(state.mountEditor.getMarkdown())
            //let timeZone = new Date().getTimezoneOffset()/60;
            //console.log(timeZone)
            if(validation()){
                let formData = new FormData()
                formData.append("channel_id", state.channelId,)
                formData.append("user_id", store.getters['root/getUserId'])
                formData.append("start_time", getTime(state.startTime))
                if(state.termToggle){
                    formData.append("end_time", getTime(state.endTime))
                }else{
                    formData.append("end_time", "")
                }
                formData.append("message", state.mountEditor.getMarkdown())
                state.fileList.forEach(file => {
                    //console.log(file)
                    formData.append("file", file)
                });
                // console.log(fileRoot.value.files)
                // formData.append("file", fileRoot.value.files)
                let payload = {
                    token: store.getters['root/getToken'],
                    notice: formData
                }
                store.dispatch('root/uploadNotice', payload)
                .then((result)=>{
                    //console.log("upload notice")
                    //console.log(result)
                })
                .catch((err)=>{
                    //console.log(err)
                })
            }else{
                notify({
                    title: "From MBOTC 😉",
                    text: "Please fill in the blanks.",
                    type: "warn"
                });
            }
        }
        onMounted(()=>{
            let wraperHeight = mdEditorWraper.value.clientHeight + 'px'
            //console.log(mdEditorWraper)
            //console.log(wraperHeight)
            state.mountEditor = new Editor({
                el: mdEditor.value,
                height: wraperHeight,
                initialEditType: "markdown",
                previewStyle: "vertical",
                plugins: [colorSyntax],
                theme: (store.getters['root/getThemeId'] == 1 || store.getters['root/getThemeId'] == 2)?"dark":"light"
            });
        })
        const init = ()=>{
            let today = new Date()
            state.today = today.getFullYear() + "-" + parseInt(today.getMonth()+1) + "-"
            if(today.getDate() < 10){
                state.today = state.today + "0" + today.getDate()
            }else{
                state.today = state.today + today.getDate()
            }
            let payload = store.getters['root/getUserData']
            store.dispatch('root/getUserSetting', payload)
            .then((result)=>{
                state.teams = []
                //console.log(result)
                let index = 0;
                result.data.teams.forEach(data => {
                    let team = data
                    team.id = index
                    team.open = false,
                    index++
                    state.teams.push(team)
                }); 
                //console.log(state.teams)
            })
            .catch((err)=>{
            })
            //console.log(state.today)
        }
        init()
        return { state, submit, mdEditor, mdEditorWraper, fileRoot, uploadFile, deleteFile }
    },
};
</script>

<style scoped>
</style>