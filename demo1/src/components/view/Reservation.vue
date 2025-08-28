<template>
  <!-- 查询区 -->
  <div class="search">
    <el-form inline :model="reserveSearchModel" label-width="90" label-position="right" ref="reserveSearchForm">

      <el-form-item label="预约：" prop="name">
        <el-select
            v-model="reserveSearchModel.name"
            placeholder="请选择预约姓名"
            clearable
            filterable
        >
          <el-option
              v-for="name in reserveNames"
              :key="name"
              :label="name"
              :value="name"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="课程：" prop="course">
        <el-select
            v-model="reserveSearchModel.course"
            placeholder="请选择课程"
            clearable
            filterable
        >
          <el-option
              v-for="course in reserveCourses"
              :key="course"
              :label="course"
              :value="course"
          />
        </el-select>
      </el-form-item>

      <el-button type="primary" style="margin-bottom: 17px" :icon="Search" @click="search()">查询</el-button>
      <el-button type="primary" style="margin-bottom: 17px" :icon="Refresh" @click="reset">重置</el-button>
    </el-form>
  </div>

  <!-- 按钮区 -->
  <div class="action">
    <el-button type="success" :icon="CirclePlus" @click="add">新增预约</el-button>
  </div>

  <!-- 表格区 -->
  <div class="grid">
    <el-table :data="reserve" stripe border @row-click="tblRowClick" ref="tblRef" class="data-grid">
      <el-table-column prop="id" label="ID" width="80" fixed/>

      <el-table-column prop="name" label="预约" width="150" fixed/>
      <el-table-column prop="course" label="课程" width="150" fixed/>
      <el-table-column prop="coach" label="教练" width="150"/>
      <el-table-column prop="createTime" label="创建时间" width="150"/>
      <el-table-column prop="length" label="时长" width="150"/>
      <el-table-column prop="reserveTime" label="预约时间" width="130"/>
      <el-table-column prop="score" label="评分" width="70"/>
      <el-table-column prop="recommend" label="评语" width="150"/>
      <el-table-column prop="status" label="状态" width="150">
        <template #default="scope">
          <span v-if="scope.row.status === 1">已预约</span>
          <span v-else-if="scope.row.status === 0">已取消预约</span>
          <span v-else>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click.stop="editRow(scope.row)">编辑</el-button>
          <el-button
              type="warning"
              size="small"
              @click.stop="toggleReservationStatus(scope.row)"
              v-if="scope.row.status === 1"
          >
            取消预约
          </el-button>
          <el-button
              type="success"
              size="small"
              @click.stop="toggleReservationStatus(scope.row)"
              v-else-if="scope.row.status === 0"
          >
            重新预约
          </el-button>
          <el-button type="primary" size="small" @click.stop="evaluateRow(scope.row)">评价</el-button>
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

  <!-- 添加对话框 -->
  <el-dialog v-model="reserveDlgShow" :title="reserveDlgTitle" width="600"
             draggable align-center :close-on-click-modal="false" @close="resetMemberForm">
    <el-form label-width="100" label-position="right"
             :model="reserveModel" :rules="rules" ref="reserveFormRef">
      <el-row>
        <el-col :span="16">
          <el-form-item label="预约：" prop="name">
            <el-select
                v-model="reserveModel.name"
                placeholder="请选择预约人"
                clearable
                filterable
            >
              <el-option
                  v-for="member in members"
                  :key="member.id"
                  :label="member.name"
                  :value="member.name"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="课程：" prop="course">
            <el-select
                v-model="reserveModel.course"
                placeholder="请选择课程"
                clearable
                filterable
            >
              <el-option
                  v-for="course in courses"
                  :key="course.id"
                  :label="course.name"
                  :value="course.name"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="时长：" prop="length">
            <el-input v-model="reserveModel.length" placeholder="请输入课程时长" :readonly="true" :disabled="true"/>
          </el-form-item>

          <el-form-item label="预约时间：" prop="reserveTime">
            <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" v-model="reserveModel.reserveTime"
                            placeholder="请输入预约时间" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="创建日期：" prop="createTime">
        <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" v-model="reserveModel.createTime"
                        placeholder="请输入预约创建日期" :readonly="true" :disabled="true"/>
      </el-form-item>

    </el-form>
    <template #footer>
      <div>
        <el-button type="primary" @click="doSubmit">确定</el-button>
        <el-button type="primary" @click="reserveDlgShow=false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <!--  评分对话框-->
  <el-dialog v-model="evaluateDlgShow" :title="evaluateDlgTitle" width="600"
             draggable align-center :close-on-click-modal="false" @close="resetEvaluateForm">
    <el-form label-width="100" label-position="right"
             :model="evaluateModel" :rules="rules" ref="evaluateFormRef">
      <el-row>
        <el-col :span="16">

          <el-form-item label="评分：" prop="score">
            <el-input v-model="evaluateModel.score" placeholder="请为课程打分"/>
          </el-form-item>
          <el-form-item label="评语：" prop="recommend">
            <el-input v-model="evaluateModel.recommend" placeholder="请输入评语"/>
          </el-form-item>

          <el-form-item label="评价时间：" prop="evaluateTime">
            <el-date-picker type="datetime" value-format="YYYY-MM-DD HH:mm:ss" v-model="evaluateModel.evaluateTime"
                            placeholder="评价时间" :readonly="true" :disabled="true"/>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
    <template #footer>
      <div>
        <el-button type="primary" @click="submitEvaluate">确定</el-button>
        <el-button type="primary" @click="evaluateDlgShow=false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

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
  loadMembers(); // 加载会员数据
  loadCourses(); // 加载课程数据
});

