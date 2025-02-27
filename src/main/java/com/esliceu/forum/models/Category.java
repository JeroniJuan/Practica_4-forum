package com.esliceu.forum.models;

import com.google.gson.Gson;
import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;

    @Transient
    int _id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "slug")
    String slug;

    @Column(name = "color")
    String color;

    String moderators;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getModerators() {
        return new Gson().fromJson(this.moderators, int[].class);
    }

    public void setModerators(int[] moderators) {
        this.moderators = new Gson().toJson(moderators);
    }

    public void setModerators(String moderators) {
        this.moderators = moderators;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
