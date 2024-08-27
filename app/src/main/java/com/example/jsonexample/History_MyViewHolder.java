package com.example.jsonexample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class History_MyViewHolder extends RecyclerView.ViewHolder {

    ImageView history_imageview;
    TextView history_nameview , history_descriptionview,history_timeview;

    public History_MyViewHolder(@NonNull View itemView) {
        super(itemView);
        history_imageview = itemView.findViewById(R.id.history_imageView);
        history_nameview = itemView.findViewById(R.id.history_name);
        history_descriptionview = itemView.findViewById(R.id.history_description);
        history_timeview = itemView.findViewById(R.id.history_time);
    }
}
