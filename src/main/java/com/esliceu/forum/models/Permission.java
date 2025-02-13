package com.esliceu.forum.models;

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

    @Column(name = "root")
    String[] root;

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


    public String[] getRoot() {
        return root;
    }

    public void setRoot(String[] root) {
        this.root = root;
    }
}
