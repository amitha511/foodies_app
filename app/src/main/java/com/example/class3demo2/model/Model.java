package com.example.class3demo2.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    private static final Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }
    private Model(){
        for(int i=0;i<20;i++){
            addRecipe(new Recipe("שם מתכון " + i,""+i,"",false));
        }
    }

    List<Recipe> data = new LinkedList<>();
    public List<Recipe> getAllRecipes(){
        return data;
    }

    public void addRecipe(Recipe re){
        data.add(re);
    }


}
