# 管理系统接口文档

本接口文档详细说明了后端各模块的 RESTful API，供前端开发对接使用。

---

## 通用说明
- 所有接口均以 `/api` 开头。
- 返回统一响应结构：
  ```json
  {
    "success": true/false, // 是否成功
    "message": "提示信息", // 可选
    "data": {} // 返回数据
  }
  ```
- 失败时 `success=false`，`message` 为错误信息。

---

## 用户模块

### 注册
- **POST** `/api/users/register`
- **请求体**：
  ```json
  {
    "username": "string",
    "password": "string",
    "confirmPassword": "string"
  }
  ```
- **返回**：`UserDTO`

### 登录
- **POST** `/api/users/login`
- **请求体**：
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **返回**：`UserDTO`

### 获取用户信息
- **GET** `/api/users/{userId}`
- **返回**：`UserDTO`

### 更新用户资料
- **PUT** `/api/users/{userId}`
- **请求体**：
  ```json
  {
    "bio": "string",
    "avatarUrl": "string"
  }
  ```
- **返回**：`UserDTO`

### 获取所有用户
- **GET** `/api/users`
- **返回**：`UserDTO[]`

---

## 菜谱模块

### 创建菜谱
- **POST** `/api/recipes`
- **请求体**：
  ```json
  {
    "title": "",
  "description": "",
  "imageUrl": "",
  "preparationTime": 0,
  "cookingTime": 0,
  "difficultyLevel": "",
  "servings": 0,
  "userId": 0,
  "categoryId": 0,
  "cuisineTypeId": 0,
  "ingredients": [
    {
      "ingredientId": 0,
      "quantity": ""
    }
  ],
  "steps": [
    {
      "stepNumber": 0,
      "description": "",
      "imageUrl": ""
    }
  ]
  }
  ```
- **返回**：`RecipeDTO`

### 获取菜谱详情（包括步骤）
- **GET** `/api/recipes/{recipeId}`
- **返回**：`RecipeDTO`

### 获取所有菜谱
- **GET** `/api/recipes`
- **返回**：`RecipeDTO[]`

### 按用户获取菜谱
- **GET** `/api/recipes/user/{userId}`
- **返回**：`RecipeDTO[]`

### 按分类获取菜谱
- **GET** `/api/recipes/category/{categoryId}`
- **返回**：`RecipeDTO[]`

### 按菜系获取菜谱
- **GET** `/api/recipes/cuisine-type/{cuisineTypeId}`
- **返回**：`RecipeDTO[]`

### 搜索菜谱
- **GET** `/api/recipes/search?keyword=xxx`
- **返回**：`RecipeDTO[]`

### 最新菜谱
- **GET** `/api/recipes/latest?limit=10`
- **返回**：`RecipeDTO[]`

### 更新菜谱
- **PUT** `/api/recipes/{recipeId}`
- **请求体**：
  ```json
  {
    "title": "",
  "description": "",
  "imageUrl": "",
  "preparationTime": 0,
  "cookingTime": 0,
  "difficultyLevel": "",
  "servings": 0,
  "categoryId": 0,
  "cuisineTypeId": 0,
  "ingredients": [
    {
      "ingredientId": 0,
      "quantity": ""
    }
  ],
  "steps": [
    {
      "stepNumber": 0,
      "description": "",
      "imageUrl": ""
    }
  ]
  }
  ```
- **返回**：`RecipeDTO`

### 删除菜谱
- **DELETE** `/api/recipes/{recipeId}`
- **返回**：无数据

---

## 分类模块

### 获取分类详情
- **GET** `/api/categories/{categoryId}`
- **返回**：`Category`

### 获取所有分类
- **GET** `/api/categories`
- **返回**：`Category[]`

### 按类型获取分类
- **GET** `/api/categories/type?type=xxx`
- **返回**：`Category[]`

---

## 菜系模块

### 获取菜系详情
- **GET** `/api/cuisine-types/{cuisineTypeId}`
- **返回**：`CuisineType`

### 获取所有菜系
- **GET** `/api/cuisine-types`
- **返回**：`CuisineType[]`

---

## 食材模块

### 获取食材详情
- **GET** `/api/ingredients/{ingredientId}`
- **返回**：`Ingredient`

### 获取所有食材
- **GET** `/api/ingredients`
- **返回**：`Ingredient[]`

### 搜索食材
- **GET** `/api/ingredients/search?keyword=xxx`
- **返回**：`Ingredient[]`

---

## 话题模块

### 创建话题
- **POST** `/api/topics`
- **请求体**：
  ```json
  {
    "title": "",
  "content": "",
  "userId": 0,
  "imageUrl": ""
  }
  ```
- **返回**：`TopicDTO`

### 获取话题详情
- **GET** `/api/topics/{topicId}`
- **返回**：`TopicDTO`

### 获取所有话题
- **GET** `/api/topics`
- **返回**：`TopicDTO[]`

### 按用户获取话题
- **GET** `/api/topics/user/{userId}`
- **返回**：`TopicDTO[]`

### 搜索话题
- **GET** `/api/topics/search?keyword=xxx`
- **返回**：`TopicDTO[]`

### 最新话题
- **GET** `/api/topics/latest?limit=10`
- **返回**：`TopicDTO[]`

### 更新话题
- **PUT** `/api/topics/{topicId}`
- **请求体**：
  ```json
  {
    "title": "",
  "content": "",
  "userId": 0,
  "imageUrl": ""
  }
  ```
- **返回**：`TopicDTO`

### 删除话题
- **DELETE** `/api/topics/{topicId}?userId=xxx`
- **返回**：无数据

### 添加评论
- **POST** `/api/topics/{topicId}/comments`
- **请求体**：
  ```json
  {
    "content": "string",
    "userId": 1
  }
  ```
- **返回**：`TopicDTO`

### 删除评论
- **DELETE** `/api/topics/comments/{commentId}?userId=xxx`
- **返回**：无数据

---

## 数据结构说明

### UserDTO
```json
{
  "id": 1,
  "username": "string",
  "bio": "string",
  "avatarUrl": "string"
}
```

### RecipeDTO
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "categoryId": 1,
  "cuisineTypeId": 1,
  "difficulty": "EASY|MEDIUM|HARD",
  "ingredients": [ ... ],
  "steps": [ ... ],
  "userId": 1,
  "createdAt": "2024-01-01T00:00:00"
}
```

### Category
```json
{
  "id": 1,
  "name": "string",
  "type": "菜品|其他"
}
```

### CuisineType
```json
{
  "id": 1,
  "name": "string"
}
```

### Ingredient
```json
{
  "id": 1,
  "name": "string"
}
```

### TopicDTO
```json
{
  "id": 1,
  "title": "string",
  "content": "string",
  "userId": 1,
  "createdAt": "2024-01-01T00:00:00",
  "comments": [ ... ]
}
```

---

如需更多字段说明，请参考后端实体类。
