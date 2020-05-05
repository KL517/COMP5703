package com.example.capstonetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ImageRecognitionActivity extends AppCompatActivity {

    private static final int IMAGE_PICK=100;
    private static final String TAG = "MainActivity";
    private Button addImageBtn;
    Button takePhotoBtn;
    private ImageView imageView;
    private Uri imageUri;
    FirebaseVisionImage image;
    TextView textView;
    TextView BStv;
    String RB;
    String BS;
    Button btn1;
    Button btn2;
    TextView noticeTv;
    Button noticeBtn;
    MyTTS myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recognition);
        addImageBtn = findViewById(R.id.button3);
        takePhotoBtn=findViewById(R.id.button);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        BStv=findViewById(R.id.textView5);
        RB=textView.getText().toString();
        BS=BStv.getText().toString();
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        noticeTv=findViewById(R.id.Noticetv);
        noticeBtn=findViewById(R.id.NoticeBtn);
        myTTS = new MyTTS(this);
        myTTS.setSpeakButton(textView,btn1);
        myTTS.setSpeakButton(BStv,btn2);
        myTTS.setSpeakButton(noticeTv,noticeBtn);
        try {
            imageUri = getIntent().getParcelableExtra("image");
            imageView.setImageURI(imageUri);
            getProbability();
        } catch (Exception e) { }
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_PICK);
            }
        });
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ImageRecognitionActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getProbability(){
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();

        FirebaseVisionImageLabeler labeler;
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)  // Evaluate your model in the Firebase console
                            // to determine an appropriate value.
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
                image = FirebaseVisionImage.fromFilePath(this, imageUri);
//                image = FirebaseVisionImage.fromBitmap(imageBitMap);
            Log.d(TAG, "getProbability: it goes well");
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            Log.d(TAG, "onSuccess: yes");
//                            stringBuffer=new StringBuffer();
                            for (FirebaseVisionImageLabel label: labels) {
                                if(label.getText().equals("HY")){
                                    textView.setText(RB+" "+
                                            Math.round(label.getConfidence()*1000)/10.0+"%");
                                    textView.setVisibility(View.VISIBLE);
                                    btn1.setVisibility(View.VISIBLE);
                                    noticeBtn.setVisibility(View.INVISIBLE);
                                    noticeTv.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    BStv.setText(BS+" "+
                                            Math.round(label.getConfidence()*1000)/10.0+"%");
                                    BStv.setVisibility(View.VISIBLE);
                                    btn2.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: no");
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, "onCreate: model error");
        }
    }

    protected void onDestroy() {
        TextToSpeech tts=myTTS.getTTS();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            getProbability();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
