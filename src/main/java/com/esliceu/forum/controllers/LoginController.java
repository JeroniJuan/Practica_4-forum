package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.models.User;
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

    @CrossOrigin
    @PostMapping("/login")
    Map<String, Object> login(@RequestBody LoginForm loginForm) throws NoSuchAlgorithmException {
        if (userService.userAutorized(loginForm)){
            Map<String, Object> response = new HashMap<>();
            User user = userService.findByUserEmail(loginForm.email());

            String token = tokenService.buildToken(loginForm.email());
            response.put("user", user);
            response.put("token", token);
            return response;
        }
        System.out.println("False");
        return null;
    }

}
