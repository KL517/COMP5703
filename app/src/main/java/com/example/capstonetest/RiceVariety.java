package com.example.capstonetest;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RiceVariety extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public ArrayList<String> stringList=new ArrayList<String>();
    public String ricevariety;
    private Button button_speak;
    private Button button_speak1;
    private Button buttonspeak_next;
    private Button button_nt1;
    private TextView tx;
    MyTTS mytts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricevariety);
        stringList=(ArrayList<String>)getIntent().getStringArrayListExtra("ListString");
        tx=findViewById(R.id.tx_q2);
        Spinner spinner = findViewById(R.id.sp_rice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.kindofrice,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        button_nt1 =findViewById(R.id.bt_next_riceseedingrate);
        button_nt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRiceseedingrate();
            }
        });
        button_speak=findViewById(R.id.sound);
        button_speak1=findViewById(R.id.sound_variety);
        buttonspeak_next=findViewById(R.id.sound_next);
        mytts=new MyTTS(this);
        mytts.setSpeakButton(tx,button_speak);
        mytts.setSpeakButton(button_nt1,buttonspeak_next);
    }



    public void openRiceseedingrate(){
        if (stringList.size() > 1) {
            stringList.set(1,ricevariety);
        }else
            stringList.add(ricevariety);
        Intent intent= new Intent(this, RiceSeedingRate.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);
    }

    protected void onDestroy() {
        TextToSpeech tts=mytts.getTTS();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        ricevariety = adapterView.getItemAtPosition(position).toString();
        mytts.setSpeakButton(ricevariety,button_speak1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
