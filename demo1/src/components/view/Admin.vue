<template>

  <!-- 按钮区 -->
  <div class="action">
    <el-button type="primary" :icon="CirclePlus" @click="add">新增</el-button>
  </div>

  <!-- 表格区 -->
  <div class="grid">
    <el-table :data="admin" stripe border @row-click="tblRowClick" ref="tblRef" class="data-grid">

      <el-table-column prop="id" label="ID" width="100" fixed/>

      <el-table-column label="头像" width="150" fixed>
        <template #default="scope">
          <div class="row-avatar" :style="'background-image: url('+(baseUrl+scope.row.avatar)+')'"></div>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="手机号" fixed/>
      <el-table-column prop="name" label="姓名" width="300" fixed/>
      <el-table-column prop="email" label="邮箱" width="300"/>
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click.stop="editRow(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click.stop="deleteRow(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- 分页条 -->
  <div class="pagination">
    <el-pagination
        v-model:current-page="memberPi.pageNo"
        v-model:page-size="memberPi.pageSize"
        :page-sizes="[10, 20, 30, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="memberPi.total"
        class="member-pi"
        background
        @change="memberPiChange"
    />
  </div>

  <!-- 添加/修改对话框 -->
  <el-dialog v-model="adminDlgShow" :title="adminDlgTitle" width="600"
             draggable align-center :close-on-click-modal="false" @close="resetMemberForm">
    <el-form label-width="100" label-position="right"
             :model="adminModel" :rules="rules" ref="adminFormRef">
      <el-row>
        <el-col :span="16">
          <el-form-item label="手机号：" prop="phone">
            <el-input v-model="adminModel.phone" placeholder="请输入管理员手机号"/>
          </el-form-item>

          <el-form-item label="姓名：" prop="name">
            <el-input v-model="adminModel.name" placeholder="请输入管理员姓名"/>
          </el-form-item>

          <el-form-item label="密码：" prop="password">
            <el-input v-model="adminModel.password" placeholder="请输入管理员密码"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-upload class="avatar" 
                     :http-request="customUpload"
                     :on-success="avatarUploadSuccess" 
                     :show-file-list="false">
            <div class="img" v-if="adminModel.avatar"
                 :style="'background-image: url('+(baseUrl+adminModel.avatar)+')'"></div>
            <el-icon v-else class="icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </el-col>
      </el-row>

    </el-form>
    <template #footer>
      <div>
        <el-button type="primary" @click="doSubmit">确定</el-button>
        <el-button type="primary" @click="adminDlgShow=false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<!--样式-->
<style scoped>
.member-pi {
  margin-top: 6px;
}

.data-grid {
  margin-top: 6px;
}

.avatar {
  width: 140px;
  height: 140px;
  border: 1px dashed #ccc;
  border-radius: 4px;
  margin-left: 8px;
  display: flex;
}

.avatar .icon {
  font-size: 28px;
  justify-content: center;
  align-items: center;
}

.avatar .img {
  width: 140px;
  height: 140px;
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center center;
}

.row-avatar {
  width: 60px;
  height: 60px;
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center center;
  border: 1px solid #ccc;
}
</style>

<style>
.el-upload {
  width: 100%;
}
</style>

<script setup>
import {onMounted, ref, reactive, toRaw, nextTick} from "vue"
import api from "@/util/api";
import {CirclePlus, Delete, Edit, Plus, Refresh, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
//深度克隆
import {cloneDeep} from "lodash";
//当组件挂载完毕之后触发
onMounted(() => {
  search();
});
///////////////////////////////////
let admin = ref();//创建一个响应式对象
//管理员数据分页
let memberPi = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
});
//搜索条件模型，双向数据绑定
let adminSearchModel = ref({
  id: null,
  name: null,
  phone: null
});
//表单对象
let adminSearchForm = ref();

//查询全部管理员
async function search(pageNo = 1, pageSize = 10) {
  //console.log(toRaw(adminSearchModel.value))
  let params = toRaw(adminSearchModel.value);//查询条件
  if (params.createRange) {
    params.createFrom = params.createRange[0];
    params.createTo = params.createRange[1];
    delete params.createRange;
  }

  //console.log(params);

  let resp = await api({
    url: "/admin",
    method: "get",
    params: {
      pageNo,
      pageSize,
      ...params
    }
  });

  //console.log(resp);
  admin.value = resp.data.records;//管理员数据
  memberPi.pageNo = resp.data.current;
  memberPi.pageSize = resp.data.size;
  memberPi.total = resp.data.total;
}

//当页码或页面大小改变时触发
function memberPiChange(pageNo, pageSize) {
  //console.log(pageNo, pageSize);
  search(pageNo, pageSize);
}

//查询表单重置
function reset() {
  //第1种解决方案
  /*adminSearchModel.value = {
    id: null,
    name: null,
    sex: null,
    createRange: [],
    phone: null
  };*/

  //第2种解决方案：调用表单对象的函数
  adminSearchForm.value.resetFields();
}

//表格对象
let tblRef = ref();

