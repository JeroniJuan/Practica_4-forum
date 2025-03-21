package com.esliceu.forum.services;

import com.esliceu.forum.forms.LoginForm;
import com.esliceu.forum.forms.RegisterForm;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Permission;
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
    @Autowired
    PermissionService permissionService;
    @Autowired
    TokenService tokenService;
    @Autowired
    CategoriesService categoriesService;

    public boolean userAutorized(LoginForm loginForm) throws NoSuchAlgorithmException {
        User user = userRepo.findByEmail(loginForm.email());
        if (user == null) return false;
        return user.getUserPassword().equals(Utils.getSHA256(loginForm.password()));
    }

    public boolean  register(RegisterForm registerForm) throws NoSuchAlgorithmException {
        if (registerForm.email() == null) return false;
        if (userRepo.findByEmail(registerForm.email()) != null) return false;
        User user = new User();
        user.setName(registerForm.name());
        user.setEmail(registerForm.email());
        user.setUserRole(registerForm.role());
        user.setUserPassword(Utils.getSHA256(registerForm.password()));
        if (registerForm.moderateCategory() != null){
            user.setModerateCategory(registerForm.moderateCategory());
        }
        userRepo.save(user);
        Permission permission = new Permission();
        permission.setUser(userRepo.findByEmail(registerForm.email()));
        if (registerForm.role().equals("admin")){
            String[] adminPermissions = {"own_topics:write", "own_topics:delete", "own_replies:write", "own_replies:delete", "categories:write", "categories:delete"};
            permission.setRoot(adminPermissions);
            permissionService.save(permission);
        } else if (registerForm.role().equals("moderator")) {
            Category category = categoriesService.findByCategorySlug(registerForm.moderateCategory());
            int[] moderators = category.getModerators();
            int[] newModerators;
            if (moderators == null) {
                newModerators = new int[1];
                newModerators[0] = userRepo.findByEmail(user.getEmail()).getId();
            }else{
                newModerators = new int[moderators.length+1];
                System.arraycopy(moderators, 0, newModerators, 0, moderators.length);
                newModerators[newModerators.length-1] = userRepo.findByEmail(user.getEmail()).getId();
            }
            category.setModerators(newModerators);
            categoriesService.save(category);
            String[] permissions = {"own_topics:write", "own_topics:delete", "own_replies:write", "own_replies:delete"};
            permission.setRoot(permissions);
            permissionService.save(permission);
        }else{
            String[] permissions = {"own_topics:write", "own_topics:delete", "own_replies:write", "own_replies:delete"};
            permission.setRoot(permissions);
            permissionService.save(permission);
        }
        return true;
    }

    public boolean hasPermissionToCategory(User user, String categoryName) {
        if (user == null) return false;

        if ("admin".equals(user.getUserRole())) {
            return true;
        }

        if ("moderator".equals(user.getUserRole()) && categoryName.equals(user.getModerateCategory())) {
            return true;
        }

        return false;
    }


    public User findByUserEmail(String email){
        return userRepo.findByEmail(email);
    }

    public void save(User user){
        userRepo.save(user);
    }

    public User getUserByAuth(String autoritzationHeader){
        String token = tokenService.getTokenFromHeader(autoritzationHeader);
        String email = tokenService.verifyAndGetUserFromToken(token).getEmail();
        return findByUserEmail(email);
    }
}
