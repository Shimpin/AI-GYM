<template>
  <!-- 查询区 -->
  <div class="search">
    <el-form inline :model="courseSearchModel" label-width="90" label-position="right" ref="courseSearchForm">
      <el-form-item label="ID：" prop="id">
        <el-input v-model="courseSearchModel.id" placeholder="请输入课程ID"/>
      </el-form-item>

      <el-form-item label="名称：" prop="name">
        <el-input v-model="courseSearchModel.name" placeholder="请输入课程名称"/>
      </el-form-item>

      <el-form-item label="开始日期：" prop="beginRange">
        <el-date-picker
            type="daterange"
            range-separator="~"
            start-placeholder="日期从"
            end-placeholder="日期止"
            v-model="courseSearchModel.beginRange"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </el-form>
  </div>

  <!-- 按钮区 -->
  <div class="action">
    <el-button type="primary" :icon="CirclePlus" @click="add">新增</el-button>
<!--    <el-button type="primary" :icon="Edit" @click="edit">修改</el-button>-->
    <el-button type="primary" :icon="Search" @click="search()">查询</el-button>
    <el-button type="primary" :icon="Refresh" @click="reset">重置</el-button>
    <el-button type="danger" :icon="Delete" @click="remove">删除</el-button>
  </div>

  <!-- 表格区 -->
  <div class="grid">
    <el-table :data="course" stripe border @row-click="tblRowClick" ref="tblRef" class="data-grid">
      <el-table-column type="selection" width="55" align="center" fixed/>
      <el-table-column prop="id" label="ID" width="80" fixed/>

      <el-table-column label="照片" width="80" fixed>
        <template #default="scope">
          <div class="row-avatar" :style="'background-image: url('+(baseUrl+scope.row.avatar)+')'"></div>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="名称" width="150" fixed/>
      <el-table-column prop="begin" label="开始日期" fixed/>
      <el-table-column prop="length" label="时长" width="100"/>
      <el-table-column prop="maxCount" label="最大人数" width="120"/>
      <el-table-column prop="room" label="位置" width="120"/>
      <el-table-column prop="coachName" label="教练" width="120"/>
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
        v-model:current-page="coursePi.pageNo"
        v-model:page-size="coursePi.pageSize"
        :page-sizes="[10, 20, 30, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="coursePi.total"
        class="member-pi"
        background
        @change="coursePiChange"
    />
  </div>

  <!-- 添加/修改对话框 -->
  <el-dialog v-model="courseDlgShow" :title="courseDlgTitle" width="600"
             draggable align-center :close-on-click-modal="false" @close="resetMemberForm">
    <el-form label-width="100" label-position="right"
             :model="courseModel" :rules="rules" ref="courseFormRef">
      <el-row>
        <el-col :span="16">

          <el-form-item label="名称：" prop="name">
            <el-input v-model="courseModel.name" placeholder="请输入课程名称"/>
          </el-form-item>

          <el-form-item label="时长：" prop="length">
            <el-input v-model="courseModel.length" placeholder="请输入课程时长"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-upload class="avatar" 
                     :http-request="customUpload"
                     :on-success="avatarUploadSuccess" 
                     :show-file-list="false">
            <div class="img" v-if="courseModel.avatar"
                 :style="'background-image: url('+(baseUrl+courseModel.avatar)+')'"></div>
            <el-icon v-else class="icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </el-col>
      </el-row>

      <el-form-item label="开始日期：" prop="begin">
        <el-date-picker type="date" value-format="YYYY-MM-DD HH:mm:ss" v-model="courseModel.begin"
                        placeholder="请输入课程开始日期"/>
      </el-form-item>

      <el-form-item label="最大人数：" prop="maxCount">
        <el-input v-model="courseModel.maxCount" placeholder="请输入课程最大人数"/>
      </el-form-item>

      <el-form-item label="上课位置：" prop="room">
        <el-input v-model="courseModel.room" placeholder="请输入课程上课位置"/>
      </el-form-item>

      <el-form-item label="教练ID：" prop="coachId">
        <el-input v-model="courseModel.coachId" placeholder="请输入教练ID"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div>
        <el-button type="primary" @click="doSubmit">确定</el-button>
        <el-button type="primary" @click="courseDlgShow=false">取消</el-button>
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
let course = ref();//创建一个响应式对象
//课程数据分页
let coursePi = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
});
//搜索条件模型，双向数据绑定
let courseSearchModel = ref({
  id: null,
  name: null,
  beginRange: [],
});
//表单对象
let courseSearchForm = ref();

