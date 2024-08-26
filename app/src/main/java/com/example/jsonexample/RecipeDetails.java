package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;


import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetails extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        //get Clicked on recipe
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");
        //declare views
        VideoView video = findViewById(R.id.recipeDetailsVideo);
        TextView recipeName = findViewById(R.id.recipeDetailsName);
        TextView recipeDesc = findViewById(R.id.recipeDetailsInstructions);
        TextView recipeTime = findViewById(R.id.recipeDetailsTime);
        TableLayout nutritionFacts = findViewById(R.id.nutritionTable);
        //set views content
        video.setVideoURI(recipe.videoURL);
        recipeName.setText(recipe.name);
        recipeDesc.setText(recipe.description);
        recipeTime.setText(recipe.time);


    }
    };
