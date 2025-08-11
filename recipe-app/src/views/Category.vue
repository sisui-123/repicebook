<template>
  <div class="category-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <h1>分类浏览</h1>
      <SearchBar 
        type="functional" 
        placeholder="搜索菜谱或食材" 
        v-model="searchValue"
        @search="onSearch"
      />
    </div>

    <!-- 分类内容 -->
    <div class="category-content" v-if="!isSearching">
      <!-- 菜系分类网格 -->
      <div class="category-grid">
        <div 
          v-for="(category, index) in cuisineTypes" 
          :key="index" 
          class="category-card"
          @click="viewCuisineTypeRecipes(category.cuisineTypeId, category.name)"
        >
          <div class="category-inner" :style="{ backgroundImage: `linear-gradient(135deg, ${getGradientColors(index)[0]}, ${getGradientColors(index)[1]})` }">
            <div class="category-icon">
              <i :class="getCuisineIcon(category.name)" :style="{ color: '#FFFFFF' }"></i>
            </div>
            <div class="category-name">{{ category.name }}</div>
            <div class="category-count">{{ category.recipeCount || '经典' }}菜系</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索结果 -->
    <div class="search-results" v-if="isSearching">
      <div class="result-header">
        <h3 class="section-title">搜索结果</h3>
        <div class="search-cancel" @click="cancelSearch">取消</div>
      </div>
      <div class="result-list app-card" v-if="searchResults.length > 0">
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
      <div class="empty-results app-card" v-else-if="searchKeyword.trim() && !searchResults.length">
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
    </div>
  </div>
</template>

<script>
// 获取随机渐变颜色方法
function getRandomColor(index) {
  const colors = [
    'rgba(255, 140, 66, 0.8)', // 橙色
    'rgba(76, 175, 80, 0.8)',  // 绿色
    'rgba(33, 150, 243, 0.8)', // 蓝色
    'rgba(156, 39, 176, 0.8)', // 紫色
    'rgba(244, 67, 54, 0.8)',  // 红色
    'rgba(255, 193, 7, 0.8)'   // 黄色
  ];
  return colors[index % colors.length];
}
</script>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { categoryApi, cuisineTypeApi, recipeApi } from '../services/api';
import SearchBar from '../components/SearchBar.vue';

const router = useRouter();

// 搜索
const searchValue = ref('');
const searchKeyword = ref('');
const isSearching = ref(false);
const searchResults = ref([]);

// 分类数据
const categories = ref([]);
const cuisineTypes = ref([]);
const loading = ref(false);

// 获取渐变色方法
const getGradientColors = (index) => {
  const gradients = [
    ['#FF9966', '#FF5E62'], // 橙红色
    ['#56CCF2', '#2F80ED'], // 蓝色
    ['#4CAF50', '#8BC34A'], // 绿色
    ['#9C27B0', '#673AB7'], // 紫色
    ['#FFC107', '#FF9800'], // 黄橙色
    ['#F44336', '#E91E63'], // 红粉色
    ['#3F51B5', '#2196F3'], // 靛蓝色
    ['#009688', '#4CAF50'], // 青绿色
    ['#FF5722', '#FF9800'], // 深橙色
    ['#607D8B', '#455A64'], // 蓝灰色
    ['#795548', '#5D4037'], // 棕色
    ['#9E9E9E', '#607D8B']  // 灰色
  ];
  return gradients[index % gradients.length];
};

// 获取菜系对应图标
const getCuisineIcon = (name) => {
  const iconMap = {
    '川菜': 'ri-restaurant-line',
    '粤菜': 'ri-fish-line',
    '湘菜': 'ri-plant-line',
    '鲁菜': 'ri-bowl-line',
    '苏菜': 'ri-cake-3-line',
    '浙菜': 'ri-leaf-line',
    '闽菜': 'ri-restaurant-2-line',
    '徽菜': 'ri-bowl-2-line'
  };
  return iconMap[name] || 'ri-restaurant-line';
};

// 获取指定类型的分类
const getFilteredCategories = (type) => {
  return categories.value.filter(category => category.type === type);
};

