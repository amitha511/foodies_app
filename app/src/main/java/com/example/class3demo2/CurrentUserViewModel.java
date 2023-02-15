package com.example.class3demo2;

import androidx.lifecycle.ViewModel;

import com.example.class3demo2.model.Recipe;
import com.example.class3demo2.model.User;

import java.util.LinkedList;
import java.util.List;


// details of user connected
public class CurrentUserViewModel extends ViewModel {

    private static User user = null;

    public void setUser(User user1) {
        user = user1;
    }

    public User getUser() {
        return user;
    }
}
