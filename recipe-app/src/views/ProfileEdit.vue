<template>
  <div class="profile-edit-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <van-icon name="arrow-left" class="back-icon" @click="goBack" />
      <h1 class="nav-title">编辑个人信息</h1>
      <div class="placeholder"></div>
    </div>

    <!-- 编辑表单 -->
    <div class="edit-form">
      <van-form @submit="onSubmit">
        <div class="avatar-section">
          <van-image
            round
            width="100"
            height="100"
            :src="formData.avatarUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
            class="preview-avatar"
          />
        </div>
        <van-cell-group inset>
      
          <van-field
            v-model="formData.username"
            label="用户名"
            placeholder="请输入用户名"
            disabled
          />
          <van-field
            v-model="formData.bio"
            label="简介"
            type="textarea"
            placeholder="请输入个人简介"
            rows="3"
            autosize
          />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button 
            round 
            block 
            type="primary" 
            native-type="submit" 
            :loading="loading"
            @click="onSubmit"
          >
            保存修改
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { userApi } from '../services/api';
import { showToast, showSuccessToast, showFailToast } from 'vant';

const router = useRouter();
const route = useRoute();
const loading = ref(false);

const formData = ref({
  avatarUrl: '',
  username: '',
  bio: ''
});

// 获取当前用户信息并填充表单
const fetchCurrentUserInfo = async () => {
  try {
    const userId = localStorage.getItem('userId') || '1'; // 假设用户ID存在localStorage
    if (userId) {
      const result = await userApi.getUserById(userId);
      if (result.data) {
        formData.value.avatarUrl = result.data.avatarUrl || '';
        formData.value.username = result.data.username || '';
        formData.value.bio = result.data.bio || '';
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
    showFailToast('获取用户信息失败');
  }
};

// 提交表单
const onSubmit = async () => {
  console.log('=== 开始保存用户信息 ===');
  console.log('表单数据:', formData.value);
  
  try {
    loading.value = true;
    const userId = localStorage.getItem('userId') || '1';
    console.log('用户ID:', userId);
    console.log('准备调用 userApi.updateUser...');
    
    // 调用更新用户信息的API
    const result = await userApi.updateUser(userId, formData.value);
    console.log('API调用成功，返回结果:', result);
    
    showSuccessToast('保存成功'); // 显示成功提示
    
    // 延迟一点时间让用户看到成功提示，然后跳转
    setTimeout(() => {
      router.back(); // 保存成功后返回上一页
    }, 500);
    
  } catch (error) {
    console.error('保存用户信息失败:', error);
    showFailToast('保存失败，请稍后重试'); // 显示失败提示
  } finally {
    loading.value = false;
    console.log('=== 保存操作结束 ===');
  }
};

// 返回上一页
const goBack = () => {
  router.back();
};

onMounted(() => {
  fetchCurrentUserInfo();
});
</script>

<style scoped>
.profile-edit-container {
  min-height: 100vh;
  background-color: #F8F9FA;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 46px;
  padding: 0 16px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon {
  font-size: 20px;
  color: #333;
}

.nav-title {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}

.placeholder {
  width: 20px; /* 占位，保持标题居中 */
}

.edit-form {
  padding-top: 20px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: var(--spacing-lg);
}

.preview-avatar {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 3px solid #FFFFFF;
}

:deep(.van-cell-group--inset) {
  margin: 0 var(--spacing-md);
}

:deep(.van-button--primary) {
  background-color: #1E293B;
  border-color: #1E293B;
}

:deep(.van-button--primary:active) {
  background-color: #374151;
  border-color: #374151;
}
</style> 