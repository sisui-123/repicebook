<script setup>
import { ref, onMounted, nextTick, computed } from 'vue';
import { useRouter } from 'vue-router';
import { recipeApi, userApi } from '../services/api';
import SearchBar from '../components/SearchBar.vue';

const router = useRouter();

// 新增：跳转到 AI 菜谱搜索页面
const goToAIRecipeSearch = () => {
  router.push('/ai-recipe-search');
};

// 数据状态
const loading = ref(false);
const finished = ref(false);
const popularRecipes = ref([]);     // 本周流行
const hotRecipes = ref([]);         // 热门排行榜（随机6个）
const featuredRecipes = ref([]);    // 特色推荐（随机8个）
const allRecipes = ref([]);         // 所有菜谱
const activeTab = ref('recommend'); // 当前活跃的标签页

// 搜索相关状态
const searchValue = ref('');
const searchKeyword = ref('');
const isSearching = ref(false);
const searchResults = ref([]);

// 瀑布流布局相关
const cardPositions = ref([]);
const containerHeight = ref(0);
const columnCount = 2;
const cardWidth = ref(0);
const gap = 2;

const categories = ref([
  { id: 1, name: '早餐', icon: 'ri-restaurant-line', color: '#FEF2E6', iconColor: '#FF8C42' },
  { id: 2, name: '川菜', icon: 'ri-fire-line', color: '#E6F7E8', iconColor: '#4CAF50' },
  { id: 3, name: '甜点', icon: 'ri-cake-3-line', color: '#F2ECFD', iconColor: '#9C27B0' },
  { id: 4, name: '汤羹', icon: 'ri-cup-line', color: '#E7F4FB', iconColor: '#2196F3' },
  { id: 5, name: '素食', icon: 'ri-leaf-line', color: '#E4F9E3', iconColor: '#8BC34A' },
  { id: 6, name: '烧烤', icon: 'ri-fire-line', color: '#FDEDE3', iconColor: '#FF5722' },
  { id: 7, name: '饮品', icon: 'ri-goblet-line', color: '#E7F5FE', iconColor: '#03A9F4' },
  { id: 8, name: '更多', icon: 'ri-apps-line', color: '#F0F0F0', iconColor: '#9E9E9E' }
]);

// 图片预加载函数
function preloadImage(url) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.src = url;
    img.onload = () => {
      resolve({ width: img.width, height: img.height });
    };
    img.onerror = (err) => {
      reject(err);
    };
  });
}

// 计算卡片位置的函数
function computeCardPositions(cards, columnHeights, cardWidth, gap) {
  return cards.map((card, index) => {
    const scale = cardWidth / card.width;
    const imageHeight = card.height * scale;
    const contentHeight = 60; // 减小内容区域高度估算（标题+用户信息）
    const totalHeight = imageHeight + contentHeight;
    
    const minHeight = Math.min(...columnHeights);
    const columnIndex = columnHeights.indexOf(minHeight);
    const x = columnIndex * (cardWidth + gap);
    const y = columnHeights[columnIndex];
    
    columnHeights[columnIndex] += totalHeight + gap;
    
    return { 
      ...card,
      width: cardWidth, 
      height: totalHeight,
      imageHeight,
      x, 
      y,
      index 
    };
  });
}

// 计算瀑布流布局
async function calculateMasonryLayout() {
  if (featuredRecipes.value.length === 0) return;
  
  // 计算卡片宽度 - 考虑实际的padding
  const paddingHorizontal = 4; // 现在使用4px的padding
  const containerWidth = window.innerWidth - paddingHorizontal * 2;
  cardWidth.value = (containerWidth - gap * (columnCount - 1)) / columnCount;
  
  // 预加载所有图片获取尺寸
  const cardsWithDimensions = await Promise.all(
    featuredRecipes.value.map(async (recipe) => {
      try {
        const imageUrl = recipe.imageUrl || 'https://images.unsplash.com/photo-1476224203421-9ac39bcb3327';
        const dimensions = await preloadImage(imageUrl);
        return {
          ...recipe,
          width: dimensions.width,
          height: dimensions.height
        };
      } catch (error) {
        console.error('图片预加载失败:', error);
        // 使用默认尺寸
        return {
          ...recipe,
          width: 300,
          height: 400
        };
      }
    })
  );
  
  // 初始化列高度
  const columnHeights = new Array(columnCount).fill(0);
  
  // 计算位置
  const positions = computeCardPositions(cardsWithDimensions, columnHeights, cardWidth.value, gap);
  cardPositions.value = positions;
  
  // 设置容器高度
  containerHeight.value = Math.max(...columnHeights);
}

