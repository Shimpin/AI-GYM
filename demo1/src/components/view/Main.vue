<template>
  <div class="common-layout h100" :class="{ 'dark-theme': !isLightTheme }">
    <el-container class="h100">
      <el-header>
        <div class="logo"></div>
        <h3>刀羊健身房管理系统</h3>

        <!--        // 切换按钮-->
        <el-switch
            v-model="isLightTheme"
            active-text="光亮"
            inactive-text="黑暗"
            style="position: absolute;top: 20px;right: 160px "
            @change="toggleTheme"
        />

        <el-dropdown style="position: absolute;right: 30px;top: 10px">
        <span class="el-dropdown-link">

        <el-avatar
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        />

          <el-icon class="el-icon--right">
          <arrow-down />
          </el-icon>
        </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="checkInformation">个人信息</el-dropdown-item>
              <el-dropdown-item divided @click="logout">
                <a href="#" style="text-decoration: none;color:#2c5781" @click="logout">注销<el-icon><SwitchButton /></el-icon></a>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <div>

        </div>
      </el-header>
      <el-container>
        <el-aside>
          <!-- 导航菜单 -->
          <el-menu class="nav" router text-color="#fff" active-text-color="#ffd04b"
                   background-color="#545c64" default-active="/dashboard">
            <template v-for="mi in menuItems">
              <el-sub-menu v-if="Array.isArray(mi.children)" :index="mi.name">
                <template #title>
                  <span>{{ mi.name }}</span>
                </template>
                <el-menu-item v-for="smi in mi.children" :index="smi.url">
                  <span>{{ smi.name }}</span>
                </el-menu-item>
              </el-sub-menu>
              <el-menu-item v-else :index="mi.url">
                <span>{{ mi.name }}</span>
              </el-menu-item>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.h100 {
  height: 100%;
}

header {
  height: 75px;
  background-color: aliceblue;
  display: flex;
}

header > .logo {
  height: 75px;
  width: 150px;
  background: url("@/assets/logo.png") no-repeat center center/cover;
}

aside {
  width: 200px;
  background-color: #545c64;
}

.nav {
  border-right: none;
}
/* 修改注销按钮样式 */
.logout-btn {
  position: absolute; /* 绝对定位 */
  top: 20px;          /* 距离顶部距离 */
  right: 20px;        /* 距离右侧距离 */
  color: #333;
  text-decoration: none;
  padding: 5px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f5f5f5;
}

/* 鼠标悬停效果 */
.logout-btn:hover {
  background-color: #e0e0e0;
}
/* 暗黑主题样式 */
.dark-theme header {
  background-color: #2d2d2d !important;
  color: #f0f0f0 !important;
}

.dark-theme h3 {
  color: #f0f0f0 !important;
}

.dark-theme .el-aside {
  background-color: #1f1f1f !important;
}

.dark-theme .el-menu {
  background-color: #1f1f1f !important;
}

.dark-theme .el-menu-item {
  background-color: #1f1f1f !important;
  color: #b0b0b0 !important;
}

.dark-theme .el-menu-item:hover {
  background-color: #333333 !important;
  color: #ffffff !important;
}

.dark-theme .el-sub-menu__title {
  background-color: #1f1f1f !important;
  color: #b0b0b0 !important;
}

.dark-theme .el-sub-menu__title:hover {
  background-color: #333333 !important;
  color: #ffffff !important;
}

/* 主题切换过渡效果 */
.common-layout {
  transition: all 0.3s ease;
}

.common-layout .el-header,
.common-layout .el-aside,
.common-layout .el-main,
.common-layout .el-menu,
.common-layout .el-menu-item,
.common-layout .el-sub-menu__title {
  transition: all 0.3s ease;
}
</style>
<style>/* 亮色主题变量 */
.common-layout {
  --bg-color: #ffffff;
  --text-color: #303133;
  --border-color: #ebeef5;
  --header-bg: aliceblue;
  --card-bg: #ffffff;
  --hover-bg: #f5f7fa;
}

