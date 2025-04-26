package com.example.jsonexample;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class RecipeDetails extends AppCompatActivity {

    DBhelper dbh = new DBhelper(this);

    Recipe recipe;
    String currentUserID;
    String currentUserName;

    Cursor favorited_recipes;
    ImageView favorite_icon;
    boolean isFavorited;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainpagemenu , menu);

        MenuItem menuitem = menu.findItem(R.id.menu_history);
        MenuItem menuitem2 = menu.findItem(R.id.menu_favorites);
        MenuItem menuitem3 = menu.findItem(R.id.menu_home);
        MenuItem menuitem4 = menu.findItem(R.id.menu_profile);

        menuitem.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this, history.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            startActivity(intent);
            return true;
        });

        menuitem2.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this, favorites.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            startActivity(intent);
            return true;
        });

        menuitem3.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            startActivity(intent);
            return true;
        });

        menuitem4.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this, Profile.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            intent.putExtra("currentUserEmail",dbh.getEmailById(Long.parseLong(currentUserID)));
            startActivity(intent);
            return true;
        });

        return true;
    }

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
        recipe = (Recipe) intent.getSerializableExtra("recipe");
        currentUserID = intent.getStringExtra("currentUserID");
        currentUserName = intent.getStringExtra("currentUserName");

        //declare views
        VideoView video = findViewById(R.id.recipeDetailsVideo);
        TextView recipeName = findViewById(R.id.recipeDetailsName);
        TextView recipeDesc = findViewById(R.id.recipeDetailsDesc);
        TableLayout nutritionFacts = findViewById(R.id.nutritionTable);
        LinearLayout recipeInstructions  = findViewById(R.id.recipeDetailsInstructions);
        TextView recipeScore = findViewById(R.id.recipeScore);
        favorite_icon = findViewById(R.id.favorite_icon);

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
        recipeScore.setText(String.valueOf(recipe.score + "% of people liked this recipe!"));

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

        // Add Instructions
        for(int i=0;i<recipe.instructions.size();i++)
        {
            TextView instruction = new TextView(this);
            instruction.setText(String.valueOf(i+1)+"- "+recipe.instructions.get(i) +"\n");
            recipeInstructions.addView(instruction);
        }

        //Check if favorited or no
        favorited_recipes = dbh.getFavorites(Integer.parseInt(currentUserID));
        if(favorited_recipes != null)
        {
            boolean flag = false;
            while(favorited_recipes.moveToNext()){
                if(favorited_recipes.getInt(0) == recipe.id){
                    isFavorited = true;
                    favorite_icon.setImageResource(R.drawable.icon_favorite_clicked);
                    flag=true;
                    break;
                }
            }
            if(!flag){
                isFavorited = false;
                favorite_icon.setImageResource(R.drawable.icon_favorite_unclicked);
            }
        } else {
            isFavorited = false;
            favorite_icon.setImageResource(R.drawable.icon_favorite_unclicked);
        }

        // Favorite icon pressed
        favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite_pressed(v);
            }
        });

    }

    public void favorite_pressed(View v){
        if (isFavorited) {
            dbh.removeFromFavorites(Integer.parseInt(currentUserID), recipe.id);
            favorite_icon.setImageResource(R.drawable.icon_favorite_unclicked);
            Toast.makeText(this, "Removed Recipe From Favorites", Toast.LENGTH_LONG).show();
            isFavorited = false;
        } else {
            dbh.addToFavorites(recipe.id, Integer.parseInt(currentUserID), recipe.name);
            favorite_icon.setImageResource(R.drawable.icon_favorite_clicked);
            Toast.makeText(this, "Added Recipe to Favorites", Toast.LENGTH_LONG).show();
            isFavorited = true;
        }
    }

}