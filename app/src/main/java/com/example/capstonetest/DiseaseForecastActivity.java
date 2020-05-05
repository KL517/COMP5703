package com.example.capstonetest;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiseaseForecastActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private MyTTS mytts;
    public ArrayList<String> stringList;
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
        mytts=new MyTTS(this);
        mytts.setSpeakButton(tx,button_speak);
        mytts.setSpeakButton(button,buttonspeak_select);
        mytts.setSpeakButton(button_1,buttonspeak_next);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRicevariety();
            }
        });
        createNotificationChannel();
    }

    public void openRicevariety(){
        try {
            stringList = (ArrayList<String>) getIntent().getStringArrayListExtra("ListString");
        } catch (Exception e) {
        }
        if (stringList == null) {
            stringList=new ArrayList<>();
        }
        if (!stringList.isEmpty()) {
            stringList.set(0,currentDateString);
        }else
            stringList.add(currentDateString);
        Intent intent= new Intent(this, RiceVariety.class);
        intent.putStringArrayListExtra("ListString",stringList);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        TextToSpeech tts=mytts.getTTS();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayofmonth);
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        System.out.println("0000000000000000000000-----------------");
        TextView textview = (TextView) findViewById(R.id.tx_day);
        textview.setText(currentDateString);
        mytts.setSpeakButton(textview,buttonspeak_date);
    }

    private void setLocale(String string){
        Locale locale = new Locale(string);
        locale.setDefault(locale);
        Configuration configuration=getResources().getConfiguration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics(
        ));
    }

    private void createNotificationChannel() {
        String title = "this is title";
        String message = "this is message";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), ImageRecognitionActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
