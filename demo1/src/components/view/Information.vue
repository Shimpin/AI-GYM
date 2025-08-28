<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Check, Close, User, Lock } from '@element-plus/icons-vue'
import api from '../../util/api'
import baseUrl from '../../util/constant.js'
import {getJwt, parseJwt} from "@/api/jwt.js";
import router from "@/router/index.js";

// 响应式数据
const editMode = ref(false)
const changePassword = ref(false)
const userFormRef = ref()
const passwordFormRef = ref()

// 用户信息
const userInfo = ref({
  id: parseJwt(getJwt()).userId,
  phone: null,
  name: '',
  password: '',
  avatar: '',
})

// 密码表单
const passwordForm = ref({
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 组件挂载后获取用户信息
onMounted(() => {
  getUserInfo()
})

// 获取用户信息
async function getUserInfo() {
  try {
    // 这里假设有获取当前用户信息的接口
    // 如果没有，可以从localStorage或其他地方获取
    const resp = await api({
      url: '/admin',
      method: 'get',
      params: {
        pageNo:1,
        pageSize:1,
        id: userInfo.value.id
      }
    })
    
    if (resp.success) {
      userInfo.value = resp.data.records[0];
    } else {
      ElMessage.error('获取用户信息失败1')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败2')
  }
}

// 保存用户信息
async function saveInfo() {
  try {
    await userFormRef.value.validate()
    
    const params = {
      ...userInfo.value
    }
    
    // 如果密码为空，则不更新密码
    if (!params.password) {
      params.password = undefined
    }
    
    const resp = await api({
      url: '/admin',
      method: 'put',
      data: params
    })
    
    if (resp.success) {
      ElMessage.success('保存成功')
      editMode.value = false
      getUserInfo() // 重新获取用户信息
    } else {
      ElMessage.error(resp.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  }
}

// 取消编辑
function cancelEdit() {
  editMode.value = false
  getUserInfo() // 重新获取用户信息，恢复原始数据
}

// 头像上传成功
function avatarUploadSuccess(resp) {
  if (resp.success) {
    userInfo.value.avatar = resp.data
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

// 自定义上传方法
function customUpload(options) {
  const formData = new FormData()
  formData.append('file', options.file)

  api({
    url: '/admin/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(resp => {
    if (resp.success) {
      options.onSuccess(resp)
    } else {
      options.onError({ message: resp.msg || '上传失败' })
    }
  }).catch(resp => {
    options.onError({ message: resp.msg || '上传失败' })
  })
}

// 提交密码修改
async function submitPasswordChange() {
  try {
    await passwordFormRef.value.validate()
    
    const resp = await api({
      url: '/admin/password',
      method: 'put',
      data: {
        id: userInfo.value.id,
        name: userInfo.value.name,
        password: passwordForm.value.newPassword
      }
    })
    
    if (resp.success) {
      ElMessage.success('密码修改成功')
      changePassword.value = false
      // 清空表单
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    } else {
      ElMessage.error(resp.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败')
  }
}
function returnMain(){
  router.push("/main")
}
</script>

<template>
  <div class="information-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>个人信息</h2>
      <p>管理您的个人资料和账户信息</p>
    </div>

    <!-- 个人信息卡片 -->
    <div class="info-card">
      <div class="card-header">
        <h3>基本信息</h3>
        <el-button type="primary" @click="editMode = true" v-if="!editMode">
          <el-icon><Edit /></el-icon>
          编辑信息
        </el-button>
        <div v-else>
          <el-button type="success" @click="saveInfo">
            <el-icon><Check /></el-icon>
            保存
          </el-button>
          <el-button @click="cancelEdit">
            <el-icon><Close /></el-icon>
            取消
          </el-button>
        </div>
      </div>

      <div class="card-content">
        <el-row :gutter="24">
          <!-- 头像区域 -->
          <el-col :span="8">
            <div class="avatar-section">
              <div class="avatar-container">
                <div class="avatar" v-if="userInfo.avatar" 
                     :style="'background-image: url('+(baseUrl+userInfo.avatar)+')'"></div>
                <div class="avatar-placeholder" v-else>
                  <el-icon><User /></el-icon>
                </div>
                
                <!-- 头像上传 -->
                <div class="avatar-upload" v-if="editMode">
                  <el-upload 
                    class="avatar-uploader"
                    :http-request="customUpload"
                    :on-success="avatarUploadSuccess" 
                    :show-file-list="false"
                    accept="image/*">
                    <el-button size="small" type="primary">更换头像</el-button>
                  </el-upload>
                </div>
              </div>
            </div>
          </el-col>

          <!-- 信息表单区域 -->
          <el-col :span="16">
            <el-form 
              :model="userInfo" 
              :rules="rules" 
              ref="userFormRef" 
              label-width="100px"
              :disabled="!editMode">
              
              <el-form-item label="手机号：" prop="phone">
                <el-input 
                  v-model="userInfo.phone" 
                  placeholder="请输入手机号"
                  :disabled="true" />
              </el-form-item>

              <el-form-item label="姓名：" prop="name">
                <el-input 
                  v-model="userInfo.name" 
                  placeholder="请输入姓名" />
              </el-form-item>

              <el-form-item label="密码：" prop="password" v-if="editMode">
                <el-input 
                  v-model="userInfo.password" 
                  type="password" 
                  placeholder="请输入新密码（不修改请留空）"
                  show-password />
              </el-form-item>

            </el-form>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 账户安全卡片 -->
    <div class="info-card">
      <div class="card-header">
        <h3>账户安全</h3>
      </div>
      <div class="card-content">
        <div class="security-item">
          <div class="security-info">
            <el-icon><Lock /></el-icon>
            <span>登录密码</span>
            <p>定期更换密码可以保护账户安全</p>
          </div>
          <el-button type="primary" @click="changePassword = true">修改密码</el-button>
        </div>
      </div>
    </div>
    <el-button type="primary" @click="returnMain">返回页面</el-button>

    <!-- 修改密码对话框 -->
    <el-dialog 
      v-model="changePassword" 
      title="修改密码" 
      width="400px"
      :close-on-click-modal="false">
      <el-form 
        :model="passwordForm" 
        :rules="passwordRules" 
        ref="passwordFormRef" 
        label-width="100px">

        <el-form-item label="新密码：" prop="newPassword">
          <el-input 
            v-model="passwordForm.newPassword" 
            type="password" 
            placeholder="请输入新密码"
            show-password />
        </el-form-item>

        <el-form-item label="确认密码：" prop="confirmPassword">
          <el-input 
            v-model="passwordForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入新密码"
            show-password />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div>
          <el-button @click="changePassword = false">取消</el-button>
          <el-button type="primary" @click="submitPasswordChange">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.information-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-header h2 {
  color: #303133;
  margin-bottom: 8px;
  font-size: 28px;
  font-weight: 600;
}

.page-header p {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.info-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  overflow: hidden;
}

.card-header {
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
}

.card-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.card-content {
  padding: 24px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.avatar-container {
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  border: 4px solid #e4e7ed;
  margin-bottom: 16px;
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background-color: #f5f7fa;
  border: 4px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.avatar-placeholder .el-icon {
  font-size: 48px;
  color: #c0c4cc;
}

.avatar-upload {
  margin-top: 8px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.security-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.security-info .el-icon {
  font-size: 20px;
  color: #409eff;
}

.security-info span {
  font-weight: 500;
  color: #303133;
}

.security-info p {
  margin: 4px 0 0 0;
  color: #909399;
  font-size: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .information-container {
    padding: 16px;
  }
  
  .card-content {
    padding: 16px;
  }
  
  .avatar {
    width: 100px;
    height: 100px;
  }
  
  .avatar-placeholder {
    width: 100px;
    height: 100px;
  }
  
  .security-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
}
</style>