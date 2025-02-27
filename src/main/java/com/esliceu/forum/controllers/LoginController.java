package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.models.Permission;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.PermissionService;
import com.esliceu.forum.services.TokenService;
import com.esliceu.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginForm loginForm) throws NoSuchAlgorithmException {
        if (userService.userAutorized(loginForm)) {
            Map<String, Object> response = new HashMap<>();
            User user = userService.findByUserEmail(loginForm.email());

            Map<String, Object> permissions = new HashMap<>();
            permissions.put("root", permissionService.findByUserId(user.getId()).getRoot());

            if (!user.getModerateCategory().isEmpty()) {
                System.out.println("Insertant categories");
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

            String token = tokenService.buildToken(user);

            response.put("token", token);
            user.set_id(user.getId());
            response.put("user", user);
            return ResponseEntity.ok(response);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Incorrect email or password");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }


}