///////////////////////////////////
let reserve = ref();//创建一个响应式对象
//预约数据分页
let memberPi = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
});

//搜索条件模型，双向数据绑定
let reserveSearchModel = ref({
  name: null,
  course: null
});

//表单对象
let reserveSearchForm = ref();

//预约名称和课程（用于查询下拉框去重）
let reserveNames = ref([]);
let reserveCourses = ref([]);

//会员和课程数据（用于新增/编辑对话框）
let members = ref([]);
let courses = ref([]);

//查询全部预约
async function search(pageNo = 1, pageSize = 10) {
  //console.log(toRaw(reserveSearchModel.value))
  let params = toRaw(reserveSearchModel.value);//查询条件
  if (params.createRange) {
    params.createFrom = params.createRange[0];
    params.createTo = params.createRange[1];
    delete params.createRange;
  }

  //console.log(params);

  let resp = await api({
    url: "/reserve",
    method: "get",
    params: {
      pageNo,
      pageSize,
      ...params
    }
  });

  //console.log(resp);
  reserve.value = resp.data.records;//预约数据
  memberPi.pageNo = resp.data.current;
  memberPi.pageSize = resp.data.size;
  memberPi.total = resp.data.total;

  // 处理预约人姓名和课程的去重
  if (resp.data.records && resp.data.records.length > 0) {
    const names = [...new Set(resp.data.records.map(item => item.name))];
    reserveNames.value = names.filter(name => name); // 过滤掉空值
    const courses = [...new Set(resp.data.records.map(item => item.course))];
    reserveCourses.value = courses.filter(course => course);
  }
}

// 加载所有会员数据
async function loadMembers() {
  try {
    const resp = await api({
      url: "/members",
      method: "get",
      params: {
        pageNo: 1,
        pageSize: 1000 // 获取所有会员
      }
    });

    if (resp.success) {
      members.value = resp.data.records;
    }
  } catch (error) {
    console.error("加载会员数据失败:", error);
    ElMessage.error("加载会员数据失败");
  }
}

// 加载所有课程数据
async function loadCourses() {
  try {
    const resp = await api({
      url: "/course",
      method: "get",
      params: {
        pageNo: 1,
        pageSize: 1000 // 获取所有课程
      }
    });

    if (resp.success) {
      courses.value = resp.data.records;
    }
  } catch (error) {
    console.error("加载课程数据失败:", error);
    ElMessage.error("加载课程数据失败");
  }
}

//当页码或页面大小改变时触发
function memberPiChange(pageNo, pageSize) {
  //console.log(pageNo, pageSize);
  search(pageNo, pageSize);
}

//查询表单重置
function reset() {
  //第2种解决方案：调用表单对象的函数
  reserveSearchForm.value.resetFields();
}

//表格对象
let tblRef = ref();

//表格行单击事件
function tblRowClick(row) {
  tblRef.value.toggleRowSelection(row);
}

