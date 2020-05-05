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

public class RiceSeedingRate extends AppCompatActivity {
    public ArrayList<String> stringList=new ArrayList<String>();
    public String riceamount;
    private Button button_nt1;
    private Button tick;
    private TextView txnumber;
    private EditText editnumber;
    private Button button_speak;
    private Button buttonspeak_next;
    private TextView tx;
    MyTTS mytts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riceseedingrate);

        stringList=(ArrayList<String>)getIntent().getStringArrayListExtra("ListString");
        button_nt1 = findViewById(R.id.bt_next_nitrogenvariety);
        button_nt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNitro();
            }
        });
        tx=findViewById(R.id.tx_q3);
        button_speak=findViewById(R.id.sound);
        buttonspeak_next=findViewById(R.id.sound_next);
        mytts=new MyTTS(this);
        mytts.setSpeakButton(tx,button_speak);
        mytts.setSpeakButton(button_nt1,buttonspeak_next);
        txnumber=findViewById(R.id.tx_q3_value);
        editnumber=findViewById(R.id.ricenumber);
        tick=findViewById(R.id.bt_tick);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=Integer.parseInt(editnumber.getText().toString());
                if (a>=0&&a<=300) {
                    riceamount=""+a;
                    txnumber.setText(riceamount);///////////////////
                    closekeyboard();
                    editnumber.clearFocus();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please input the number between 0 - 300 !",Toast.LENGTH_SHORT).show();
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

    private void closekeyboard(){
        View view = this.getCurrentFocus();
        if(view!= null){
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public void openNitro(){
        if (stringList.size() > 2) {
            stringList.set(2,riceamount);
        }else
            stringList.add(riceamount);
        Intent intent= new Intent(this, NitrogenVariety.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);
    }
}
