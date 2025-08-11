<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { recipeApi, categoryApi, cuisineTypeApi, ingredientApi } from '../services/api';

const route = useRoute();
const router = useRouter();
const loading = ref(true);
const recipes = ref([]);
const categoryTitle = ref(''); // 分类标题

// 获取菜谱列表
const fetchRecipes = async () => {
  try {
    loading.value = true;
    const { type, id } = route.params;
    const queryName = route.query.name; // 从query中获取名称
    
    if (!type || !id) {
      console.error('缺少必要参数');
      return;
    }
    
    let result;
    
    // 根据类型获取不同的菜谱列表
    switch (type) {
      case 'category':
        result = await recipeApi.getCategoryRecipes(id);
        // 优先使用query中的名称，否则调用API获取
        if (queryName) {
          categoryTitle.value = queryName;
        } else {
          const categoryResult = await categoryApi.getCategoryById(id);
          categoryTitle.value = categoryResult.data ? categoryResult.data.name : '分类菜谱';
        }
        break;
        
      case 'cuisine-type':
        result = await recipeApi.getCuisineTypeRecipes(id);
        // 优先使用query中的名称，否则调用API获取
        if (queryName) {
          categoryTitle.value = queryName;
        } else {
          const cuisineTypeResult = await cuisineTypeApi.getCuisineTypeById(id);
          categoryTitle.value = cuisineTypeResult.data ? cuisineTypeResult.data.name : '烹饪类型';
        }
        break;
        
      case 'ingredient':
        // 假设有一个根据食材搜索菜谱的API
        result = await recipeApi.searchRecipes(id);
        // 优先使用query中的名称，否则调用API获取
        if (queryName) {
          categoryTitle.value = `含${queryName}的菜谱`;
        } else {
          const ingredientResult = await ingredientApi.getIngredientById(id);
          categoryTitle.value = ingredientResult.data ? `含${ingredientResult.data.name}的菜谱` : '食材菜谱';
        }
        break;
        
      default:
        result = await recipeApi.getAllRecipes();
        categoryTitle.value = queryName || '全部菜谱';
    }
    
    recipes.value = result.data || [];
    console.log('获取到的菜谱数据:', result.data);
    console.log('页面标题:', categoryTitle.value);
  } catch (error) {
    console.error('获取菜谱列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 查看菜谱详情
const viewRecipeDetail = (recipeId) => {
  router.push(`/recipe/${recipeId}`);
};

// 返回上一页
const goBack = () => {
  router.back();
};

onMounted(() => {
  fetchRecipes();
});
</script>

<template>
  <div class="recipe-list-container">
    <!-- 顶部导航栏 -->
    <div class="nav-bar">
      <van-icon name="arrow-left" class="back-icon" @click="goBack" />
      <h1 class="nav-title">{{ categoryTitle }}</h1>
      <div class="placeholder"></div>
    </div>
    
    <!-- 加载状态 -->
    <div class="loading-container" v-if="loading">
      <van-loading type="spinner" color="#1989fa" />
      <div class="loading-text">加载中...</div>
    </div>
    
    <!-- 菜谱列表 -->
    <div v-else class="recipe-list">
      <div 
        v-for="(recipe, index) in recipes" 
        :key="recipe.recipeId || index" 
        class="recipe-item"
        @click="viewRecipeDetail(recipe.recipeId)"
      >
        <van-image
          width="120"
          height="90"
          fit="cover"
          :src="recipe.imageUrl || 'https://img01.yzcdn.cn/vant/cat.jpeg'"
          class="recipe-image"
        />
        <div class="recipe-info">
          <div class="recipe-title">{{ recipe.title || '菜谱标题' }}</div>
          <div class="recipe-desc">{{ recipe.description || '暂无描述' }}</div>
          <div class="recipe-tags">
            <van-tag type="primary" v-if="recipe.categoryName">{{ recipe.categoryName }}</van-tag>
            <van-tag type="success" v-if="recipe.cuisineTypeName">{{ recipe.cuisineTypeName }}</van-tag>
            <van-tag type="warning" v-if="recipe.cookingTime">{{ recipe.cookingTime }}分钟</van-tag>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && recipes.length === 0">
        <van-empty description="暂无菜谱" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.recipe-list-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.nav-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon {
  font-size: 20px;
}

.nav-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
}

.placeholder {
  width: 20px;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

.loading-text {
  margin-top: 10px;
  color: #999;
}

.recipe-list {
  padding: 12px;
}

.recipe-item {
  display: flex;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 12px;
  padding: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.recipe-image {
  border-radius: 4px;
  overflow: hidden;
  margin-right: 12px;
}

.recipe-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.recipe-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 4px;
}

.recipe-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.recipe-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.empty-state {
  padding: 40px 0;
}
</style> 