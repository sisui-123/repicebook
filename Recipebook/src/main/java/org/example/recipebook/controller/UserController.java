package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.dto.UserDTO;
import org.example.recipebook.dto.UserLoginRequest;
import org.example.recipebook.dto.UserProfileUpdateRequest;
import org.example.recipebook.dto.UserRegisterRequest;
import org.example.recipebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody UserRegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();
        
        if (!password.equals(confirmPassword)) {
            return ApiResponse.error("两次输入的密码不一致");
        }
        
        try {
            UserDTO userDTO = userService.register(username, password);
            return ApiResponse.success("注册成功", userDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ApiResponse<UserDTO> login(@RequestBody UserLoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        
        try {
            UserDTO userDTO = userService.login(username, password);
            return ApiResponse.success("登录成功", userDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Integer userId) {
        try {
            UserDTO userDTO = userService.getUserById(userId);
            return ApiResponse.success(userDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @PutMapping("/{userId}")
    public ApiResponse<UserDTO> updateUserProfile(
            @PathVariable Integer userId,
            @RequestBody UserProfileUpdateRequest request) {
        String bio = request.getBio();
        String avatarUrl = request.getAvatarUrl();
        
        try {
            UserDTO userDTO = userService.updateUserProfile(userId, bio, avatarUrl);
            return ApiResponse.success("更新用户资料成功", userDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
