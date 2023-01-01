package com.example.class3demo2;


import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

public class AddRecipeFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGED=1;
    FragmentAddRecipeBinding binding;
    String imageString = "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg?quality=90&resize=768,574";

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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGED && resultCode == RESULT_OK && data != null) {
            Uri imageDataUri = data.getData();
            binding.avatarImg.setImageURI(imageDataUri);
            if(imageDataUri.toString() != null)
                imageString = imageDataUri.toString();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddRecipeBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.avatarImg:
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGED);
                        break;
                    case R.id.removeImage:

                        break;
                }
            }

        });

        binding.removeImage.setOnClickListener(view1->{
            imageString = null;
        });

        binding.saveBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString();
            String inst = binding.instructionsEt.getText().toString();
            String ingr = binding.ingredientsEt.getText().toString();
            String id = name;
            if(imageString == null)
                imageString = "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg?quality=90&resize=768,574";
            Recipe re = new Recipe(name,id,imageString,false,inst,ingr);
            Model.instance().addRecipe(re,()->{
                Navigation.findNavController(view1).popBackStack();
            });
        });


        binding.cancellBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.RecipesListFragment,false));


        return view;
    }


}



