package com.example.class3demo2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.class3demo2.databinding.FragmentAddUserBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;
import com.example.class3demo2.model.User;

public class RegisterUserFragment extends Fragment {

    FragmentAddUserBinding binding;
    String imageString = "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg?quality=90&resize=768,574";
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
        binding = FragmentAddUserBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.saveBtnUser.setOnClickListener(view1 -> {
            String firstName = binding.firstName.getText().toString();
            String lastName = binding.lastName.getText().toString();
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();

            Model.instance().createUserWithEmailAndPassword(email, password, status->{
                if(status){
                    //Toast.makeText(getActivity(), "successful", Toast.LENGTH_SHORT).show();
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
                                Navigation.findNavController(view).navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToRecipesListFragment());
                            });
                        });
                    }else {
                        Model.instance().addUser(us, (unused) -> {
                            Navigation.findNavController(view).navigate(RegisterUserFragmentDirections.actionRegisterUserFragmentToRecipesListFragment());
                        });
                    }
                }

                else{
                    Navigation.findNavController(view1).popBackStack();

                }
                   // Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            });


        });

        binding.camerabutton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.gallerybutton.setOnClickListener(view1->{
            galleryAppLauncher.launch("image/*");
        });


        return view;

    }
}