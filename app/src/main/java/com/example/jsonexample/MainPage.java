package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

enum suggestionIngredients {
    TOMATO,
    LETTUCE,
    CHICKEN,
    BEEF,
    CARROT,
    POTATO,
    ONION,
    GARLIC,
    CHEESE,
    BUTTER,
    MILK,
    EGG,
    FLOUR,
    SUGAR,
    SALT,
    PEPPER,
    RICE,
    PASTA,
    BREAD,
    FISH;
}

public class MainPage extends AppCompatActivity implements RecyclerViewInterface {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Recipe> recipeList;
    String currentUserName;
    String currentUserID;
   DBhelper dbh = new DBhelper(this);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainpagemenu , menu);
        MenuItem menuitem = menu.findItem(R.id.menu_history);

        menuitem.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this, history.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            startActivity(intent);

            return true;
        });

        MenuItem menuitem2 = menu.findItem(R.id.menu_favourites);
        menuitem2.setOnMenuItemClickListener(item->{
            Intent intent = new Intent(this,favourites.class);
            intent.putExtra("currentUserName",currentUserName);
            intent.putExtra("currentUserID",currentUserID);
            startActivity(intent);

            return true;
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        currentUserName = intent.getStringExtra("currentUserName");
        currentUserID = intent.getStringExtra("currentUserID");

        // Setting up textBox and searchButton
        TextView searchTextBox = findViewById(R.id.textQuery);
        Button searchButton = findViewById(R.id.searchButton);

        // Setting up recycler view
        recyclerView = findViewById(R.id.recommendationsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize recipe list and adapter
        recipeList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(recipeList,this);
        recyclerView.setAdapter(recyclerAdapter);

        // For Recommending recipes
        apiRequestGET(Recommend_trois(),recyclerAdapter,"default");

        //Make the GET request when clicked on search
        searchButton.setOnClickListener(v -> {
            recyclerAdapter.clearData();
            String query = searchTextBox.getText().toString();
            apiRequestGET(query,recyclerAdapter,"20");
        });

    }

    public static String Recommend_trois()
    {
        StringBuilder recIngs = new StringBuilder("");
        int ingredientNum = 3;
        for(int i =0 ; i < 3; i++)
        {
            suggestionIngredients[] ing = suggestionIngredients.values();
            Random rand = new Random();
            recIngs.append(ing[rand.nextInt(ing.length)]).append(" ");
        }
        return recIngs.toString().toLowerCase();
    }

    public void apiRequestGET(String query,RecyclerAdapter recyclerAdapter, String size){
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
        dbh.addToHistory(recipeList.get(position).id,Integer.parseInt(currentUserID),recipeList.get(position).name);
        Intent intent = new Intent(this,RecipeDetails.class);
        intent.putExtra("recipe" , recipeList.get(position));
        startActivity(intent);
    }

}
