<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { recipeApi } from '../services/api';

const route = useRoute();
const router = useRouter();
const loading = ref(true);
const recipe = ref(null);
const showCommentForm = ref(false);
const commentText = ref('');
const commentRating = ref(5);
const isCollected = ref(false);

// 获取菜谱详情
const fetchRecipeDetail = async () => {
  try {
    loading.value = true;
    const recipeId = route.params.id;
    if (!recipeId) {
      console.error('菜谱ID不存在');
      return;
    }
    
    const result = await recipeApi.getRecipeById(recipeId);
    if (result && result.data) {
      recipe.value = result.data;
      
      // 处理标签信息，如果有其他标签字段的话
      if (recipe.value.tags && typeof recipe.value.tags === 'string') {
        recipe.value.tags = recipe.value.tags.split(',').map(tag => tag.trim());
      }
      
      // 确保步骤和配料数组存在
      if (!recipe.value.steps) {
        recipe.value.steps = [];
      }
      if (!recipe.value.ingredients) {
        recipe.value.ingredients = [];
      }
      
      console.log('菜谱详情：', recipe.value);
    } else {
      recipe.value = null;
    }
  } catch (error) {
    console.error('获取菜谱详情失败:', error);
    recipe.value = null;
  } finally {
    loading.value = false;
  }
};

// 收藏菜谱
const toggleCollection = async () => {
  try {
    isCollected.value = !isCollected.value;
    // 这里应该调用收藏API
    // 例如: await recipeApi.collectRecipe(recipe.value.recipeId, isCollected.value);
    console.log('收藏/取消收藏菜谱:', recipe.value.recipeId);
  } catch (error) {
    console.error('操作收藏失败:', error);
    // 如果API调用失败，恢复之前的状态
    isCollected.value = !isCollected.value;
  }
};

// 提交评论
const submitComment = () => {
  if (!commentText.value.trim()) return;
  
  // 实际项目中应调用评论API
  console.log('提交评论:', {
    recipeId: recipe.value.recipeId,
    content: commentText.value,
    rating: commentRating.value
  });
  
  // 重置表单
  commentText.value = '';
  commentRating.value = 5;
  showCommentForm.value = false;
};

// 返回上一页
const goBack = () => {
  router.back();
};

// 根据标签内容获取样式类名
const getTagClass = (tag) => {
  if (!tag) return '';
  
  const tagLower = tag.toLowerCase();
  
  // 辣味相关
  if (tagLower.includes('麻辣') || tagLower.includes('辣') || tagLower.includes('微辣') || tagLower.includes('中辣') || tagLower.includes('重辣')) {
    return 'tag-spicy';
  }
  // 甜味相关
  if (tagLower.includes('甜') || tagLower.includes('糖') || tagLower.includes('蜜')) {
    return 'tag-sweet';
  }
  // 酸味相关
  if (tagLower.includes('酸') || tagLower.includes('醋')) {
    return 'tag-sour';
  }
  // 清淡相关
  if (tagLower.includes('清淡') || tagLower.includes('素') || tagLower.includes('清爽')) {
    return 'tag-light';
  }
  
  return 'tag-default';
};

onMounted(() => {
  fetchRecipeDetail();
});
</script>

