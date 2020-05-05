package com.example.capstonetest;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MyTTS {
    private static final String TAG = "MyTTS";
    TextToSpeech mTTS;
    Context context;

    public MyTTS(Context context) {
        this.context = context;
        initialTTS();
    }


    private void initialTTS() {
        mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result;
                    result = mTTS.setLanguage(new Locale("km"));
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d(TAG, "Language not supported");
                    }
                } else {
                    Log.d(TAG, "Initialization failed");
                }
            }
        });
    }

    public void setSpeakButton(TextView textView, Button button) {
        final String text = textView.getText().toString();
        //Check language is English or Khmer
        if (!text.isEmpty() && Character.isAlphabetic(text.charAt(0)))
            mTTS.setLanguage(Locale.ENGLISH);
            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public void setSpeakButton(String string, Button button) {
        final String text = string;
        //Check language is English or Khmer
        if (!text.isEmpty() && Character.isAlphabetic(text.charAt(0)))
            mTTS.setLanguage(Locale.ENGLISH);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public void setSpeakButton(Button textButton, Button button) {
        final String text = textButton.getText().toString();
        //Check language is English or Khmer
        if (!text.isEmpty() && Character.isAlphabetic(text.charAt(0)))
            mTTS.setLanguage(Locale.ENGLISH);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    //terminate TTS in OnDestroy is a good practise
    public TextToSpeech getTTS() {
        return mTTS;
    }

}
