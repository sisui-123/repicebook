<template>
  <div class="login-container">
    <!-- 动态背景装饰 -->
    <div class="floating-shapes">
      <div class="shape shape1"></div>
      <div class="shape shape2"></div>
      <div class="shape shape3"></div>
    </div>

    <div class="login-content">
      <!-- Logo和标题区域 -->
      <div class="header-section">
        <div class="logo-wrapper">
          <i class="ri-restaurant-2-line logo-icon"></i>
        </div>
        <h1 class="app-title">美食分享</h1>
        <p class="app-subtitle">发现美味，分享生活</p>
      </div>

      <!-- 登录表单 -->
      <van-form @submit="onSubmit" class="login-form-wrapper">
        <div class="form-card">
          <h2 class="form-title">欢迎回来</h2>
          
          <van-field
            v-model="formData.username"
            label-width="0"
            placeholder="请输入用户名"
            class="input-field"
            :border="false"
          >
            <template #left-icon>
              <i class="ri-user-line input-icon"></i>
            </template>
          </van-field>
          
          <van-field
            v-model="formData.password"
            type="password"
            label-width="0"
            placeholder="请输入密码"
            class="input-field"
            :border="false"
          >
            <template #left-icon>
              <i class="ri-lock-password-line input-icon"></i>
            </template>
          </van-field>
          
          <van-button 
            round 
            block 
            type="primary" 
            native-type="submit" 
            :loading="loading"
            class="login-btn"
          >
            <span v-if="!loading">登 录</span>
            <van-loading v-else type="spinner" size="20px" />
          </van-button>
          
          <div class="form-footer">
            <span class="footer-text">还没有账号？</span>
            <span class="register-link" @click="handleRegister">立即注册</span>
          </div>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { userApi } from '../services/api';
import { showSuccessToast, showFailToast, Loading as VanLoading } from 'vant';

const router = useRouter();
const loading = ref(false);

const formData = ref({
  username: '',
  password: ''
});

// 提交登录表单
const onSubmit = async () => {
  if (!formData.value.username.trim()) {
    showFailToast('请输入用户名');
    return;
  }
  
  if (!formData.value.password.trim()) {
    showFailToast('请输入密码');
    return;
  }
  
  try {
    loading.value = true;
    
    const result = await userApi.login(formData.value);
    
    if (result.data) {
      localStorage.setItem('userId', result.data.userId || result.data.id);
      localStorage.setItem('username', result.data.username);
      localStorage.setItem('userInfo', JSON.stringify(result.data));
      
      showSuccessToast('登录成功');
      
      setTimeout(() => {
        router.replace('/home');
      }, 300);
    } else {
      showFailToast(result.message || '登录失败，请检查用户名和密码');
    }
  } catch (error) {
    console.error('登录失败:', error);
    showFailToast('登录失败，请检查用户名和密码');
  } finally {
    loading.value = false;
  }
};

const handleRegister = () => {
  showFailToast('注册功能暂未开放');
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.login-container {
  min-height: 100vh;
  /* 浅色渐变背景 */
  background: linear-gradient(135deg, #F8F9FA 0%, #E8ECF1 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  overflow: hidden;
  font-family: 'Inter', sans-serif;
}

/* 动态背景装饰 - 调整为更浅的颜色 */
.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.shape {
  position: absolute;
  /* 更浅的形状颜色 */
  background: rgba(0, 100, 255, 0.05); /* 非常淡的蓝色 */
  border-radius: 50%;
  animation: float 20s infinite ease-in-out;
}

.shape1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 15%;
  animation-duration: 25s;
  background: rgba(0, 100, 255, 0.05);
}

.shape2 {
  width: 150px;
  height: 150px;
  top: 60%;
  left: 70%;
  animation-duration: 30s;
  animation-delay: -5s;
  background: rgba(0, 150, 255, 0.06);
}

.shape3 {
  width: 100px;
  height: 100px;
  top: 30%;
  left: 40%;
  animation-duration: 20s;
  animation-delay: -10s;
  background: rgba(0, 120, 255, 0.04);
}

@keyframes float {
  0% { transform: translateY(0px) translateX(0px) rotate(0deg); }
  25% { transform: translateY(-20px) translateX(15px) rotate(45deg); }
  50% { transform: translateY(10px) translateX(-10px) rotate(90deg); }
  75% { transform: translateY(-15px) translateX(20px) rotate(135deg); }
  100% { transform: translateY(0px) translateX(0px) rotate(180deg); }
}


.login-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  text-align: center;
  animation: fadeIn 1s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 头部Logo区域 - 调整文字颜色 */
.header-section {
  margin-bottom: 40px;
}

.logo-wrapper {
  width: 80px;
  height: 80px;
  /* 玻璃拟态 - 浅色背景下的调整 */
  background: rgba(255, 255, 255, 0.6); /* 更不透明的白色背景 */
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(0, 0, 0, 0.08); /* 浅灰色细线边框 */
  box-shadow: 0 8px 32px 0 rgba(100, 100, 100, 0.1); /* 更柔和的阴影 */
}

.logo-icon {
  font-size: 40px;
  color: #007BFF; /* 保持或使用鲜明的蓝色 */
}

.app-title {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 8px;
  color: #1A202C; /* 深灰色文字 */
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); /* 更柔和的文字阴影 */
}

