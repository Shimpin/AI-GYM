<script setup lang="ts">
import { ref, reactive, onMounted, toRaw } from "vue";
import api from "../../util/api";
import { ElMessage, ElMessageBox } from "element-plus";
import {Checked} from "@element-plus/icons-vue";

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
      url: "/reserve/status",
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
      url: "/reserve/status",
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
    <div class="section-header">
      <h2>预约日历</h2>
      <p>点击查看每日预约详情</p>
    </div>
    <el-calendar v-model="calendarDate">
      <template #date-cell="{ data }">
        <div class="calendar-day"
             :class="{
               'has-reservation': hasReservation(data.day),
               'is-selected': data.isSelected,
               'is-today': data.isToday
             }"
             @click="handleDateClick(data)">
          <div class="calendar-day-content">
            <span class="calendar-day-number">{{ data.day.split('-').slice(2).join('-') }}</span>
            <span v-if="hasReservation(data.day)" class="reservation-indicator">
              <el-icon style="color: #409eff;margin-left: -7px"><Checked /></el-icon>
            </span>
          </div>
        </div>
      </template>
    </el-calendar>
  </div>

  <!-- 日期标题 -->
  <div class="date-title">
    <h3>
      <i class="el-icon-date"></i>
      {{ selectedDate }} 的预约信息
    </h3>
    <el-tag type="primary" effect="dark">{{ reserve.length }} 条预约</el-tag>
  </div>

  <div class="reservations-container">
    <el-table :data="reserve" stripe border class="data-grid">
      <el-table-column prop="id" label="ID" width="80" fixed/>
      <el-table-column prop="name" label="预约人" width="120" fixed/>
      <el-table-column prop="course" label="课程" width="150" fixed/>
      <el-table-column prop="coach" label="教练" width="120"/>
      <el-table-column prop="createTime" label="创建时间" width="160"/>
      <el-table-column prop="length" label="时长(分钟)" width="110"/>
      <el-table-column prop="reserveTime" label="预约时间" width="160"/>
      <el-table-column prop="score" label="评分" width="180">
        <template #default="scope">
          <el-rate
            v-model="scope.row.score"
            disabled
            show-score
            score-template="{value}">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column prop="recommend" label="评语" width="150" :show-overflow-tooltip="true"/>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag
            :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '已预约' : '已取消' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.calendar-container {
  margin-bottom: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 20px;
  background: white;
  transition: all 0.3s ease;
}

.section-header {
  margin-bottom: 20px;
  text-align: center;
}

.section-header h2 {
  color: #303133;
  font-size: 24px;
  margin-bottom: 8px;
  font-weight: 600;
}

.section-header p {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.calendar-day {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.calendar-day:hover {
  background-color: #f0f7ff;
  transform: translateY(-2px);
}

.calendar-day-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.calendar-day-number {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.calendar-day.has-reservation .calendar-day-number {
  color: #409eff;
  font-weight: 600;
}

.calendar-day.is-today .calendar-day-number {
  color: #e6a23c;
  font-weight: 700;
}

.calendar-day.is-selected {
  background-color: #ecf5ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.calendar-day.is-selected .calendar-day-number {
  color: #409eff;
  font-weight: 700;
}

.reservation-indicator {
  position: absolute;
  top: 25px;
  right: 50%;

  transform: translateX(50%);
  width: 0;
  height: 0;
  border-radius: 50%;
  background-color: #409eff;
  box-shadow: 0 0 4px rgba(64, 158, 255, 0.5);
}
.date-title {
  margin: 20px 0;
  padding: 16px 24px;
  background: linear-gradient(120deg, #e0f2ff, #f0f9ff);
  border-radius: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.date-title h3 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.reservations-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 20px;
  margin-bottom: 24px;
}

.data-grid {
  width: 100%;
}

:deep(.el-calendar__header) {
  padding: 12px 0;
}

:deep(.el-calendar__body) {
  padding: 10px 0;
}

:deep(.el-calendar-table .el-calendar-day) {
  height: 80px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table .el-table__cell) {
  padding: 8px 0;
}

@media (max-width: 768px) {
  .calendar-container {
    padding: 12px;
  }

  .date-title {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }

  :deep(.el-calendar-table .el-calendar-day) {
    height: 60px;
  }

  .calendar-day-number {
    font-size: 14px;
  }
}
</style>
