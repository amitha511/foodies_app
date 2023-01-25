package com.example.class3demo2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentAddUserBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterUserFragment extends Fragment {

    FragmentAddUserBinding binding;
    Boolean isAvatarSelected = false;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    String firstName ;
    String lastName;
    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
                    new ActivityResultCallback<Bitmap>() {
                        @Override
                        public void onActivityResult(Bitmap result) {
                            if (result != null) {
                                binding.avatarImg2.setImageBitmap(result);
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
                                binding.avatarImg2.setImageURI(result);
                                isAvatarSelected = true;
                            }
                        }
                    });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        binding.saveBtnUser.setOnClickListener(view1 -> {
             firstName = binding.firstName.getText().toString();
             lastName = binding.lastName.getText().toString();
             email = binding.email.getText().toString();
             password = binding.password1.getText().toString();
            if(!checkInput()){

                Model.instance().createUserWithEmailAndPassword(email, password, status->{
                    if(status){
                        User us = new User(firstName,lastName,email,"");

                        if(isAvatarSelected){
                            binding.avatarImg2.setDrawingCacheEnabled(true);
                            binding.avatarImg2.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) binding.avatarImg2.getDrawable()).getBitmap();
                            Model.instance().uploadImage(email,bitmap,url->{
                                if(url != null){
                                    us.setAvatarUrl(url);
                                }
                                Model.instance().addUser(us,(unused)->{
                                    homePage(binding.getRoot());
                                });
                            });
                        }
                        else {
                            Model.instance().addUser(us, (unused) -> {
                                homePage(binding.getRoot());
                            });
                        }
                    }
                    else{
//                        homePage(binding.getRoot());
                    }
                });
            }
        });


        binding.camerabutton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.gallerybutton.setOnClickListener(view1->{
            galleryAppLauncher.launch("image/*");
        });

        return view;

    }


    public boolean checkInput(){
        Boolean bool =false;
        if(email.isEmpty()) {
            TextInputEditText input = binding.email;
            input.setError("This field cannot be empty");
            bool=true;
        }
        if(password.isEmpty()) {
            TextInputEditText input = binding.password1;
            input.setError("This field cannot be empty");
            bool=true;

        }
        if(firstName.isEmpty()) {
            TextInputEditText input = binding.firstName;
            input.setError("This field cannot be empty");
            bool=true;

        }
        if(lastName.isEmpty()) {
            TextInputEditText input = binding.lastName;
            input.setError("This field cannot be empty");
            bool=true;

        }

        return bool;
    }



    public void homePage(View view){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_navhost, RecipesListFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();
    }
}