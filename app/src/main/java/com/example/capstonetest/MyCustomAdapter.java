package com.example.capstonetest;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyCustomAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;
    LayoutInflater inflater;
    MyTTS myTTS;



    public MyCustomAdapter(Activity activity, ArrayList<VideoDetails> videoDetailsArrayList,MyTTS myTTS){
        this.activity=activity;
        this.videoDetailsArrayList=videoDetailsArrayList;
        this.myTTS=myTTS;


    }
    @Override
    public Object getItem(int i) {
        return this.videoDetailsArrayList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater==null){
            inflater = this.activity.getLayoutInflater();

        }
        if(view==null){
            view = inflater.inflate(R.layout.custom_item,null);
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        TextView textView = (TextView)view.findViewById(R.id.mytitle);
        Button button = (Button)view.findViewById(R.id.titleSound);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.root);
        final VideoDetails videoDetails = (VideoDetails)this.videoDetailsArrayList.get(i);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, YoutubeVideo.class);
                i.putExtra("videoId",videoDetails.getVideoId());
                activity.startActivity(i);
            }
        });


        Picasso.get().load(videoDetails.getUrl()).into(imageView);
        textView.setText(videoDetails.getTitle());

        myTTS.setSpeakButton(textView,button);


        return view;
    }

    @Override
    public int getCount() {
        return this.videoDetailsArrayList.size();
    }
}
