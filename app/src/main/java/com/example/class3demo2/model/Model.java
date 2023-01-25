package com.example.class3demo2.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    public void saveLike(String namePost){
        firebaseModel.saveLike(namePost);
    }

    public void getAllLikes(Listener<List<String>> listener){
        firebaseModel.getLike(listener);
    }


    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }


    //create user
    public void createUserWithEmailAndPassword(String email, String password, Listener<Boolean> listener) {
        firebaseModel.createAccount(email,password,listener);
    }

    //login user
    public void login(String email, String password,Listener<Boolean> listener){
        firebaseModel.login(email,password,listener);
    }

    //update user
    public void updateUser(User us,Listener<Boolean> listener){
        firebaseModel.updateUser(us, listener);
    }

    //check if user login
    public void isSignedIn(Model.Listener<Boolean> listener) {
        firebaseModel.isSignedIn(listener);
    }

    //get all details user
    public void getCurrentUser(Listener<User> listener){
        firebaseModel.getCurrentUser(listener);
    }
    //logout user
    public void logout(){
        firebaseModel.logOut();
    }

    //api
    public interface TranslateAPI {
        @Headers({"content-type: application/x-www-form-urlencoded",
                "Accept-Encoding: application/gzip",
                "X-RapidAPI-Key: 1d7822b503mshee9d8ae2c3caa64p15c394jsn4a064b10b3af",
                "X-RapidAPI-Host: google-translate1.p.rapidapi.com"})
        @POST("language/translate/v2")
        @FormUrlEncoded
        Call<ResponseBody> translate(@Field("q") String q, @Field("target") String target, @Field("source") String source);
        //q = string that need to translate
        //target = language target
        //source = language source
    }


    public void restApi(String preTrans,Listener<String> listener){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://google-translate1.p.rapidapi.com/")
                .build();

        TranslateAPI api = retrofit.create(TranslateAPI.class);

        Call<ResponseBody> call = api.translate(preTrans, "he", "en");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        //convert from json to object
                        Gson gson = new Gson();
                        TranslateResponse translateResponse = gson.fromJson(responseString, TranslateResponse.class);
                        String translatedText = translateResponse.getData().getTranslations().get(0).getTranslatedText();

                        // return String
                        Log.d("res1","********" + translatedText);
                        listener.onComplete(translatedText);
                    } catch (IOException e) {
                        // handle IOException
                    }
                } else {
                    // handle error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // handle failure
            }

        });
    }

}


