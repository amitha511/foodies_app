package com.example.class3demo2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentRecipeListBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class RecipesListFragment extends Fragment {
    FragmentRecipeListBinding binding;
    RecipeRecyclerAdapter adapter;
    RecipeListFragmentViewModel viewModel;
    CurrentUserViewModel currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getActivity().findViewById(R.id.main_bottomNavigationView).setVisibility(View.VISIBLE);

        //*******************************list ********************:
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set to inflater and data live list
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getLiveData().getValue());
        binding.recyclerView.setAdapter(adapter);

        //click on recipe (get pos)
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                // set the recipe
                Recipe re = viewModel.getLiveData().getValue().get(pos);

                //send data of recipes to next fragment (userRecipePage)
                RecipesListFragmentDirections.ActionRecipesListFragmentToRecipeFragment action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(re.getName(),re.getIngredients(),re.getInstructions(),re.getAvatarUrl());
                Navigation.findNavController(view).navigate(action);
            }
        });

//********************************************************************************:

        //done loading
        binding.progressBar.setVisibility(View.GONE);

        //refresh >> update loading status
        Model.instance().EventRecipesListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.swipeRefresh.setRefreshing(status == Model.LoadingState.LOADING);
        });


        //update the data list by data live (cache)
        viewModel.getLiveData().observe( getViewLifecycleOwner(),list->{
            //list == cache - update
            Collections.sort(list, Comparator.comparing(Recipe::getName));  //sort the cache
            adapter.setData(list);   //set cache in the data list
        });

        //update
        binding.swipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(RecipeListFragmentViewModel.class);
        //currentUser = new ViewModelProvider(this).get(CurrentUserViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
        //list of recipe
    }

    void reloadData(){
        //get recipes by live data !!
        Model.instance().refreshAllRecipes();
    }
}