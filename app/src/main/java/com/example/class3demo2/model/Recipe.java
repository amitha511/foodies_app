package com.example.class3demo2.model;

public class Recipe {
    public String name;
    public String id;
    public String avatarUrl;
    public Boolean cb;

    public Recipe(String name, String id, String avatarUrl, Boolean cb) {
        this.name = name;
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.cb = cb;
    }
}
