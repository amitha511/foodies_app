package com.example.class3demo2;

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

    //create 1 student row
    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener, List<Recipe> data) {
        super(itemView);
        this.data = data;

        nameTv = itemView.findViewById(R.id.recipelistrow_name_tv);
        idTv = itemView.findViewById(R.id.recipelistrow_id_tv);
        cb = itemView.findViewById(R.id.recipelistrow_cb);
        img = itemView.findViewById(R.id.recipelistrow_avatar_img);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) cb.getTag();
                Recipe re = data.get(pos);
                re.cb = cb.isChecked();
                Model.instance().saveLike(re.name);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {  //click on student in the list
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    //bind the student to element in the fragment
    public void bind(Recipe re, int pos) {
        nameTv.setText(re.name);
        idTv.setText(re.id);
        Model.instance().getAllLikes(likes->{
            if(likes.contains(re.name)){
                cb.setChecked(true);

            }else{
                cb.setChecked(false);
            }
        });

        Log.d("bol",re.getCb().toString());
        cb.setTag(pos);
        if(re.getAvatarUrl() != "")
            Picasso.get().load(re.getAvatarUrl()).into(img);
        else{
            img.setImageResource(R.drawable.photorecipe);
        }

        // if photo up from gallery you see - error pizza because the permission of google.
        //if you see sakshoka photo  - is default photo , user don't entry photo

    }

}


//create all row of student list :
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    OnItemClickListener listener;
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    LayoutInflater inflater;
    List<Recipe> data;
    public void setData(List<Recipe> data){
        this.data=data;
        notifyDataSetChanged();
    }
    public RecipeRecyclerAdapter(LayoutInflater inflater, List<Recipe> data){
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_list_row,parent,false); //get 1 row of student
        return new RecipeViewHolder(view,listener, data);
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe re = data.get(position);
        holder.bind(re,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

