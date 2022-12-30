package com.example.class3demo2;

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

import java.util.LinkedList;
import java.util.List;


public class SaveFragment extends Fragment {
    FragmentSaveRecipeBinding binding;
    List<Recipe> data = new LinkedList<>();
    RecipeRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSaveRecipeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),data);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int pos) {
            Log.d("TAG", "Row was clicked " + pos);
            Recipe re = data.get(pos);
            SaveFragmentDirections.ActionLikesFragmentToRecipeFragment action = SaveFragmentDirections.actionLikesFragmentToRecipeFragment(re.name,re.ingredients,re.instructions,null);
            Navigation.findNavController(view).navigate(action);
        }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
        //list of student
    }

    void reloadData(){
        binding.progressBar2.setVisibility(View.VISIBLE);
        Model.instance().getAllRecipes((reList)->{
            data.removeAll(data);
            for(Recipe re : reList){
                if(re.cb == true)
                    data.add(re);
            }
            adapter.setData(data);
            binding.progressBar2.setVisibility(View.GONE);
        });
    }
}