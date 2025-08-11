// API服务统一导出
import userApi from './user';
import recipeApi from './recipe';
import categoryApi from './category';
import cuisineTypeApi from './cuisine-type';
import ingredientApi from './ingredient';
import topicApi from './topic';

export {
  userApi,
  recipeApi,
  categoryApi,
  cuisineTypeApi,
  ingredientApi,
  topicApi
};

export default {
  user: userApi,
  recipe: recipeApi,
  category: categoryApi,
  cuisineType: cuisineTypeApi,
  ingredient: ingredientApi,
  topic: topicApi
};
