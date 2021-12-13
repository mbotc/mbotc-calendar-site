<template>
    <div class="w-full h-screen flex flex-wrap content-center">
        <div class="w-1/2 mx-auto bg-gray-100 text-center rounded-2xl shadow-lg">
            <div class="pt-16 text-dark bold flex justify-start">
                <div class="ml-10 h-16 items-center">
                    <p class="font-bold text-2xl text-blue-500">Sign in</p><br/>
                </div>
            </div>
            <div v-if="!state.hasToken&&!state.hasCookie">
                <div>
                    <input type="text" class="rounded w-4/5 h-10 border-2 mt-3 pl-2" :disabled="state.serverLock" placeholder="Server URL" v-model="state.url" @change="validationCheck">
                    <input type="text" class="rounded w-4/5 h-10 border-2 mt-3 pl-2" placeholder="Email" v-model="state.email" @change="validationCheck">
                    <input type="password" class="rounded w-4/5 h-10 border-2 mt-3 pl-2" placeholder="Password" v-model="state.password" @change="validationCheck" @keyup.enter="submit">
                </div>
                <div class="flex justify-between p-8">
                    <div>
                        <p v-if="!state.clickable" class="text-red-700">Please check your input.</p>
                    </div>
                    <button class="bg-gray-200 text-white font-bold py-2 px-4 m-2 rounded" :class="{'bg-blue-500':state.clickable, 'hover:bg-blue-700':state.clickable, 'cursor-not-allowed':!state.clickable}" @click="submit">Take Me!</button>
                </div>
            </div>
            <div v-else>
                <div class="mx-auto h-16 w-4/5 items-center">
                    <p v-if="state.hasToken" class="text-xl">Welcome Back!</p> 
                    <p v-else-if="state.hasCookie" class="text-xl">You're already logged in Mattermost</p>
                    <p class="font-bold text-2xl">{{state.userData.userName}}</p>
                    <br/>
                </div>
                <div class="m-5">
                    <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 m-2 rounded"  @click="comeBack">Take Me!</button>
                    <button v-if="!state.hasCookie" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 m-2 rounded"  @click="logout">Logout</button>
                </div>
            </div>
            <div>
                <div class="text-blue-700 text-right mr-5">
                    <p class="text-2xl">
                        The most <br>
                        beautiful and convenient <br>
                        Mattermost Calendar <br>
                    </p>
                    <div class="my-5 flex justify-end items-center">
                        <p class="italic text-xl mr-5">with mattermost</p>
                        <img class="w-10 h-10" src="@/assets/mattermost.png" alt="mattermost">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { getServerURL, getServerLock } from '../../common/lib/function.js'
import { notify } from '@kyvg/vue3-notification'

export default {
    name: 'WelcomeLogin',
    components: {
    },

    setup(){
        const store = useStore()
        const router = useRouter()
        const state = reactive({
            url:"",
            email:"",
            password:"",
            loginToggle: false,
            clickable: false,
            hasCookie: false,
            hasToken: false,
            serverLock: getServerLock(),
            userData:{
                token: "",
                url: getServerURL(),
                userEmail: "",
                userId: "",
                userName: "",
            }
        })
        const submit = ()=>{
            if(state.clickable){
                let payload = {
                    url: state.url,
                    loginData:{
                        "device_id": "",
                        "login_id": state.email,
                        "password": state.password,
                        "token": ""
                    }
                }
                store.dispatch('root/userLoginMM',payload)
                .then((result)=>{
                    console.log(result)
                    let userData = {
                        token: result.headers.token,
                        url: getServerURL(),
                        userEmail: result.data.email,
                        userId: result.data.id,
                        userName: (result.data.nickname==="")?result.data.username:result.data.nickname,
                    }
                    store.commit('root/setUserData', userData)
                    register()
                })
                .catch((err)=>{
                    console.log('------>',err)
                    notify({
                        title: "From MBOTC 😅",
                        text: "Check your server URL, Email and PW",
                        type: "warn"
                    });
                })
            }
        }
        const register = ()=>{
            let userData = store.getters['root/getUserData']
            store.dispatch('root/userLogin', userData)
            .then((result)=>{
                if(result.status == 200 ||  result.status == 201){
                    if(!state.hasCookie){
                        router.push("/main")
                    }else{
                        store.commit('root/setToken', result.data.token)
                    }
                }
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        const validationCheck = ()=>{
            var emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
            
            if(!state.email.match(emailRegExp)){
                state.clickable = false
            }else if(state.password.length == 0){
                state.clickable = false
            }else{
                state.clickable = true
            }

        }
        const comeBack = ()=>{
            router.push("/main")
        }
        const init = ()=>{
            state.url = getServerURL()
            if(document.cookie && hasCookie()){
                state.hasCookie = true
                store.dispatch('root/getUserMM')
                .then((result)=>{
                    state.userData = {
                        token: "cookie",
                        url: getServerURL(),
                        userEmail: result.data.email,
                        userId: result.data.id,
                        userName: (result.data.nickname==="")?result.data.username:result.data.nickname,
                    }
                    store.commit('root/setUserData', state.userData)
                    register()
                })
                .catch((err)=>{
                })
            }else if(hasToken()){
                state.hasToken = true
            }
        }
        const hasCookie = ()=>{
            let userId = document.cookie.match(new RegExp('(^| )' + "MMUSERID" + '=([^;]+)'));
            let csrf = document.cookie.match(new RegExp('(^| )' + "MMCSRF" + '=([^;]+)'));
            if(userId != null && csrf != null){
                return ((userId.length>0) && (csrf.length>0))
            }
            return false
        }
        const hasToken = ()=>{
            let userData = store.getters['root/getUserData']
            if(userData.token!='' && userData.url!='' && userData.userEmail!='' && userData.userId!='' && userData.userName!=''){
                return true
            }
            return false
        }
        const logout = ()=>{
            if(state.hasToken){
                store.commit('root/logout')
                state.hasCookie = false
                state.hasToken = false
            }
            router.push("/")
        }
        init()
        return { state, submit, validationCheck, comeBack, logout }
    }
};
</script>

<style scoped>
</style>