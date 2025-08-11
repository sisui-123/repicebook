package org.example.recipebook.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpdateRequest {
    private String title;
    private String content;
    private String imageUrl;
    private Integer userId;
}
