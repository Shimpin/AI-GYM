<template>
  <!-- 查询区 -->
  <div class="search">
    <el-form inline :model="memberSearchModel" label-width="90" label-position="right" ref="memberSearchForm">
      <el-form-item label="ID：" prop="id">
        <el-input v-model="memberSearchModel.id" placeholder="请输入会员ID"/>
      </el-form-item>

      <el-form-item label="手机号：" prop="phone">
        <el-input v-model="memberSearchModel.phone" placeholder="请输入会员手机号"/>
      </el-form-item>

      <el-form-item label="姓名：" prop="name">
        <el-input v-model="memberSearchModel.name" placeholder="请输入会员姓名"/>
      </el-form-item>

      <el-form-item label="创建日期：" prop="createRange">
        <el-date-picker
            type="daterange"
            range-separator="~"
            start-placeholder="日期从"
            end-placeholder="日期止"
            v-model="memberSearchModel.createRange"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </el-form>
  </div>

  <!-- 按钮区 -->
  <div class="action">
    <el-button type="primary" :icon="CirclePlus" @click="add">新增</el-button>
    <el-button type="primary" :icon="Edit" @click="edit">修改</el-button>
    <el-button type="primary" :icon="Search" @click="search()">查询</el-button>
    <el-button type="primary" :icon="Refresh" @click="reset">重置</el-button>
    <el-button type="danger" :icon="Delete" @click="remove">删除</el-button>
  </div>

  <!-- 表格区 -->
  <div class="grid">
    <el-table :data="members" stripe border @row-click="tblRowClick" ref="tblRef" class="data-grid">
      <el-table-column type="selection" width="55" align="center" fixed/>
      <el-table-column prop="id" label="ID" width="80" fixed/>

      <el-table-column label="头像" fixed>
        <template #default="scope">
          <div class="row-avatar" :style="'background-image: url('+(baseUrl+scope.row.avatar)+')'"></div>
        </template>
      </el-table-column>

      <el-table-column prop="phone" label="手机号" width="150" fixed/>
      <el-table-column prop="name" label="姓名" width="150" fixed/>
      <el-table-column prop="createTime" label="创建日期" width="150"/>
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
  <el-dialog v-model="memberDlgShow" :title="memberDlgTitle" width="600"
             draggable align-center :close-on-click-modal="false" @close="resetMemberForm">
    <el-form label-width="100" label-position="right"
             :model="memberModel" :rules="rules" ref="memberFormRef">
      <el-row>
        <el-col :span="16">
          <el-form-item label="手机号：" prop="phone">
            <el-input v-model="memberModel.phone" placeholder="请输入会员手机号"/>
          </el-form-item>

          <el-form-item label="姓名：" prop="name">
            <el-input v-model="memberModel.name" placeholder="请输入会员姓名"/>
          </el-form-item>

        </el-col>
        <el-col :span="8">
          <el-upload class="avatar" 
                     :http-request="customUpload"
                     :on-success="avatarUploadSuccess" 
                     :show-file-list="false">
            <div class="img" v-if="memberModel.avatar"
                 :style="'background-image: url('+(baseUrl+memberModel.avatar)+')'"></div>
            <el-icon v-else class="icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </el-col>
      </el-row>

      <el-form-item label="创建日期：" prop="createTime">
        <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" v-model="memberModel.createTime"
                        placeholder="请输入会员创建日期" :readonly="true" :disabled="true"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <div>
        <el-button type="primary" @click="doSubmit">确定</el-button>
        <el-button type="primary" @click="memberDlgShow=false">取消</el-button>
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
let members = ref();//创建一个响应式对象
//会员数据分页
let memberPi = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
});
//搜索条件模型，双向数据绑定
let memberSearchModel = ref({
  id: null,
  name: null,
  sex: null,
  createRange: [],
  phone: null
});
//表单对象
let memberSearchForm = ref();

