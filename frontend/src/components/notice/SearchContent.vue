<template>
    <div class="border-label border-4 rounded-xl shadow-lg p-12 m-4 text-font" :class="{'bg-black':(state.themeId == 2), 'bg-panel':(state.themeId != 2) }">
        <div class="overflow-hidden flex justify-between">
            <div class="overflow-hidden flex justify-start">
                <span class="h-10 text-3xl font-bold inline-block align-bottom overflow-hidden mr-2">{{notice.team}}</span>
                <span class="h-10 text-xl inline-block align-bottom overflow-hidden pt-2">{{notice.channel}}</span>
            </div>
            <span class="w-1/6 h-10 text-xl font-bold inline-block align-bottom overflow-hidden cursor-pointer text-right mr-5" @click="close">x Close</span>
        </div>
        <div class="overflow-hidden flex justify-between">
            <div class="overflow-hidden flex justify-start mt-3">
                <img :src="logo" alt="logo" class="h-6 w-6 mr-2">
                <div class="h-10 text-xl align-text-bottom overflow-hidden mr-8 ">{{notice.user}}</div>
                <div class="h-10 text-xl align-text-bottom overflow-hidden text-gray-500">{{notice.startTime}} ~ {{notice.endTime}}</div>
            </div>
            <div class="overflow-hidden flex justify-end">
                <div v-if="notice.files!=null" class="h-10 pb-2 mr-4">
                    <button class="h-8 px-2 bg-back text-main align-bottom rounded text-sm" @click="download">
                        Download files
                    </button>
                </div>
                <div v-if="state.myNoticeFlag" class="pt-2">
                    <button class="bg-red-500 h-8 px-2 text-white align-bottom rounded text-sm hover:bg-red-700 mr-5" @click="deleteNotice">Delete</button>
                </div>
            </div>
        </div>
        <hr>
        <perfect-scrollbar ref="mdViewerWraperSearch" class="text-lg max-h-96 p-4">
            <div id="editor" ref="mdViewerSearch" class="text-font"></div>
            <div class="h-32"></div>
        </perfect-scrollbar>
    </div>
</template>
<script>
import "@toast-ui/editor/dist/toastui-editor.css"; 
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import Editor from "@toast-ui/editor";
// import abc from '@/components/'
import { reactive, ref, onUpdated } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import logo_0 from '@/assets/logo/logo_0.png'

export default {
    name: 'SearchContent',
    components: {
    },
    props:{
        notice:{
            title : {                
                type: String,
                default: " ",
            },
            team : {
                type: String,
                default: " ",
            },
            channel : {
                type: String,
                default: " ",
            },
            content : {
                type: String,
                default: " ",
            },
            files : {
                type: Array,
                default: [],
            },
            check : {
                type: Boolean,
                default: false
            },
            user : {
                type: String,
                default: " ",
            },
            userId : {
                type: String,
                default: " ",
            },
            startTime : {
                type: String,
                default: " ",
            },
            endTime : {
                type: String,
                default: " ",
            },
            time : {
                type: String,
                default: " ",
            },
        },
    },
    setup(props, {emit}){
        const router = useRouter()
        const logo = logo_0
        const mdViewerSearch = ref(null)
        const mdViewerWraperSearch = ref(null)
        const store = useStore()
        const state = reactive({
            mountViewr: null,
            themeId : store.getters['root/getThemeId'],
            fileList:[],
            targetFile: 0,
            myNoticeFlag: false,
        })

        const close = ()=>{
            emit('close')
        }

        onUpdated(()=>{
            if(store.getters['root/getUserId'] === props.notice.userId){
                state.myNoticeFlag = true
            }else{
                state.myNoticeFlag = false
            }
            state.fileList = []
            state.mountViewer = new Editor.factory({
                el: mdViewerSearch.value,
                viewer: true,
                // height: wraperHeight,
                initialValue: props.notice.content,
                theme: (state.themeId== 1 || state.themeId == 2)?"dark":"light"
            });

            changeFontSize();
        })

        const download = ()=>{
            if(props.notice.files != null){
                let fileIds = props.notice.files.split(",")
                fileIds.forEach(file => {
                    var xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function(){
                        if (this.readyState == 4 && this.status == 200){
                        
                            let fileName = "unknown";
                            let disposition = xhr.getResponseHeader('Content-Disposition');
                            if (disposition) {
                                const [ fileNameMatch ] = disposition.split(';').filter(str => str.includes('filename'));
                                if (fileNameMatch)
                                    [ , fileName ] = fileNameMatch.split('=');
                            }
                            fileName = fileName.replace(new RegExp('["]','g'), '');
                            //this.response is what you're looking for
                            //console.log(this.response, typeof this.response);
                            let a = document.createElement("a");
                            let url = URL.createObjectURL(this.response)
                            a.href = url;
                            a.download = fileName;
                            document.body.appendChild(a);
                            a.click();
                            window.URL.revokeObjectURL(url);
                        }
                    }
                    xhr.open('get', '/api/v4/files/' + file);
                    xhr.setRequestHeader("Authorization", "bearer " + store.getters['root/getToken'])
                    xhr.responseType = 'blob';
                    xhr.send();
                });
            }
        }
        const deleteNotice = ()=>{
            let payload={
                token: store.getters['root/getToken'],
                postId: props.notice.token
            }
            store.dispatch('root/deleteNotice', payload)
            .then((result)=>{
                if(result.status == 200){
                    router.go()
                }
            })
            .catch((err)=>{
            })
            
        }
        const changeFontSize = () => {
            document.getElementsByClassName("toastui-editor-contents")[0].style.fontSize = "20px";
        }
        return { mdViewerSearch, mdViewerWraperSearch, close, state, logo, onUpdated, download, deleteNotice  }
    }
};
</script>

<style scoped>
</style>