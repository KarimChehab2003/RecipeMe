package com.example.jsonexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;

import androidx.activity.result.contract.ActivityResultContract;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceResultContract extends ActivityResultContract<Void,String> {
    public Intent createIntent(@NonNull Context context,Void input){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        return intent;
    }

    public String parseResult(int resultCode, Intent intent){
        if(resultCode == Activity.RESULT_OK && intent != null){
            ArrayList<String> textResult = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(textResult != null && !textResult.isEmpty()){
                return textResult.get(0);
            }
        }
        return null;
    }
}
