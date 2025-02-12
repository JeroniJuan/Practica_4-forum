package com.esliceu.forum.controllers;

import com.esliceu.forum.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {
    @CrossOrigin
    @GetMapping("/getProfile")
    public Map<String, String> getProfile(@RequestAttribute String email){
        return null;
    }
}
