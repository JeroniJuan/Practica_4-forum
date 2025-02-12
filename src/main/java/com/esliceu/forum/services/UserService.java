package com.esliceu.forum.services;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.forms.RegisterForm;
import com.esliceu.forum.models.User;
import com.esliceu.forum.repos.UserRepo;
import com.esliceu.forum.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public boolean userAutorized(LoginForm loginForm) throws NoSuchAlgorithmException {
        User user = userRepo.findByUserEmail(loginForm.email());
        return user.getUserPassword().equals(Utils.getSHA256(loginForm.password()));
    }

    public boolean register(RegisterForm registerForm) throws NoSuchAlgorithmException {
        User user = new User();
        user.setUserName(registerForm.name());
        user.setUserEmail(registerForm.email());
        user.setUserRole(registerForm.role());
        user.setUserPassword(Utils.getSHA256(registerForm.password()));
        if (registerForm.moderateCategory() != null){
            user.setModerateCategory(registerForm.moderateCategory());
        }
        userRepo.save(user);
        return true;
    }
    public User findByUserEmail(String email){
        return userRepo.findByUserEmail(email);
    }
}