//表格行单击事件
function tblRowClick(row) {
  tblRef.value.toggleRowSelection(row);
}

//点击删除按钮
function remove() {
  let rows = tblRef.value.getSelectionRows();
  if (rows.length === 0) {
    ElMessage.warning("请选中您要删除的行");
  } else {
    ElMessageBox.confirm("是否确认删除选中的行?", "警告", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(() => {
      //执行操作
      let ids = rows.map(it => it.id);
      removeByIds(ids);
    }).catch(() => {
      //do nothing...
    });
  }
}

//批量删除管理员，参数为管理员id数组
async function removeByIds(ids) {
  let resp = await api({
    url: "/admin",
    method: "delete",
    data: ids
  });

  //console.log(resp);
  if (resp.success) {
    ElMessage.success("删除操作成功，共删除" + resp.data + "条");
    search();
  } else {
    ElMessage.error("删除失败，请稍候再试或联系管理员");
  }
}

//////////////////////////////////////////////////
let adminDlgTitle = ref("新增管理员");
//对话框是否显示
let adminDlgShow = ref(false);
//管理员添加/新增表单
let adminModel = ref({
  phone: null,
  name: null,
  password: null,
  avatar: null
});
//新增表单对象
let adminFormRef = ref();
let mode = "add";//标志位

//新增按钮
function add() {
  adminDlgTitle.value = "新增管理员";
  adminDlgShow.value = true;
  mode = "add";
  const now = new Date();
  adminModel.value.createTime = formatDate(now);
}

// 格式化日期时间函数
function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

//点击确定按钮的操作
function doSubmit() {
  if (mode === "add") {
    submitAdd();
  } else {
    submitEdit();
  }
}

//提交，保存管理员信息
function submitAdd() {
  let params = toRaw(adminModel.value);

  //手动校验
  adminFormRef.value.validate(valid => {
    if (valid) {
      //以异步回调形式调用
      api({
        url: "/admin",
        method: "post",
        data: params
      }).then(resp => {
        if (resp.success) {
          adminDlgShow.value = false;
          search();
          ElMessage.success("保存管理员信息成功");
        } else {
          ElMessage.error(resp.msg || "保存管理员信息失败");
        }
      }).catch(resp => {
        ElMessage.error(resp.msg || "保存管理员信息失败");
      });
    } else {
      ElMessage.error("输入不正确");
    }
  });
}

//重置管理员表单
function resetMemberForm() {
  adminFormRef.value.resetFields();
}

/////////////////////////////////////////////////////////////////
function edit() {
  let rows = tblRef.value.getSelectionRows();
  if (rows.length === 0) {
    ElMessage.warning("请选中您要修改的行");
  } else if (rows.length > 1) {
    ElMessage.warning("您一次只能修改一行");
  } else {
    adminDlgShow.value = true;
    adminDlgTitle.value = "修改管理员";
    mode = "edit";

    //在下一个时间滴答中操作
    nextTick(() => {
      //深度克隆
      adminModel.value = cloneDeep(toRaw(rows[0]));
    });

    ////
  }
}

//提交修改
async function submitEdit() {
  let params = toRaw(adminModel.value);
  let resp = await api({
    url: "/admin",
    method: "put",
    data: params
  });

  if (resp.success) {
    adminDlgShow.value = false;
    search();
    ElMessage.success("修改管理员信息成功");
  }
}

/////////////////////////////////////////////////////////////////
function editRow(row) {
  //console.log(row);
  //console.log(row);
  adminDlgShow.value = true;
  adminDlgTitle.value = "修改管理员";
  mode = "edit";

  //在下一个时间滴答中操作
  nextTick(() => {
    //深度克隆
    adminModel.value = cloneDeep(toRaw(row));
  });
}

function deleteRow(row) {
  ElMessageBox.confirm("是否确认删除选中的行?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    removeByIds([row.id]);
  }).catch(() => {
    //do nothing...
  });
}

/////////////////////////////////////////////////////////
//校验规则
const rules = {
  phone: [ {
    min: 11,
    max: 11,
    message: "手机号必须是11位",
    trigger: "blur"
  }, {
    validator: validatePhone,
    trigger: "blur"
  }],
  name: [{
    required: true,
    message: "姓名不可为空",
    trigger: "blur"
  }]
};

//手机号校验
function validatePhone(rule, value, cb) {
  if (value.startsWith("1")) {
    return cb();
  } else {
    return cb(new Error("手机号必须以1开头"));
  }
}

/////////////////////////////////////////////
import baseUrl from "@/util/constant.js";

//头像上传成功
function avatarUploadSuccess(resp) {
  //console.log(url)
  adminModel.value.avatar = resp.data;
}

// 自定义上传方法
function customUpload(options) {
  const formData = new FormData();
  formData.append('file', options.file);

  api({
    url: '/admin/avatar', // 假设上传接口是 /admin/avatar
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(resp => {
    if (resp.success) {
      options.onSuccess(resp);
    } else {
      options.onError({ message: resp.msg || '上传失败' });
    }
  }).catch(resp => {
    options.onError({ message: resp.msg || '上传失败' });
  });
}
</script>