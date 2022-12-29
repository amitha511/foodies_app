package com.example.class3demo2;


import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
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

import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.class3demo2.databinding.FragmentAddRecipeBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.io.File;

public class AddRecipeFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGED=1;
    FragmentAddRecipeBinding binding;
    Uri imageUri = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity(); //return this

        //update menu:
        parentActivity.addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.saveRecipe);  //remove bottom add in up menu
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
            imageUri = data.getData();
            binding.avatarImg.setImageURI(imageUri);

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
                    case R.id.addImage:

                        break;
                }
            }

        });
        binding.saveBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString();
            String id = binding.idEt.getText().toString();
            Recipe re = new Recipe(name,id,imageUri.toString(),false);
            Model.instance().addRecipe(re,()->{
                Navigation.findNavController(view1).popBackStack();
            });
        });


        binding.cancellBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.RecipesListFragment,false));

        return view;
    }

}



