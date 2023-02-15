package com.example.class3demo2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.class3demo2.databinding.FragmentMyProfileBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// user page
public class ProfileFragment extends RecipesListFragment {
    FragmentMyProfileBinding binding;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // ********************get the details of user from firebase **********************
        Model.instance().getCurrentUser(currentUser-> {
            email = currentUser.getEmail();
            binding.email.setText(currentUser.email);
            binding.firstName.setText(currentUser.firstName);
            binding.lastName.setText(currentUser.lastName);
            if(currentUser.avatarUrl !="")
                Picasso.get().load(currentUser.avatarUrl).error(R.drawable.errorpizza).into(binding.avatarImg3);
        });

        //*******************************list ********************:
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set to inflater and data live list
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);

        //click on recipe (get pos)
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe re = viewModel.getData().get(pos);   //save the recipe in line "pos"(int) ;

                // send the arguments to userRecipeFragment
                ProfileFragmentDirections.ActionProfileToFragmentUserRecipePage action = ProfileFragmentDirections.actionProfileToFragmentUserRecipePage(re.getName(),re.getIngredients(),re.getInstructions(),re.getAvatarUrl());
                Navigation.findNavController(view).navigate(action);
            }
        });


        return view;
    }


    @Override
    void reloadData(){

        binding.progressBar3.setVisibility(View.VISIBLE); //show loading

        //not live data !!!
        Model.instance().getAllRecipes((reList)->{
            //relist = all the recipes in app
            //clear the data list
            viewModel.getData().removeAll(viewModel.getData());

            //get all likes of the user connect from firebase
            for(Recipe re : reList){
                if(re.author.equals(email))  //filter list by email of user
                    viewModel.getData().add(re);
            }
            //viewModel.data = list of user recipes
            //send to adapter
            adapter.setData(viewModel.getData());
            binding.progressBar3.setVisibility(View.GONE); //remove loading
        });
    }
}