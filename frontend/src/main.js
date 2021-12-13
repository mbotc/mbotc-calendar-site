import App from './App.vue'
import router from "./common/lib/router.js";
import store from "./common/lib/store.js";
import VueAxios from "./common/lib/axios.js";
import axios from "./common/lib/axios.js";
import './common/css/main.css'
import Notifications from '@kyvg/vue3-notification'
import { createApp, h } from 'vue'
import PerfectScrollbar from 'vue3-perfect-scrollbar'
import 'vue3-perfect-scrollbar/dist/vue3-perfect-scrollbar.css'

//const app = createApp(App);
const app = createApp({
    render: () => h(App)
})
app.use(VueAxios, axios);
app.use(store);
app.use(router);
app.use(PerfectScrollbar)
app.use(Notifications);

app.mount("#app");
