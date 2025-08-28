<template>
  <div class="main">
    <div class="login-body">
      <div class="pic"></div>
      <div class="form">
        <h1>健身会馆管理系统</h1>
        <el-form style="padding: 10px" :model="loginFormModel" :rules="rules" ref="loginFormRef">
          <el-row>
            <el-col :span="24">
              <el-form-item label="用户名:" prop="username" :label-width="80">
                <el-input v-model="loginFormModel.username" placeholder="请输入用户名" style="height: 40px;"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="密码:" prop="password" :label-width="80">
                <el-input type="password" v-model="loginFormModel.password" show-password placeholder="请输入密码"
                          style="height: 40px" autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="验证码:" prop="captcha" :label-width="80">
                <el-input placeholder="请输入验证码" v-model="loginFormModel.captcha" maxlength="4" minLength="4"
                          style="height: 40px;" autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <img class="captcha" :src="captchaUrl" style="height: 40px;" @click="refresh" alt="验证码">
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-button type="primary" class="login-btn" @click="submitLogin">登&nbsp;&nbsp;录</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-button type="primary" style="margin-top: 20px" @click="submitRegister">手机号&nbsp;&nbsp;注册</el-button>
                <el-button type="danger" style="margin-top: 20px;margin-left: 300px" @click="submitEmailLogin">邮箱&nbsp;&nbsp;登录</el-button>
            </el-col>
            <el-col>
              <el-button type="primary" style="margin-top: 20px" @click="submitEmailRegister">邮箱&nbsp;&nbsp;注册</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    <div class="mask"></div>
    <div class="copyright">
      <h2>&copy;版权所有 刀羊健身会馆</h2>
    </div>
  </div>
</template>

<style scoped>
.main {
  height: 100%;
  background: url("@/assets/login_bg.png") no-repeat center center/cover;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.main > .mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.4);
}

.login-body {
  width: 700px;
  height: 450px;
  background-color: #fff;
  display: flex;
  flex-direction: row;
  z-index: 1000;
}

.login-body > .pic {
  width: 180px;
  background: url("@/assets/login.png") no-repeat center center/cover;
}

.login-body > .form {
  flex-grow: 1;
}

.login-body > .form > h1 {
  font-size: 24px;
  color: #333;
  text-align: center
}

.copyright {
  position: fixed;
  bottom: 100px;
  color: #333;
  font-size: 12px;
}

.login-btn {
  width: 100%;
  height: 50px;
  background-color: #16b777;
  color: #fff;
  font-size: 18px;
}

.captcha {
  cursor: pointer;
}
</style>

<script setup>
import {ref, reactive, toRaw,onMounted} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {login} from "@/api/user.js";
import {v4 as uuid} from "uuid";

onMounted(() => {
  refresh();
});
//验证码id
const captchaId = ref();
//验证码地址
const captchaUrl = ref();

//刷新验证码
function refresh() {
  let id = uuid();
  captchaId.value = id;
  //此地址会被反向代理成http://localhost:8080的地址
  captchaUrl.value = "/api/users/captcha?id=" + captchaId.value;
}

//由于此规则不会对数据进行更改，所以没有使用ref函数
const rules = {
  username: [
    {required: true, message: "用户名不可为空"}
  ],
  password: [{
    required: true, message: "密码不可为空"
  }],
  captcha: [{
    required: true, message: "验证码不可为空"
  }, {
    min: 4, max: 4, message: "验证码必须为4位字符"
  }]
};

//登录数据模型
const loginFormModel = reactive({
  username: "15963294666",
  password: "123456",
  captcha: ""
});

let loginFormRef = ref();//表单实例引用


import {getJwt, parseJwt, saveJwt} from "@/api/jwt.js";
import router from "@/router/index.js";

//提交登录
function submitLogin() {
  loginFormRef.value.validate(async valid => {
    if (valid) {
      let model = toRaw(loginFormModel);
      model.captchaId = captchaId.value;
      let resp = await login(model);

      if (resp.success) {
        //保存jwt
        saveJwt(resp.data);
        //路由
        router.push("/main");
      } else {
        ElMessage.error(resp.error || "登录失败");
        refresh();
      }
    } else {
      ElMessage.error("输入不合法");
      refresh();
    }
  });
}
//提交注册
function submitRegister() {
  router.push("/register");
}
function submitEmailLogin() {
  router.push("/emailLogin");
}
function submitEmailRegister() {
  router.push("/emailRegister");
}
</script>