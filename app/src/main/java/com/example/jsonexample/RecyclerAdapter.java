package com.example.jsonexample;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<Recipe> items;

    public RecyclerAdapter(List<Recipe> items) {
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recipeName.setText(items.get(position).name);
        holder.recipeTime.setText(items.get(position).time + " mins");
        holder.recipeReview.setText(String.valueOf(items.get(position).score));
        Picasso.get()
                .load(items.get(position).imageURL)
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
