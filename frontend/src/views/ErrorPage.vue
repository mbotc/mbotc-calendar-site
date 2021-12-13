<template>
    <div class="h-screen w-full p-10 bg-blue-300 text-font font-bold text-center">
        <div class="w-1/2 mx-auto bg-panel rounded-full">
            <p class="text-9xl">OOPS!</p>
            <p class="text-4xl">Something wrong</p>
            <br>
            <br>
                <img :src="src" alt="404" class="h-56 w-auto mx-auto">
            <br>
        </div>
        <button class="bg-back text-font font-bold py-2 px-4 m-2 rounded" @click="goBack">Previous Page</button>
    </div>
</template>
<script>
import error404 from '@/assets/404ERROR.png'
import { useRouter } from 'vue-router'

export default {
    name: 'ErrorPage',
    components: {
    },

    setup(){
        const router = useRouter()
        const src = error404;
        const goBack = () =>{
            router.go(-1)
        }
        return { src, goBack }

    }
};
</script>

<style scoped>
</style>