<template>
  <div class="recipe-detail-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <div class="nav-left">
        <van-icon name="arrow-left" class="back-icon" @click="goBack" />
      </div>
      <h1 class="nav-title">{{ recipe?.title || '菜谱详情' }}</h1>
      <div class="nav-right">
        <van-icon 
          :name="isCollected ? 'star' : 'star-o'" 
          class="collect-icon" 
          :class="{'collected': isCollected}"
          @click="toggleCollection" 
        />
      </div>
    </div>
    
    <!-- 加载状态 -->
    <div class="loading-container" v-if="loading">
      <van-loading type="spinner" color="var(--color-primary)" size="36px" />
      <div class="loading-text">精彩美食加载中...</div>
    </div>
    
    <!-- 菜谱详情 -->
    <div v-else-if="recipe" class="recipe-content">
      <!-- 顶部图片 -->
      <div class="recipe-header">
        <div class="recipe-cover-wrap">
          <van-image 
            width="100%" 
            height="240" 
            fit="cover" 
            :src="recipe.imageUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'" 
            class="recipe-cover"
          />
          <div class="recipe-cover-overlay"></div>
        </div>
        <div class="recipe-header-info app-card">
          <h1 class="recipe-title">{{ recipe.title }}</h1>
          <div class="recipe-meta">
            <div class="recipe-tags" v-if="recipe.categoryName || recipe.cuisineTypeName || recipe.tags">
              <span class="recipe-tag tag-category" v-if="recipe.categoryName">{{ recipe.categoryName }}</span>
              <span class="recipe-tag tag-cuisine" v-if="recipe.cuisineTypeName">{{ recipe.cuisineTypeName }}</span>
              <span 
                class="recipe-tag" 
                :class="getTagClass(tag)"
                v-for="tag in recipe.tags" 
                :key="tag"
              >{{ tag }}</span>
            </div>
            <div class="recipe-stats">
              <div class="recipe-stat-item">
                <van-icon name="clock-o" />
                <span>{{ recipe.cookingTime || 30 }}分钟</span>
              </div>
              <div class="recipe-stat-item">
                <van-icon name="fire-o" />
                <span>{{ recipe.difficultyLevel || '一般' }}</span>
              </div>
            </div>
          </div>
          <div class="recipe-actions">
            <van-button 
              :class="['action-btn', {'collected-btn': isCollected}]"
              :type="isCollected ? 'success' : 'default'" 
              icon="star" 
              size="small"
              @click="toggleCollection"
            >
              {{ isCollected ? '已收藏' : '收藏' }}
            </van-button>
            <van-button 
              class="action-btn comment-btn"
              type="primary" 
              icon="comment-o" 
              size="small"
              @click="showCommentForm = true"
            >
              评论
            </van-button>
          </div>
        </div>
      </div>
      
      <!-- 菜谱介绍 -->
      <div class="tab-content">
        <div class="recipe-description">
          <h3 class="section-title">菜谱介绍</h3>
          <p class="description-text">{{ recipe.description || '暂无介绍' }}</p>
        </div>
      </div>
        
      <!-- 食材 -->
      <div class="tab-content">
        <h3 class="section-title">食材清单</h3>
        <div class="ingredient-list">
          <div 
            v-for="(ingredient, index) in recipe.ingredients" 
            :key="index"
            class="ingredient-item"
          >
            <div class="ingredient-name">
              <van-icon name="circle" class="ingredient-icon" />
              {{ ingredient.ingredientName }}
            </div>
            <div class="ingredient-quantity">{{ ingredient.quantity }}</div>
          </div>
          <div class="empty-tip" v-if="!recipe.ingredients || recipe.ingredients.length === 0">
            <van-empty description="暂无食材信息" />
          </div>
        </div>
      </div>
        
      <!-- 步骤 -->
      <div class="tab-content">
        <h3 class="section-title">烹饪步骤</h3>
        <div class="step-list">
          <div 
            v-for="(step, index) in recipe.steps" 
            :key="index"
            class="step-item"
          >
            <div class="step-number">{{ step.stepNumber }}</div>
            <div class="step-content">
              <div class="step-desc">{{ step.description }}</div>
              <van-image 
                v-if="step.imageUrl"
                width="120" 
                height="120" 
                fit="cover" 
                :src="step.imageUrl" 
                class="step-image"
              />
            </div>
          </div>
          <div class="empty-tip" v-if="!recipe.steps || recipe.steps.length === 0">
            <van-empty description="暂无步骤信息" />
          </div>
        </div>
      </div>
        
      <!-- 评论 -->
      <div class="tab-content">
        <h3 class="section-title">菜谱点评</h3>
        <div class="comment-list">
          <!-- 实际项目中这里应该显示评论列表 -->
          <div class="empty-tip">
            <van-empty description="暂无评论，快来发表第一条评论吧！" />
            <div class="empty-action">
              <van-button type="primary" size="small" @click="showCommentForm = true">写评论</van-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 评论表单 -->
      <van-popup 
        v-model:show="showCommentForm" 
        position="bottom"
        round
        class="comment-popup"
      >
        <div class="comment-form">
          <div class="form-header">
            <h3>发表评论</h3>
            <van-icon name="cross" class="close-icon" @click="showCommentForm = false" />
          </div>
          <div class="form-content">
            <div class="rating-field">
              <span class="rating-label">您的评分：</span>
              <van-rate v-model="commentRating" color="var(--color-primary)" />
            </div>
            <van-field
              v-model="commentText"
              rows="3"
              autosize
              type="textarea"
              maxlength="200"
              placeholder="请分享您的烹饪体验和建议..."
              show-word-limit
            />
            <div class="form-actions">
              <van-button plain type="default" @click="showCommentForm = false">取消</van-button>
              <van-button type="primary" @click="submitComment">提交评论</van-button>
            </div>
          </div>
        </div>
      </van-popup>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-state">
      <van-empty description="未找到菜谱信息" />
      <van-button plain type="primary" @click="goBack" class="back-btn">返回</van-button>
    </div>
  </div>
</template>

<style scoped>
.recipe-detail-container {
  min-height: 100vh;
  background-color: #F8F9FA;
  padding-bottom: var(--spacing-xl);
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md) var(--spacing-lg);
  background-color: #F8F9FA;
  color: #1A202C;
  position: relative;
  z-index: 10;
}

.nav-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 60%;
  color: #1A202C;
}

.nav-left, .nav-right {
  display: flex;
  align-items: center;
  min-width: 44px;
}

.back-icon, .collect-icon {
  font-size: 24px;
  color: #4A5568;
}

