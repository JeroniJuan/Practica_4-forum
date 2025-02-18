package com.esliceu.forum.services;

import com.esliceu.forum.models.Topic;
import com.esliceu.forum.repos.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepo topicRepo;

    public List<Topic> findTopicsByCategory(String categoryTitle){
        List<Topic> topicList = topicRepo.findByCategoryTitle(categoryTitle);
        for (Topic topic : topicList) {
            topic.set_id(topic.getId());
        }
        return topicList;
    }

    public void save(Topic topic) {
        topicRepo.save(topic);
    }

    public Topic findByTopicId(int topicId) {
        Topic topic = topicRepo.findById(topicId).orElse(null);
        topic.set_id(topicId);
        return topic;
    }

    public Topic findByLatest() {
        return topicRepo.findFirstByOrderByCreatedAtDesc();
    }
}
