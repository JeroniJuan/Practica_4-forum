package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.PasswordForm;
import com.esliceu.forum.forms.ProfileUpdateForm;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Permission;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.CategoriesService;
import com.esliceu.forum.services.PermissionService;
import com.esliceu.forum.services.TokenService;
import com.esliceu.forum.services.UserService;
import com.esliceu.forum.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.execution.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class ProfileController {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    CategoriesService categoriesService;

    
    @GetMapping("/getprofile")
    public Map<String, Object> getProfile(HttpServletRequest req){
        Map<String, Object> resp = new HashMap<>();
        String authorizationHeader = req.getHeader("Authorization");
        if (authorizationHeader == null){
            return null;
        }
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        User user = tokenService.verifyAndGetUserFromToken(token);
        resp.put("role", user.getUserRole());
        resp.put("id", user.getId());
        resp.put("_id", user.getId());
        resp.put("__v", 0);
        resp.put("email", user.getUserEmail());
        resp.put("name", user.getName());
        resp.put("avatarUrl", user.getAvatarUrl());
        resp.put("permissions", user.getPermissions());
        return resp;
    }

    
    @PutMapping("/profile")
    public void putProfile(HttpServletRequest req, @RequestBody ProfileUpdateForm profileUpdateForm){
        String authorizationHeader = req.getHeader("Authorization");
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        String email = tokenService.verifyAndGetUserFromToken(token).getUserEmail();

        String newName = profileUpdateForm.name();
        String newEmail = profileUpdateForm.email();
        String avatar = profileUpdateForm.avatar();


        User user = userService.findByUserEmail(email);
        if (user != null) {
            user.setName(newName);
            user.setUserEmail(newEmail);

            user.setAvatarUrl("");
            userService.save(user);
        }
    }

    @PutMapping("/profile/password")
    public boolean putPassword(HttpServletRequest req, @RequestBody PasswordForm passwordForm) throws NoSuchAlgorithmException {
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);

        if (Utils.getSHA256(passwordForm.currentPassword()).equals(user.getUserPassword())){
            user.setUserPassword(Utils.getSHA256(passwordForm.newPassword()));
            userService.save(user);
            return true;
        }
        return false;
    }
}