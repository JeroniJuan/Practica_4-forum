package com.esliceu.forum.repos;

import com.esliceu.forum.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permission, Integer> {

    Permission findByUserId(int id);
}
