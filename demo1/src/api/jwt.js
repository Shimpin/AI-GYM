const jwtKey = "@#jwt_key"

function saveJwt(jwt) {
    sessionStorage.setItem(jwtKey, jwt)
}

function getJwt() {
    return sessionStorage.getItem(jwtKey)
}

function removeJwt() {
    sessionStorage.removeItem(jwtKey)
}

function parseJwt(token) {
    try {
        const base64Payload = token.split('.')[1];
        const base64= base64Payload.replace(/-/g, '+').replace(/_/g, '/');
        return JSON.parse(decodeURIComponent(escape(window.atob(base64))));
    } catch (error) {
        console.error('解析 JWT 失败:', error);
        return null;
    }
}

export {
    saveJwt,
    getJwt,
    removeJwt,
    parseJwt
}