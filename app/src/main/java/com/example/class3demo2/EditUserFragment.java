package com.example.class3demo2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentEditUserBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;


//edit details profile of user (first name, last name, photo)
public class EditUserFragment extends Fragment {
    FragmentEditUserBinding binding;
    String firstName;
    String lastName;
    String email;
    String avatarUrl;
    Boolean isAvatarSelected = false;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditUserBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //set from argument fragment to Strings;
        firstName = EditUserFragmentArgs.fromBundle(getArguments()).getFirstName();
        lastName = EditUserFragmentArgs.fromBundle(getArguments()).getLastName();
        email = EditUserFragmentArgs.fromBundle(getArguments()).getEmail();
        avatarUrl = EditUserFragmentArgs.fromBundle(getArguments()).getAvatarUrl();

        // set data user in the ui
        if (firstName != null){
            binding.firstName.setText(firstName);
        }
        if (lastName != null){
            binding.lastName.setText(lastName);
        }
        if (email != null){
            binding.email.setText(email);
        }
        if (avatarUrl != ""){
            Picasso.get().load(avatarUrl).error(R.drawable.errorpizza).into(binding.avatarImg2);
        }else{
            binding.avatarImg2.setImageResource(R.drawable.user);
        }

        binding.saveBtnUser.setOnClickListener(view1 -> {

            String firstName = binding.firstName.getText().toString();
            String lastName = binding.lastName.getText().toString();
            String email = binding.email.getText().toString();

            if(!checkInput(firstName, lastName)) {
                //create new user object
                User us = new User(firstName, lastName, email, "");

                if (isAvatarSelected || avatarUrl != "") {
                    binding.avatarImg2.setDrawingCacheEnabled(true);
                    binding.avatarImg2.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.avatarImg2.getDrawable()).getBitmap();

                    //save image in firebase
                    Model.instance().uploadImage(email, bitmap, url -> {
                        if (url != null) {
                            us.setAvatarUrl(url);
                        }

                        // save user on firebase
                        Model.instance().addUser(us, (unused) -> {
                            homePage(binding.getRoot());  //back
                        });
                    });

                } else { // if the user not up photo

                    // save user on firebase
                    Model.instance().addUser(us, (unused) -> {
                        homePage(binding.getRoot()); //back
                    });
                }
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

    //************check the field input***********
    public boolean checkInput(String firstName, String lastName){
        Boolean bool =false;

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


    public void homePage(View view) {
        Navigation.findNavController(view).popBackStack();
    }
}