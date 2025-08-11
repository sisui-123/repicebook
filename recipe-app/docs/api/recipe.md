# RecipeController 接口文档

## 获取单个菜谱

- **接口**: GET `/api/recipes/{recipeId}`

- **描述**: 根据ID获取菜谱详细信息

- **路径参数**: 
  - recipeId: 菜谱ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "recipeId": "菜谱ID",
    "userId": "用户ID",
    "username": "用户名",
    "title": "菜谱标题",
    "description": "菜谱描述",
    "categoryId": "分类ID",
    "categoryName": "分类名称",
    "cuisineTypeId": "烹饪类型ID",
    "cuisineTypeName": "烹饪类型名称",
    "cookingTime": "烹饪时间(分钟)",
    "difficultyLevel": "难度等级",
    "imageUrl": "菜谱图片URL",
    "createdAt": "创建时间",
    "steps": [
      {
        "stepId": "步骤ID",
        "stepNumber": "步骤序号",
        "description": "步骤描述",
        "imageUrl": "步骤图片URL"
      }
    ],
    "ingredients": [
      {
        "ingredientId": "食材ID",
        "ingredientName": "食材名称",
        "quantity": "用量"
      }
    ]
  }
}
```

## 获取所有菜谱

- **接口**: GET `/api/recipes`

- **描述**: 获取所有菜谱信息

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 获取用户的菜谱

- **接口**: GET `/api/recipes/user/{userId}`

- **描述**: 获取指定用户的所有菜谱

- **路径参数**: 
  - userId: 用户ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 获取分类下的菜谱

- **接口**: GET `/api/recipes/category/{categoryId}`

- **描述**: 获取指定分类下的所有菜谱

- **路径参数**: 
  - categoryId: 分类ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 获取烹饪类型下的菜谱

- **接口**: GET `/api/recipes/cuisine-type/{cuisineTypeId}`

- **描述**: 获取指定烹饪类型下的所有菜谱

- **路径参数**: 
  - cuisineTypeId: 烹饪类型ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 搜索菜谱

- **接口**: GET `/api/recipes/search`

- **描述**: 根据关键词搜索菜谱

- **查询参数**: 
  - keyword: 搜索关键词

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 获取最新菜谱

- **接口**: GET `/api/recipes/latest`

- **描述**: 获取最新的菜谱列表

- **查询参数**: 
  - limit: 返回数量，默认为10

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "recipeId": "菜谱ID",
      "userId": "用户ID",
      "username": "用户名",
      "title": "菜谱标题",
      "description": "菜谱描述",
      "categoryId": "分类ID",
      "categoryName": "分类名称",
      "cuisineTypeId": "烹饪类型ID",
      "cuisineTypeName": "烹饪类型名称",
      "cookingTime": "烹饪时间(分钟)",
      "difficultyLevel": "难度等级",
      "imageUrl": "菜谱图片URL",
      "createdAt": "创建时间"
    }
  ]
}
```

## 创建菜谱

- **接口**: POST `/api/recipes`

- **描述**: 创建新的菜谱

- **请求体**: 
```json
{
  "title": "菜谱标题",
  "description": "菜谱描述",
  "imageUrl": "菜谱图片URL",
  "preparationTime": "准备时间(分钟)",
  "cookingTime": "烹饪时间(分钟)",
  "difficultyLevel": "难度等级",
  "servings": "份量",
  "userId": "用户ID",
  "categoryId": "分类ID",
  "cuisineTypeId": "烹饪类型ID",
  "ingredients": [
    {
      "ingredientId": "食材ID",
      "quantity": "用量"
    }
  ],
  "steps": [
    {
      "stepNumber": "步骤序号",
      "description": "步骤描述",
      "imageUrl": "步骤图片URL"
    }
  ]
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "创建菜谱成功",
  "data": {
    "recipeId": "菜谱ID",
    "userId": "用户ID",
    "username": "用户名",
    "title": "菜谱标题",
    "description": "菜谱描述",
    "categoryId": "分类ID",
    "categoryName": "分类名称",
    "cuisineTypeId": "烹饪类型ID",
    "cuisineTypeName": "烹饪类型名称",
    "cookingTime": "烹饪时间(分钟)",
    "difficultyLevel": "难度等级",
    "imageUrl": "菜谱图片URL",
    "createdAt": "创建时间",
    "steps": [
      {
        "stepId": "步骤ID",
        "stepNumber": "步骤序号",
        "description": "步骤描述",
        "imageUrl": "步骤图片URL"
      }
    ],
    "ingredients": [
      {
        "ingredientId": "食材ID",
        "ingredientName": "食材名称",
        "quantity": "用量"
      }
    ]
  }
}
```

## 更新菜谱

- **接口**: PUT `/api/recipes/{recipeId}`

- **描述**: 更新已有的菜谱

- **路径参数**: 
  - recipeId: 菜谱ID

- **请求体**: 
```json
{
  "title": "菜谱标题",
  "description": "菜谱描述",
  "imageUrl": "菜谱图片URL",
  "preparationTime": "准备时间(分钟)",
  "cookingTime": "烹饪时间(分钟)",
  "difficultyLevel": "难度等级",
  "servings": "份量",
  "categoryId": "分类ID",
  "cuisineTypeId": "烹饪类型ID",
  "ingredients": [
    {
      "ingredientId": "食材ID",
      "quantity": "用量"
    }
  ],
  "steps": [
    {
      "stepNumber": "步骤序号",
      "description": "步骤描述",
      "imageUrl": "步骤图片URL"
    }
  ]
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "更新菜谱成功",
  "data": {
    "recipeId": "菜谱ID",
    "userId": "用户ID",
    "username": "用户名",
    "title": "菜谱标题",
    "description": "菜谱描述",
    "categoryId": "分类ID",
    "categoryName": "分类名称",
    "cuisineTypeId": "烹饪类型ID",
    "cuisineTypeName": "烹饪类型名称",
    "cookingTime": "烹饪时间(分钟)",
    "difficultyLevel": "难度等级",
    "imageUrl": "菜谱图片URL",
    "createdAt": "创建时间",
    "steps": [
      {
        "stepId": "步骤ID",
        "stepNumber": "步骤序号",
        "description": "步骤描述",
        "imageUrl": "步骤图片URL"
      }
    ],
    "ingredients": [
      {
        "ingredientId": "食材ID",
        "ingredientName": "食材名称",
        "quantity": "用量"
      }
    ]
  }
}
```

## 删除菜谱

- **接口**: DELETE `/api/recipes/{recipeId}`

- **描述**: 删除指定ID的菜谱

- **路径参数**: 
  - recipeId: 菜谱ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "删除菜谱成功",
  "data": null
}
``` 