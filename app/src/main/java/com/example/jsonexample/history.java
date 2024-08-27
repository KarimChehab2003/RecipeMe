package com.example.jsonexample;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class history extends AppCompatActivity implements RecyclerViewInterface {
    List<Recipe> recipes_list;
    Cursor retrieved_records;
    String TAG ="history_activity";
    String currentUserName;
    String currentUserID;
    DBhelper dbh = new DBhelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        Intent intent =getIntent();
        currentUserName = intent.getStringExtra("currentUserName");
        currentUserID = intent.getStringExtra("currentUserID");

        RecyclerView history_recyclerView = findViewById(R.id.history_recyclerView);

        recipes_list = new ArrayList<>();
         history_adapter recyc_hist_fav = new history_adapter(recipes_list,getApplicationContext());

          retrieved_records = dbh.getHistory(Integer.parseInt(currentUserID));

        history_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        history_recyclerView.setAdapter(recyc_hist_fav);

        while(retrieved_records.moveToNext()){
            apiRequestGET(retrieved_records.getString(1),recyc_hist_fav,"10" , retrieved_records.getInt(0));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    public void apiRequestGET(String query,history_adapter recyclerAdapter, String size , int recipe_id){
        if(size.equals("default"))
            size = "3";

        AsyncHttpClient client = new AsyncHttpClient();
        String fullQuery = "https://tasty.p.rapidapi.com/recipes/list?from=0&size=" +size+ "&tags=under_30_minutes&q=" + query;
        // Set the headers
        client.addHeader("x-rapidapi-key", "22dd1262bfmsh49d655961be4f5ep12060cjsn32dab31309f4");
        client.addHeader("x-rapidapi-host", "tasty.p.rapidapi.com");

        client.get(fullQuery,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Handle JSON Object response

                        try {

                            JSONArray recipes = response.getJSONArray("results"); //Object containing returned results
                            for (int i = 0; i < recipes.length(); i++) {
                                JSONObject recipe = recipes.getJSONObject(i); // for each recipe in recipes
                                List<String> recipeInstructions = new ArrayList<>();
                                Map<String, Integer> recipeNutrition = new HashMap<>();

                                // Adding instructions and nutrition facts into recipe
                                JSONArray instructions = recipe.optJSONArray("instructions");
                                addInstructionToRecipe(instructions,recipeInstructions);

                                JSONObject nutrition = recipe.optJSONObject("nutrition");
                                addNutritionToRecipe(nutrition,recipeNutrition);

                                float score = (float) recipe.optDouble("score", 1.000);

                                Recipe newRecipe = new Recipe(
                                        Integer.parseInt(recipe.optString("id", "0")),
                                        recipe.optString("name", ""),
                                        recipe.optString("total_time_minutes", ""),
                                        recipe.optString("description", ""),
                                        recipeInstructions,
                                        recipe.optString("original_video_url", ""),
                                        recipe.optString("thumbnail_url", ""),
                                        recipeNutrition,
                                        score
                                );

                                System.out.println(
                                        newRecipe.id +" "+
                                                newRecipe.name +" "+
                                                newRecipe.time +" "+
                                                newRecipe.description +" "+
                                                newRecipe.instructions.size() + " "+
                                                newRecipe.nutritionFacts.size());

                                if(newRecipe.id == recipe_id) {
                                    recipes_list.add(newRecipe);
                                }

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

    public void addInstructionToRecipe(JSONArray instructions,List<String> recipeInstructions) {
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
    }

    public void addNutritionToRecipe(JSONObject nutrition,Map<String, Integer> recipeNutrition) {
        if (nutrition != null) {
            for (Iterator<String> it = nutrition.keys(); it.hasNext(); ) {
                String key = it.next();
                // Ensure proper type casting
                Object value = nutrition.opt(key);
                if (value instanceof Integer) {
                    recipeNutrition.put(key, (Integer) value);
                } else if (value instanceof Double) {
                    recipeNutrition.put(key, ((Double) value).intValue());
                }
            }
        }
    }



    @Override
    public void onItemClick(int position) {
        // to be directed to the recipe info page
        Intent intent = new Intent(this,RecipeDetails.class);
        intent.putExtra("recipe" , recipes_list.get(position));
        startActivity(intent);
    }
}