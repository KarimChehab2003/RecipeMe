package com.example.jsonexample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Voice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        String userID = getIntent().getStringExtra("currentUserID");
        String userName = getIntent().getStringExtra("currentUserName");

        VoiceFragment fragment = new VoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentUserID", userID);
        bundle.putString("currentUserName", userName);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.voice_fragment_container, fragment)
                .commit();
    }
}