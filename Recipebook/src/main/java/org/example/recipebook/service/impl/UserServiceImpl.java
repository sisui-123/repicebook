package org.example.recipebook.service.impl;

import org.example.recipebook.dto.UserDTO;
import org.example.recipebook.entity.User;
import org.example.recipebook.repository.UserRepository;
import org.example.recipebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    // 模拟当前登录用户
    private static Integer currentUserId;

    @Override
    @Transactional
    public UserDTO register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        // 模拟登录
        currentUserId = savedUser.getUserId();
        
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 模拟登录
        currentUserId = user.getUserId();
        
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(Integer userId, String bio, String avatarUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setBio(bio);
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            user.setAvatarUrl(avatarUrl);
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setImageUrl(user.getImageUrl());
        
        return dto;
    }

    @Override
    public User getCurrentUser() {
        if (currentUserId == null) {
            throw new RuntimeException("未登录");
        }
        
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("当前用户不存在"));
    }
}
