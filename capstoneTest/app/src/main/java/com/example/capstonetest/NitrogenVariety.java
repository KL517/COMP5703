package com.example.capstonetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NitrogenVariety extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public ArrayList<String> stringList=new ArrayList<String>();
    public String nitvariety;
    private Button button_speak;
    private Button button_speak1;
    private Button buttonspeak_next;
    private Button button_nt1;
    private TextView tx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nitrogenvariety);

        stringList=(ArrayList<String>)getIntent().getStringArrayListExtra("ListString");



        Spinner spinner = findViewById(R.id.sp_nit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.nitrogentype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button_nt1 = (Button)findViewById(R.id.bt_next_nitrogenamount);
        button_nt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opennitrogenamount();
            }
        });

        tx=findViewById(R.id.tx_q4);
        button_speak=findViewById(R.id.sound);
        button_speak1=findViewById(R.id.sound_variety);
        buttonspeak_next=findViewById(R.id.sound_next);

        MyTTS mytts=new MyTTS(this,tx,button_speak);
        MyTTS mytts1=new MyTTS(this,button_nt1,buttonspeak_next);

    }


    public void opennitrogenamount(){
        if (stringList.size() > 3) {
            for (int i = 3; i < stringList.size(); i++) {
                stringList.remove(3);
            }
        }
        stringList.add(nitvariety);
        Intent intent= new Intent(this, NitrogenAmount.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
       nitvariety = adapterView.getItemAtPosition(position).toString();
        //Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
        MyTTS mytts=new MyTTS(this,nitvariety,button_speak1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
