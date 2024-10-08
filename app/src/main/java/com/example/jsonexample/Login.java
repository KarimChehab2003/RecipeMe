package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    public User loggedin;
    DBhelper dbh = new DBhelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login_pressed(View v){
        EditText email = (EditText) findViewById(R.id.editText_email);
        EditText password= (EditText) findViewById(R.id.editText_password);

        this.loggedin = dbh.getUser(email.getText().toString(),password.getText().toString());

        if( loggedin == null){
            Toast.makeText(this, "No users with such info", Toast.LENGTH_SHORT).show();
        }else{

            //intent to next page with the made user
            Intent intent = new Intent(this,MainPage.class);

            //Intent Extras that contain username and ID
            intent.putExtra("currentUserName",loggedin.getName());
            intent.putExtra("currentUserID",String.valueOf(loggedin.getId()));
            startActivity(intent);

           // Toast.makeText(this, "logged in successfully", Toast.LENGTH_SHORT).show();

        }
    }

}