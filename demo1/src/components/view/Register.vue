<!-- Register.vue -->
<template>
  <div class="main">
    <div class="register-body">
      <div class="pic"></div>
      <div class="form">
        <h1>健身会馆注册</h1>
        <el-form style="padding: 10px" :model="registerFormModel" :rules="rules" ref="registerFormRef">
          <el-row>
            <el-col :span="24">
              <el-form-item label="用户名:" prop="name" :label-width="80">
                <el-input v-model="registerFormModel.name" placeholder="请输入用户名" style="height: 40px;"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="手机号:" prop="phone" :label-width="80">
                <el-input v-model="registerFormModel.phone" placeholder="请输入手机号" style="height: 40px;"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="密码:" prop="password" :label-width="80">
                <el-input type="password" v-model="registerFormModel.password" show-password placeholder="请输入密码"
                          style="height: 40px" autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="确认密码:" prop="confirmPassword" :label-width="85">
                <el-input type="password" v-model="registerFormModel.confirmPassword" show-password placeholder="请再次输入密码"
                          style="height: 40px" autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>

          </el-row>
          <el-row>
            <el-col :span="24">
              <el-button type="primary" class="register-btn" @click="submitRegister">注&nbsp;&nbsp;册</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-button type="primary" style="margin-top: 20px" @click="goToLogin">返回登录</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    <div class="mask"></div>
    <div class="copyright">
      <h2>&copy;版权所有 中享思途-健身会馆</h2>
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

.register-body {
  width: 700px;
  height: 550px;
  background-color: #fff;
  display: flex;
  flex-direction: row;
  z-index: 1000;
}

.register-body > .pic {
  width: 180px;
  background: url("@/assets/login.png") no-repeat center center/cover;
}

.register-body > .form {
  flex-grow: 1;
}

.register-body > .form > h1 {
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

.register-btn {
  width: 100%;
  height: 50px;
  background-color: #16b777;
  color: #fff;
  font-size: 18px;
}

</style>

<script setup>
import {ref, reactive, toRaw} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {v4 as uuid} from "uuid";

// 表单验证规则
const rules = {
  name: [
    {required: true, message: "用户名不可为空"},
    {min: 3, max: 20, message: "用户名长度必须在3-20个字符之间"}
  ],
  phone: [
    {required: true, message: "手机号不可为空"},
    {pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号"}
  ],
  password: [{
    required: true, message: "密码不可为空"
  }, {
    min: 6, max: 20, message: "密码长度必须在6-20个字符之间"
  }],
  confirmPassword: [{
    required: true, message: "请确认密码"
  }, {
    validator: (rule, value, callback) => {
      if (value !== registerFormModel.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    }
  }]
};

// 注册数据模型
const registerFormModel = reactive({
  name: "",
  phone: "",
  password: "",
  confirmPassword: "",
});

let registerFormRef = ref(); // 表单实例引用

import {saveJwt} from "@/api/jwt.js";
import router from "@/router/index.js";
import api from "@/util/api.js";

// 提交注册
async function submitRegister() {
  const {phone, password, name} = registerFormModel;
  let params = {phone, password, name};
  registerFormRef.value.validate(async valid => {
    if (valid) {
       api({
         url: "/users/register",
         method: "post",
         data: params
       }).then(resp => {
         if (resp.success) {
           ElMessage.success("注册成功");
           router.push("/login")
         }else {
           ElMessage.error(resp.msg || "注册失败");
         }
       }).catch(resp => {
         ElMessage.error(resp.msg || "注册失败");
       });
    } else {
      ElMessage.error("输入不合法");
    }
  });
}

// 返回登录页
function goToLogin() {
  router.push("/login");
}

</script>
