package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class histroy extends AppCompatActivity {

    String currentUserName;
    String currentUserID;
    DBhelper dbh = new DBhelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_histroy);

        Intent intent =getIntent();
        currentUserName = intent.getStringExtra("currentUserName");
        currentUserID = intent.getStringExtra("currentUserID");

        RecyclerView history_recyclerView = findViewById(R.id.history_recyclerView);

        List<Recipe> recipes_list = new ArrayList<Recipe>();
        recipes_list = dbh.getHistory(Integer.parseInt(currentUserID));

        history_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        history_recyclerView.setAdapter(new history_adapter(recipes_list,getApplicationContext()));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}