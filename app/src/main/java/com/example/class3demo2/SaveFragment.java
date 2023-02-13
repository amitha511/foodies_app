package com.example.class3demo2;

import static com.example.class3demo2.MyApplication.getAppContext;
import static com.example.class3demo2.model.Model.isOnline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentSaveRecipeBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class SaveFragment extends RecipesListFragment {
    FragmentSaveRecipeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaveRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),viewModel.getData());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe re = viewModel.getData().get(pos);
                SaveFragmentDirections.ActionLikesFragmentToRecipeFragment action = SaveFragmentDirections.actionLikesFragmentToRecipeFragment(re.name,re.ingredients,re.instructions,re.avatarUrl);
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }

    @Override
    void reloadData(){
        binding.progressBar2.setVisibility(View.VISIBLE);
        Model.instance().getAllRecipes((reList)->{
            viewModel.getData().removeAll(viewModel.getData());
            if(isOnline(getAppContext())) {
                Model.instance().getAllLikes(likes -> {
                    for (Recipe re : reList) {
                        if (likes.contains(re.name))
                            viewModel.getData().add(re);
                    }
                    Collections.sort(viewModel.getData(), Comparator.comparing(Recipe::getName));
                    adapter.setData(viewModel.getData());
                });
            }
        });

        binding.progressBar2.setVisibility(View.GONE);

    }
}