package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
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
    List<Recipe> data = new LinkedList<>();
    RecipeRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
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
                RecipesListFragmentDirections.ActionStudentsListFragmentToBlueFragment action = RecipesListFragmentDirections.actionStudentsListFragmentToBlueFragment(re.name);
                Navigation.findNavController(view).navigate(action);
            }
        });





        // Inflate the layout for this fragment
       // View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
//
//        Model.instance().getAllRecipes((stList)->{
//            data = stList;
//            adapter.setData(data);
//        });  //list of student
//
//        RecyclerView list = view.findViewById(R.id.recipelistfrag_list);  //the list in student list
//        list.setHasFixedSize(true);
//
//        // crate the rows of student in the list :
//        list.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new RecipeRecyclerAdapter(getLayoutInflater(),data);
//        list.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//
//        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int pos) {
//                Log.d("TAG", "Row was clicked " + pos);
//                Recipe st = data.get(pos);
//
//                //move from listStudent fragment to blue fragment:
//                RecipesListFragmentDirections.ActionStudentsListFragmentToBlueFragment action =
//                        RecipesListFragmentDirections.actionStudentsListFragmentToBlueFragment(st.name);
//                Navigation.findNavController(view).navigate(action);
//                adapter.notifyDataSetChanged();
//
//            }
//        });

        View addButton = view.findViewById(R.id.btnAdd);
        //move from global (every) fragment to add fragment:
        NavDirections action = RecipesListFragmentDirections.actionGlobalAddStudentFragment();
        addButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
  //list of student
    }

    void reloadData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Model.instance().getAllRecipes((reList)->{
            data = reList;
            adapter.setData(data);
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}