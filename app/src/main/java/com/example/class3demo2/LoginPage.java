package com.example.class3demo2;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {
    Boolean isAvatarSelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Foodies");

        //check if the user connect
        Model.instance().isSignedIn(status ->{
            if(status == Boolean.TRUE){
                Model.instance().logout();
            }
        });


        //**************************************login details***************************:
        setContentView(R.layout.fragment_welcome_page);

        //binding:
        Button login = findViewById(R.id.login);
        TextInputEditText emailEt = findViewById(R.id.emailEt);
        TextInputEditText passEt = findViewById(R.id.passEt);
        TextView error = findViewById(R.id.wrong1);

        //*****************************************************************:

        //***********************************scroll down to register *************************:
        //binding:
        ImageView scrollBtn = findViewById(R.id.scroll);
        ScrollView scroll = findViewById(R.id.scrollWelcome);

        scrollBtn.setOnClickListener(view ->{
            scroll.fullScroll(scroll.FOCUS_DOWN);
        });

        //*****************************************************************:


        //**************************************register details***************************:
        ActivityResultLauncher<Void> cameraLauncher;
        ActivityResultLauncher<String> galleryAppLauncher;

        //binding :
        ImageButton camerabutton = findViewById(R.id.camerabutton);
        ImageButton gallerybutton = findViewById(R.id.gallerybutton);
        ImageView avatarRegister = findViewById(R.id.avatarImg2);
        TextInputEditText firstName = findViewById(R.id.first_name); ;
        TextInputEditText lastName = findViewById(R.id.last_name);;
        TextInputEditText emailRegister = findViewById(R.id.email1);;
        TextInputEditText passwordRegister = findViewById(R.id.password1);;


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
                new ActivityResultCallback<Bitmap>() {
                    @Override
                    public void onActivityResult(Bitmap result) {
                        if (result != null) {
                            avatarRegister.setImageBitmap(result);
                            isAvatarSelected = true;
                        }
                    }
                });

        galleryAppLauncher = registerForActivityResult(new
                ActivityResultContracts.GetContent(), new
                ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            avatarRegister.setImageURI(result);
                            isAvatarSelected = true;
                        }
                    }
                });
//*****************************************************************************




//******************************** login to user ******************************************

        login.setOnClickListener(view1->{
            //save text
            String email = emailEt.getText().toString();
            String password = passEt.getText().toString();

            //check the filed input
            if(email.isEmpty() || password.isEmpty()){
                TextInputEditText em = emailEt;
                em.setError("This field cannot be empty");
                TextInputEditText pas = passEt;
                pas.setError("This field cannot be empty");

            }
            else {  //if filed not empty
                //login to the user:
                Model.instance().login(email, password, status -> {
                    if (status) {
                        homePage();

                    } else {
                        error.setText("email or password wrong");

                    }
                });
            }
        });

//*****************************************************************************


//******************************** create new user - register ******************************************
        //binding
        Button saveBtn = findViewById(R.id.saveBtnUser);

        //create user + save in firebase + connect to the new user
        saveBtn.setOnClickListener(view1 -> {
                //check the filed input
            if(!checkInput(emailRegister,passwordRegister,firstName,lastName)){
                // save the user in firebase auth and connect
                Model.instance().createUserWithEmailAndPassword(emailRegister.getText().toString(), passwordRegister.getText().toString(), status->{

                    if(status){   //if connect is success
                        //create user with details
                        User us = new User(firstName.getText().toString(),lastName.getText().toString(), emailRegister.getText().toString(),"");
                        //save image user
                        if(isAvatarSelected){
                            avatarRegister.setDrawingCacheEnabled(true);
                            avatarRegister.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) avatarRegister.getDrawable()).getBitmap();

                            //save in firebase store the image and return url
                            Model.instance().uploadImage(emailRegister.getText().toString(),bitmap, url->{
                                if(url != null){
                                    us.setAvatarUrl(url);
                                }
                                //save the details of the user in firebase
                                Model.instance().addUser(us,(unused)->{
                                    //go to main activity and finish login activity
                                    Intent i = new Intent(LoginPage.this, MainActivity.class);
                                    startActivity(i);
                                    //finish login activity
                                    finish();
                                });
                            });
                        }
                        //photo is empty
                        else {
                            //save the user in firebase
                            Model.instance().addUser(us, (unused) -> {
                                //go to main activity and finish login activity
                                homePage();
                            });
                        }
                    }

                });
            }
        });


        camerabutton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        gallerybutton.setOnClickListener(view1->{
            galleryAppLauncher.launch("image/*");
        });

    }

//*****************************************************************************



//********************************check the field input***********
    public boolean checkInput(TextInputEditText email,TextInputEditText password,TextInputEditText firstName,TextInputEditText lastName){
        Boolean bool =false;
        if(email.getText().toString().isEmpty()) {
            TextInputEditText input = email;
            input.setError("This field cannot be empty");
            bool=true;
        }
        if(password.getText().toString().isEmpty()) {
            TextInputEditText input = password;
            input.setError("This field cannot be empty");
            bool=true;

        }
        if(firstName.getText().toString().isEmpty()) {
            TextView input = firstName;
            input.setError("This field cannot be empty");
            bool=true;

        }
        if(lastName.getText().toString().isEmpty()) {
            TextView input = lastName;
            input.setError("This field cannot be empty");
            bool=true;

        }

        return bool;
    }

    //*****************************************************************************


    //go to main activity and finish login activity
    public void homePage(){
        Intent i = new Intent(LoginPage.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    

}