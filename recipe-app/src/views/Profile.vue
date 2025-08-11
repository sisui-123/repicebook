<template>
  <div class="profile-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <h1>个人中心</h1>
      <div class="nav-actions">
        <van-button 
          v-if="false" 
          class="edit-btn" 
          round 
          icon="edit" 
          size="small" 
          @click="editProfile">编辑</van-button>
        <van-button 
          class="logout-btn" 
          round 
          icon="sign-out" 
          size="small" 
          @click="handleLogout">退出</van-button>
      </div>
    </div>
    
    <!-- 用户信息卡片 -->
    <div class="user-card">
      <div class="user-header">
        <div class="user-avatar-section">
          <van-image
            round
            width="80"
            height="80"
            :src="user.avatarUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
            class="user-avatar"
          />
          <div class="user-info">
            <h2 class="username">{{ user.username || 'sisui' }}</h2>
            <p class="bio">{{ user.bio || '爱做饭的工程师' }}</p>
          </div>
        </div>
      </div>
      
      <!-- 用户统计信息 -->
      <div class="user-stats">
        <div class="stat-item">
          <span class="stat-value">{{ collections.length || 0 }}</span>
          <span class="stat-label">收藏</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ userTopics.length || 0 }}</span>
          <span class="stat-label">话题</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ tasteTags.length || 0 }}</span>
          <span class="stat-label">口味标签</span>
        </div>
      </div>
    </div>
    
    <!-- 标签页 -->
    <van-tabs v-model:active="activeTab" sticky color="#1E293B" swipeable animated>
      <!-- 我的口味标签 -->
      <van-tab title="口味标签">
        <div class="tab-content">
          <h3 class="section-title">我的口味偏好</h3>
          <div class="taste-tags">
            <div 
              v-for="tag in tasteTags" 
              :key="tag.id" 
              class="tag-item"
            >
              <template v-if="editingTagId === tag.id">
                <van-field
                  v-model="newTagInput"
                  autofocus
                  size="small"
                  class="edit-tag-input"
                  @blur="saveEditTag(tag)"
                  @keyup.enter="saveEditTag(tag)"
                  style="width: 80px;"
                />
              </template>
              <template v-else>
                <van-tag 
                  type="primary" 
                  size="medium"
                  color="#1E293B"
                  @click="editTag(tag)"
                  round
                  style="cursor:pointer;user-select:none;"
                >
                  {{ tag.name }}
                </van-tag>
                <van-icon name="cross" style="margin-left:4px;cursor:pointer;" @click.stop="removeTag(tag)" />
              </template>
            </div>
            <div v-if="editingTagId === 'new'" class="tag-item">
              <van-field
                v-model="newTagInput"
                autofocus
                size="small"
                class="edit-tag-input"
                @blur="addTag"
                @keyup.enter="addTag"
                placeholder="输入"
                style="width: 80px;"
              />
            </div>
            <van-button 
              icon="plus" 
              size="small" 
              class="add-tag-btn"
              @click="showAddTagInput"
              round
              plain
              type="primary"
              v-if="editingTagId !== 'new'"
            >
              添加标签
            </van-button>
          </div>
        </div>
      </van-tab>
      
      <!-- 我的收藏 -->
      <van-tab title="我的收藏">
        <div class="tab-content">
          <h3 class="section-title">我收藏的美食</h3>
          <van-loading v-if="collectionLoading" color="#1E293B" />
          <div v-else class="collection-list">
            <div 
              v-for="(recipe, index) in collections" 
              :key="index" 
              class="collection-item"
              @click="viewRecipeDetail(recipe.recipeId)"
            >
              <van-image
                width="80"
                height="80"
                fit="cover"
                radius="8"
                :src="recipe.imageUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
                class="collection-image"
              />
              <div class="collection-info">
                <div class="collection-title">{{ recipe.title || '菜谱标题' }}</div>
                <div class="collection-desc">{{ recipe.description || '暂无描述' }}</div>
                <van-tag round color="#1E293B" v-if="recipe.categoryName" size="small">{{ recipe.categoryName }}</van-tag>
              </div>
              <i class="ri-arrow-right-s-line arrow-icon"></i>
            </div>
            <div class="empty-state" v-if="!collectionLoading && collections.length === 0">
              <van-empty description="暂无收藏" />
              <van-button type="primary" size="small" plain round class="empty-btn" @click="router.push('/home')">去浏览菜谱</van-button>
            </div>
          </div>
        </div>
      </van-tab>
      
      <!-- 我的话题 -->
      <van-tab title="我的话题">
        <div class="tab-content">
          <h3 class="section-title">我发布的话题</h3>
          <van-loading v-if="topicLoading" color="#1E293B" />
          <div v-else class="topic-list">
            <div 
              v-for="(topic, index) in userTopics" 
              :key="index" 
              class="topic-item"
              @click="viewTopicDetail(topic.topicId)"
            >
              <div class="topic-header">
                <div class="topic-title">{{ topic.title }}</div>
                <div class="topic-time">{{ topic.createdAt }}</div>
              </div>
              <div class="topic-content">{{ topic.content }}</div>
              <div class="topic-footer">
                <div class="topic-stats">
                  <div class="stat-chip">
                    <i class="ri-eye-line"></i>
                    <span>{{ topic.views || 0 }}</span>
                  </div>
                  <div class="stat-chip">
                    <i class="ri-heart-line"></i>
                    <span>{{ topic.likes || 0 }}</span>
                  </div>
                  <div class="stat-chip">
                    <i class="ri-chat-3-line"></i>
                    <span>{{ topic.comments ? topic.comments.length : 0 }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="empty-state" v-if="!topicLoading && userTopics.length === 0">
              <van-empty description="暂无话题" />
              <van-button type="primary" size="small" plain round class="empty-btn" @click="router.push('/topic')">去发布话题</van-button>
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, onActivated } from 'vue';
import { useRouter } from 'vue-router';
import { userApi, recipeApi, topicApi } from '../services/api';
import { showConfirmDialog } from 'vant';

