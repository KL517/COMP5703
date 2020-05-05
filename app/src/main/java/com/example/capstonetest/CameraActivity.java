package com.example.capstonetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CameraActivity extends AppCompatActivity {

    private Button back;
    private TextureView tv;
    private Button btn;
    private String mCameraId = "0"; //“0” stands for rear camera
    private final int RESULT_CODE_CAMERA=1;
    private CameraDevice cameraDevice;
    private CameraCaptureSession mPreviewSession;
    private CaptureRequest.Builder mCaptureRequestBuilder,captureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private ImageReader imageReader;
    private int height=0,width=0;
    private Size previewSize;
//    private ImageView iv;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static
    {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        tv = (TextureView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
//        iv= (ImageView) findViewById(R.id.iv);


        //create a button to return the homepage
//        back = (Button) findViewById(R.id.back);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(CameraActivity.this, Homepage.class);
//                startActivity(intent);
//            }
//        });


        //take photo
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        //set TextureView monitoring
        tv.setSurfaceTextureListener(surfaceTextureListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraDevice!=null) {
            stopCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    //TextureView monitoring
    private TextureView.SurfaceTextureListener surfaceTextureListener= new TextureView.SurfaceTextureListener() {

        //if access
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            CameraActivity.this.width=width;
            CameraActivity.this.height=height;
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

       //stop
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            stopCamera();
            return true;
        }

        //update
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    //open the camera
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        setCameraCharacteristics(manager);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Prompt user to open permission
                String[] perms = {"android.permission.CAMERA"};
                ActivityCompat.requestPermissions(CameraActivity.this,perms, RESULT_CODE_CAMERA);

            }else {
                manager.openCamera(mCameraId, stateCallback, null);
            }

        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }


    //set camera characteristics
    private void setCameraCharacteristics(CameraManager manager)
    {
        try
        {
            // get camera characteristics
            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(mCameraId);
            // get camera configuration
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // Get the largest size
            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),new CompareSizesByArea());

            imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, 2);
            //set getting photos' monitoring
            imageReader.setOnImageAvailableListener(imageAvailableListener,null);

            previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceTexture.class), width, height, largest);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
        }
    }
    private static Size chooseOptimalSize(Size[] choices
            , int width, int height, Size aspectRatio)
    {

        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices)
        {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height)
            {
                bigEnough.add(option);
            }
        }

        if (bigEnough.size() > 0)
        {
            return Collections.min(bigEnough, new CompareSizesByArea());
        }
        else
        {

            return choices[0];
        }
    }



    static class CompareSizesByArea implements Comparator<Size>
    {
        @Override
        public int compare(Size lhs, Size rhs)
        {

            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }


    //Camera status monitoring
    private CameraDevice.StateCallback stateCallback = new CameraDevice. StateCallback()
    {
        // when camera starting
        @Override
        public void onOpened(CameraDevice cameraDevice){
            CameraActivity.this.cameraDevice = cameraDevice;
            takePreview();
        }

        // when camera disconnecting
        @Override
        public void onDisconnected(CameraDevice cameraDevice)
        {
            CameraActivity.this.cameraDevice.close();
            CameraActivity.this.cameraDevice = null;

        }
        // when error
        @Override
        public void onError(CameraDevice cameraDevice, int error)
        {
            cameraDevice.close();
        }
    };

    //start preview
    private void takePreview() {
        SurfaceTexture mSurfaceTexture = tv.getSurfaceTexture();

        mSurfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

        Surface mSurface = new Surface(mSurfaceTexture);
        try {

            mCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            mCaptureRequestBuilder.addTarget(mSurface);

            cameraDevice.createCaptureSession(Arrays.asList(mSurface,imageReader.getSurface()),new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {

                        mCaptureRequest = mCaptureRequestBuilder.build();
                        mPreviewSession = session;
                        //realtime preview
                        mPreviewSession.setRepeatingRequest(mCaptureRequest, null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    //take photo
    private void takePicture()
    {
        try
        {
            if (cameraDevice == null)
            {
                return;
            }
            // create request
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            // set auto focus
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            captureRequestBuilder.addTarget(imageReader.getSurface());
            // get device orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            // calculate photo's orientation
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION
                    , ORIENTATIONS.get(rotation));

            mPreviewSession.stopRepeating();
            //take photo
            CaptureRequest captureRequest = captureRequestBuilder.build();
            //set shooting monitoring
            mPreviewSession.capture(captureRequest,captureCallback, null);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    //monitoring shooting result
    private CameraCaptureSession.CaptureCallback captureCallback= new CameraCaptureSession.CaptureCallback()
    {
        // success
        @Override
        public void onCaptureCompleted(CameraCaptureSession session,CaptureRequest request,TotalCaptureResult result)
        {
            // reset auto focus
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            // set automatic exposure
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            try {
                //re-preview
                mPreviewSession.setRepeatingRequest(mCaptureRequest, null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    //monitoring shooting pictures
    private ImageReader.OnImageAvailableListener imageAvailableListener= new ImageReader.OnImageAvailableListener()
    {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            FileOutputStream fileOutputStream = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            String fname = "IMG_" + sdf.format(new Date()) + ".jpg";

            String sdpath = getApplication().getExternalFilesDir(null).getPath();

            //save the pictures to this path
            File file = new File(getApplication().getExternalFilesDir(null), fname);
            try {

                fileOutputStream  = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.close();

                //display pictures
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                int deg = readPictureDegree(sdpath);
                Bitmap Nbitmap = rotaingImageView(deg,bitmap);
                Uri imageUri=getImageUri(CameraActivity.this,Nbitmap);
//                iv.setImageURI(imageUri);
                Intent intent=new Intent(CameraActivity.this,ImageRecognitionActivity.class);
                intent.putExtra("image",imageUri);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                image.close();
            }
        }


    };

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public int readPictureDegree(String path) {
        int degree  = 90;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        //System.out.println("angle2=" + angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getCirleBitmap(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int r = Math.min(w, h);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap newBitmap = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);

        BitmapShader bitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        canvas.drawCircle(r / 2, r / 2, r / 2, paint);

        return newBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case RESULT_CODE_CAMERA:
                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    openCamera();
                }else{
                    //if user don't allow to get permission
                    Toast.makeText(CameraActivity.this,"please allow the camera permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void startCamera(){
        if (tv.isAvailable()) {
            if(cameraDevice==null) {
                openCamera();
            }
        } else {
            tv.setSurfaceTextureListener(surfaceTextureListener);
        }
    }


    private void stopCamera(){
        if(cameraDevice!=null){
            cameraDevice.close();
            cameraDevice=null;
        }
    }
}
