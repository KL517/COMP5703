package com.example.capstonetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NitrogenAmount extends AppCompatActivity {
    public ArrayList<String> stringList=new ArrayList<String>();
    public String nitamount;
    private Button tick;
    private TextView txnumber;
    private EditText editnumber;
    private Button button_speak;
    private Button buttonspeak_done;
    private Button button_done;
    private TextView tx;
    MyTTS mytts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nitrogenamount);


        stringList=(ArrayList<String>)getIntent().getStringArrayListExtra("ListString");

        tx=findViewById(R.id.tx_q5);
        button_speak=findViewById(R.id.sound);
        buttonspeak_done=findViewById(R.id.sound_done);
        button_done=findViewById(R.id.bt_done);
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForecast();
            }
        });
        mytts=new MyTTS(this);
        mytts.setSpeakButton(tx,button_speak);
        mytts.setSpeakButton(button_done,buttonspeak_done);
        txnumber=findViewById(R.id.tx_q5_value);
        editnumber=findViewById(R.id.nitnumber);
        tick=findViewById(R.id.bt_tick);

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=Integer.parseInt(editnumber.getText().toString());
                if (a>=0&&a<=500) {
                    nitamount=""+a;
                    txnumber.setText(nitamount);
                    closekeyboard();
                    editnumber.clearFocus();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please input the number between 0 - 500 !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onDestroy() {
        TextToSpeech tts=mytts.getTTS();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void openForecast(){
        if (stringList.size() > 4) {
            stringList.set(4, nitamount);
        }else
            stringList.add(nitamount);
        Intent intent= new Intent(this, Forecast.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);
    }

    private void closekeyboard(){
        View view = this.getCurrentFocus();
        if(view!= null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
