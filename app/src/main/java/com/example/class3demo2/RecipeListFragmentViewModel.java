package com.example.class3demo2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.util.LinkedList;
import java.util.List;

public class RecipeListFragmentViewModel extends ViewModel {

    private List<Recipe> data = new LinkedList<>();
    private LiveData<List<Recipe>> liveData = Model.instance().getAllRecipesNew();

    List<Recipe> getData(){
        return data;
    }

    LiveData<List<Recipe>> getLiveData(){
        return liveData;
    }

}
