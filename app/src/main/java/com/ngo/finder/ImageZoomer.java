package com.ngo.finder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.squareup.picasso.Picasso;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoomConfig;

public class ImageZoomer extends AppCompatActivity {
String url;
ImageViewZoom imgZoomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_image_zoomer);
        url=getIntent().getStringExtra("url");
        Log.i("IMGZOM", "onCreate: "+url);
        imgZoomer=findViewById(R.id.imgZoomer);
        ImageViewZoomConfig imageViewZoomConfig=new ImageViewZoomConfig();
        imageViewZoomConfig.saveProperty(true);
        ImageViewZoomConfig.ImageViewZoomConfigSaveMethod imageViewZoomConfigSaveMethod = ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog; // You can use always
        imageViewZoomConfig.setImageViewZoomConfigSaveMethod(imageViewZoomConfigSaveMethod);

        try{
            Picasso.with(this).load(url).into(imgZoomer);
        }catch (Exception e){
            e.printStackTrace();

        }
        imgZoomer.setConfig(imageViewZoomConfig);

    }
}
