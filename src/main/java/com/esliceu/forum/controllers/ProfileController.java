package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.PasswordForm;
import com.esliceu.forum.forms.ProfileUpdateForm;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.CategoriesService;
import com.esliceu.forum.services.PermissionService;
import com.esliceu.forum.services.TokenService;
import com.esliceu.forum.services.UserService;
import com.esliceu.forum.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        resp.put("email", user.getEmail());
        resp.put("name", user.getName());
        resp.put("avatarUrl", user.getAvatarUrl());
        resp.put("permissions", user.getPermissions());
        return resp;
    }

    @PutMapping("/profile")
    public Map<String, Object> putProfile(HttpServletRequest req, @RequestBody ProfileUpdateForm profileUpdateForm){
        String authorizationHeader = req.getHeader("Authorization");
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        String email = tokenService.verifyAndGetUserFromToken(token).getEmail();
        Map<String, Object> resposta = new HashMap<>();
        String newName = profileUpdateForm.name();
        String newEmail = profileUpdateForm.email();

        User user = userService.findByUserEmail(email);
        if (user != null) {
            user.setName(newName);
            if (userService.findByUserEmail(newEmail) != null){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email already exists");
            }
            user.setEmail(newEmail);

            userService.save(user);
        }

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("root", permissionService.findByUserId(user.getId()).getRoot());

        if (!user.getModerateCategory().isEmpty()) {
            Map<String, String[]> categories = new HashMap<>();
            categories.put(user.getModerateCategory(), new String[]{
                    "categories_topics:write",
                    "categories_topics:delete",
                    "categories_replies:write",
                    "categories_replies:delete"
            });
            permissions.put("categories", categories);
        } else {
            permissions.put("categories", new String[0]);
        }
        user.setPermissions(permissions);

        token = tokenService.buildToken(user);

        resposta.put("user", user);
        resposta.put("token", token);

        return resposta;
    }

    @PutMapping("/profile/password")
    public ResponseEntity<Map<String, String>> putPassword(HttpServletRequest req, @RequestBody PasswordForm passwordForm) throws NoSuchAlgorithmException {
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);

        if (Utils.getSHA256(passwordForm.currentPassword()).equals(user.getUserPassword())){
            user.setUserPassword(Utils.getSHA256(passwordForm.newPassword()));
            userService.save(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "true"));
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Your current password is wrong!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


}