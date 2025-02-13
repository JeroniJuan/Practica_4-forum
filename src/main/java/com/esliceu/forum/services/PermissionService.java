package com.esliceu.forum.services;

import com.esliceu.forum.models.Permission;
import com.esliceu.forum.repos.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    PermissionRepo permissionRepo;
    public void save(Permission permission) {
        permissionRepo.save(permission);
    }

    public Permission findByUserId(int id) {
        return permissionRepo.findByUserId(id);
    }
}
