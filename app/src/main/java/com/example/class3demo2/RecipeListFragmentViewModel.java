package com.example.class3demo2;

import androidx.lifecycle.ViewModel;

import com.example.class3demo2.model.Recipe;

import java.util.LinkedList;
import java.util.List;

public class RecipeListFragmentViewModel extends ViewModel {

    private List<Recipe> data = new LinkedList<>();

    List<Recipe> getData(){
        return data;
    }

    void setData(List<Recipe> list){
        data = list;
    }
}
