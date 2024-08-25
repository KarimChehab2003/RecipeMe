package com.example.jsonexample;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jsonexample.Login;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import com.squareup.picasso.Picasso;
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
        // Image construction
        ImageView imageView = findViewById(R.id.imageView);
        String imageUrl = "https://img.buzzfeed.com/thumbnailer-prod-us-east-1/45b4efeb5d2c4d29970344ae165615ab/FixedFBFinal.jpg\n";

        Picasso.get()
                .load(imageUrl)
                .into(imageView);

        // Create an instance of AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        // Set the headers
        client.addHeader("x-rapidapi-key", "22dd1262bfmsh49d655961be4f5ep12060cjsn32dab31309f4");
        client.addHeader("x-rapidapi-host", "tasty.p.rapidapi.com");

        // Make the GET request
        client.get("https://tasty.p.rapidapi.com/recipes/list?from=0&size=20&tags=under_30_minutes&q=cake",
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Handle JSON Object response
                        try {
                            JSONArray recipes = response.getJSONArray("results");
                            for (int i = 0; i < recipes.length(); i++) {
                                JSONObject recipe = recipes.getJSONObject(i);
                                String recipeName = recipe.getString("name");
                                // Do something with the recipe name
                                Log.d(TAG, "Recipe: " + recipeName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
    }

}
