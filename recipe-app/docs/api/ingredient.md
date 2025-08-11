# IngredientController 接口文档

## 获取单个食材

- **接口**: GET `/api/ingredients/{ingredientId}`

- **描述**: 根据ID获取食材信息

- **路径参数**: 
  - ingredientId: 食材ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "ingredientId": "食材ID",
    "name": "食材名称",
    "description": "食材描述",
    "category": "食材分类"
  }
}
```

## 获取所有食材

- **接口**: GET `/api/ingredients`

- **描述**: 获取所有食材信息

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "ingredientId": "食材ID",
      "name": "食材名称",
      "description": "食材描述",
      "category": "食材分类"
    },
    {
      "ingredientId": "食材ID",
      "name": "食材名称",
      "description": "食材描述",
      "category": "食材分类"
    }
  ]
}
```

## 搜索食材

- **接口**: GET `/api/ingredients/search`

- **描述**: 根据关键词搜索食材

- **查询参数**: 
  - keyword: 搜索关键词

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "ingredientId": "食材ID",
      "name": "食材名称",
      "description": "食材描述",
      "category": "食材分类"
    },
    {
      "ingredientId": "食材ID",
      "name": "食材名称",
      "description": "食材描述",
      "category": "食材分类"
    }
  ]
}
``` 