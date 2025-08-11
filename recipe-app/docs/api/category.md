# CategoryController 接口文档

## 获取单个分类信息

- **接口**: GET `/api/categories/{categoryId}`

- **描述**: 根据ID获取分类信息

- **路径参数**: 
  - categoryId: 分类ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "categoryId": "分类ID",
    "name": "分类名称",
    "type": "分类类型"
  }
}
```

## 获取所有分类

- **接口**: GET `/api/categories`

- **描述**: 获取所有分类信息

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "categoryId": "分类ID",
      "name": "分类名称",
      "type": "分类类型"
    },
    {
      "categoryId": "分类ID",
      "name": "分类名称",
      "type": "分类类型"
    }
  ]
}
```

## 根据类型获取分类

- **接口**: GET `/api/categories/type`

- **描述**: 根据类型获取分类列表

- **查询参数**: 
  - type: 分类类型（枚举值CategoryType）

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "categoryId": "分类ID",
      "name": "分类名称",
      "type": "分类类型"
    },
    {
      "categoryId": "分类ID",
      "name": "分类名称",
      "type": "分类类型"
    }
  ]
}
``` 