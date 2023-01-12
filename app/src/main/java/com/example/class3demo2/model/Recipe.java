package com.example.class3demo2.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;


@Entity
public class Recipe {
    @PrimaryKey
    @NonNull
    public String name="";
    public String id="";
    public String avatarUrl="";
    public Boolean cb=false;
    public String instructions="";
    public String ingredients="";

    public Recipe(){}

    public Recipe(String name, String id, String avatarUrl, Boolean cb,String instructions,String ingredients) {
        this.name = name;
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.cb = cb;
        this.ingredients=ingredients;
        this.instructions=instructions;
    }

    public static Recipe fromJson(Map<String,Object> json){
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String avatarUrl = (String)json.get("avatarUrl");
        String instructions =(String) json.get("instructions");
        String ingredients = (String) json.get("ingredients");
        Boolean cb = (Boolean)json.get("cb");
        Recipe re= new Recipe(name,id,avatarUrl,cb,instructions,ingredients);

        return re;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("id", getId());
        json.put("name", getName());
        json.put("avatarUrl", getAvatarUrl());
        json.put("instructions", getInstructions());
        json.put("ingredients",getIngredients());
        json.put("cb", getCb());
        return json;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Boolean getCb() {
        return cb;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCb(Boolean cb) {
        this.cb = cb;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getIngredients() {
        return ingredients;
    }


    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
