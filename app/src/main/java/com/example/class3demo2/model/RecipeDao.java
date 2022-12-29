package com.example.class3demo2.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

        @Query("select * from Recipe")
        List<Recipe> getAll();

        @Query("select * from Recipe where id = :recipeId")
        Recipe getRecipeById(String recipeId);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(Recipe... recipes);

        @Delete
        void delete(Recipe recipe);
}