// 获取分类数据
const fetchCategories = async () => {
  try {
    loading.value = true;
    const result = await categoryApi.getAllCategories();
    
    // 为每个分类设置默认属性
    categories.value = (result.data || []).map(category => ({
      ...category,
      recipeCount: Math.floor(Math.random() * 50) + 10,  // 随机菜谱数量
      isPopular: Math.random() > 0.7  // 随机设置热门标志
    }));
    
    console.log('处理后的分类数据:', categories.value);
  } catch (error) {
    console.error('获取分类失败:', error);
  } finally {
    loading.value = false;
  }
};

// 获取烹饪类型数据
const fetchCuisineTypes = async () => {
  try {
    const result = await cuisineTypeApi.getAllCuisineTypes();
    cuisineTypes.value = result.data || [];
  } catch (error) {
    console.error('获取烹饪类型失败:', error);
  }
};

// 搜索菜谱
const onSearch = async () => {
  if (!searchValue.value.trim()) return;
  
  try {
    searchKeyword.value = searchValue.value;
    isSearching.value = true;
    const result = await recipeApi.searchRecipes(searchValue.value);
    searchResults.value = result.data || [];
  } catch (error) {
    console.error('搜索菜谱失败:', error);
  }
};

// 查看烹饪类型下的菜谱
const viewCuisineTypeRecipes = (cuisineTypeId, cuisineTypeName) => {
  router.push({
    path: `/recipes/cuisine-type/${cuisineTypeId}`,
    query: { name: cuisineTypeName }
  });
};

// 查看菜谱详情
const viewRecipeDetail = (recipeId) => {
  router.push(`/recipe/${recipeId}`);
};

// 取消搜索
const cancelSearch = () => {
  searchValue.value = '';
  searchKeyword.value = '';
  isSearching.value = false;
  searchResults.value = [];
};

onMounted(() => {
  fetchCategories();
  fetchCuisineTypes();
});
</script>

<style scoped>
.category-container {
  background-color: #F8F9FA;
  min-height: 100vh;
  padding-bottom: var(--spacing-xl);
}

/* 顶部导航栏 */
.nav-bar {
  background-color: #F8F9FA;
  padding: var(--spacing-md) var(--spacing-md) var(--spacing-lg);
  position: relative;
  overflow: hidden;
}

/* 移除装饰元素 */
.nav-bar::before,
.nav-bar::after {
  display: none;
}

.nav-bar h1 {
  color: #1E293B;
  font-size: var(--font-size-xl);
  margin: 0 0 var(--spacing-md);
  text-align: center;
  font-weight: var(--font-weight-bold);
  letter-spacing: 1px;
}

/* 分类内容 */
.category-content {
  padding: var(--spacing-lg) 0 0;
}

/* 分类网格样式 - 现代化卡片设计 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.category-card {
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--box-shadow-md);
  transition: transform var(--transition-fast);
}

.category-card:active {
  transform: scale(0.96);
}

.category-inner {
  height: 160px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: var(--spacing-md);
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;
}

/* 卡片光效装饰 */
.category-inner::before {
  content: '';
  position: absolute;
  top: -30%;
  right: -30%;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  filter: blur(15px);
}

.category-inner::after {
  content: '';
  position: absolute;
  bottom: -10%;
  left: -10%;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.15);
  filter: blur(15px);
}

/* 食材卡片特效 */
.food-card::before {
  background: rgba(255, 255, 255, 0.3);
  width: 150px;
  height: 150px;
}

/* 分类图标 */
.category-icon {
  width: 60px;
  height: 60px;
  border-radius: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.25);
  margin-bottom: var(--spacing-md);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(5px);
}

.category-icon i {
  font-size: 30px;
  color: white;
}

.category-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  margin-bottom: var(--spacing-xs);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.category-count {
  font-size: var(--font-size-xs);
  opacity: 0.95;
  background-color: rgba(0, 0, 0, 0.25);
  padding: 4px 12px;
  border-radius: var(--border-radius-md);
  backdrop-filter: blur(5px);
}

/* 搜索结果部分 */
.search-results {
  padding: var(--spacing-lg);
  animation: slideUp 0.3s ease;
}

.result-header {
  margin-bottom: var(--spacing-md);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
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
  color: #1E293B;
  font-size: var(--font-size-md);
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
  border-radius: var(--border-radius-md);
  position: relative;
  transition: transform var(--transition-fast);
  border-bottom: 1px solid var(--color-border);
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

/* 空结果 */
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

/* App卡片通用样式 */
.app-card {
  background: #FFFFFF;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}
</style> 