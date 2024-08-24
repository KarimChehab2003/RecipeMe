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
import java.util.regex.*;

public class SignUp extends AppCompatActivity {

    enum passstate {length_error,no_caps,no_digits,no_symbols,valid};
    DBhelper dbh = new DBhelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void signup_pressed(View v){
        EditText username = (EditText) findViewById(R.id.editText_username);
        EditText email = (EditText) findViewById(R.id.editText_email);
        EditText password = (EditText) findViewById(R.id.editText_password);
        EditText RePassword = (EditText) findViewById(R.id.editText_repassword);

        Boolean correctflag = true;

        if(!validemail(email.getText().toString())){
            Toast.makeText(this,"email should follow this pattern : JohnDoe@domain.com",Toast.LENGTH_LONG).show();
            correctflag =false;
        }
        if(!password.getText().toString().equals(RePassword.getText().toString())){
            Toast.makeText(this,"password do not match",Toast.LENGTH_LONG).show();
            correctflag =false;
        } else{
            passstate validation = validpass(password.getText().toString());
           if(validation !=passstate.valid){
               correctflag =false;
            switch(validation){
                case length_error:{
                    Toast.makeText(this,"your password's length should be between 8 to 20 characters",Toast.LENGTH_LONG).show();
                    break;
                }
                case no_caps:{
                    Toast.makeText(this,"your password should include capitals",Toast.LENGTH_LONG).show();

                    break;
                }
                case no_digits:{

                    Toast.makeText(this,"your password should include digits",Toast.LENGTH_LONG).show();

                    break;
                }
                case no_symbols:{
                    Toast.makeText(this,"your password should include symbols ",Toast.LENGTH_LONG).show();
                    break;
                }
            }
           }

           }

        if(!correctflag){
            return; // in case of incorrect information
        }



        dbh.createNewUser(username.getText().toString(),email.getText().toString(),password.getText().toString());


        Toast.makeText(this, "User successfully created", Toast.LENGTH_SHORT).show();

        // intent to the next page with registered use


    }


    public Boolean validemail (String email){

        Pattern mailpat = Pattern.compile(".+@.+.com");

        Matcher mailmatch = mailpat.matcher(email);

        return mailmatch.find();
    }


    public passstate validpass(String pass){


        passstate verdict = null;
        Pattern checklength = Pattern.compile(".{8,20}");
        Pattern checkcaps = Pattern.compile("[A-Z]");
        Pattern checksymbols = Pattern.compile("\\W");
        Pattern checkdigits = Pattern.compile("\\d");


        Matcher matchlength = checklength.matcher(pass);
        Matcher matchcaps = checkcaps.matcher(pass);
        Matcher matchsymbols = checksymbols.matcher(pass);
        Matcher matchdigits = checkdigits.matcher(pass);


        if(!matchlength.find()) {
            verdict = passstate.length_error;
        }else if (!matchsymbols.find()) {
            verdict =passstate.no_symbols;
        }else if(!matchdigits.find()) {
            verdict = passstate.no_digits;
        }else if(!matchcaps.find()) {
            verdict= passstate.no_caps;
        }else {
            verdict = passstate.valid;
        }


        return verdict;
    }


}