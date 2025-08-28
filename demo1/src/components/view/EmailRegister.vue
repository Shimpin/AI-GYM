<template>
  <div class="main">
    <div class="register-body">
      <div class="pic"></div>
      <div class="form">
        <h1>健身会馆邮箱注册</h1>
        <el-form style="padding: 10px" :model="registerFormModel" :rules="rules" ref="registerFormRef">
          <el-row>
            <el-col :span="24">
              <el-form-item label="邮箱:" prop="email" :label-width="80">
                <el-input v-model="registerFormModel.email" placeholder="请输入邮箱" style="height: 40px;"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="验证码:" prop="code" :label-width="80">
                <div class="input-group">
                  <el-input v-model="registerFormModel.code" placeholder="请输入验证码" style="height: 40px;"
                            autocomplete="off"/>
                  <el-button
                      type="primary"
                      @click="sendVerificationCode"
                      :disabled="isSending || !isEmailValid || countdown > 0"
                      style="margin-left: 10px; width: 120px"
                  >
                    <span v-if="isSending">发送中</span>
                    <span v-else-if="countdown > 0">{{ countdown }}秒后重试</span>
                    <span v-else>获取验证码</span>
                  </el-button>
                </div>
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
            <el-col :span="24">
              <el-button type="primary" class="register-btn" @click="submitRegister" :loading="isRegistering">
                注&nbsp;&nbsp;册
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-button type="primary" style="margin-top: 20px" @click="goToLogin">返回登录</el-button>
              <el-button type="danger" style="margin-top: 20px; margin-left: 20px" @click="goToPhoneRegister">手机号注册</el-button>
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

.input-group {
  display: flex;
  align-items: center;
}
</style>

<script setup>
import {ref, reactive, computed} from "vue";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import api from "@/util/api.js";
import {saveJwt} from "@/api/jwt.js";

const router = useRouter();

// 表单数据
const registerFormModel = reactive({
  email: '',
  code: '',
  password: '',
  confirmPassword: ''
});

// 状态变量
const isSending = ref(false);
const countdown = ref(0);
const isRegistering = ref(false);
const registerFormRef = ref();

// 验证邮箱格式
const isEmailValid = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(registerFormModel.email);
});

// 表单验证规则
const rules = {
  email: [
    { required: true, message: "邮箱不可为空" },
    { pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/, message: "请输入有效的邮箱地址" }
  ],
  code: [
    { required: true, message: "验证码不可为空" },
    { len: 6, message: "验证码必须为6位数字" }
  ],
  password: [
    { required: true, message: "密码不可为空" },
    { min: 6, message: "密码长度至少为6位" }
  ],
  confirmPassword: [
    { required: true, message: "请确认密码" },
    {
      validator: (rule, value, callback) => {
        if (value !== registerFormModel.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      }
    }
  ]
};

// 发送验证码
const sendVerificationCode = async () => {
  if (!isEmailValid.value) {
    ElMessage.error('请输入有效的邮箱地址');
    return;
  }

  isSending.value = true;

  try {
    await api({
      url: '/auth/send-code',
      method: 'get',
      params: {
        email: registerFormModel.email
      }
    });

    ElMessage.success('验证码已发送到您的邮箱');
    countdown.value = 300;

    // 开始倒计时
    const timer = setInterval(() => {
      if (countdown.value <= 0) {
        clearInterval(timer);
      } else {
        countdown.value--;
      }
    }, 1000);
  // }catch (error) {
  //   ElMessage.error('发送验证码失败: ' + (error.response?.data?.message || '请稍后重试'));
  } finally {
    isSending.value = false;
  }
};

// 提交注册
const submitRegister = async () => {
  registerFormRef.value.validate(async valid => {
    if (!valid) {
      ElMessage.error('请填写正确的注册信息');
      return;
    }

    isRegistering.value = true;

    try {
      const response = await api.post('/auth/register', {
        email: registerFormModel.email,
        code: registerFormModel.code,
        password: registerFormModel.password
      });

      if (response.data) {
        // saveJwt(response.data);
        ElMessage.success('注册成功！正在跳转...');
        setTimeout(() => {
          router.push('/login');
        }, 1500);
      }
    } catch (error) {
      ElMessage.error('注册失败: ' + (error.response?.data?.message || '请稍后重试'));
    } finally {
      isRegistering.value = false;
    }
  });
};

// 返回登录页
function goToLogin() {
  router.push("/login");
}

// 跳转到手机号注册
function goToPhoneRegister() {
  router.push("/register");
}
</script>