// 随机抽取 n 个菜谱
function getRandomRecipes(sourceList, count) {
  if (!Array.isArray(sourceList)) return [];
  const shuffled = sourceList.slice().sort(() => Math.random() - 0.5);
  return shuffled.slice(0, count);
}

// 设置热门排行榜（随机6个）
function setHotRecipes() {
  hotRecipes.value = getRandomRecipes(allRecipes.value, 20);
}

// 设置特色推荐（随机8个），并获取用户信息
async function setFeaturedRecipes() {
  const selectedRecipes = getRandomRecipes(allRecipes.value, 20);
  
  // 为每个菜谱获取用户信息
  const recipesWithUserInfo = await Promise.all(
    selectedRecipes.map(async (recipe) => {
      try {
        if (recipe.userId) {
          const userResult = await userApi.getUserById(recipe.userId);
          return {
            ...recipe,
            authorName: userResult.data?.username || '美食达人',
            authorAvatar: userResult.data?.avatarUrl || 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=40&h=40&fit=crop&crop=face'
          };
        }
        return recipe;
      } catch (error) {
        console.error('获取用户信息失败:', error);
        return recipe;
      }
    })
  );
  
  featuredRecipes.value = recipesWithUserInfo;
  
  // 计算瀑布流布局
  await nextTick();
  await calculateMasonryLayout();
}

// 获取所有菜谱并设置热门和推荐
const fetchAllRecipes = async () => {
  try {
    const result = await recipeApi.getAllRecipes();
    allRecipes.value = result.data || [];

    // 从全部菜谱中随机取出热门和推荐
    setHotRecipes();
    await setFeaturedRecipes(); // 等待用户信息加载完成
  } catch (error) {
    console.error('获取所有菜谱失败:', error);
  }
};

// 获取本周流行菜谱（使用专门API）
const fetchPopularRecipes = async () => {
  try {
    loading.value = true;
    const result = await recipeApi.getLatestRecipes(6);
    popularRecipes.value = result.data || [];
  } catch (error) {
    console.error('获取本周流行菜谱失败:', error);
  } finally {
    loading.value = false;
  }
};

// 切换标签页
const switchTab = async (tab) => {
  activeTab.value = tab;
  if (tab === 'recommend') {
    await nextTick();
    await calculateMasonryLayout();
  }
};

// 查看菜谱详情
const viewRecipeDetail = (recipeId) => {
  router.push(`/recipe/${recipeId}`);
};

// 查看分类
const viewCategory = (categoryId) => {
  router.push(`/category/${categoryId}`);
};

// 搜索菜谱
const onSearch = async () => {
  if (!searchValue.value.trim()) {
    // 如果搜索内容为空，取消搜索状态
    cancelSearch();
    return;
  }
  
  try {
    searchKeyword.value = searchValue.value;
    isSearching.value = true;
    const result = await recipeApi.searchRecipes(searchValue.value);
    searchResults.value = result.data || [];
  } catch (error) {
    console.error('搜索菜谱失败:', error);
  }
};

// 取消搜索
const cancelSearch = () => {
  searchValue.value = '';
  searchKeyword.value = '';
  isSearching.value = false;
  searchResults.value = [];
};

// 窗口大小变化时重新计算布局
const handleResize = async () => {
  await calculateMasonryLayout();
};

