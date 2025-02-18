package com.esliceu.forum.repos;

import com.esliceu.forum.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepo extends JpaRepository<Reply, Integer> {
    List<Reply> findByTopicId(int topicId);

    Reply findFirstByOrderByCreatedAtDesc();
}
