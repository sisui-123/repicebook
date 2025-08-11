# TopicController 接口文档

## 获取单个话题

- **接口**: GET `/api/topics/{topicId}`

- **描述**: 根据ID获取话题详细信息

- **路径参数**: 
  - topicId: 话题ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "topicId": "话题ID",
    "title": "话题标题",
    "content": "话题内容",
    "userId": "用户ID",
    "username": "用户名",
    "userAvatar": "用户头像URL",
    "createdAt": "创建时间",
    "imageUrl": "话题图片URL",
    "comments": [
      {
        "commentId": "评论ID",
        "userId": "评论用户ID",
        "username": "评论用户名称",
        "userAvatar": "评论用户头像",
        "content": "评论内容",
        "createdAt": "评论时间"
      }
    ]
  }
}
```

## 获取所有话题

- **接口**: GET `/api/topics`

- **描述**: 获取所有话题信息

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "topicId": "话题ID",
      "title": "话题标题",
      "content": "话题内容",
      "userId": "用户ID",
      "username": "用户名",
      "userAvatar": "用户头像URL",
      "createdAt": "创建时间",
      "imageUrl": "话题图片URL"
    }
  ]
}
```

## 获取用户的话题

- **接口**: GET `/api/topics/user/{userId}`

- **描述**: 获取指定用户的所有话题

- **路径参数**: 
  - userId: 用户ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "topicId": "话题ID",
      "title": "话题标题",
      "content": "话题内容",
      "userId": "用户ID",
      "username": "用户名",
      "userAvatar": "用户头像URL",
      "createdAt": "创建时间",
      "imageUrl": "话题图片URL"
    }
  ]
}
```

## 搜索话题

- **接口**: GET `/api/topics/search`

- **描述**: 根据关键词搜索话题

- **查询参数**: 
  - keyword: 搜索关键词

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "topicId": "话题ID",
      "title": "话题标题",
      "content": "话题内容",
      "userId": "用户ID",
      "username": "用户名",
      "userAvatar": "用户头像URL",
      "createdAt": "创建时间",
      "imageUrl": "话题图片URL"
    }
  ]
}
```

## 获取最新话题

- **接口**: GET `/api/topics/latest`

- **描述**: 获取最新的话题列表

- **查询参数**: 
  - limit: 返回数量，默认为10

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "topicId": "话题ID",
      "title": "话题标题",
      "content": "话题内容",
      "userId": "用户ID",
      "username": "用户名",
      "userAvatar": "用户头像URL",
      "createdAt": "创建时间",
      "imageUrl": "话题图片URL"
    }
  ]
}
```

## 创建话题

- **接口**: POST `/api/topics`

- **描述**: 创建新的话题

- **请求体**: 
```json
{
  "title": "话题标题",
  "content": "话题内容",
  "userId": "用户ID",
  "imageUrl": "话题图片URL"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "创建话题成功",
  "data": {
    "topicId": "话题ID",
    "title": "话题标题",
    "content": "话题内容",
    "userId": "用户ID",
    "username": "用户名",
    "userAvatar": "用户头像URL",
    "createdAt": "创建时间",
    "imageUrl": "话题图片URL",
    "comments": []
  }
}
```

## 更新话题

- **接口**: PUT `/api/topics/{topicId}`

- **描述**: 更新已有的话题

- **路径参数**: 
  - topicId: 话题ID

- **请求体**: 
```json
{
  "title": "话题标题",
  "content": "话题内容",
  "imageUrl": "话题图片URL",
  "userId": "用户ID"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "更新话题成功",
  "data": {
    "topicId": "话题ID",
    "title": "话题标题",
    "content": "话题内容",
    "userId": "用户ID",
    "username": "用户名",
    "userAvatar": "用户头像URL",
    "createdAt": "创建时间",
    "imageUrl": "话题图片URL",
    "comments": []
  }
}
```

## 删除话题

- **接口**: DELETE `/api/topics/{topicId}`

- **描述**: 删除指定ID的话题

- **路径参数**: 
  - topicId: 话题ID

- **查询参数**: 
  - userId: 用户ID（用于验证权限）

- **响应数据**:  
```json
{
  "success": true,
  "message": "删除话题成功",
  "data": null
}
```

## 添加评论

- **接口**: POST `/api/topics/{topicId}/comments`

- **描述**: 对话题添加新评论

- **路径参数**: 
  - topicId: 话题ID

- **请求体**: 
```json
{
  "content": "评论内容",
  "userId": "用户ID"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "添加评论成功",
  "data": {
    "topicId": "话题ID",
    "title": "话题标题",
    "content": "话题内容",
    "userId": "用户ID",
    "username": "用户名",
    "userAvatar": "用户头像URL",
    "createdAt": "创建时间",
    "imageUrl": "话题图片URL",
    "comments": [
      {
        "commentId": "评论ID",
        "userId": "评论用户ID",
        "username": "评论用户名称",
        "userAvatar": "评论用户头像",
        "content": "评论内容",
        "createdAt": "评论时间"
      }
    ]
  }
}
```

## 删除评论

- **接口**: DELETE `/api/topics/comments/{commentId}`

- **描述**: 删除指定ID的评论

- **路径参数**: 
  - commentId: 评论ID

- **查询参数**: 
  - userId: 用户ID（用于验证权限）

- **响应数据**:  
```json
{
  "success": true,
  "message": "删除评论成功",
  "data": null
}
``` 