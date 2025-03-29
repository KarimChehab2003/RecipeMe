package com.example.jsonexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class favorites_adapter extends RecyclerView.Adapter<Favorites_MyViewHolder> implements RecyclerViewInterface {

    Context context;
    List<Recipe> recipes_list;
    private final RecyclerViewInterface recyclerInterface;

    public favorites_adapter(List<Recipe> recipes_list, Context context, RecyclerViewInterface recyclerInterface) {
        this.recipes_list = recipes_list;
        this.context = context;
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public Favorites_MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Favorites_MyViewHolder(LayoutInflater.from(context).inflate(R.layout.favorites_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Favorites_MyViewHolder holder, int position) {

        holder.favorites_nameview.setText(recipes_list.get(position).name);
        holder.favorites_descriptionview.setText(recipes_list.get(position).description);
        holder.favorites_timeview.setText(recipes_list.get(position).time + " mins");

        Picasso.get()
                .load(recipes_list.get(position).imageURL)
                .into(holder.favorites_imageview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION)
                    recyclerInterface.onItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes_list.size();
    }

    public void onItemClick(int position) {
        Recipe clickedItem = recipes_list.get(position);
    }

}
