import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from "path"

// https://vite.dev/config/
export default defineConfig({
    plugins: [vue()],
    server: {
        host: "localhost",
        port: 5000,
        open: true,
        //代理
        proxy: {
            "/api": {
                target: "http://localhost:8080",
                changeOrigin: true,
                rewrite: url => url.replace(/^\/api/, "/api/v1")
            }
        }
    },
    resolve: {
        alias: [
            {
                find: "@",
                replacement: resolve(__dirname, "src")
            }
        ]
    }
})
