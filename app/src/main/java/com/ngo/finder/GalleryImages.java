package com.ngo.finder;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryImages extends AppCompatActivity {
    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    DatabaseReference databaseReference;
    DataAdapterGalleryImages dataAdapterGallery;
    ArrayList<ImageUrl> imageUrlList = new ArrayList();
    ArrayList<String> lstAlbum = new ArrayList();
String album_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        album_name=getIntent().getStringExtra("album_name");
        imageView = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataAdapterGallery = new DataAdapterGalleryImages(getApplicationContext(), imageUrlList);
        recyclerView.setAdapter(dataAdapterGallery);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        databaseReference.child(Constant.upload).orderByChild("upload_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrlList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ImageUrl imageUrl = dataSnapshot1.getValue(ImageUrl.class);
                    if(album_name.equalsIgnoreCase(imageUrl.getAlbum_name())){
//                        lstAlbum.add(imageUrl.getAlbum_name());
                        imageUrlList.add(imageUrl);
                    }

                }
                dataAdapterGallery.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}