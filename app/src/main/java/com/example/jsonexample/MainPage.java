package com.example.jsonexample;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainpagemenu , menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setting up recycler view
        RecyclerView recyclerView = findViewById(R.id.recommendationsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Recipe> recipeList = new ArrayList<>();
        Recipe recipeObject;
        ArrayList<String> recipeInstructions = null;
        Map<String,Integer> recipeNutritions = null;

        // Create an instance of AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        // Set the headers
        client.addHeader("x-rapidapi-key", "22dd1262bfmsh49d655961be4f5ep12060cjsn32dab31309f4");
        client.addHeader("x-rapidapi-host", "tasty.p.rapidapi.com");

        // Make the GET request
        client.get("https://tasty.p.rapidapi.com/recipes/list?from=0&size=3&tags=under_30_minutes&q=cake",
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Handle JSON Object response
                        try {
                            JSONArray recipes = response.getJSONArray("results");
                            for (int i = 0; i < recipes.length(); i++) {
                                JSONObject recipe = recipes.getJSONObject(i);
                                
                                // Get the instructions array from the recipe object
                                JSONArray instructions = recipe.getJSONArray("instructions");

                                // Loop through the instructions array
                                for (int j = 0; j < instructions.length(); j++) {
                                    // Get each instruction object
                                    JSONObject instruction = instructions.getJSONObject(j);
                                    
                                    // Get the display_text from the instruction object
                                    String displayText = instruction.getString("display_text");
                                    recipeInstructions.add(displayText);
                                }

                                JSONObject nutrition = recipe.getJSONObject("nutrition");
                                for (Iterator<String> it = nutrition.keys(); it.hasNext(); ) {
                                    String key = it.next();
                                    recipeNutritions.put(key, (Integer) nutrition.get(key));
                                }
                                
                                recipeList.add(new Recipe(Integer.parseInt(recipe.getString("id")),recipe.getString("name"),recipe.getString("total_time_minutes"),recipe.getString("description"),recipeInstructions,recipe.getString("original_video_url"),recipe.getString("thumbnail_url"),recipeNutritions,Float.parseFloat(recipe.getString("score"))));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
        recyclerView.setAdapter(new RecyclerAdapter(recipeList));
    }

}
