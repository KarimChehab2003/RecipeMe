package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    String currentUserName;
    String currentUserID;
    String currentUserEmail;

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

        menuitem4.setVisible(false);

        return true;
    }

    private TextView nameTv, emailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        currentUserName = intent.getStringExtra("currentUserName");
        currentUserID = intent.getStringExtra("currentUserID");
        currentUserEmail = intent.getStringExtra("currentUserEmail");

        nameTv = findViewById(R.id.tv_name);
        emailTv = findViewById(R.id.tv_email);

        // set text & image holder
        nameTv.setText(currentUserName);
        emailTv.setText(currentUserEmail);

    }

    public void logoutPressed(View v){
        Intent intent = new Intent(this,Welcome.class);
        startActivity(intent);
    }

}
