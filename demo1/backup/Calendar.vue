<script setup lang="ts">
import { ref, reactive, onMounted, toRaw } from "vue";
import api from "../../util/api";
import { ElMessage, ElMessageBox } from "element-plus";

// 1. 首先定义所有响应式变量
const calendarDate = ref(new Date());
const selectedDate = ref(formatDate(new Date()));
const allReservations = ref([]);
const reserve = ref([]);
const reserveNames = ref([]); // 确保reserveNames在这里定义
const reserveCourse = ref([]); // 确保reserveCourse在这里定义

const reserveSearchModel = ref({
  name: null,
  course: null,
  reserveDate: null,
  reserveFrom: null,
  reserveTo: null
});

const memberPi = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
});

// 2. 然后定义所有函数
function updateNameAndCourseLists() {
  if (reserve.value.length > 0) {
    reserveNames.value = [...new Set(reserve.value.map(item => item.name).filter(Boolean))];
    reserveCourse.value = [...new Set(reserve.value.map(item => item.course).filter(Boolean))];
  }
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
function formatDateTime(dateStr, timeStr) {
  return `${dateStr}`; // 直接组合成 yyyy-MM-dd HH:mm:ss 格式
}

function hasReservation(dateStr) {
  return allReservations.value.some(item => {
    const reserveDate = item.reserveTime?.split(' ')[0];
    return reserveDate === dateStr;
  });
}

function handleDateClick(data) {
  selectedDate.value = data.day;
  filterReservationsByDate(data.day);
}

function filterReservationsByDate(date) {
  reserveSearchModel.value.reserveDate = date;
  search();
}

async function search(pageNo = 1, pageSize = 10) {
  try {
    const params = toRaw(reserveSearchModel.value);

    if (params.reserveDate) {
      // 修正时间格式为 yyyy-MM-dd HH:mm:ss
      params.reserveFrom = formatDateTime(params.reserveDate, '00:00:00');
      params.reserveTo = formatDateTime(params.reserveDate, '23:59:59');
      delete params.reserveDate;
    }

    const resp = await api({
      url: "/reserve",
      method: "get",
      params: {
        pageNo,
        pageSize,
        ...params
      }
    });

    reserve.value = resp.data.records;
    memberPi.pageNo = resp.data?.current || 1;
    memberPi.pageSize = resp.data?.size || 10;
    memberPi.total = resp.data?.total || 0;

    updateNameAndCourseLists();
  } catch (error) {
    console.error('获取预约数据失败:', error);
    ElMessage.error('获取预约数据失败');
  }
}

async function loadAllReservations() {
  try {
    const resp = await api({
      url: "/reserve",
      method: "get",
      params: {
        pageNo: 1,
        pageSize: 1000
      }
    });
    allReservations.value = resp.data?.records || [];
  } catch (error) {
    console.error('加载预约数据失败:', error);
    ElMessage.error('加载日历数据失败');
  }
}

onMounted(() => {
  search();
  loadAllReservations();
});
</script>

<template>
  <!-- 新增日历部分 -->
  <div class="calendar-container">
    <el-calendar v-model="calendarDate">
      <template #date-cell="{ data }">
        <div class="calendar-day"
             :class="{
               'has-reservation': hasReservation(data.day),
               'is-selected': data.isSelected
             }"
             @click="handleDateClick(data)">
          {{ data.day.split('-').slice(2).join('-') }}
          <span v-if="hasReservation(data.day)" class="reservation-dot"></span>
        </div>
      </template>
    </el-calendar>
  </div>

  <!-- 日期标题 -->
  <div class="date-title">
    <h3>{{ selectedDate }} 的预约信息</h3>
  </div>

  <div class="grid">
    <el-table :data="reserve" stripe border ref="tblRef" class="data-grid">
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
    </el-table>
  </div>
</template>

<style scoped>
.calendar-container {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 15px;
  background: white;
}

.calendar-day {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  cursor: pointer;
}

.has-reservation {
  color: #1989fa;
  font-weight: bold;
}

.reservation-dot {
  position: absolute;
  bottom: 5px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #1989fa;
}

.is-selected {
  background-color: #f0f7ff;
}

.date-title {
  margin: 15px 0;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>