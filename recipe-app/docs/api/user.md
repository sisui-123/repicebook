# UserController 接口文档

## 用户注册

- **接口**: POST `/api/users/register`

- **描述**: 注册新用户

- **请求体**: 
```json
{
  "username": "用户名",
  "password": "密码",
  "confirmPassword": "确认密码"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "注册成功",
  "data": {
    "userId": "用户ID",
    "username": "用户名",
    "avatarUrl": "头像URL",
    "bio": "个人简介",
    "imageUrl": "用户图片URL"
  }
}
```

## 用户登录

- **接口**: POST `/api/users/login`

- **描述**: 用户登录

- **请求体**: 
```json
{
  "username": "用户名",
  "password": "密码"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "userId": "用户ID",
    "username": "用户名",
    "avatarUrl": "头像URL",
    "bio": "个人简介",
    "imageUrl": "用户图片URL"
  }
}
```

## 获取用户详情

- **接口**: GET `/api/users/{userId}`

- **描述**: 根据ID获取用户详细信息

- **路径参数**: 
  - userId: 用户ID

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "userId": "用户ID",
    "username": "用户名",
    "avatarUrl": "头像URL",
    "bio": "个人简介",
    "imageUrl": "用户图片URL"
  }
}
```

## 更新用户资料

- **接口**: PUT `/api/users/{userId}`

- **描述**: 更新用户个人资料

- **路径参数**: 
  - userId: 用户ID

- **请求体**: 
```json
{
  "bio": "个人简介",
  "avatarUrl": "头像URL"
}
```

- **响应数据**:  
```json
{
  "success": true,
  "message": "更新用户资料成功",
  "data": {
    "userId": "用户ID",
    "username": "用户名",
    "avatarUrl": "头像URL",
    "bio": "个人简介",
    "imageUrl": "用户图片URL"
  }
}
```

## 获取所有用户

- **接口**: GET `/api/users`

- **描述**: 获取系统中所有用户

- **请求参数**: 无

- **响应数据**:  
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "userId": "用户ID",
      "username": "用户名",
      "avatarUrl": "头像URL",
      "bio": "个人简介",
      "imageUrl": "用户图片URL"
    },
    {
      "userId": "用户ID",
      "username": "用户名",
      "avatarUrl": "头像URL",
      "bio": "个人简介",
      "imageUrl": "用户图片URL"
    }
  ]
}
```
