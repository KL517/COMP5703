package com.example.capstonetest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Forecast extends AppCompatActivity {
    public ArrayList<String> stringList;
    private String date;
    private String ricevariety;
    private String riceamount;
    private String nitvariety;
    private String nitamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        stringList=(ArrayList<String>)getIntent().getStringArrayListExtra("ListString");


        date=stringList.get(0);
        ricevariety=stringList.get(1);
        riceamount=stringList.get(2);
        nitvariety=stringList.get(3);
        nitamount=stringList.get(4);

        TextView q3=findViewById(R.id.tx_q3_a);
        TextView q4=findViewById(R.id.tx_q4_a);
        TextView q5=findViewById(R.id.tx_q5_a);
        TextView q6=findViewById(R.id.tx_q6_a);
        TextView q7=findViewById(R.id.tx_q7_a);

        q3.setText(date);
        q4.setText(ricevariety);
        q5.setText(riceamount);
        q6.setText(nitvariety);
        q7.setText(nitamount);


    }
}
