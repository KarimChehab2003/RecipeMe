package com.example.jsonexample;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class RecipeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //get Clicked on recipe
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        //declare views
        VideoView video = findViewById(R.id.recipeDetailsVideo);
        TextView recipeName = findViewById(R.id.recipeDetailsName);

        TextView recipeDesc = findViewById(R.id.recipeDetailsDesc);
        TextView recipeTime = findViewById(R.id.recipeDetailsNutritionsTitle);
        TableLayout nutritionFacts = findViewById(R.id.nutritionTable);
        LinearLayout recipeInstructions  = findViewById(R.id.recipeDetailsInstructions);

        // Adding video
        video.setVideoURI(Uri.parse(recipe.videoURL));
        Uri uri = Uri.parse(recipe.videoURL);
        video.setVideoURI(uri);

        // Add media controls for play/pause
        MediaController mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
        video.start();

        //set views content
        recipeName.setText(recipe.name);
        recipeDesc.setText(recipe.description);

        // Styling table
        Drawable border = ContextCompat.getDrawable(this, R.drawable.table_cell_border);


        // Making nutrition facts table
        for (Map.Entry<String, Integer> entry : recipe.nutritionFacts.entrySet()) {
            TableRow tableRow = new TableRow(this);

            TextView keyTextView = new TextView(this);
            keyTextView.setText(entry.getKey());
            tableRow.addView(keyTextView);
            keyTextView.setBackground(border);
            keyTextView.setPadding(8,8,8,8);

            TextView valueTextView = new TextView(this);
            valueTextView.setText(String.valueOf(entry.getValue()));
            tableRow.addView(valueTextView);
            valueTextView.setBackground(border);
            valueTextView.setPadding(8,8,8,8);

            nutritionFacts.addView(tableRow);
        }

        for(int i=0;i<recipe.instructions.size();i++)
        {
            TextView instruction = new TextView(this);
            instruction.setText(String.valueOf(i+1)+"- "+recipe.instructions.get(i) +"\n");
            recipeInstructions.addView(instruction);
        }

    }
}
