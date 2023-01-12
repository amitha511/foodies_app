package com.example.class3demo2;


import static android.app.Activity.RESULT_OK;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentAddRecipeBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

public class AddRecipeFragment extends Fragment {
    FragmentAddRecipeBinding binding;
    Boolean isAvatarSelected = false;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity(); //return this


        //update menu:
        parentActivity.addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.addRecipe);  //remove bottom add in up menu
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
                new ActivityResultCallback<Bitmap>() {
                    @Override
                    public void onActivityResult(Bitmap result) {
                        if (result != null) {
                            binding.avatarImg.setImageBitmap(result);
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
                            binding.avatarImg.setImageURI(result);
                            isAvatarSelected = true;
                        }
                    }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.saveBtn.setOnClickListener(view1 -> {
          saveRecipe(view1);
        });

        binding.cancellBtn.setOnClickListener(view1 ->
                Navigation.findNavController(view1).popBackStack(R.id.RecipesListFragment,false));

        binding.camerabutton.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.gallerybutton.setOnClickListener(view1->{
            galleryAppLauncher.launch("image/*");
        });

        return view;
    }


    public void saveRecipe(View view1){
        String name = binding.nameEt.getText().toString();
        String instructions = binding.instructionsEt.getText().toString();
        String ingredients = binding.ingredientsEt.getText().toString();
        String id = name;
        Recipe re = new Recipe(name,id,"",false,instructions,ingredients);

        if(isAvatarSelected){
            binding.avatarImg.setDrawingCacheEnabled(true);
            binding.avatarImg.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.avatarImg.getDrawable()).getBitmap();
            Model.instance().uploadImage(id,bitmap,url->{
                if(url != null){
                    re.setAvatarUrl(url);
                }
                Model.instance().addRecipe(re,(unused)->{
                    Navigation.findNavController(view1).popBackStack();
                });
            });
        }else {
            Model.instance().addRecipe(re, (unused) -> {
                Navigation.findNavController(view1).popBackStack();
            });
        }
    }


}