.app-subtitle {
  font-size: 16px;
  margin: 0;
  color: #4A5568; /* 中等灰色文字 */
}

/* 登录表单区域 - 调整颜色 */
.login-form-wrapper {
  width: 100%;
}

.form-card {
  /* 玻璃拟态 - 浅色背景下的调整 */
  background: rgba(255, 255, 255, 0.7); /* 更不透明的白色背景 */
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 30px;
  border: 1px solid rgba(0, 0, 0, 0.1); /* 浅灰色细线边框 */
  box-shadow: 0 8px 32px 0 rgba(100, 100, 100, 0.15);
}

.form-title {
  font-size: 22px;
  font-weight: 600;
  color: #1A202C; /* 深灰色标题 */
  text-align: center;
  margin: 0 0 30px;
}

.input-field {
  background: transparent !important;
  margin-bottom: 20px;
  padding: 0;
  /* 输入框下划线 - 浅色主题 */
  border-bottom: 1px solid rgba(0, 0, 0, 0.2); 
  transition: border-color 0.3s ease;
}

.input-field:focus-within {
  border-bottom-color: #007BFF; /* 聚焦时下划线蓝色 */
}

:deep(.van-field__body) {
  padding: 10px 4px;
}
:deep(.van-cell::after) {
  border-bottom: none !important;
}


.input-icon {
  font-size: 20px;
  color: rgba(0, 0, 0, 0.5); /* 半透明黑色图标 */
  margin-right: 12px;
}

.login-btn {
  /* 按钮颜色 - 浅色主题 */
  background: linear-gradient(135deg, #007BFF 0%, #0056b3 100%); /* 蓝色渐变 */
  border: none;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  margin-top: 20px;
  color: #FFFFFF; /* 按钮文字白色 */
  box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.login-btn:hover {
  transform: scale(1.03);
  box-shadow: 0 6px 20px rgba(0, 123, 255, 0.5);
}

.login-btn:active {
  transform: scale(0.98);
}

.form-footer {
  text-align: center;
  margin-top: 24px;
}

.footer-text {
  color: #4A5568; /* 中等灰色文字 */
  font-size: 14px;
}

.register-link {
  color: #007BFF; /* 蓝色链接 */
  font-size: 14px;
  font-weight: 500;
  margin-left: 4px;
  cursor: pointer;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #0056b3; /* 悬停时深蓝色 */
  text-decoration: underline;
}

/* Vant组件样式覆盖 - 浅色主题 */
:deep(.van-field__control) {
  color: #1A202C !important; /* 输入文字深灰色 */
  font-size: 16px;
  line-height: 24px;
}

:deep(.van-field__control::placeholder) {
  color: rgba(0, 0, 0, 0.4) !important; /* 半透明黑色占位符 */
}

:deep(.van-button--primary) {
 /* 通过 .login-btn 类控制 */
}

:deep(.van-loading__spinner) {
  color: #FFFFFF !important; /* 加载动画颜色 - 保持白色，因为按钮背景是深色 */
}
</style> 