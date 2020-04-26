package com.ngo.finder;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity {
TextView textTitle,txtDetails;
ImageView imageView;
String latitude,longitude,title,details,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        latitude=getIntent().getStringExtra("latitude");
        longitude=getIntent().getStringExtra("longitude");
        title=getIntent().getStringExtra("title");
        details=getIntent().getStringExtra("details");
        url=getIntent().getStringExtra("url");
        textTitle=findViewById(R.id.textTitle);
        txtDetails=findViewById(R.id.txtDetails);
        imageView=findViewById(R.id.imgDisplay);
        Picasso.with(this).load(url).into(imageView);
        txtDetails.setText(details);
        textTitle.setText(title);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void map(View v) {
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setComponent((new ComponentName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity")));
        startActivity(intent);
    }
}
