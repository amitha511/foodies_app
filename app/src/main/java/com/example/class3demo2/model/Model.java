package com.example.class3demo2.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Model {

    private static final Model _instance = new Model();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    public static Model instance(){
        return _instance;
    }

    private Model(){

    }

    AppLocalDbRepository localDb = AppLocalDb.getAppDb();
    public interface getAllRecipeListener{
        void onComplete(List<Recipe> data);

    }


    public void getAllRecipes(getAllRecipeListener callback ){
        executor.execute(()->{
            List<Recipe> data = localDb.recipeDao().getAll();
            mainHandler.post(()->{
                callback.onComplete(data);
            });
        });
    }

    public interface AddRecipeListener{
        void onComplete();
    }

    public void addRecipe(Recipe re, AddRecipeListener listener){
        executor.execute(()->{
            localDb.recipeDao().insertAll(re);
            mainHandler.post(()->{
                listener.onComplete();
            });

        });
    }


    public void UpdateRecipe(Recipe re,AddRecipeListener listener){
        executor.execute(()->{
            localDb.recipeDao().update(re);
            mainHandler.post(()->{
                listener.onComplete();
            });
        });
    }




}