/* 暗色主题变量 */
.common-layout.dark-theme {
  --bg-color: #1f1f1f;
  --text-color: #f0f0f0;
  --border-color: #4c4d4f;
  --header-bg: #2d2d2d;
  --card-bg: #2d2d2d;
  --hover-bg: #333333;
}

/* 应用到子组件的样式 */
.common-layout .el-main {
  background-color: var(--bg-color);
  color: var(--text-color);
  transition: all 0.3s ease;
}

/* 为子页面提供通用的暗黑主题样式 */
.common-layout.dark-theme .el-card {
  background-color: var(--card-bg);
  color: var(--text-color);
  border-color: var(--border-color);
}

.common-layout.dark-theme .el-table {
  background-color: var(--card-bg);
  color: var(--text-color);
}

.common-layout.dark-theme .el-table__header th {
  background-color: #333333;
  color: var(--text-color);
}

.common-layout.dark-theme .el-table__body td {
  background-color: var(--card-bg);
  color: var(--text-color);
}

.common-layout.dark-theme .el-table__body tr:hover td {
  background-color: var(--hover-bg);
}

.common-layout.dark-theme .el-input__inner {
  background-color: #333333;
  border-color: var(--border-color);
  color: var(--text-color);
}

.common-layout.dark-theme .el-input__inner::placeholder {
  color: #aaa;
}

.common-layout.dark-theme .el-button {
  border-color: var(--border-color);
}

.common-layout.dark-theme .el-pagination {
  color: var(--text-color);
}

.common-layout.dark-theme .el-pagination .btn-next,
.common-layout.dark-theme .el-pagination .btn-prev {
  background-color: var(--card-bg);
  color: var(--text-color);
}

.common-layout.dark-theme .el-pagination .el-pager li {
  background-color: var(--card-bg);
  color: var(--text-color);
}

.common-layout.dark-theme .el-pagination .el-pager li.active {
  background-color: #409eff;
}
.common-layout.dark-theme .el-table--striped .el-table__body tr.el-table__row--striped td {
  background-color: var(--card-bg);
}
.common-layout.dark-theme .el-table__body td {
  background-color: var(--card-bg);
  color: var(--text-color);
}
/* 悬停效果 */
.common-layout.dark-theme .el-table__body tr:hover > td {
  background-color: var(--hover-bg);
}

/* 选择行的样式 */
.common-layout.dark-theme .el-table__body tr.current-row > td {
  background-color: var(--hover-bg);
}
</style>

<script setup>
import {onMounted, reactive, ref} from "vue";
import {removeJwt} from "@/api/jwt.js";
import router from "@/router/index.js";
import {ArrowDown, SwitchButton} from "@element-plus/icons-vue";
//所有导航菜单
const menuItems = reactive([{
  name: "数据统计",
  url: "/main/dashboard",
},{
  name: "预约管理",
  children: [{
    name: "预约列表",
    url: "/main/reservation"
  },{
    name: "日历展示",
    url: "/main/calendar"
  }]
}, {
  name: "课程管理",
  children: [{
    name: "课程列表",
    url: "/main/course"
  }]
}, {
  name: "会员管理",
  children: [
    {
      name: "会员列表",
      url: "/main/member"
    }
  ]
}, {
  name: "教练管理",
  children: [
    {
      name: "教练列表",
      url: "/main/coach"
    }
  ]
},{
  name: "管理员管理",
  children: [
    {
      name: "管理员列表",
      url: "/main/admin"
    }
  ]
},{
  name:"AI在线客服",
  url:"/main/ai-chat"
}
]);
// 主题状态 - true表示亮色主题，false表示暗色主题
const isLightTheme = ref(true);

// 切换主题
function toggleTheme() {
  // 保存用户偏好到localStorage
  localStorage.setItem('theme', isLightTheme.value ? 'light' : 'dark');
}

// 页面加载时恢复用户主题偏好
onMounted(() => {
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme) {
    isLightTheme.value = savedTheme === 'light';
  }
});

function logout() {
  removeJwt();
  router.push("/login");
}

function checkInformation() {
  router.push("/information");
}
</script>