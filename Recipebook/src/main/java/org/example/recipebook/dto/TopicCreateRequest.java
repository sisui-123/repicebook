package org.example.recipebook.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicCreateRequest {
    private String title;
    private String content;
    private Integer userId;
    private String imageUrl;
}
