<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import HelloWorld from './components/HelloWorld.vue'
import TheWelcome from './components/TheWelcome.vue'

const active = ref(0);
const router = useRouter();
const currentTime = ref('');

// 更新当前时间
const updateTime = () => {
  const now = new Date();
  const hours = now.getHours().toString().padStart(2, '0');
  const minutes = now.getMinutes().toString().padStart(2, '0');
  currentTime.value = `${hours}:${minutes}`;
};

// 检查用户登录状态
const checkLogin = () => {
  // 检查localStorage中是否有userId，如果没有，设置一个默认值便于测试
  const userId = localStorage.getItem('userId');
  if (!userId) {
    // 设置一个默认用户ID，在生产环境中应该是通过登录获取的
    localStorage.setItem('userId', '1');
  }
};

const onChange = (index) => {
  active.value = index;
  switch (index) {
    case 0:
      router.push('/home');
      break;
    case 1:
      router.push('/category');
      break;
    case 2:
      router.push('/topic');
      break;
    case 3:
      router.push('/profile');
      break;
  }
};

onMounted(() => {
  checkLogin();
  updateTime();
  // 每分钟更新一次时间
  setInterval(updateTime, 60000);
});
</script>

<template>
  <div class="app-container">
    <!-- 状态栏 -->
    <div class="status-bar">
      <div class="status-left">
        <span class="status-time">{{ currentTime }}</span>
      </div>
      <div class="status-center">
        <div class="status-notch"></div>
      </div>
      <div class="status-right">
        <i class="ri-wifi-line"></i>
        <i class="ri-signal-wifi-3-line"></i>
        <i class="ri-battery-line"></i>
      </div>
    </div>
    
    <!-- 路由视图 -->
    <router-view v-slot="{ Component, route }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
    
    <!-- 底部导航栏 -->
    <van-tabbar v-if="!$route.meta.hideNavbar" v-model="active" @change="onChange" class="app-tabbar" route>
      <van-tabbar-item to="/home">
        <template #icon="props">
          <i class="ri-home-5-line" :class="{ 'active-icon': props.active }"></i>
        </template>
        首页
      </van-tabbar-item>
      <van-tabbar-item to="/category">
        <template #icon="props">
          <i class="ri-apps-line" :class="{ 'active-icon': props.active }"></i>
        </template>
        分类
      </van-tabbar-item>
      <van-tabbar-item to="/topic">
        <template #icon="props">
          <i class="ri-discuss-line" :class="{ 'active-icon': props.active }"></i>
        </template>
        话题
      </van-tabbar-item>
      <van-tabbar-item to="/profile">
        <template #icon="props">
          <i class="ri-user-3-line" :class="{ 'active-icon': props.active }"></i>
        </template>
        我的
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.app-container {
  padding-bottom: 60px;
  background-color: var(--color-background);
  min-height: 100vh;
  position: relative;
  font-family: 'Nunito', 'Helvetica', sans-serif;
  color: var(--color-text-primary);
}

/* 状态栏样式 */
.status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-xs) var(--spacing-md);
  font-size: var(--font-size-xs);
  color: var(--color-text-primary);
  background-color: var(--color-background);
  position: relative;
  z-index: 10;
}

.status-left {
  font-weight: var(--font-weight-semibold);
}

.status-center {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  height: 100%;
  display: flex;
  align-items: center;
}

.status-notch {
  width: 120px;
  height: 18px;
  background-color: var(--color-background);
  border-radius: 0 0 12px 12px;
}

.status-right {
  display: flex;
  gap: 6px;
}

.status-right i {
  font-size: 14px;
}

/* 美化底部导航栏 */
.app-tabbar {
  box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.05);
  height: 60px;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  overflow: hidden;
  border-top: none;
  background-color: white;
}

:deep(.van-tabbar-item--active) {
  color: #000000;
  font-weight: var(--font-weight-semibold);
}

/* RemixIcon样式 */
.ri-home-5-line,
.ri-apps-line,
.ri-discuss-line,
.ri-user-3-line {
  font-size: 24px;
  margin-bottom: 2px;
  position: relative;
}

.active-icon {
  color: #000000;
}

/* 移除下划线效果 */
/* .active-icon::after {
  content: '';
  position: absolute;
  bottom: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 16px;
  height: 2px;
  background-color: #000000;
  border-radius: 1px;
} */

:deep(.van-tabbar-item__text) {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
}

/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
