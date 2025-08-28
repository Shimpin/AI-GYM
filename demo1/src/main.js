import { createApp } from 'vue'
import '@/style.css'
import App from '@/App.vue'

//导入路由转发器
import router from "@/router"
//导入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);
app.use(router);

//设置使用element-plus
app.use(ElementPlus, {
    locale: zhCn,
})

//创建一个应用
app.mount('#app')