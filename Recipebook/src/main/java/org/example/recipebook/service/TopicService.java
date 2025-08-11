package org.example.recipebook.service;

import org.example.recipebook.dto.TopicCreateRequest;
import org.example.recipebook.dto.TopicDTO;
import org.example.recipebook.dto.TopicUpdateRequest;
import org.example.recipebook.entity.Topic;
import java.util.List;

public interface TopicService {
    TopicDTO getTopicById(Integer topicId);
    List<TopicDTO> getAllTopics();
    List<TopicDTO> getTopicsByUser(Integer userId);
    List<TopicDTO> searchTopics(String keyword);
    TopicDTO createTopic(TopicCreateRequest topicRequest);
    TopicDTO updateTopic(Integer topicId, TopicUpdateRequest topicRequest, Integer userId);
    void deleteTopic(Integer topicId, Integer userId);
    TopicDTO convertToDTO(Topic topic);
    List<TopicDTO> getLatestTopics(int limit);    // 评论相关
    TopicDTO addComment(Integer topicId, String content, Integer userId);
    void deleteComment(Integer commentId, Integer userId);
}
