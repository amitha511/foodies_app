package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

import java.util.List;

public class RecipesListFragment extends Fragment {
    List<Recipe> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        data = Model.instance().getAllRecipes();  //list of student

        RecyclerView list = view.findViewById(R.id.recipelistfrag_list);  //the list in student list
        list.setHasFixedSize(true);

        // crate the rows of student in the list :
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(getLayoutInflater(),data);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        adapter.setOnItemClickListener(new RecipeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Recipe st = data.get(pos);

                //move from listStudent fragment to blue fragment:
                RecipesListFragmentDirections.ActionStudentsListFragmentToBlueFragment action =
                        RecipesListFragmentDirections.actionStudentsListFragmentToBlueFragment(st.name);
                Navigation.findNavController(view).navigate(action);
                adapter.notifyDataSetChanged();

            }
        });

        View addButton = view.findViewById(R.id.recipelistfrag_add_btn);

        //move from global (every) fragment to add fragment:
        NavDirections action = RecipesListFragmentDirections.actionGlobalAddStudentFragment();
        addButton.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return view;
    }
}