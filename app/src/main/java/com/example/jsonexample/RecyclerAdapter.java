package com.example.jsonexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements RecyclerViewInterface {

    List<Recipe> items;
    private AdapterView.OnItemClickListener listener;
    private final RecyclerViewInterface recyclerInterface;

    public RecyclerAdapter(List<Recipe> items, RecyclerViewInterface recyclerInterface) {
        this.items = items;
        this.recyclerInterface = recyclerInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendations,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe currentRecipe = items.get(position);

        holder.recipeName.setText(currentRecipe.name);
        holder.recipeTime.setText(currentRecipe.time + " mins");
        holder.recipeReview.setText(String.valueOf(currentRecipe.score + "% of people liked this recipe!"));
        holder.recipeDesc.setText(currentRecipe.description);

        Picasso.get()
                .load(currentRecipe.imageURL)
                .into(holder.recipeImage);

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
        return items.size();
    }

    public void clearData() {
        if (items != null) {
            items.clear();
        }
    }

    @Override
    public void onItemClick(int position) {
        Recipe clickedItem = items.get(position);
    }
}

