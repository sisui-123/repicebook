package org.example.recipebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Integer topicId;
    private String title;
    private String content;
    private Integer userId;
    private String username;
    private String userAvatar;
    private LocalDateTime createdAt;
    private String imageUrl;
    private List<CommentDTO> comments;
}
