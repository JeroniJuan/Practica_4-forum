package com.esliceu.forum.services;

import com.esliceu.forum.models.Reply;
import com.esliceu.forum.repos.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    @Autowired
    private ReplyRepo replyRepo;

    public void save(Reply reply) {
        replyRepo.save(reply);
    }

    public List<Reply> findByTopicId(int topicId) {
        return replyRepo.findByTopicId(topicId);
    }

    public Reply findByLatest() {
        return replyRepo.findFirstByOrderByCreatedAtDesc();
    }
}