//查询全部会员
async function search(pageNo = 1, pageSize = 10) {
  //console.log(toRaw(memberSearchModel.value))
  let params = toRaw(memberSearchModel.value);//查询条件
  if (params.createRange) {
    params.createFrom = params.createRange[0];
    params.createTo = params.createRange[1];
    delete params.createRange;
  }

  //console.log(params);

  let resp = await api({
    url: "/members",
    method: "get",
    params: {
      pageNo,
      pageSize,
      ...params
    }
  });

  //console.log(resp);
  members.value = resp.data.records;//会员数据
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
  /*memberSearchModel.value = {
    id: null,
    name: null,
    sex: null,
    createRange: [],
    phone: null
  };*/

  //第2种解决方案：调用表单对象的函数
  memberSearchForm.value.resetFields();
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

//批量删除会员，参数为会员id数组
async function removeByIds(ids) {
  let resp = await api({
    url: "/members",
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
let memberDlgTitle = ref("新增会员");
//对话框是否显示
let memberDlgShow = ref(false);
//会员添加/新增表单
let memberModel = ref({
  phone: null,
  name: null,
  sex: "男",
  pinyin: null,
  createTime: null,
  email: null,
  wechat: null,
  description: null,
  avatar: null
});
//新增表单对象
let memberFormRef = ref();
let mode = "add";//标志位

//新增按钮
function add() {
  memberDlgTitle.value = "新增会员";
  memberDlgShow.value = true;
  mode = "add";
  const now = new Date();
  memberModel.value.createTime = formatDate(now);
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

//提交，保存会员信息
function submitAdd() {
  let params = toRaw(memberModel.value);

  //手动校验
  memberFormRef.value.validate(valid => {
    if (valid) {
      //以异步回调形式调用
      api({
        url: "/members",
        method: "post",
        data: params
      }).then(resp => {
        if (resp.success) {
          memberDlgShow.value = false;
          search();
          ElMessage.success("保存会员信息成功");
        } else {
          ElMessage.error(resp.msg || "保存会员信息失败");
        }
      }).catch(resp => {
        ElMessage.error(resp.msg || "保存会员信息失败");
      });
    } else {
      ElMessage.error("输入不正确");
    }
  });
}

//重置会员表单
function resetMemberForm() {
  memberFormRef.value.resetFields();
}

/////////////////////////////////////////////////////////////////
function edit() {
  let rows = tblRef.value.getSelectionRows();
  if (rows.length === 0) {
    ElMessage.warning("请选中您要修改的行");
  } else if (rows.length > 1) {
    ElMessage.warning("您一次只能修改一行");
  } else {
    memberDlgShow.value = true;
    memberDlgTitle.value = "修改会员";
    mode = "edit";

    //在下一个时间滴答中操作
    nextTick(() => {
      //深度克隆
      memberModel.value = cloneDeep(toRaw(rows[0]));
    });

    ////
  }
}

//提交修改
async function submitEdit() {
  let params = toRaw(memberModel.value);
  let resp = await api({
    url: "/members",
    method: "put",
    data: params
  });

  if (resp.success) {
    memberDlgShow.value = false;
    search();
    ElMessage.success("修改会员信息成功");
  }
}

/////////////////////////////////////////////////////////////////
function editRow(row) {
  //console.log(row);
  //console.log(row);
  memberDlgShow.value = true;
  memberDlgTitle.value = "修改会员";
  mode = "edit";

  //在下一个时间滴答中操作
  nextTick(() => {
    //深度克隆
    memberModel.value = cloneDeep(toRaw(row));
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
  phone: [{
    required: true,
    message: "手机号不可为空",
    trigger: "blur"
  }, {
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
  memberModel.value.avatar = resp.data;
}

//自定义上传方法
function customUpload(options) {
  const formData = new FormData();
  formData.append("file", options.file);

  api({
    url: "/members/avatar", // 假设上传接口是 /members/avatar
    method: "post",
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  }).then(resp => {
    if (resp.success) {
      options.onSuccess(resp);
    } else {
      options.onError({ message: resp.msg || "上传失败" });
    }
  }).catch(resp => {
    options.onError({ message: resp.msg || "上传失败" });
  });
}
</script>