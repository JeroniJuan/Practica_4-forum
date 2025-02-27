package com.esliceu.forum.models;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Transient
    int _id;

    @Column(name = "user_name")
    String name;

    @Column(name = "user_password")
    String userPassword;

    @Column(name = "user_email")
    String email;

    @Column(name = "user_role")
    String userRole;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "moderate_Category")
    String moderateCategory;

    @Transient
    private Map<String, Object> permissions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getModerateCategory() {
        return moderateCategory;
    }

    public void setModerateCategory(String moderateCategory) {
        this.moderateCategory = moderateCategory;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Map<String, Object> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Object> permissions) {
        this.permissions = permissions;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