const router = useRouter();
const userLoading = ref(false);
const collectionLoading = ref(false);
const topicLoading = ref(false);

// 用户数据
const user = ref({});
// 收藏数据
const collections = ref([]);
// 话题数据
const userTopics = ref([]);
// 口味标签
const tasteTags = ref([]);
const editingTagId = ref(null); // 当前正在编辑的标签id
const newTagInput = ref('');   // 新标签输入内容

// 当前标签页
const activeTab = ref(0);

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    userLoading.value = true;
    // 从localStorage获取userId
    const userId = localStorage.getItem('userId') || '1'; // 默认ID为1，便于测试
    const result = await userApi.getUserById(userId);
    user.value = result.data || {};
    // 只前端处理
    if (result.data && result.data.tasteTags && result.data.tasteTags.length > 0) {
      tasteTags.value = result.data.tasteTags.map((t, i) => ({ id: t.id || `fromapi-${i}`, name: t.name }));
    } else {
      tasteTags.value = [
        { id: 'default-1', name: '甜' },
        { id: 'default-2', name: '辣' }
      ];
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  } finally {
    userLoading.value = false;
  }
};

// 获取用户收藏
const fetchUserCollections = async () => {
  try {
    collectionLoading.value = true;
    const userId = localStorage.getItem('userId') || '1';
    // 调用获取用户收藏的API
    // 假设有一个getUserRecipes API，用于获取用户收藏的菜谱
    const result = await recipeApi.getUserRecipes(userId);
    collections.value = result.data || [];
  } catch (error) {
    console.error('获取用户收藏失败:', error);
  } finally {
    collectionLoading.value = false;
  }
};

// 获取用户话题
const fetchUserTopics = async () => {
  try {
    topicLoading.value = true;
    const userId = localStorage.getItem('userId') || '1';
    const result = await topicApi.getUserTopics(userId);
    userTopics.value = result.data || [];
  } catch (error) {
    console.error('获取用户话题失败:', error);
  } finally {
    topicLoading.value = false;
  }
};

// 查看菜谱详情
const viewRecipeDetail = (recipeId) => {
  router.push(`/recipe/${recipeId}`);
};

// 查看话题详情
const viewTopicDetail = (topicId) => {
  router.push(`/topic/${topicId}`);
};

// 编辑个人信息
const editProfile = async () => {
  router.push('/profile/edit');
};

// 添加标签
const showAddTagInput = () => {
  newTagInput.value = '';
  editingTagId.value = 'new';
};
const addTag = () => {
  const name = newTagInput.value.trim();
  if (name && !tasteTags.value.some(t => t.name === name)) {
    tasteTags.value.push({ id: `tag-${Date.now()}`, name });
  }
  newTagInput.value = '';
  editingTagId.value = null;
};

// 编辑标签
const editTag = (tag) => {
  editingTagId.value = tag.id;
  newTagInput.value = tag.name;
};
const saveEditTag = (tag) => {
  const name = newTagInput.value.trim();
  if (!name) {
    removeTag(tag);
  } else {
    tag.name = name;
  }
  editingTagId.value = null;
  newTagInput.value = '';
};

// 删除标签
const removeTag = (tag) => {
  tasteTags.value = tasteTags.value.filter(t => t.id !== tag.id);
  if (editingTagId.value === tag.id) {
    editingTagId.value = null;
    newTagInput.value = '';
  }
};

// 处理退出登录
const handleLogout = async () => {
  try {
    await showConfirmDialog({
      title: '确认退出',
      message: '确定要退出登录吗？',
      confirmButtonText: '退出',
      cancelButtonText: '取消',
      confirmButtonColor: '#ee0a24'
    });
    
    // 清除所有用户相关的localStorage数据
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('userInfo');
    
    // 可以在这里调用退出登录的API
    // await userApi.logout();
    
    // 跳转到登录页面
    router.replace('/login');
    
  } catch {
    // 用户取消了退出操作
  }
};

onMounted(() => {
  fetchUserInfo();
  fetchUserCollections();
  fetchUserTopics();
});

