package com.example.class3demo2;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.class3demo2.databinding.FragmentAddRecipeBinding;
import com.example.class3demo2.databinding.FragmentRecipePageBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeFragment extends Fragment {
    String title;
    String ingredients;
    String instructions;
    String avatarImg;
    FragmentRecipePageBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //save state
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecipePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        getElement();

        if (title != null){
            binding.recipeTitleTv.setText(title);
        }
        if (ingredients != null){
            binding.IngredientsTv.setText(ingredients);
        }
        if (instructions != null){
            binding.InstructionsTv.setText(instructions);
        }
        if (avatarImg != ""){
            Picasso.get().load(avatarImg).error(R.drawable.errorpizza).into(binding.avatarImg);
        }else{
            binding.avatarImg.setImageResource(R.drawable.photorecipe);
        }

        //View button = view.findViewById(R.id.recipe_back_btn);
        binding.backBtn.setOnClickListener((view1)->{
            Navigation.findNavController(view1).popBackStack();
        });
        return view;
    }

    public void getElement() {

        title = RecipeFragmentArgs.fromBundle(getArguments()).getNameRecipe();
        ingredients = RecipeFragmentArgs.fromBundle(getArguments()).getIngredients();
        instructions = RecipeFragmentArgs.fromBundle(getArguments()).getInstructions();
        avatarImg = (RecipeFragmentArgs.fromBundle(getArguments()).getAvatarUrl());

    }
}