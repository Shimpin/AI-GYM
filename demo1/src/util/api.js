import axios from "axios";
import {getJwt, removeJwt} from "@/api/jwt.js";

//创建一个axios实例
let api = axios.create({
    baseURL: "/api",
    timeout: 3000
});

//配置响应拦截器
api.interceptors.response.use(resp => {
    let captchakey = resp.headers["Captcha-Key"];
    return resp.data;
}, resp => {
    let data = resp.response.data;
    if(resp.status === 401){
        removeJwt();
        location.href = "/login";
    }
    return data;
});

api.interceptors.request.use(config => {
    config.headers["Authorization"] = getJwt();
    return config;
}, error => Promise.reject(error))


export default api;