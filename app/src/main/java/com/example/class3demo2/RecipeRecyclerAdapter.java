package com.example.class3demo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class3demo2.model.Recipe;

import java.util.List;


class RecipeViewHolder extends RecyclerView.ViewHolder{

    //findViewBy:
    TextView nameTv;
    TextView idTv;
    CheckBox cb;
    List<Recipe> data;



    //create 1 student row
    public RecipeViewHolder(@NonNull View itemView, RecipeRecyclerAdapter.OnItemClickListener listener, List<Recipe> data) {
        super(itemView);
        this.data = data;
        nameTv = itemView.findViewById(R.id.recipelistrow_name_tv);
        idTv = itemView.findViewById(R.id.recipelistrow_id_tv);
        cb = itemView.findViewById(R.id.recipelistrow_cb);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)cb.getTag();
                Recipe st = data.get(pos);
                st.cb = cb.isChecked();
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
        cb.setChecked(re.cb);
        cb.setTag(pos);
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

