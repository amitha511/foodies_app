//package com.example.class3demo2;
//
//import android.app.ActionBar;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.MenuProvider;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.Lifecycle;
//import androidx.navigation.NavController;
//import androidx.navigation.NavDirections;
//import androidx.navigation.NavHost;
//import androidx.navigation.NavHostController;
//import androidx.navigation.Navigation;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.NavigationUI;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.class3demo2.databinding.FragmentAddUserBinding;
//import com.example.class3demo2.databinding.FragmentWelcomePageBinding;
//import com.example.class3demo2.model.Model;
//import com.google.android.material.textfield.TextInputEditText;
//
//// we can remove this page
//public class WelcomeFragment extends Fragment {
//
//    FragmentWelcomePageBinding binding;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FragmentActivity parentActivity = getActivity(); //return this
//
//
//        //hide menu_bar:
//
//        //update menu:
//        parentActivity.addMenuProvider(new MenuProvider() {
//
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menu.clear();
//            }
//
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                return false;
//            }
//
//        }, this, Lifecycle.State.RESUMED);
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = FragmentWelcomePageBinding.inflate(inflater,container,false);
//        View view = binding.getRoot();
////        getActivity().findViewById(R.id.main_bottomNavigationView).setVisibility(View.GONE);
//
//
//        Model.instance().isSignedIn(status ->{
//            if(status){
//                Model.instance().logout();
//               // Navigation.findNavController(view).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecipesListFragment());
//            }
//        });
//
//
//        //to connect
//        binding.login.setOnClickListener(view1->{
//            View view2;
//
//            String email = binding.emailEt.getText().toString();
//            String password = binding.passEt.getText().toString();
//            if(email.isEmpty() || password.isEmpty()){
//                TextInputEditText em = binding.emailEt;
//                em.setError("This field cannot be empty");
//                TextInputEditText pas = binding.passEt;
//                pas.setError("This field cannot be empty");
//
//            }
//            else {
//                Model.instance().login("11@gmail.com", "123456789", status -> {
//                    if (status) {
//                        // home();
//
////                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.main_navhost, RecipesListFragment.class,null);
////                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();
//                        Navigation.findNavController(view).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRecipesListFragment());
//
//                    } else {
//                        binding.welcome.setText("email or password wrong");
//
//                    }
//                });
//            }
//        });
//
//      //to create user
//        binding.signup.setOnClickListener(view1->{
//            Navigation.findNavController(view1).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterUserFragment());
//        });
//
//        return view;
//    }
//
//    public void home(){
//        getFragmentManager().beginTransaction()
//        .setReorderingAllowed(true)
//// Replace whatever is in the fragment_container view with this fragment
//        .replace(R.id.welcomeFragment, RecipesListFragment.class, null)
//// Commit the transaction
//        .commit();
//    }
//
//}