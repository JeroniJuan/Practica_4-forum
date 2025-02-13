package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.ProfileUpdateForm;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.PermissionService;
import com.esliceu.forum.services.TokenService;
import com.esliceu.forum.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ProfileController {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @CrossOrigin
    @GetMapping("/getprofile")
    public Map<String, Object> getProfile(HttpServletRequest req){
        Map<String, Object> resp = new HashMap<>();
        String authorizationHeader = req.getHeader("Authorization");
        if (authorizationHeader == null){
            return null;
        }
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        String email = tokenService.verifyAndGetEmailFromToken(token);
        User user = userService.findByUserEmail(email);
        resp.put("role", user.getUserRole());
        resp.put("id", user.getId());
        resp.put("email", user.getUserEmail());
        resp.put("name", user.getUserName());
        resp.put("avatarUrl", user.getAvatarUrl());
        resp.put("permissions", permissionService.findByUserId(user.getId()));
        return resp;
    }

    @CrossOrigin
    @PutMapping("/profile")
    public void putProfile(HttpServletRequest req, @RequestBody ProfileUpdateForm profileUpdateForm){
        String authorizationHeader = req.getHeader("Authorization");
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        String email = tokenService.verifyAndGetEmailFromToken(token);

        String newName = profileUpdateForm.name();
        String newEmail = profileUpdateForm.email();
        String avatar = profileUpdateForm.avatar();

        User user = userService.findByUserEmail(email);
        if (user != null) {
            user.setUserName(newName);
            user.setUserEmail(newEmail);

            user.setAvatarUrl("");
            userService.save(user);
        }
    }
}