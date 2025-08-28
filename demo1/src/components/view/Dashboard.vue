<script setup lang="ts">
  // 统计组件
  import {ref, onMounted, computed} from 'vue'
  import { useTransition } from '@vueuse/core'
  import {
    Calendar,
    ChatLineRound,
    Failed,
    Male,
    PhoneFilled,
    Star,
    StarFilled,
    User,
    UserFilled
  } from '@element-plus/icons-vue'
  import board1 from '@/assets/board1.png'
  import board2 from '@/assets/board2.png'
  import board3 from '@/assets/board3.png'
  import board4 from '@/assets/board4.png'
  import api from "@/util/api.js";

  onMounted(()=>{
    fetchStatistics();
    fetchReservationList();
  })

  // 统计数据
  const memberCount = ref(0);
  const courseCount = ref(0);
  const reservationCount = ref(0);
  const coachCount = ref(0);


  // 获取统计数据
  async function fetchStatistics() {
    try {
      // 获取会员总数
      const memberResp = await api({
        url: "/members",
        method: "get",
        params: {
          pageNo: 1,
          pageSize: 1
        }
      });
      memberCount.value = memberResp.data.total;

      // 获取课程总数
      const courseResp = await api({
        url: "/course",
        method: "get",
        params: {
          pageNo: 1,
          pageSize: 1
        }
      });
      source.value = courseResp.data.total;

      // 获取预约总数
      const reservationResp = await api({
        url: "/reserve",
        method: "get",
        params: {
          pageNo: 1,
          pageSize: 1
        }
      });
      reservationCount.value = reservationResp.data.total;

      // 获取评价总数（评分不为null的数量）
      const coachResp = await api({
        url: "/coach",
        method: "get",
        params: {
          pageNo: 1,
          pageSize: 1, // 获取所有数据来统计
        }
      });
      coachCount.value = coachResp.data.total;

    } catch (error) {
      console.error("获取统计数据失败:", error);
    }
  }

  const source = ref(0)
  const courseChange = useTransition(source, {
    duration: 1500,
  })
  source.value = courseCount.value;

  //最近预约
  const reservationList = ref([]);
  async function fetchReservationList() {
    try {
      const resp = await api({
        url: "/reserve",
        method: "get",
        params: {
          pageNo: 1,
          pageSize: 8
        }
      });
      reservationList.value = resp.data.records;
    } catch (error) {
      console.error("获取最近预约数据失败:", error);
    }
  }

// 表格
  const tableData = computed(() => {
    if (reservationList.value && reservationList.value.length > 0) {
      return reservationList.value.map(item => ({
        name: item.name,
        course: item.course,
        reserveTime: item.reserveTime, // 根据实际字段调整
        coach: item.coach,
        score: item.score,
        recommend: item.recommend
      }));
    } else {
      // 默认数据或空数据
      return [];
    }
  });
  //走马灯
  const imgList = [
    {
      url: board1,
      title: '第一张图片'
    },
    {
      url: board2,
      title: '第二张图片'
    },
    {
      url: board3,
      title: '第三张图片'
    },
    {
      url: board4,
      title: '第四张图片'
    }
  ]
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header >
        <div class="block text-center">
          <span class="demonstration">健身房器材展示</span>
          <el-carousel height="220px" style="width: 1350px; margin: 0 auto;" motion-blur>
            <el-carousel-item v-for="(item, index) in imgList" :key="index">
              <img :src="item.url" :alt="'图片' + (index + 1)" class="carousel-img">
              <div class="carousel-title">{{ item.title }}</div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </el-header>
      <el-main style="height: 150px" >
<!--        //组件二-->
        <el-row>
          <el-col :span="5">
            <el-statistic title="会员数量"  :value="memberCount">
            <template #suffix>
              <el-icon style="vertical-align: -0.125em">
                <UserFilled />
              </el-icon>
            </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic :value="courseChange">
              <template #title>
                <div style="display: inline-flex; align-items: center">
                  课程数量
                </div>
              </template>
              <template #suffix>/100
                <el-icon><Failed /></el-icon>
              </template>

            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="教练数量" :value="coachCount">
              <template #suffix>
                <el-icon><StarFilled /></el-icon>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="预约数量" :value="reservationCount">
              <template #suffix>
                <el-icon><PhoneFilled /></el-icon>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
      </el-main>
      <el-footer >
<!--        表格-->
        <div style="color: #2c5781;font-size: 20px">最近预约<el-icon><Calendar /></el-icon></div>
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="course" label="课程" width="200" />
          <el-table-column prop="reserveTime" label="预约时间" />
          <el-table-column prop="coach" label="教练" width="180" />
          <el-table-column prop="score" label="评分" />
          <el-table-column prop="recommend" label="评语" width="180" />
        </el-table>
      </el-footer>
    </el-container>
  </div>
</template>

<style scoped>
.el-col {
  text-align: center;
}
header, main, footer {
  padding: 20px;
  height: 300px;
}
/* 走马灯 */
.demonstration {
  color: var(--el-text-color-secondary);
  text-align: center;
  display: block;
  font-size: 25px;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.demo-progress .el-progress--line {
  margin-bottom: 15px;
  max-width: 600px;
  margin-top: 20px;
}
</style>