// 页面挂载时加载数据
onMounted(() => {
  fetchAllRecipes();       // 包括设置热门和推荐
  fetchPopularRecipes();   // 最新流行菜谱
  
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize);
});
</script>


<template>
  <div class="home-container">
    <!-- 顶部搜索区域 -->
    <header class="header-search">
      <div class="header-container">
        <SearchBar 
          type="functional" 
          placeholder="搜索菜谱" 
          v-model="searchValue"
          @search="onSearch"
        />
      </div>
    </header>

    <!-- 导航栏 -->
    <section class="nav-tabs" v-if="!isSearching">
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'recommend' }"
        @click="switchTab('recommend')"
      >
        <span>推荐</span>
        <div class="tab-indicator" v-if="activeTab === 'recommend'"></div>
      </div>
      <div 
        class="tab-item"
        :class="{ active: activeTab === 'ranking' }"
        @click="switchTab('ranking')"
      >
        <span>榜单</span>
        <div class="tab-indicator" v-if="activeTab === 'ranking'"></div>
      </div>
    </section>

    <!-- 搜索结果 -->
    <section class="search-results" v-if="isSearching">
      <div class="result-header">
        <h3 class="section-title">搜索结果</h3>
        <div class="search-cancel" @click="cancelSearch">取消</div>
      </div>
      <div class="result-list" v-if="searchResults.length > 0">
        <div 
          v-for="(recipe, index) in searchResults" 
          :key="index" 
          class="result-item"
          @click="viewRecipeDetail(recipe.recipeId)"
        >
          <van-image
            width="80"
            height="80"
            fit="cover"
            radius="8"
            :src="recipe.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c'"
            class="result-image"
          />
          <div class="result-info">
            <div class="result-title">{{ recipe.title || '菜谱标题' }}</div>
            <div class="result-desc">{{ recipe.description || '暂无描述' }}</div>
            <div class="result-tags">
              <span class="result-tag" v-for="(tag, tagIndex) in (recipe.tags || []).slice(0, 2)" :key="tagIndex">
                {{ tag }}
              </span>
            </div>
          </div>
          <i class="ri-arrow-right-s-line arrow-icon"></i>
        </div>
      </div>
      <div class="empty-results" v-else-if="searchKeyword.trim() && !searchResults.length">
        <van-empty image="search" description="未找到相关菜谱" />
        <div class="empty-suggest">
          <p>建议您：</p>
          <ul>
            <li>检查输入是否正确</li>
            <li>尝试其他相关关键词</li>
            <li>尝试使用更通用的词语</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- 推荐菜谱 -->
    <section class="recipe-grid-section" v-if="activeTab === 'recommend' && !isSearching">
      <div class="recipe-masonry-container" :style="{ height: containerHeight + 'px' }">
        <div 
          v-for="(card, index) in cardPositions" 
          :key="card.recipeId || index"
          class="recipe-card"
          :style="{
            position: 'absolute',
            left: card.x + 'px',
            top: card.y + 'px',
            width: card.width + 'px',
          }"
          @click="viewRecipeDetail(card.recipeId)"
        >
          <div class="recipe-image" :style="{ height: card.imageHeight + 'px' }">
            <van-image 
              fit="cover" 
              :src="card.imageUrl || 'https://images.unsplash.com/photo-1476224203421-9ac39bcb3327'" 
            />
          </div>
          <div class="recipe-content">
            <h3>{{ card.title || '清蒸鲈鱼' }}</h3>
            <div class="recipe-author">
              <div class="author-avatar">
                <van-image 
                  fit="cover" 
                  :src="card.authorAvatar || 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=40&h=40&fit=crop&crop=face'" 
                  round
                />
              </div>
              <span class="author-name">{{ card.authorName || '美食达人' }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 榜单页面 -->
    <section class="ranking-section" v-if="activeTab === 'ranking' && !isSearching">
      <div class="section-header">
        <h2>本周热榜</h2>
        <a class="link-more">更多 <i class="ri-arrow-right-s-line"></i></a>
      </div>
      <div class="rank-list">
        <div 
          v-for="(recipe, index) in hotRecipes" 
          :key="index"
          class="rank-item"
          @click="viewRecipeDetail(recipe.recipeId)"
        >
          <div class="rank-number" :class="{'top-rank': index < 3}">{{ index + 1 }}</div>
          <div class="rank-image">
            <van-image fit="cover" :src="recipe.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c'" />
          </div>
          <div class="rank-content">
            <h3>{{ recipe.title || '香煎三文鱼配蔬菜沙拉' }}</h3>
            <div class="rank-info">
              <span class="rank-stats"><i class="ri-fire-line"></i> {{ 5000 + Math.floor(Math.random() * 10000) }}</span>
              <span class="rank-author">by {{ recipe.authorName || '米其林主厨' }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- AI 助手悬浮按钮 -->
    <button @click="goToAIRecipeSearch" class="fab-ai-button">
      <i class="ri-robot-2-fill"></i>
    </button>
  </div>
</template>

<style scoped>
.home-container {
  padding-bottom: var(--spacing-xl);
  background-color: #F8F9FA;
  transition: transform 0.3s ease;
}

/* 顶部搜索区域 - 简洁白色风格 */
.header-search {
  background-color: #F8F9FA;
  padding: var(--spacing-md) 0;
  position: relative;
}

.header-container {
  padding: 0 var(--spacing-md);
  position: relative;
}

/* 导航栏样式 */
.nav-tabs {
  display: flex;
  padding: var(--spacing-md) var(--spacing-xl);
  margin-bottom: var(--spacing-md);
  background-color: #F8F9FA;
}

.tab-item {
  position: relative;
  padding: var(--spacing-xs) var(--spacing-md);
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  color: #64748B;
  text-align: center;
  margin-right: var(--spacing-lg);
  cursor: pointer;
  transition: color var(--transition-fast);
}

.tab-item:hover {
  color: #1E293B;
}

.tab-item.active {
  color: #1E293B;
  font-weight: var(--font-weight-bold);
}

.tab-indicator {
  position: absolute;
  bottom: -11px;
  left: 0;
  right: 0;
  height: 3px;
  background-color: #1E293B;
  border-radius: 3px 3px 0 0;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: scaleX(0);
  }
  to {
    transform: scaleX(1);
  }
}

/* 推荐菜谱 - 真正的瀑布流布局 */
.recipe-grid-section {
  margin: var(--spacing-md) 0 var(--spacing-xl);
  padding: 0 4px;
}

.recipe-masonry-container {
  position: relative;
  width: 100%;
}

.recipe-card {
  background-color: #FFFFFF;
  border-radius: 8px;
  overflow: hidden;
  transition: transform var(--transition-fast);
  position: absolute;
}

.recipe-card:active {
  transform: scale(0.98);
}

.recipe-image {
  position: relative;
  width: 100%;
}

.recipe-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.recipe-content {
  padding: 6px;
}

.recipe-content h3 {
  margin: 0 0 4px;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  line-height: 1.3;
  color: #1A202C;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recipe-author {
  display: flex;
  align-items: center;
  gap: 4px;
}

.author-avatar {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-name {
  font-size: var(--font-size-xs);
  color: #718096;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 本周热榜 - 更现代的列表设计 */
.ranking-section {
  margin: var(--spacing-xl) 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--spacing-md);
  margin-bottom: var(--spacing-md);
}

.section-header h2 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: 0;
  position: relative;
  padding-left: var(--spacing-sm);
}

.section-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 25%;
  height: 50%;
  width: 4px;
  background-color: var(--color-primary);
  border-radius: 2px;
}

