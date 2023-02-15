package com.example.class3demo2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.util.LinkedList;
import java.util.List;

//cache
public class RecipeListFragmentViewModel extends ViewModel {

    private List<Recipe> data = new LinkedList<>();  //list recipes - for display data that not in cache (likes, user recipes)

    private LiveData<List<Recipe>> liveData = Model.instance().getAllRecipesNew(); //cache recipes = livedata

    List<Recipe> getData(){
        return data;
    }

    LiveData<List<Recipe>> getLiveData(){
        return liveData;
    }

}