onActivated(() => {
  fetchUserInfo();
  fetchUserCollections();
  fetchUserTopics();
});
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: 60px;
}

/* 顶部导航栏 */
.nav-bar {
  background-color: #F8F9FA;
  padding: var(--spacing-md) var(--spacing-md) var(--spacing-lg);
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-bar h1 {
  color: var(--color-text-primary);
  font-size: var(--font-size-xl);
  margin: 0;
  font-weight: var(--font-weight-bold);
  letter-spacing: 1px;
  flex-grow: 1;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.edit-btn {
  background-color: var(--color-primary);
  border: none;
  color: white;
  border-radius: 20px;
  padding: 6px 12px;
  font-size: var(--font-size-xs);
}

.edit-btn:hover {
  background-color: var(--color-primary-dark);
}

.logout-btn {
  background-color: #DC2626;
  border: none;
  color: white;
  border-radius: 20px;
  padding: 6px 12px;
  font-size: var(--font-size-xs);
}

/* 用户信息卡片 */
.user-card {
  background-color: #FFFFFF;
  margin: 0 var(--spacing-md) var(--spacing-lg);
  border-radius: 12px;
  padding: var(--spacing-lg);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.user-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: var(--spacing-lg);
  text-align: center;
}

.user-avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
}

.user-avatar {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 3px solid #FFFFFF;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.username {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: 0 0 var(--spacing-xs);
  color: #1A202C;
}

.bio {
  font-size: var(--font-size-sm);
  color: #718096;
  margin: 0;
  line-height: 1.5;
}

/* 用户统计 */
.user-stats {
  display: flex;
  border-top: 1px solid #E2E8F0;
  padding-top: var(--spacing-md);
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-sm) 0;
}

.stat-value {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: #1A202C;
  margin-bottom: 4px;
}

.stat-label {
  font-size: var(--font-size-xs);
  color: #718096;
}

/* 标签页内容 */
.tab-content {
  padding: var(--spacing-lg);
  background-color: #FFFFFF;
  margin: 0 var(--spacing-md) var(--spacing-lg);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: 0 0 var(--spacing-md);
  color: #1A202C;
  display: flex;
  align-items: center;
}

.section-title::before {
  display: none !important;
  content: '';
  display: block;
  width: 4px;
  height: 20px;
  background-color: #1E293B;
  margin-right: var(--spacing-sm);
  border-radius: 2px;
}

/* 口味标签 */
.taste-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  align-items: center;
}

.add-tag-btn {
  margin-left: var(--spacing-xs);
  color: #1E293B;
  border-color: #1E293B;
}

/* 收藏列表 */
.collection-list, .topic-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.collection-item {
  display: flex;
  align-items: center;
  background-color: #F7FAFC;
  border-radius: 12px;
  padding: var(--spacing-md);
  transition: transform var(--transition-fast);
  position: relative;
  border: 1px solid #E2E8F0;
}

.collection-item:active {
  transform: scale(0.98);
  background-color: #EDF2F7;
}

.collection-image {
  margin-right: var(--spacing-md);
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.collection-info {
  flex: 1;
  padding-right: var(--spacing-lg);
}

.collection-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--spacing-xs);
  color: #1A202C;
}

.collection-desc {
  font-size: var(--font-size-xs);
  color: #718096;
  margin-bottom: var(--spacing-xs);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
}

.arrow-icon {
  position: absolute;
  right: var(--spacing-md);
  color: #718096;
  font-size: 18px;
}

/* 话题列表 */
.topic-item {
  background-color: #F7FAFC;
  border-radius: 12px;
  padding: var(--spacing-lg);
  transition: transform var(--transition-fast);
  border: 1px solid #E2E8F0;
}

.topic-item:active {
  transform: scale(0.98);
  background-color: #EDF2F7;
}

.topic-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-md);
}

.topic-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: #1A202C;
  flex: 1;
  margin-right: var(--spacing-md);
}

.topic-time {
  font-size: var(--font-size-xs);
  color: #718096;
  white-space: nowrap;
}

.topic-content {
  font-size: var(--font-size-sm);
  color: #64748B;
  margin-bottom: var(--spacing-md);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.topic-footer {
  display: flex;
  justify-content: flex-end;
}

.topic-stats {
  display: flex;
  gap: var(--spacing-lg);
}

.stat-chip {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: var(--font-size-xs);
  color: #718096;
}

.stat-chip i {
  font-size: 14px;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-xl) 0;
  text-align: center;
}

.empty-btn {
  margin-top: var(--spacing-md);
  color: #1E293B;
  border-color: #1E293B;
}

/* 标签页样式覆盖 */
:deep(.van-tabs__nav) {
  background-color: #F8F9FA;
}

:deep(.van-tab) {
  color: #64748B;
  font-weight: var(--font-weight-medium);
}

:deep(.van-tab--active) {
  color: #1E293B;
  font-weight: var(--font-weight-bold);
}

:deep(.van-tabs__line) {
  background-color: #1E293B;
}

:deep(.van-tabs__content) {
  background-color: #F8F9FA;
  padding: 0;
}
</style> 