package com.example.jsonexample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recipeImage;
    TextView recipeName,recipeTime,recipeReview;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeImage = itemView.findViewById(R.id.recipeImage);
        recipeName = itemView.findViewById(R.id.recipeName);
        recipeTime = itemView.findViewById(R.id.recipeTime);
        recipeReview = itemView.findViewById(R.id.recipeReview);
    }
}
