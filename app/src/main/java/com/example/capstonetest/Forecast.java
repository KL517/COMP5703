package com.example.capstonetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
        MyIO.saveItemsToFile(this,stringList);
        ArrayList arrayList=MyIO.readItemsFromFile(this);
        System.out.println("result is-------------------"+arrayList.toString());

    }



}
