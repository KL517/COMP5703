package com.example.capstonetest;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeVideo extends YouTubeBaseActivity {
    private static final String TAG ="aa" ;
    private String videoUrl;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_video);
        youTubePlayerView=findViewById(R.id.youtubeView);
        getIntentData();



        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize("AIzaSyCa5bG92yF7Mkr2kFJB3M75wkeodWdZ0oI",onInitializedListener);
    }

//    public static void actionStart(Context context, String videoUrl) {
//
//        Intent intent = new Intent(context, YoutubeVideo.class);
//        intent.putExtra("videoUrl", videoUrl);
//        context.startActivity(intent);
//        Log.d(TAG, "getIntentData:before "+videoUrl);
//    }

    private void getIntentData() {
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra("videoId");

    }

}
