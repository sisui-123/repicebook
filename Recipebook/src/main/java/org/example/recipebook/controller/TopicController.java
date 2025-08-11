package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.dto.CommentRequest;
import org.example.recipebook.dto.TopicCreateRequest;
import org.example.recipebook.dto.TopicDTO;
import org.example.recipebook.dto.TopicUpdateRequest;
import org.example.recipebook.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/{topicId}")
    public ApiResponse<TopicDTO> getTopicById(@PathVariable Integer topicId) {
        try {
            TopicDTO topicDTO = topicService.getTopicById(topicId);
            return ApiResponse.success(topicDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<TopicDTO>> getAllTopics() {
        try {
            List<TopicDTO> topics = topicService.getAllTopics();
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<TopicDTO>> getTopicsByUser(@PathVariable Integer userId) {
        try {
            List<TopicDTO> topics = topicService.getTopicsByUser(userId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<TopicDTO>> searchTopics(@RequestParam String keyword) {
        try {
            List<TopicDTO> topics = topicService.searchTopics(keyword);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public ApiResponse<List<TopicDTO>> getLatestTopics(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<TopicDTO> topics = topicService.getLatestTopics(limit);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @PostMapping
    public ApiResponse<TopicDTO> createTopic(@RequestBody TopicCreateRequest topicRequest) {
        try {
            TopicDTO topicDTO = topicService.createTopic(topicRequest);
            return ApiResponse.success("创建话题成功", topicDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @PutMapping("/{topicId}")
    public ApiResponse<TopicDTO> updateTopic(
            @PathVariable Integer topicId,
            @RequestBody TopicUpdateRequest topicRequest) {
        try {
            Integer userId = topicRequest.getUserId(); // 添加用户ID
            TopicDTO topicDTO = topicService.updateTopic(topicId, topicRequest, userId);
            return ApiResponse.success("更新话题成功", topicDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @DeleteMapping("/{topicId}")
    public ApiResponse<Void> deleteTopic(@PathVariable Integer topicId, @RequestParam Integer userId) {
        try {
            topicService.deleteTopic(topicId, userId);
            return ApiResponse.success("删除话题成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }@PostMapping("/{topicId}/comments")
    public ApiResponse<TopicDTO> addComment(
            @PathVariable Integer topicId,
            @RequestBody CommentRequest commentRequest) {
        try {
            String content = commentRequest.getContent();
            Integer userId = commentRequest.getUserId();
            // 使用路径中的topicId，不需要从请求体中再次获取
            TopicDTO topicDTO = topicService.addComment(topicId, content, userId);
            return ApiResponse.success("添加评论成功", topicDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }@DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Integer commentId, @RequestParam Integer userId) {
        try {
            topicService.deleteComment(commentId, userId);
            return ApiResponse.success("删除评论成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
