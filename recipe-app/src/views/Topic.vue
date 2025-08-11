<template>
  <div class="topic-container">
    <!-- 顶部搜索栏 -->
    <div class="search-bar">
      <SearchBar 
        type="functional" 
        placeholder="搜索话题" 
        v-model="searchKeyword"
        @search="searchTopics"
      />
    </div>
    
    <!-- 话题列表 -->
    <div class="topic-list">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text="没有更多了"
      >
        <div class="topic-item" v-for="(topic, index) in topics" :key="index">
          <div class="topic-header">
            <div class="user-info">
              <van-image
                round
                width="40"
                height="40"
                :src="topic.userAvatar || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
                class="avatar"
              />
              <div class="username">{{ topic.username || '用户名' }}</div>
            </div>
            <div class="topic-time">{{ topic.createdAt || '刚刚' }}</div>
          </div>
          <div class="topic-content" @click="viewTopicDetail(topic.topicId)">
            <h3 class="topic-title">{{ topic.title || '话题标题' }}</h3>
            <p class="topic-text">{{ topic.content || '话题内容' }}</p>
            <van-image
              v-if="topic.imageUrl"
              width="100%"
              fit="cover"
              :src="topic.imageUrl"
              class="topic-image"
            />
          </div>
          <div class="topic-actions">
            <div class="action-btn" @click="likeTopic(topic)">
              <van-icon name="good-job-o" />
              <span>{{ topic.likeCount || 0 }}</span>
            </div>
            <div class="action-btn" @click="viewTopicDetail(topic.topicId)">
              <van-icon name="chat-o" />
              <span>{{ (topic.comments && topic.comments.length) || 0 }}</span>
            </div>
          </div>
        </div>
      </van-list>
      
      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && topics.length === 0">
        <van-empty description="暂无话题" />
      </div>
    </div>
    
    <!-- 发布话题按钮 -->
    <div class="publish-btn">
      <van-button 
        round 
        type="primary" 
        icon="plus" 
        @click="showTopicForm = true"
      >
        发起话题
      </van-button>
    </div>
    
    <!-- 发布话题表单 -->
    <van-popup
      v-model:show="showTopicForm"
      position="bottom"
      :style="{ height: '70%' }"
      round
    >
      <div class="topic-form">
        <div class="form-header">
          <h3>发起话题</h3>
          <van-icon name="cross" @click="showTopicForm = false" />
        </div>
        <div class="form-content">
          <van-field
            v-model="newTopic.title"
            label="标题"
            placeholder="请输入话题标题"
            maxlength="50"
            show-word-limit
          />
          <van-field
            v-model="newTopic.content"
            type="textarea"
            label="内容"
            placeholder="请输入话题内容"
            rows="4"
            maxlength="500"
            show-word-limit
          />
          <van-field
            v-model="newTopic.imageUrl"
            label="图片URL"
            placeholder="请输入图片URL（选填）"
          />
          <div class="form-actions">
            <van-button type="default" @click="showTopicForm = false">取消</van-button>
            <van-button type="primary" @click="createTopic" :loading="loading">发布</van-button>
          </div>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { topicApi } from '../services/api';
import SearchBar from '../components/SearchBar.vue';

const router = useRouter();
const loading = ref(false);
const finished = ref(false);
const topics = ref([]); // 话题列表
const searchKeyword = ref(''); // 搜索关键词
const showTopicForm = ref(false); // 是否显示发布话题表单
const newTopic = ref({
  title: '',
  content: '',
  imageUrl: ''
});

// 获取话题列表
const fetchTopics = async () => {
  try {
    loading.value = true;
    const result = await topicApi.getLatestTopics(20);
    topics.value = result.data || [];
  } catch (error) {
    console.error('获取话题列表失败:', error);
  } finally {
    loading.value = false;
    finished.value = true;
  }
};

// 搜索话题
const searchTopics = async () => {
  if (!searchKeyword.value.trim()) {
    await fetchTopics();
    return;
  }
  
  try {
    loading.value = true;
    const result = await topicApi.searchTopics(searchKeyword.value);
    topics.value = result.data || [];
  } catch (error) {
    console.error('搜索话题失败:', error);
  } finally {
    loading.value = false;
  }
};

// 点赞话题
const likeTopic = async (topic) => {
  try {
    // 这里应该调用点赞API
    // const result = await topicApi.likeTopic(topic.topicId);
    
    // 暂时在前端模拟点赞效果
    topic.likeCount = (topic.likeCount || 0) + 1;
  } catch (error) {
    console.error('点赞话题失败:', error);
  }
};

// 查看话题详情
const viewTopicDetail = (topicId) => {
  router.push(`/topic/${topicId}`);
};

// 创建新话题
const createTopic = async () => {
  if (!newTopic.value.title.trim() || !newTopic.value.content.trim()) {
    // 简单验证
    alert('请填写标题和内容');
    return;
  }
  
  try {
    loading.value = true;
    
    // 获取用户ID用于创建话题
    const userId = localStorage.getItem('userId') || 1;
    
    // 构建话题数据
    const topicData = {
      ...newTopic.value,
      userId
    };
    
    // 调用创建话题API
    await topicApi.createTopic(topicData);
    
    // 重置表单
    newTopic.value = {
      title: '',
      content: '',
      imageUrl: ''
    };
    
    // 关闭表单
    showTopicForm.value = false;
    
    // 刷新列表
    await fetchTopics();
  } catch (error) {
    console.error('创建话题失败:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTopics();
});
</script>

<style scoped>
.topic-container {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: 80px;
}

.search-bar {
  position: sticky;
  top: 0;
  z-index: 100;
  background-color: #F8F9FA;
  padding: var(--spacing-md);
}

.topic-list {
  padding: 10px;
}

.topic-item {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.topic-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  margin-right: 10px;
}

.username {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.topic-time {
  font-size: 12px;
  color: #999;
}

.topic-content {
  margin-bottom: 15px;
}

.topic-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 8px;
  color: #333;
}

.topic-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0 0 10px;
}

.topic-image {
  border-radius: 8px;
  overflow: hidden;
  margin-top: 10px;
}

.topic-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
  font-size: 14px;
}

.publish-btn {
  position: fixed;
  bottom: 70px;
  right: 20px;
  z-index: 90;
}

.empty-state {
  padding: 30px 0;
}

.topic-form {
  padding: 15px;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.form-header h3 {
  margin: 0;
  font-size: 16px;
}

.form-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}
</style> 