package com.esliceu.forum.repos;

import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepo extends JpaRepository<Topic, Integer> {
    List<Topic> findByCategoryTitle(String categoryTitle);

    Topic findFirstByOrderByCreatedAtDesc();

    Category findByTitle(String categoryName);
}