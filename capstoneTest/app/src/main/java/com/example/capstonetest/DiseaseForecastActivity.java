package com.example.capstonetest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiseaseForecastActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public ArrayList<String> stringList=new ArrayList<String>();
    public String currentDateString;
    private Button button;
    private Button button_1;
    private Button button_speak;
    private Button buttonspeak_select;
    private Button buttonspeak_next;
    private Button buttonspeak_date;
    private TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setLocale("en");
         //setLocale("km");
        setContentView(R.layout.activity_disease_forecast);
        tx=findViewById(R.id.tx_q1);
        button=findViewById(R.id.bt_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        button_speak=findViewById(R.id.sound);
        buttonspeak_next=findViewById(R.id.sound_next);
        buttonspeak_select=findViewById(R.id.sound_select);
        button_1 =findViewById(R.id.bt_next_ricevarirty);
        buttonspeak_date=findViewById(R.id.sound_date);

        MyTTS mytts=new MyTTS(this,tx,button_speak);
        MyTTS mytts1=new MyTTS(this,button,buttonspeak_select);
        MyTTS mytts2=new MyTTS(this,button_1,buttonspeak_next);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRicevariety();
            }
        });

    }



    public void openRicevariety(){
        stringList.add(currentDateString);
        Intent intent= new Intent(this, RiceVariety.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayofmonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textview = (TextView) findViewById(R.id.tx_day);
        textview.setText(currentDateString);
        MyTTS mytts0=new MyTTS(this,textview,buttonspeak_date);

    }

    private void setLocale(String string){
        Locale locale = new Locale(string);
        locale.setDefault(locale);
        Configuration configuration=getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics(
        ));
    }
}