.collect-icon.collected {
  color: #F56565;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 50vh;
}

.loading-text {
  margin-top: var(--spacing-lg);
  color: var(--color-text-light);
  font-size: var(--font-size-sm);
}

.recipe-header {
  position: relative;
  margin-bottom: var(--spacing-md);
}

.recipe-cover-wrap {
  position: relative;
  margin: 0 8px;
  border-radius: 12px;
  overflow: hidden;
}

.recipe-cover {
  width: 100%;
}

.recipe-cover-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.3), transparent);
}

.recipe-header-info {
  margin: var(--spacing-sm) 8px 0;
  background-color: #FFFFFF;
  border-radius: 12px;
  padding: var(--spacing-md);
  position: relative;
  z-index: 5;
}

.recipe-title {
  font-size: var(--font-size-xl);
  font-weight: 600;
  margin: 0 0 var(--spacing-md);
  color: #1A202C;
}

.recipe-meta {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}

.recipe-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: var(--spacing-sm);
}

.recipe-tag {
  background-color: #F7FAFC;
  color: #4A5568;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 500;
  border: 1px solid #E2E8F0;
}

.recipe-tag.tag-category {
  background-color: #EBF4FF;
  color: #3182CE;
  border-color: #BEE3F8;
}

.recipe-tag.tag-cuisine {
  background-color: #F0FFF4;
  color: #38A169;
  border-color: #C6F6D5;
}

.recipe-tag.tag-spicy {
  background-color: #FED7D7;
  color: #E53E3E;
  border-color: #FEB2B2;
}

.recipe-tag.tag-sweet {
  background-color: #FFF5F5;
  color: #D53F8C;
  border-color: #FBB6CE;
}

.recipe-tag.tag-sour {
  background-color: #FFFBEB;
  color: #D69E2E;
  border-color: #F6E05E;
}

.recipe-tag.tag-light {
  background-color: #F0F9FF;
  color: #0C4A6E;
  border-color: #BAE6FD;
}

.recipe-tag.tag-default {
  background-color: #F7FAFC;
  color: #4A5568;
  border-color: #E2E8F0;
}

.recipe-stats {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-sm);
}

.recipe-stat-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  color: #718096;
  font-size: var(--font-size-sm);
}

.recipe-actions {
  display: flex;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-md);
}

.action-btn {
  border-radius: 20px;
  font-weight: 500;
  flex: 1;
}

.collected-btn {
  color: white;
  background: #F56565;
  border-color: #F56565;
}

.comment-btn {
  background: #1A202C;
  border: none;
  color: white;
}

.tab-content {
  margin: var(--spacing-sm) 8px var(--spacing-md);
  background-color: #FFFFFF;
  border-radius: 12px;
  padding: var(--spacing-md);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0 0 var(--spacing-md);
  color: #1A202C;
  display: flex;
  align-items: center;
}

.section-title::before {
  display: none !important;
}

.description-text {
  line-height: 1.6;
  color: #4A5568;
  font-size: var(--font-size-sm);
}

.ingredient-list, .step-list {
  display: flex;
  flex-direction: column;
}

.ingredient-item {
  display: flex;
  justify-content: space-between;
  padding: var(--spacing-sm) 0;
  border-bottom: 1px solid #F1F5F9;
  align-items: center;
}

.ingredient-name {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: #1A202C;
  font-size: var(--font-size-sm);
}

.ingredient-icon {
  color: #1A202C;
  font-size: var(--font-size-xs);
}

.ingredient-quantity {
  color: #718096;
  font-size: var(--font-size-xs);
}

.step-item {
  display: flex;
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid #F1F5F9;
}

.step-number {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #1A202C;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: var(--spacing-md);
  font-weight: bold;
  font-size: var(--font-size-sm);
}

.step-content {
  flex: 1;
}

.step-desc {
  margin-bottom: var(--spacing-sm);
  line-height: 1.6;
  color: #4A5568;
  font-size: var(--font-size-sm);
}

.step-image {
  border-radius: 8px;
  overflow: hidden;
}

.empty-tip {
  padding: var(--spacing-xl) 0;
}

.empty-action {
  margin-top: var(--spacing-md);
  display: flex;
  justify-content: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 50vh;
}

.back-btn {
  margin-top: var(--spacing-lg);
}

.comment-popup {
  height: 45%;
  padding-bottom: var(--spacing-md);
  background-color: #FFFFFF;
}

.comment-form {
  padding: var(--spacing-lg);
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid #F1F5F9;
}

.form-header h3 {
  margin: 0;
  font-size: var(--font-size-lg);
  color: #1A202C;
}

.close-icon {
  font-size: var(--font-size-lg);
  color: #718096;
}

.form-content {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.rating-field {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.rating-label {
  color: #4A5568;
  font-size: var(--font-size-sm);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
  margin-top: var(--spacing-lg);
}
</style> 