//批量删除预约，参数为预约id数组
async function removeByIds(ids) {
  let resp = await api({
    url: "/reserve",
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
let reserveDlgTitle = ref("新增预约");
let evaluateDlgTitle = ref("评价");
//对话框是否显示
let reserveDlgShow = ref(false);
let evaluateDlgShow = ref(false);
//预约添加/新增表单
let reserveModel = ref({
  name:null,
  course:null,
  length:90,
  reserveTime:null,
  createTime:null
});
//评价表单
let evaluateModel = ref({
  score:null,
  recommend:null,
  evaluateTime:null
});
//新增表单对象
let reserveFormRef = ref();
let evaluateFormRef = ref();
let mode = "add";//标志位

//新增按钮
function add() {
  reserveDlgTitle.value = "新增预约";
  reserveDlgShow.value = true;
  mode = "add";
  const now = new Date();
  reserveModel.value.createTime = formatDate(now);
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

//提交，保存预约信息
function submitAdd() {
  let params = toRaw(reserveModel.value);

  //手动校验
  reserveFormRef.value.validate(valid => {
    if (valid) {
      //以异步回调形式调用
      api({
        url: "/reserve",
        method: "post",
        data: params
      }).then(resp => {
        if (resp.success) {
          reserveDlgShow.value = false;
          search();
          ElMessage.success("保存预约信息成功");
        } else {
          ElMessage.error(resp.msg || "保存预约信息失败");
        }
      }).catch(resp => {
        ElMessage.error(resp.msg || "保存预约信息失败");
      });
    } else {
      ElMessage.error("输入不正确");
    }
  });
}

//重置预约表单
function resetMemberForm() {
  reserveFormRef.value.resetFields();
}
function resetEvaluateForm() {
  if (evaluateFormRef.value) {
    evaluateFormRef.value.resetFields();
  }
}

/////////////////////////////////////////////////////////////////
function edit() {
  let rows = tblRef.value.getSelectionRows();
  if (rows.length === 0) {
    ElMessage.warning("请选中您要修改的行");
  } else if (rows.length > 1) {
    ElMessage.warning("您一次只能修改一行");
  } else {
    reserveDlgShow.value = true;
    reserveDlgTitle.value = "修改预约";
    mode = "edit";

    //在下一个时间滴答中操作
    nextTick(() => {
      //深度克隆
      reserveModel.value = cloneDeep(toRaw(rows[0]));
    });

    ////
  }
}

//提交修改
async function submitEdit() {
  let params = toRaw(reserveModel.value);
  let resp = await api({
    url: "/reserve",
    method: "put",
    data: params
  });

  if (resp.success) {
    reserveDlgShow.value = false;
    search();
    ElMessage.success("修改预约信息成功");
  }
}

/////////////////////////////////////////////////////////////////
function editRow(row) {
  //console.log(row);
  //console.log(row);
  reserveDlgShow.value = true;
  reserveDlgTitle.value = "修改预约";
  mode = "edit";

  //在下一个时间滴答中操作
  nextTick(() => {
    //深度克隆
    reserveModel.value = cloneDeep(toRaw(row));
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

function evaluateRow(row) {
  evaluateDlgShow.value = true;
  evaluateDlgTitle.value = "修改评价";
  mode = "evaluate";


  //在下一个时间滴答中操作
  nextTick(() => {
    //深度克隆
    evaluateModel.value = cloneDeep(toRaw(row));
    const now = new Date();
    evaluateModel.value.evaluateTime = formatDate(now);
  });
}

// 提交评价
async function submitEvaluate() {
  let params = toRaw(evaluateModel.value);

  // 表单校验
  evaluateFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let resp = await api({
          url: "/reserve/evaluate", // 假设后端有评价接口
          method: "put",
          data: params
        });

        if (resp.success) {
          evaluateDlgShow.value = false;
          search(); // 刷新表格数据
          ElMessage.success("评价提交成功");
        } else {
          ElMessage.error(resp.msg || "评价提交失败");
        }
      } catch (error) {
        ElMessage.error("评价提交失败，请稍后再试");
      }
    } else {
      ElMessage.error("请填写完整的评价信息");
    }
  });
}

// 切换预约状态
async function toggleReservationStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1;
  const actionText = row.status === 1 ? '取消预约' : '重新预约';

  try {
    let resp = await api({
      url: "/reserve",
      method: "put",
      data: {
        ...row,
        status: newStatus
      }
    });

    if (resp.success) {
      search(); // 刷新表格数据
      ElMessage.success(`${actionText}成功`);
    } else {
      ElMessage.error(resp.msg || `${actionText}失败`);
    }
  } catch (error) {
    ElMessage.error(`${actionText}失败，请稍后再试`);
  }
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
  }],
  score: [{
    required: true,
    message: "请为课程打分",
    trigger: "blur"
  }],
  recommend: [{
    required: true,
    message: "请输入评语",
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
  reserveModel.value.avatar = resp.data;
}
</script>


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