.link-more {
  display: flex;
  align-items: center;
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}

.link-more i {
  margin-left: 2px;
}

.rank-list {
  padding: 0 var(--spacing-md);
}

.rank-item {
  display: flex;
  align-items: center;
  background-color: var(--color-surface);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-sm) var(--spacing-md);
  margin-bottom: var(--spacing-md);
  box-shadow: var(--box-shadow-sm);
  transition: transform var(--transition-fast);
  position: relative;
  overflow: hidden;
}

.rank-item:active {
  transform: scale(0.98);
}

.rank-number {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 26px;
  height: 26px;
  border-radius: 50%;
  background-color: #f0f0f0;
  margin-right: var(--spacing-md);
  font-weight: var(--font-weight-semibold);
  font-size: var(--font-size-sm);
}

.rank-number.top-rank {
  background-color: var(--color-primary);
  color: white;
  box-shadow: 0 2px 6px rgba(255, 107, 53, 0.3);
}

.rank-image {
  margin-right: var(--spacing-md);
  overflow: hidden;
  border-radius: var(--border-radius-md);
  width: 70px;
  height: 70px;
  box-shadow: var(--box-shadow-sm);
}

.rank-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rank-content {
  flex: 1;
}

.rank-content h3 {
  margin: 0 0 var(--spacing-xs);
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.rank-info {
  display: flex;
  font-size: var(--font-size-xs);
  color: var(--color-text-light);
}

.rank-stats {
  display: flex;
  align-items: center;
  margin-right: var(--spacing-md);
}

.rank-stats i {
  margin-right: 2px;
  color: var(--color-primary);
}

/* 搜索结果样式 */
.search-results {
  padding: var(--spacing-md);
  animation: slideUp 0.3s ease;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: 0;
  color: #1A202C;
  display: flex;
  align-items: center;
}

/* 覆盖原来的伪元素 */
.section-title::before {
  display: none !important;   /* 或者 content: none; */
}

.search-cancel {
  cursor: pointer;
  color: #64748B;
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  background: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.result-item {
  display: flex;
  align-items: center;
  padding: var(--spacing-md);
  border-radius: 8px;
  position: relative;
  transition: transform var(--transition-fast);
  border-bottom: 1px solid #E2E8F0;
}

.result-item:last-child {
  border-bottom: none;
}

.result-item:active {
  transform: scale(0.98);
  background-color: #F7FAFC;
}

.result-image {
  border-radius: 8px;
  overflow: hidden;
  margin-right: var(--spacing-md);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.result-info {
  flex: 1;
}

.result-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  margin-bottom: 4px;
  color: #1A202C;
}

.result-desc {
  font-size: var(--font-size-xs);
  color: #718096;
  margin-bottom: var(--spacing-xs);
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.result-tags {
  display: flex;
  gap: var(--spacing-xs);
}

.result-tag {
  font-size: var(--font-size-xs);
  color: #1E293B;
  background-color: #F7FAFC;
  padding: 2px 8px;
  border-radius: 10px;
  border: 1px solid #E2E8F0;
}

.arrow-icon {
  margin-left: auto;
  color: #718096;
}

.empty-results {
  padding: var(--spacing-lg);
  background: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.empty-suggest {
  margin-top: var(--spacing-md);
  padding: 0 var(--spacing-lg);
}

.empty-suggest p {
  font-weight: var(--font-weight-medium);
  color: #1A202C;
  margin-bottom: var(--spacing-xs);
}

.empty-suggest ul {
  padding-left: var(--spacing-lg);
  color: #718096;
}

.empty-suggest li {
  margin-bottom: var(--spacing-xs);
  font-size: var(--font-size-sm);
}

/* 动画 */
@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 新增：AI 助手悬浮按钮样式 */
.fab-ai-button {
  position: fixed;
  bottom: 90px;
  right: 20px;
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 1000;
}

.fab-ai-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.fab-ai-button i {
  transition: transform 0.3s ease;
}

.fab-ai-button:hover i {
  transform: rotate(15deg);
}

/* 使底部导航栏透明 */
.bottom-nav {
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

/* 确保内容不被底部导航栏遮挡 */
.home-container {
  padding-bottom: 70px;
}
</style> 