package com.example.capstonetest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private MyTTS myTTS;



    String API_KEY="AIzaSyAeCDZ7MSz2a0Lg7p4F6eOhjqD4OH5CbAM";
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
//    String url="https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UC1O-B05t2T1GjCjUo9bNuzw&maxResults=10&key=AIzaSyAeCDZ7MSz2a0Lg7p4F6eOhjqD4OH5CbAM";
    String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=3" +
        "&playlistId=PLCsuqbR8ZoiCO028dfW1n5XmbHGEmim1g&key=AIzaSyAeCDZ7MSz2a0Lg7p4F6eOhjqD4OH5CbAM";
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setLocale("km"); //-> Khmer
        loadLocale();
        setContentView(R.layout.activity_main);

        Button button1 = (Button)findViewById(R.id.forecastSound);
        Button button2 = (Button)findViewById(R.id.healthSound);
        Button button3 = (Button)findViewById(R.id.otherSound);
        Button button4 = (Button)findViewById(R.id.switcher);

        TextView textView1 = (TextView)findViewById(R.id.forecastTitle);
        TextView textView2 = (TextView)findViewById(R.id.healthTitle);
        TextView textView3 = (TextView)findViewById(R.id.otherTitle);

        myTTS = new MyTTS(this);

        myTTS.setSpeakButton(textView1,button1);
        myTTS.setSpeakButton(textView2,button2);
        myTTS.setSpeakButton(textView3,button3);

        listView=(ListView)findViewById(R.id.listView);
        videoDetailsArrayList =new ArrayList<>();
        myCustomAdapter=new MyCustomAdapter(MainActivity.this,videoDetailsArrayList,myTTS);
        displayVideos();

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


    private void displayVideos() {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("items");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId=jsonObject1.getJSONObject("contentDetails");
                        JSONObject jsonObjectSnippet=jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectDefault=jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                        String video_id=jsonVideoId.getString("videoId");

                        VideoDetails vd = new VideoDetails();
                        vd.setVideoId(video_id);
                        vd.setTitle(jsonObjectSnippet.getString("title"));
                        vd.setDescription(jsonObjectSnippet.getString("description"));
                        vd.setUrl(jsonObjectDefault.getString("url"));

                        videoDetailsArrayList.add(vd);

                    }

                    listView.setAdapter(myCustomAdapter);
                    myCustomAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }
        );

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        TextToSpeech tts = myTTS.getTTS();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
