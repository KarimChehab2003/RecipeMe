package com.example.jsonexample;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VoiceFragment extends Fragment {

    private String currentUserID;
    private String currentUserName;

    private String micResult;

    private ActivityResultLauncher<Void> speechRecognitionLauncher;

    public VoiceFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserID = getArguments().getString("currentUserID");
            currentUserName = getArguments().getString("currentUserName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        Button micButton = view.findViewById(R.id.mic_button);
        Button searchMicResultButton = view.findViewById(R.id.mic_search_btn);

        speechRecognitionLauncher = registerForActivityResult(new VoiceResultContract(), result ->{
            if(result != null){
                micResult = result;
                ((TextView) view.findViewById(R.id.mic_result)).setText("Did you say : " + String.valueOf(result));
            }
        });

        micButton.setOnClickListener(v -> {
            speechRecognitionLauncher.launch(null);
        });

        searchMicResultButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(requireContext(), MainPage.class);
            intent2.putExtra("currentUserID", currentUserID);
            intent2.putExtra("currentUserName", currentUserName);
            intent2.putExtra("micSearchResult", micResult);
            startActivity(intent2);
        });

        return view;
    }
}