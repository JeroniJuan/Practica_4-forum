package com.esliceu.forum.repos;

import com.esliceu.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUserEmail(String email);
}
