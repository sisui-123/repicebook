import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/Login.vue'
import HomeView from '../views/Home.vue'
import CategoryView from '../views/Category.vue'
import TopicView from '../views/Topic.vue'
import ProfileView from '../views/Profile.vue'
import ProfileEditView from '../views/ProfileEdit.vue'
import RecipeDetailView from '../views/RecipeDetail.vue'
import TopicDetailView from '../views/TopicDetail.vue'
import RecipeCategoryListView from '../views/RecipeCategoryList.vue'
import AIRecipeSearchView from '../views/AIRecipeSearch.vue'

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { hideNavbar: true }
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  },
  {
    path: '/category',
    name: 'category',
    component: CategoryView
  },
  {
    path: '/topic',
    name: 'topic',
    component: TopicView
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView
  },
  {
    path: '/profile/edit',
    name: 'profile-edit',
    component: ProfileEditView
  },
  {
    path: '/recipe/:id',
    name: 'recipe-detail',
    component: RecipeDetailView
  },
  {
    path: '/topic/:id',
    name: 'topic-detail',
    component: TopicDetailView
  },
  {
    path: '/recipes/:type/:id',
    name: 'recipe-category-list',
    component: RecipeCategoryListView
  },
  {
    path: '/ai-recipe-search',
    name: 'ai-recipe-search',
    component: AIRecipeSearchView
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router 