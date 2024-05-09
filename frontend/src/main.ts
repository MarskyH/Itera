import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import money from 'v-money3'
import VueTheMask from 'vue-the-mask'
import '@oruga-ui/oruga-next/dist/oruga.css'
import '@oruga-ui/oruga-next/dist/oruga-full.css'
import './style.css'
import VueScrollTo from 'vue-scrollto'

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import { fas } from '@fortawesome/free-solid-svg-icons'

/* add icons to the library */
library.add(fas)

const pinia = createPinia()

const app = createApp(App)

app.use(router).use(money).use(VueTheMask).use(pinia).use(VueScrollTo).component('FontAwesomeIcon', FontAwesomeIcon).mount('#app')
