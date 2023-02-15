package com.example.class3demo2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class Model {

    private static final Model _instance = new Model();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();


    public static Model instance() {
        return _instance;
    }

    private Model() {
    }

    public interface Listener<T> {
        void onComplete( T data);
    }


    public void getAllRecipes(Listener<List<Recipe>> callback) {
         firebaseModel.getAllRecipes(callback);

    }
                //******************** for cache*********************
    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }

    final public MutableLiveData<LoadingState> EventRecipesListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);
//
//
    private LiveData<List<Recipe>> studentList;
    public LiveData<List<Recipe>> getAllRecipesNew() { // for cache
        if(studentList == null){
            studentList = localDb.recipeDao().getAll();
            refreshAllRecipes();

        }
        return studentList;
    }
    public void refreshAllRecipes(){
        EventRecipesListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = Recipe.getLocalLastUpdate();

        // get all updated recorde from firebase since local last update
        firebaseModel.getAllRecipesSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Recipe re:list){
                    // insert new records into ROOM
                    localDb.recipeDao().insertAll(re);
                    if (time < re.getLastUpdated()){
                        time = re.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(0000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Recipe.setLocalLastUpdate(time);
                EventRecipesListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }



    public void addRecipe(Recipe re, Listener<Void> listener) {
        firebaseModel.addRecipe(re, listener);
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

    }
    public void saveLike(String namePost){
        firebaseModel.saveLike(namePost);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                "X-RapidAPI-Key: e175984e71msh6747f7d1396e2c2p128431jsn185b4b5207f7",
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
                        String translatedText = translateResponse.getData().getTranslations().get(0).getTranslatedText(); // convert to string
                        // return String
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


