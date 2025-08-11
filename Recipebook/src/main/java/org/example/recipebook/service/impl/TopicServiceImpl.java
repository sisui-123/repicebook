package org.example.recipebook.service.impl;

import org.example.recipebook.dto.CommentDTO;
import org.example.recipebook.dto.TopicCreateRequest;
import org.example.recipebook.dto.TopicDTO;
import org.example.recipebook.dto.TopicUpdateRequest;
import org.example.recipebook.entity.Comment;
import org.example.recipebook.entity.Topic;
import org.example.recipebook.entity.User;
import org.example.recipebook.repository.CommentRepository;
import org.example.recipebook.repository.TopicRepository;
import org.example.recipebook.repository.UserRepository;
import org.example.recipebook.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {    @Autowired
    private TopicRepository topicRepository;
      @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public TopicDTO getTopicById(Integer topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("话题不存在"));
        
        return convertToDTO(topic);
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopicDTO> getTopicsByUser(Integer userId) {
        return topicRepository.findByUserUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopicDTO> searchTopics(String keyword) {
        return topicRepository.findByTitleContainingOrContentContaining(keyword, keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }    @Override
    @Transactional
    public TopicDTO createTopic(TopicCreateRequest topicRequest) {
        Integer userId = topicRequest.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Topic topic = new Topic();
        topic.setUser(user);
        topic.setTitle(topicRequest.getTitle());
        topic.setContent(topicRequest.getContent());
        topic.setImageUrl(topicRequest.getImageUrl());
        topic.setCreatedAt(LocalDateTime.now());
        
        Topic savedTopic = topicRepository.save(topic);
        return convertToDTO(savedTopic);
    }    @Override
    @Transactional
    public TopicDTO updateTopic(Integer topicId, TopicUpdateRequest topicRequest, Integer userId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("话题不存在"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!topic.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("只能修改自己的话题");
        }
        
        topic.setTitle(topicRequest.getTitle());
        topic.setContent(topicRequest.getContent());
        
        String imageUrl = topicRequest.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            topic.setImageUrl(imageUrl);
        }
        
        Topic updatedTopic = topicRepository.save(topic);
        return convertToDTO(updatedTopic);
    }    @Override
    @Transactional
    public void deleteTopic(Integer topicId, Integer userId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("话题不存在"));
        
        // 检查用户是否存在
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!topic.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("只能删除自己的话题");
        }
        
        // 删除话题相关的评论
        commentRepository.deleteAll(commentRepository.findByTopicTopicId(topicId));
        
        // 删除话题
        topicRepository.delete(topic);
    }

    @Override
    public TopicDTO convertToDTO(Topic topic) {
        if (topic == null) {
            return null;
        }
        
        TopicDTO dto = new TopicDTO();
        dto.setTopicId(topic.getTopicId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setUserId(topic.getUser().getUserId());
        dto.setUsername(topic.getUser().getUsername());
        dto.setUserAvatar(topic.getUser().getAvatarUrl());
        dto.setCreatedAt(topic.getCreatedAt());
        dto.setImageUrl(topic.getImageUrl());
          // 获取评论
        List<CommentDTO> commentDTOs = commentRepository.findByTopicTopicId(topic.getTopicId())
                .stream()
                .map(comment -> new CommentDTO(
                        comment.getCommentId(),
                        comment.getUser().getUserId(),
                        comment.getUser().getUsername(),
                        comment.getUser().getAvatarUrl(),
                        comment.getContent(),
                        comment.getCreatedAt()))
                .collect(Collectors.toList());
        dto.setComments(commentDTOs);
        
        return dto;
    }

    @Override
    public List<TopicDTO> getLatestTopics(int limit) {
        return topicRepository.findLatestTopics().stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }    @Override
    @Transactional
    public TopicDTO addComment(Integer topicId, String content, Integer userId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("话题不存在"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Comment comment = new Comment();
        comment.setTopic(topic);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        
        commentRepository.save(comment);
        
        return getTopicById(topicId);
    }    @Override
    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        // 检查用户是否存在
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("只能删除自己的评论");
        }
        
        commentRepository.delete(comment);
    }
}
