package com.example.jsonexample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Favorites_MyViewHolder extends RecyclerView.ViewHolder {

    ImageView favorites_imageview;
    TextView favorites_nameview , favorites_descriptionview,favorites_timeview;

    public Favorites_MyViewHolder(@NonNull View itemView) {
        super(itemView);
        favorites_imageview = itemView.findViewById(R.id.favorites_imageView);
        favorites_nameview = itemView.findViewById(R.id.favorites_name);
        favorites_descriptionview = itemView.findViewById(R.id.favorites_description);
        favorites_timeview = itemView.findViewById(R.id.favorites_time);
    }
}
