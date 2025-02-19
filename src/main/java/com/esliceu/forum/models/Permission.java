package com.esliceu.forum.models;

import com.google.gson.Gson;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Transient
    Map<String, String[]> categories = new HashMap<>();

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

    public Map<String, String[]> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String[]> categories) {
        this.categories = categories;
    }
}
