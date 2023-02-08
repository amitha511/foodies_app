package com.example.class3demo2.model;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.class3demo2.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

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
    public String author = "";
    public Long lastUpdated; // need to add in firebase

    public Recipe(){}

    public Recipe(String name, String id, String avatarUrl, Boolean cb, String instructions, String ingredients, String author) {
        this.name = name;
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.cb = cb;
        this.ingredients=ingredients;
        this.instructions=instructions;
        this.author = author;
    }

    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "recipes_local_last_update";

    public static Recipe fromJson(Map<String,Object> json){
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        String avatarUrl = (String)json.get("avatarUrl");
        String instructions =(String) json.get("instructions");
        String ingredients = (String) json.get("ingredients");
        String author = (String) json.get("author");
        Boolean cb = (Boolean)json.get("cb");
        Recipe re = new Recipe(name, id, avatarUrl, cb, instructions, ingredients, author);

        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED) ;
            re.setLastUpdated(time.getSeconds());
        }catch (Exception e){

        }

        return re;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
    }


    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("id", getId());
        json.put("name", getName());
        json.put("avatarUrl", getAvatarUrl());
        json.put("instructions", getInstructions());
        json.put("ingredients",getIngredients());
        json.put("author",getAuthor());
        json.put("cb", getCb());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }


    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
