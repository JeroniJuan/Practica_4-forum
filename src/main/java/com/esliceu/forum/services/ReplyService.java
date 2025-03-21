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

    public boolean deleteById(int id) {
        if (replyRepo.existsById(id)){
            replyRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteByTopicId(int topicId) {
        List<Reply> replies = replyRepo.findByTopicId(topicId);
        for (Reply reply : replies) {
            replyRepo.deleteById(reply.getId());
        }
        return true;
    }


    public Reply findById(int replyId) {
        return replyRepo.findById(replyId).orElse(null);
    }

}
