package com.esliceu.forum.models;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_name")
    String userName;

    @Column(name = "user_password")
    String userPassword;

    @Column(name = "user_email")
    String userEmail;

    @Column(name = "user_role")
    String userRole;

    @Column(name = "moderate_Category")
    String moderateCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
}
