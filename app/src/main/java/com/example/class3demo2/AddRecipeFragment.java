package com.example.class3demo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;

public class AddRecipeFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity(); //return this

        //update menu:
        parentActivity.addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.saveRecipe);  //remove bottom add in up menu
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        },this, Lifecycle.State.RESUMED);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment   ---> view get fragment of add student
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false); //gey add student fragment

        //config this fragment :
        EditText nameEt = view.findViewById(R.id.addrecipe_name_et);
        EditText idEt = view.findViewById(R.id.addrecipe_id_et);
        TextView messageTv = view.findViewById(R.id.addrecipe_message);
        Button saveBtn = view.findViewById(R.id.addrecipe_save_btn);
        Button cancelBtn = view.findViewById(R.id.addrecipe_cancell_btn);

        saveBtn.setOnClickListener(view1 -> {
            String name = nameEt.getText().toString();
            String id = idEt.getText().toString();

            Recipe st = new Recipe(name,id,null,false);
            Model.instance().addRecipe(st);
            messageTv.setText(name);
            Navigation.findNavController(view1).popBackStack();

        });

        cancelBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).popBackStack(R.id.RecipesListFragment,false));

        return view;
    }

}