package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.RegisterForm;
import com.esliceu.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("/register")
    Map<String, String> register(@RequestBody RegisterForm registerForm) throws NoSuchAlgorithmException {
        if (userService.register(registerForm)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "done");
            return response;
        }
        return null;
    }
}
