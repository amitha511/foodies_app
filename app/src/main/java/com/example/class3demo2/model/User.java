package com.example.class3demo2.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String firstName="";
    public String lastName="";
    public String email ="";
    public String avatarUrl="";


    public User(String firstName, String lastName, String email,String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }


    public static User fromJson(Map<String,Object> json){
        String firstName = (String) json.get("firstName");
        String LastName =(String) json.get("lastName");
        String email =(String) json.get("email");
        String avatarUrl = (String)json.get("avatarUrl");
        User us= new User(firstName,LastName,email,avatarUrl);
        return us;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put("firstName", getFirstName());
        json.put("lastName", getLastName());
        json.put("email", getEmail());
        json.put("avatarUrl", getAvatarUrl());
        return json;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
