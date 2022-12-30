package com.example.class3demo2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.class3demo2.databinding.FragmentEditUserBinding;
import com.example.class3demo2.databinding.FragmentEditUserRecipePageBinding;
import com.example.class3demo2.databinding.FragmentSaveRecipeBinding;
import com.example.class3demo2.databinding.FragmentUserRecipePageBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;


public class EditUserRecipePageFragment extends Fragment {
    TextView titleTv;
    String title;
    String ingredients;
    String instructions;
    ImageView avatarImg;
    FragmentEditUserRecipePageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //save state
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditUserRecipePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        title = RecipeFragmentArgs.fromBundle(getArguments()).getNameRecipe();
        ingredients = RecipeFragmentArgs.fromBundle(getArguments()).getIngredients();
        instructions = RecipeFragmentArgs.fromBundle(getArguments()).getInstructions();
        //  avatarImg.setImageURI(Uri.parse(RecipeFragmentArgs.fromBundle(getArguments()).getAvatarUrl()));

        if (title != null){
            binding.nameEt.setText(title);
        }
        if (ingredients != null){
            binding.ingredientsEt.setText(ingredients);
        }
        if (instructions != null){
            binding.instructionsEt.setText(instructions);
        }

        binding.saveBtn.setOnClickListener(view1 -> {
            String name = binding.nameEt.getText().toString();
            String inst = binding.instructionsEt.getText().toString();
            String ingr = binding.ingredientsEt.getText().toString();
            String id = name;
            String image = "";

//            if(imageUri != null)
//                image = imageUri.toString();

            Recipe re = new Recipe(name,id,image,false,inst,ingr);
            Model.instance().addRecipe(re,()->{
                Navigation.findNavController(view1).popBackStack();
                Navigation.findNavController(view1).popBackStack();

            });
        });

        binding.cancellBtn.setOnClickListener((view1)->{
            Navigation.findNavController(view1).popBackStack();
        });
        return view;
    }

    public void setTitle(String title) {
        this.title = title;
        if (titleTv != null){
            titleTv.setText(title);
        }
    }


}