//查询全部课程
async function search(pageNo = 1, pageSize = 10) {
  //console.log(toRaw(courseSearchModel.value))
  let params = toRaw(courseSearchModel.value);//查询条件
  if (params.beginRange) {
    params.beginTime = params.beginRange[0];
    params.endTime = params.beginRange[1];
    delete params.beginRange;
  }

  //console.log(params);

  let resp = await api({
    url: "/course",
    method: "get",
    params: {
      pageNo,
      pageSize,
      ...params
    }
  });

  //console.log(resp);
  course.value = resp.data.records;//课程数据
  coursePi.pageNo = resp.data.current;
  coursePi.pageSize = resp.data.size;
  coursePi.total = resp.data.total;
}

//当页码或页面大小改变时触发
function coursePiChange(pageNo, pageSize) {
  //console.log(pageNo, pageSize);
  search(pageNo, pageSize);
}

//查询表单重置
function reset() {
  //第1种解决方案
  /*courseSearchModel.value = {
    id: null,
    name: null,
    sex: null,
    beginRange: [],
    phone: null
  };*/

  //第2种解决方案：调用表单对象的函数
  courseSearchForm.value.resetFields();
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

//批量删除课程，参数为课程id数组
async function removeByIds(ids) {
  let resp = await api({
    url: "/course",
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
let courseDlgTitle = ref("新增课程");
//对话框是否显示
let courseDlgShow = ref(false);
//课程添加/新增表单
let courseModel = ref({
  name: null,
  begin: null,
  length: null,
  maxCount: null,
  room: null,
  coachId: null,
  avatar: null
});
//新增表单对象
let courseFormRef = ref();
let mode = "add";//标志位

//新增按钮
function add() {
  courseDlgTitle.value = "新增课程";
  courseDlgShow.value = true;
  mode = "add";
}

//点击确定按钮的操作
function doSubmit() {
  if (mode === "add") {
    submitAdd();
  } else {
    submitEdit();
  }
}

//提交，保存课程信息
function submitAdd() {
  let params = toRaw(courseModel.value);

  //手动校验
  courseFormRef.value.validate(valid => {
    if (valid) {
      //以异步回调形式调用
      api({
        url: "/course",
        method: "post",
        data: params
      }).then(resp => {
        if (resp.success) {
          courseDlgShow.value = false;
          search();
          ElMessage.success("保存课程信息成功");
        } else {
          ElMessage.error(resp.msg || "保存课程信息失败");
        }
      }).catch(resp => {
        ElMessage.error(resp.msg || "保存课程信息失败");
      });
    } else {
      ElMessage.error("输入不正确");
    }
  });
}

//重置课程表单
function resetMemberForm() {
  courseFormRef.value.resetFields();
}

/////////////////////////////////////////////////////////////////
function edit() {
  let rows = tblRef.value.getSelectionRows();
  if (rows.length === 0) {
    ElMessage.warning("请选中您要修改的行");
  } else if (rows.length > 1) {
    ElMessage.warning("您一次只能修改一行");
  } else {
    courseDlgShow.value = true;
    courseDlgTitle.value = "修改课程";
    mode = "edit";

    //在下一个时间滴答中操作
    nextTick(() => {
      //深度克隆
      courseModel.value = cloneDeep(toRaw(rows[0]));
    });

    ////
  }
}

//提交修改
async function submitEdit() {
  let params = toRaw(courseModel.value);
  let resp = await api({
    url: "/course",
    method: "put",
    data: params
  });

  if (resp.success) {
    courseDlgShow.value = false;
    search();
    ElMessage.success("修改课程信息成功");
  }
}

/////////////////////////////////////////////////////////////////
function editRow(row) {
  //console.log(row);
  //console.log(row);
  courseDlgShow.value = true;
  courseDlgTitle.value = "修改课程";
  mode = "edit";

  //在下一个时间滴答中操作
  nextTick(() => {
    //深度克隆
    courseModel.value = cloneDeep(toRaw(row));
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
  name: [{
    required: true,
    message: "名称不可为空",
    trigger: "blur"
  }],
  begin: [{
    required: true,
    message: "开始时间不可为空",
    trigger: "blur"
  }],
  length: [{
    required: true,
    message: "课程时长不可为空",
    trigger: "blur"
  }],
  maxCount: [{
    required: true,
    message: "最大人数不可为空",
    trigger: "blur"
  }],
  room: [{
    required: true,
    message: "教室不可为空",
    trigger: "blur"
  }],
  coachId: [{
    required: true,
    message: "教练不可为空",
    trigger: "blur"
  }],
};

/////////////////////////////////////////////
import baseUrl from "@/util/constant.js";

//头像上传成功
function avatarUploadSuccess(resp) {
  //console.log(url)
  courseModel.value.avatar = resp.data;
}

//自定义上传
function customUpload(options) {
  const formData = new FormData();
  formData.append('file', options.file);

  api({
    url: '/course/avatar',
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