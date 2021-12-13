<template>
  <div v-bind:class="state.theme">
    <router-view/>
  </div>
</template>
<script>
import { reactive, computed } from 'vue'
import { useStore } from 'vuex'

export default {
    name: 'app',
    setup(){
      const store = useStore()
      store.commit('root/settingInit')
      const state = reactive({
          theme: computed(() => store.getters['root/getTheme']),
      })

      return { state }
    }
};
</script>
<style src="./common/css/main.css">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
body {
  padding: 0;
  margin: 0;
}
</style>

