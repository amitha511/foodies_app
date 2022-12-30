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

import com.example.class3demo2.databinding.FragmentUserRecipePageBinding;

public class UserRecipePageFragment extends Fragment {

    TextView titleTv;
    String title;
    String ingredients;
    String instructions;
    ImageView avatarImg;
    FragmentUserRecipePageBinding binding;

    public static UserRecipePageFragment newInstance(String title){
        UserRecipePageFragment frag = new UserRecipePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE",title);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //save state
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserRecipePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        title = RecipeFragmentArgs.fromBundle(getArguments()).getNameRecipe();
        ingredients = RecipeFragmentArgs.fromBundle(getArguments()).getIngredients();
        instructions = RecipeFragmentArgs.fromBundle(getArguments()).getInstructions();
        //  avatarImg.setImageURI(Uri.parse(RecipeFragmentArgs.fromBundle(getArguments()).getAvatarUrl()));

        if (title != null){
            binding.recipeTitleTv.setText(title);
        }
        if (ingredients != null){
            binding.IngredientsTv.setText(ingredients);
        }
        if (instructions != null){
            binding.InstructionsTv.setText(instructions);
        }

        binding.recipeEditBtn.setOnClickListener((view1)->{
            UserRecipePageFragmentDirections.ActionFragmentUserRecipePageToEditUserRecipePageFragment2 action= UserRecipePageFragmentDirections.actionFragmentUserRecipePageToEditUserRecipePageFragment2(title,ingredients,instructions,null);
            Navigation.findNavController(view).navigate(action);
        });

        binding.backBtn.setOnClickListener((view1)->{
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