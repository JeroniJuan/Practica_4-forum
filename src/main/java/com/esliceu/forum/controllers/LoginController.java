package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    Map<String, String> login(@RequestBody LoginForm loginForm){
        if (userAutorized(loginForm)){
            Map<String, String> map = new HashMap<>();
            map.put("token", tokenService.buildToken("bill@microsoft.com"));
            return map;
        }
        return null;
    }
    @CrossOrigin
    @GetMapping("/private/test")
    public Map<String, String> doPrivate(@RequestAttribute String email){
        Map<String, String> map = new HashMap<>();
        map.put("user", email);
        map.put("message", "This is a secret message");
        return map;
    }



    private boolean userAutorized(LoginForm loginForm){
        return (loginForm.email().equals("bill@microsoft.com") && loginForm.password().equals("1234"));
    }
}
