package com.example.jsonexample;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Recipe> recipeList;

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
        recyclerView = findViewById(R.id.recommendationsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize recipe list and adapter
        recipeList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(recipeList);
        recyclerView.setAdapter(recyclerAdapter);

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

                                List<String> recipeInstructions = new ArrayList<>();
                                Map<String, Integer> recipeNutritions = new HashMap<>();

                                // Get the instructions array from the recipe object
                                JSONArray instructions = recipe.optJSONArray("instructions");
                                if (instructions != null) {
                                    // Loop through the instructions array
                                    for (int j = 0; j < instructions.length(); j++) {
                                        JSONObject instruction = instructions.optJSONObject(j);
                                        if (instruction != null) {
                                            // Get the display_text from the instruction object
                                            String displayText = instruction.optString("display_text", "");
                                            recipeInstructions.add(displayText);
                                        }
                                    }
                                }

                                JSONObject nutrition = recipe.optJSONObject("nutrition");
                                if (nutrition != null) {
                                    for (Iterator<String> it = nutrition.keys(); it.hasNext(); ) {
                                        String key = it.next();
                                        // Ensure proper type casting
                                        Object value = nutrition.opt(key);
                                        if (value instanceof Integer) {
                                            recipeNutritions.put(key, (Integer) value);
                                        } else if (value instanceof Double) {
                                            recipeNutritions.put(key, ((Double) value).intValue());
                                        }
                                    }
                                }
                                float score = (float) recipe.optDouble("score", 1.000);

                                Recipe newRecipe = new Recipe(
                                        Integer.parseInt(recipe.optString("id", "0")),
                                        recipe.optString("name", ""),
                                        recipe.optString("total_time_minutes", ""),
                                        recipe.optString("description", ""),
                                        recipeInstructions,
                                        recipe.optString("original_video_url", ""),
                                        recipe.optString("thumbnail_url", ""),
                                        recipeNutritions,
                                        score
                                );

                                System.out.println(newRecipe.score);

                                recipeList.add(newRecipe);
                            }

                            // Notify adapter about data changes on the main thread
                            runOnUiThread(() -> recyclerAdapter.notifyDataSetChanged());

                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Number format error", e);
                        }
                    }
                });
    }
}
