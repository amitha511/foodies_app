package com.example.class3demo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.class3demo2.databinding.FragmentAddUserBinding;
import com.example.class3demo2.databinding.FragmentWelcomePageBinding;
import com.example.class3demo2.model.Model;


public class WelcomeFragment extends Fragment {

    FragmentWelcomePageBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity(); //return this


        //update menu:
        parentActivity.addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }


            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }

        }, this, Lifecycle.State.RESUMED);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWelcomePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        Model.instance().isSignedIn(status ->{
            if(status){
                Model.instance().logout();
            }
        });


        String email = binding.Email.getText().toString();
        String password = binding.pas.getText().toString();
        //to connect
        binding.login.setOnClickListener(view1->{
            Model.instance().login(email,password,status->{
                if(status){
                    Navigation.findNavController(view1).navigate( WelcomeFragmentDirections.actionWelcomeFragmentToRecipesListFragment() );                }
                else{
                    binding.welcome.setText("email or password wrong");

                }
            });
        });

      //to create user
        binding.signup.setOnClickListener(view1->{
            Navigation.findNavController(view1).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterUserFragment());
        });

        return view;
    }
}