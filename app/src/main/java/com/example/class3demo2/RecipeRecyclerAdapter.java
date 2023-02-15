package com.example.class3demo2;

import static com.example.class3demo2.MyApplication.getAppContext;
import static com.example.class3demo2.model.Model.isOnline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class3demo2.databinding.FragmentRecipeListBinding;
import com.example.class3demo2.databinding.RecipeListRowBinding;
import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Recipe;
import com.squareup.picasso.Picasso;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;


class RecipeViewHolder extends RecyclerView.ViewHolder {

    //findViewBy:
    TextView nameTv;
    TextView idTv;
    ImageView img;
    CheckBox cb;
    List<Recipe> data;
    RecipeListRowBinding binding;

    //*******************************************create 1 recipe row*********************************************
    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener, List<Recipe> data) {
        super(itemView);
        this.data = data;

        //binding:
        nameTv = itemView.findViewById(R.id.recipelistrow_name_tv);
        idTv = itemView.findViewById(R.id.recipelistrow_id_tv);
        cb = itemView.findViewById(R.id.recipelistrow_cb);
        img = itemView.findViewById(R.id.recipelistrow_avatar_img);

        //click on like box:
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) cb.getTag();
                Recipe re = data.get(pos);  // save the recipe in line "pos"(int)
                re.cb = cb.isChecked();  // like == true
                Model.instance().saveLike(re.name);  //save like in firebase
            }
        });


        //send the number line (pos) item to adapter when click on recipe in the list
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition(); // get the number line of recipe
                listener.onItemClick(pos); //return the pos
            }
        });
    }
    //****************************************************************************************


    ////************************ bind the recipe to element in the fragment **********************************

    //binding recipe with the row in number "pos"
    public void bind(Recipe re, int pos) {
        nameTv.setText(re.name);

        //set the like box true/false:
        if(isOnline(getAppContext())) {

            //check in firebase the like box
            Model.instance().getAllLikes(likes -> {
                if (likes.contains(re.name)) {
                    cb.setChecked(true); //full heart

                } else {
                    cb.setChecked(false); //empty heart
                }
            });
        }
        cb.setTag(pos);  //set in the line "pos"(int)

        //binding to the image recipe
        if(re.getAvatarUrl() == "" || re.getAvatarUrl().isEmpty())
            img.setImageResource(R.drawable.photorecipe);      // is default photo , user don't entry photo
        else
            Picasso.get().load(re.getAvatarUrl()).error(R.drawable.photorecipe).into(img);

    }
}
//****************************************************************************************


//********************************** create all the row of recipes list *************************************************:
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;

    // ************** save the list of recipes ****************
    public void setData(List<Recipe> data){
        this.data=data;
        notifyDataSetChanged();  // update if have change in the list
    }

    //********** set inflater and data list ******************
    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data){
        this.inflater = inflater;
        this.data = data;
    }

    //********** set listener ******************
    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    // **************** create 1 row of recipe ************
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_list_row,parent,false); //binding to 1 row of recipe
        return new RecipeViewHolder(view,listener, data);    //create new 1 row
    }


    //******* binding recipe with the row "pos" *********
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe re = data.get(position);    //get recipe in row "pos" os list
        holder.bind(re,position);    //binding recipe with the row
    }

    @Override
    public int getItemCount() {   //number of recipes
        if(data == null)
            return 0;
        return data.size();
    }

    //****************************************************************************************

}

