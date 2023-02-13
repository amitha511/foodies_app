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

public class ProfileFragment extends RecipesListFragment {
    FragmentMyProfileBinding binding;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Model.instance().getCurrentUser(currentUser-> {
//            currentUser.setUser(user);
            email = currentUser.getEmail();
            binding.email.setText(currentUser.email);
            binding.firstName.setText(currentUser.firstName);
            binding.lastName.setText(currentUser.lastName);
            if(currentUser.avatarUrl !="")
                Picasso.get().load(currentUser.avatarUrl).error(R.drawable.errorpizza).into(binding.avatarImg3);
        });

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe re = viewModel.getData().get(pos);
                ProfileFragmentDirections.ActionProfileToFragmentUserRecipePage action = ProfileFragmentDirections.actionProfileToFragmentUserRecipePage(re.getName(),re.getIngredients(),re.getInstructions(),re.getAvatarUrl());
                Navigation.findNavController(view).navigate(action);
            }
        });


        return view;
    }

    @Override
    void reloadData(){

        binding.progressBar3.setVisibility(View.VISIBLE);


        Model.instance().getAllRecipes((reList)->{
            viewModel.getData().removeAll(viewModel.getData());
            for(Recipe re : reList){
                Log.d("email",email +"re.au:"+ re.author );
                if(re.author.equals(email))
                    viewModel.getData().add(re);
            }
            adapter.setData(viewModel.getData());
            binding.progressBar3.setVisibility(View.GONE);
        });
    }
}