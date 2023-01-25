package com.example.class3demo2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe re = viewModel.getData().get(pos);
                RecipesListFragmentDirections.ActionRecipesListFragmentToRecipeFragment action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(re.getName(),re.getIngredients(),re.getInstructions(),re.getAvatarUrl());
                Navigation.findNavController(view).navigate(action);
            }
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
  //list of student
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().getAllRecipes((reList) -> {

                viewModel.setData(reList);
                adapter.setData(viewModel.getData());
                binding.progressBar.setVisibility(View.GONE);

//            binding.search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus) {
//                        String search = binding.search.getQuery().toString();
//                        viewModel.getData().removeAll(viewModel.getData());
//                        for (Recipe re : reList) {
//                            if (re.name.contains(v.toString()))
//                                viewModel.getData().add(re);
//                        }
//                        adapter.setData(viewModel.getData());
//
//                        Log.d("l", search + v.toString());
//                    }
//                }
//
//            });
        });
    }
}