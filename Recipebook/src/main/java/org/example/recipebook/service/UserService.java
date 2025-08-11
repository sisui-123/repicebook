package org.example.recipebook.service;

import org.example.recipebook.dto.UserDTO;
import org.example.recipebook.entity.User;
import java.util.List;

public interface UserService {
    UserDTO register(String username, String password);
    UserDTO login(String username, String password);
    UserDTO getUserById(Integer userId);
    UserDTO updateUserProfile(Integer userId, String bio, String avatarUrl);
    List<UserDTO> getAllUsers();
    UserDTO convertToDTO(User user);
    User getCurrentUser();
}
