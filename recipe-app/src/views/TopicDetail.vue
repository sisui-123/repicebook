<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { topicApi } from '../services/api';

const route = useRoute();
const router = useRouter();
const loading = ref(true);
const topic = ref(null);
const commentText = ref('');
const isLiked = ref(false);

// 获取话题详情
const fetchTopicDetail = async () => {
  try {
    loading.value = true;
    const topicId = route.params.id;
    if (!topicId) return;
    
    const result = await topicApi.getTopicById(topicId);
    topic.value = result.data || null;
  } catch (error) {
    console.error('获取话题详情失败:', error);
  } finally {
    loading.value = false;
  }
};

// 点赞话题
const likeTopic = async () => {
  try {
    isLiked.value = !isLiked.value;
    // 这里应该调用点赞API
    // 例如: await topicApi.likeTopic(topic.value.topicId);
    console.log('点赞/取消点赞话题:', topic.value.topicId);
  } catch (error) {
    console.error('点赞话题失败:', error);
    // 如果API调用失败，恢复之前的状态
    isLiked.value = !isLiked.value;
  }
};

// 添加评论
const addComment = async () => {
  if (!commentText.value.trim()) return;
  
  try {
    const userId = localStorage.getItem('userId') || '1';
    
    // 构建评论数据
    const commentData = {
      content: commentText.value,
      userId
    };
    
    // 调用添加评论API
    const result = await topicApi.addComment(topic.value.topicId, commentData);
    
    // 更新话题对象，包含新评论
    if (result.data) {
      topic.value = result.data;
    } else {
      // 如果API没有返回更新后的话题，手动添加评论到列表
      if (!topic.value.comments) {
        topic.value.comments = [];
      }
      
      topic.value.comments.push({
        commentId: Date.now().toString(),
        userId: userId,
        username: '当前用户',
        userAvatar: 'https://img01.yzcdn.cn/vant/cat.jpeg',
        content: commentText.value,
        createdAt: '刚刚'
      });
    }
    
    // 重置评论框
    commentText.value = '';
  } catch (error) {
    console.error('添加评论失败:', error);
  }
};

// 返回话题列表
const goBack = () => {
  router.back();
};

onMounted(() => {
  fetchTopicDetail();
});
</script>

<template>
  <div class="topic-detail-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <van-icon name="arrow-left" class="back-icon" @click="goBack" />
      <h1 class="nav-title">话题详情</h1>
      <div class="placeholder"></div>
    </div>
    
    <!-- 加载状态 -->
    <div class="loading-container" v-if="loading">
      <van-loading type="spinner" color="#1989fa" />
      <div class="loading-text">加载中...</div>
    </div>
    
    <!-- 话题详情 -->
    <div v-else-if="topic" class="topic-content">
      <!-- 话题信息 -->
      <div class="topic-card">
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
        <div class="topic-body">
          <h2 class="topic-title">{{ topic.title }}</h2>
          <p class="topic-text">{{ topic.content }}</p>
          <van-image
            v-if="topic.imageUrl"
            width="100%"
            fit="cover"
            :src="topic.imageUrl"
            class="topic-image"
          />
        </div>
        <div class="topic-actions">
          <div class="action-btn" @click="likeTopic">
            <van-icon :name="isLiked ? 'good-job' : 'good-job-o'" :class="{ 'liked': isLiked }" />
            <span>点赞</span>
          </div>
        </div>
      </div>
      
      <!-- 评论区 -->
      <div class="comments-section">
        <h3 class="comments-title">评论 ({{ topic.comments ? topic.comments.length : 0 }})</h3>
        
        <!-- 评论列表 -->
        <div class="comment-list" v-if="topic.comments && topic.comments.length > 0">
          <div 
            v-for="(comment, index) in topic.comments" 
            :key="comment.commentId || index"
            class="comment-item"
          >
            <van-image
              round
              width="36"
              height="36"
              :src="comment.userAvatar || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
              class="comment-avatar"
            />
            <div class="comment-content">
              <div class="comment-user">
                <span class="comment-username">{{ comment.username }}</span>
                <span class="comment-time">{{ comment.createdAt }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
            </div>
          </div>
        </div>
        
        <!-- 空评论状态 -->
        <div class="empty-comments" v-else>
          <van-empty description="暂无评论" />
        </div>
        
        <!-- 评论框 -->
        <div class="comment-form">
          <van-field
            v-model="commentText"
            placeholder="写下你的评论..."
            class="comment-input"
          >
            <template #button>
              <van-button 
                size="small" 
                type="primary" 
                class="comment-btn"
                @click="addComment"
                :disabled="!commentText.trim()"
              >
                发送
              </van-button>
            </template>
          </van-field>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <van-empty description="未找到话题信息" />
    </div>
  </div>
</template>

<style scoped>
.topic-detail-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding-bottom: 60px;
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
  width: 20px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
}

.loading-text {
  margin-top: 15px;
  color: #999;
}

.topic-content {
  padding: 15px;
}

.topic-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 15px;
}

.topic-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
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

.topic-body {
  margin-bottom: 15px;
}

.topic-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px;
  color: #333;
}

.topic-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0 0 15px;
  white-space: pre-wrap;
}

.topic-image {
  border-radius: 8px;
  overflow: hidden;
}

.topic-actions {
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
  font-size: 14px;
}

.liked {
  color: #1989fa;
}

.comments-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.comments-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 15px;
  color: #333;
}

.comment-list {
  margin-bottom: 15px;
}

.comment-item {
  display: flex;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f5f5f5;
}

.comment-avatar {
  margin-right: 10px;
}

.comment-content {
  flex: 1;
}

.comment-user {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.comment-username {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.empty-comments {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.comment-form {
  position: fixed;
  bottom: 60px;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 10px;
  box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.1);
}

.comment-input {
  background-color: #f5f5f5;
  border-radius: 20px;
}

.comment-btn {
  margin-left: 10px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
}
</style> 