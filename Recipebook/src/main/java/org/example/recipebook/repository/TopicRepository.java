package org.example.recipebook.repository;

import org.example.recipebook.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByUserUserId(Integer userId);
    
    @Query("SELECT t FROM Topic t ORDER BY t.createdAt DESC")
    List<Topic> findLatestTopics();
    
    List<Topic> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
}
