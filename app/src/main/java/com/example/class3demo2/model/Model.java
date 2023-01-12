package com.example.class3demo2.model;

import android.graphics.Bitmap;
import android.net.Uri;
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
    private FirebaseModel firebaseModel = new FirebaseModel();

    public static Model instance() {
        return _instance;
    }

    private Model() {
    }

    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    public interface Listener<T> {
        void onComplete( T data);
    }

    public void getAllRecipes(Listener<List<Recipe>> callback) {
        firebaseModel.getAllRecipes(callback);
//        executor.execute(()->{
//            List<Recipe> data = localDb.recipeDao().getAll();
//            mainHandler.post(()->{
//                callback.onComplete(data);
//            });
//        });
    }



    public void addRecipe(Recipe re, Listener<Void> listener) {
        firebaseModel.addRecipe(re, listener);
//        executor.execute(()->{
//            localDb.recipeDao().insertAll(re);
//            mainHandler.post(()->{
//                listener.onComplete();
//            });
//
//        });
    }


    public void UpdateRecipe(Recipe re, Listener<Void> listener) {
        executor.execute(() -> {
            localDb.recipeDao().update(re);
            mainHandler.post(() -> {
                listener.onComplete(null);
            });
        });
    }

    public void addUser(User us, Listener<Void> listener) {
        firebaseModel.addUser(us, listener);
//        executor.execute(()->{
//            localDb.recipeDao().insertAll(re);
//            mainHandler.post(()->{
//                listener.onComplete();
//            });
//
//        });
    }


    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }


    public void createUserWithEmailAndPassword(String email, String password, Listener<Boolean> listener) {
        firebaseModel.createAccount(email,password,listener);

    }
    public void login(String email, String password,Listener<Boolean> listener){
        firebaseModel.login(email,password,listener);
    }
    public void isSignedIn(Model.Listener<Boolean> listener) {
        firebaseModel.isSignedIn(listener);
    }
    public void getCurrentUser(Listener<User> listener){
        firebaseModel.getCurrentUser(listener);
    }

    public void logout(){
        firebaseModel.logOut();
    }
}
