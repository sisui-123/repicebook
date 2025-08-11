# CuisineTypeController 接口文档

## 获取单个烹饪类型

- **接口**: GET `/api/cuisine-types/{cuisineTypeId}`

- **描述**: 根据ID获取烹饪类型信息

- **路径参数**: 
  - cuisineTypeId: 烹饪类型ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "cuisineTypeId": "烹饪类型ID",
    "name": "烹饪类型名称",
    "description": "烹饪类型描述"
  }
}
```

## 获取所有烹饪类型

- **接口**: GET `/api/cuisine-types`

- **描述**: 获取所有烹饪类型信息

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "cuisineTypeId": "烹饪类型ID",
      "name": "烹饪类型名称",
      "description": "烹饪类型描述"
    },
    {
      "cuisineTypeId": "烹饪类型ID",
      "name": "烹饪类型名称",
      "description": "烹饪类型描述"
    }
  ]
}
``` 