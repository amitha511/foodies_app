package com.example.class3demo2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentAddRecipeBinding;
import com.example.class3demo2.databinding.FragmentEditUserRecipePageBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


//for edit recipe user and save (photo recipe, ingredients,instructions )
public class EditUserRecipePageFragment extends AddRecipeFragment {
    String title;
    String ingredients;
    String instructions;
    String imageString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        getElement();
        Model.instance().getCurrentUser(user -> {
            email = user.getEmail();
        });

        binding.saveBtn.setOnClickListener(view1 -> {
            saveRecipe(view1);
        });


        binding.cancellBtn.setOnClickListener((view1)->{
            Navigation.findNavController(view1).popBackStack();
            Navigation.findNavController(view1).popBackStack();

        });

        binding.camerabutton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });
        binding.gallerybutton.setOnClickListener(view1->{
            galleryAppLauncher.launch("image/*");
        });

        return view;
    }

    public void getElement(){

        //set from argument fragment to Strings;
        title = RecipeFragmentArgs.fromBundle(getArguments()).getNameRecipe();
        ingredients = RecipeFragmentArgs.fromBundle(getArguments()).getIngredients();
        instructions = RecipeFragmentArgs.fromBundle(getArguments()).getInstructions();
        imageString = (RecipeFragmentArgs.fromBundle(getArguments()).getAvatarUrl());

        // set data user in the ui
        if (title != null){
            binding.nameEt.setText(title);
        }
        if (ingredients != null){
            binding.ingredientsEt.setText(ingredients);
        }
        if (instructions != null){
            binding.instructionsEt.setText(instructions);
        }
        if (imageString.isEmpty() || imageString ==""){
            binding.avatarImg.setImageResource(R.drawable.photorecipe);

        }else{
            Picasso.get().load(imageString).error(R.drawable.errorpizza).into(binding.avatarImg);
        }

        //set Enabled name of recipe
        binding.nameEt.setEnabled(false);
        binding.styleNameEt.setStartIconDrawable(null);
    }

    @Override
    public void saveRecipe(View view1) {

        String name = binding.nameEt.getText().toString();
        String instructions = binding.instructionsEt.getText().toString();
        String ingredients = binding.ingredientsEt.getText().toString();
        String id = name;

        // create new recipe object
        Recipe re = new Recipe(name, id, "", false, instructions, ingredients, email);

        //********** save image recipe****************
        if (isAvatarSelected || imageString != "") {
                binding.avatarImg.setDrawingCacheEnabled(true);
                binding.avatarImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.avatarImg.getDrawable()).getBitmap();

                //save the photo in firebase and return the url
                Model.instance().uploadImage(id, bitmap, url -> {
                    if (url != null) {
                        re.setAvatarUrl(url);
                    }

                    //save recipe                  //null
                    Model.instance().addRecipe(re, (unused) -> {
                        Navigation.findNavController(view1).popBackStack();
                    });
                });

            } else {

                Model.instance().addRecipe(re, (unused) -> {
                    Navigation.findNavController(view1).popBackStack();
                });
            }

    }
}