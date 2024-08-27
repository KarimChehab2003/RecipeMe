package com.example.jsonexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class history_adapter extends RecyclerView.Adapter<History_MyViewHolder> {

    Context context;
    List<Recipe> recipes_list;

    public history_adapter(List<Recipe> recipes_list, Context context) {
        this.recipes_list = recipes_list;
        this.context = context;
    }

    @Override
    public History_MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new History_MyViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull History_MyViewHolder holder, int position) {
        holder.history_nameview.setText(recipes_list.get(position).name);
        holder.history_nameview.setText(recipes_list.get(position).description);
        holder.history_nameview.setText(recipes_list.get(position).time + " mins");
        Picasso.get()
                .load(recipes_list.get(position).imageURL)
                .into(holder.history_imageview);
    }

    @Override
    public int getItemCount() {
        return recipes_list.size();
    }
}
