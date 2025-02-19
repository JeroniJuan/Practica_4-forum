package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.models.Permission;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.PermissionService;
import com.esliceu.forum.services.TokenService;
import com.esliceu.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @CrossOrigin
    @PostMapping("/login")
    Map<String, Object> login(@RequestBody LoginForm loginForm) throws NoSuchAlgorithmException {
        if (userService.userAutorized(loginForm)){
            Map<String, Object> response = new HashMap<>();
            User user = userService.findByUserEmail(loginForm.email());

            String token = tokenService.buildToken(loginForm.email());
            Permission permission = permissionService.findByUserId(userService.findByUserEmail(user.getUserEmail()).getId());
            try{
                user.setPermissions(permission.getRoot());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (user.getPermissions() == null){
                String[] perms = new String[0];
                user.setPermissions(perms);
            }
            response.put("user", user);
            response.put("token", token);
            return response;
        }
        System.out.println("False");
        return null;
    }

}
