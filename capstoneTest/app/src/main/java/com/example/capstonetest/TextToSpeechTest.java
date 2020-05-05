package com.example.capstonetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TextToSpeechTest extends AppCompatActivity {
    private static final String TAG = "TextToSpeechTest";
    TextToSpeech mTTS;
    TextView textView;
    TextView textView2;
    Button button;
    Button button2;
    Button btnExample;
    Button btnLanguage;
    Button btnGo;
    Button btnGoSpeak;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_text_to_speech_test);
        btnGoSpeak=findViewById(R.id.btnGoSpeak);
        btnGo=findViewById(R.id.btnGo);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        button=findViewById(R.id.button);
        button2=findViewById(R.id.button2);
        btnExample=findViewById(R.id.btnExample);
        btnLanguage=findViewById(R.id.btnLanguage);
        textView3=findViewById(R.id.textView3);
        MyTTS myTTS = new MyTTS(this, textView,button);
        MyTTS myTTS1 = new MyTTS(this,textView2,button2);
        MyTTS myTTS2=new MyTTS(this,textView3,btnGoSpeak);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TextToSpeechTest.this,localisationTest.class));
            }
        });
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });
    }

    private void showLanguageDialog() {
        final String[] languageList={"English","\n" +
                "ខ្មែរ"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languageList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setLocale("en");
                    recreate();
                }else{
                    setLocale("km");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void setLocale(String string){
        Locale locale = new Locale(string);
        locale.setDefault(locale);
        Configuration configuration=getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",string);
        editor.apply();
}



    private void loadLocale(){
        SharedPreferences sharedPreferences=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=sharedPreferences.getString("My_Lang","");
        setLocale(language);
    }


}
