package au.edu.sydney.comp5703.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Homepage extends AppCompatActivity {

    private Button start;
    private GridView gridView;
    private List<String> imagePath = new ArrayList<String>();
    private static String[] imageFormatSet = new String[]{"jpg", "png", "gif"};

    private static boolean isImageFile(String path) {
        for (String format : imageFormatSet) {
            //Determine if it is a picture
            if (path.contains(format)) {
                return true;
            }
        }
        return false;
    }

    private void getFiles(String url) {
        File files = new File(url);
        File[] file = files.listFiles();
        try {
            //Get array by traversing
            for (File f : file) {
                if (f.isDirectory()) {
                    getFiles(f.getAbsolutePath());
                } else {
                    if (isImageFile(f.getPath())) {
                        imagePath.add(f.getPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //create button to next page
        start = (Button) findViewById(R.id.takephoto);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Homepage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //get sdcard's path
        String sdpath = getApplication().getExternalFilesDir(null).getPath();
        getFiles(sdpath);
        //if there is no picture file
        if (imagePath.size() < 1) {
            return;
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView iv;
                if (convertView == null) {
                    iv = new ImageView(Homepage.this);
                    //set picture's width and height
                    iv.setAdjustViewBounds(true);
                    iv.setMaxWidth(1920);
                    iv.setMaxHeight(1080);

                    //set mageView's Margin
                    iv.setPadding(5, 5, 5, 5);
                } else {
                    iv = (ImageView) convertView;
                }

                Bitmap bm = BitmapFactory.decodeFile(imagePath.get(position));
                iv.setImageBitmap(bm);
                return iv;
            }


            @Override
            public int getCount() {
                return imagePath.size();
            }


            @Override
            public Object getItem(int position) {
                return position;
            }


            @Override
            public long getItemId(int position) {
                return position;
            }
        };

        gridview.setAdapter(adapter);



    }
}






