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
    String text;
    Context context;
    Button button;

    public MyTTS(Context context,String text,Button button){
        this.text=text;
        this.context=context;
        this.button=button;
        initialTTS();
    }

    public MyTTS(Context context, TextView textView, Button button){
        text= textView.getText().toString();
        this.context=context;
        this.button=button;
        initialTTS();
    }

    public MyTTS(Context context,Button textButton,Button button){
        this.text=textButton.getText().toString();
        this.context=context;
        this.button=button;
        initialTTS();
    }

    private void initialTTS(){
        mTTS=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result;
                    //Check language is English or Khmer
                    if(!text.isEmpty() && Character.isAlphabetic(text.charAt(0)))
                        result=mTTS.setLanguage(Locale.ENGLISH);
                    else
                        result=mTTS.setLanguage(new Locale("km"));

                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.d(TAG, "Language not supported");
                    }
                }else {
                    Log.d(TAG, "Initialization failed");
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
    }

    //terminate TTS in OnDestroy is a good practise
    public TextToSpeech getTTS(){
        return mTTS;
    }

}
