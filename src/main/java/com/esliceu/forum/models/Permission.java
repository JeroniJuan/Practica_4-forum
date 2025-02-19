package com.esliceu.forum.models;

import com.google.gson.Gson;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "root", columnDefinition = "JSON")
    String root;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setRoot(String[] permissions) {
        this.root = new Gson().toJson(permissions);
    }

    public String[] getRoot() {
        return new Gson().fromJson(this.root, String[].class);
    }
}
