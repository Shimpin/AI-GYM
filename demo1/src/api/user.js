import api from "@/util/api.js";


const url = "/users";

function login(params) {
    return api({
        url: url + "/login",
        method: "post",
        data: params
    });
}

export {login}