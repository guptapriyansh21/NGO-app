package com.ngo.finder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ViewProfile extends AppCompatActivity {
    TextView txtLoc, txtProfile, txtDetails;
    Button btnMap, btnCall;
    ImageView imgUser;
    DatabaseReference mDatabaseReference;
    String userid;
    String location, name, phone, image, gender, city, pincode, state, blood_group, email,usertype,roll;
    double latitude, longitude;
SharedPreferences preferences;
String TAG="Hello";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgUser = findViewById(R.id.imgUser);
        userid = getIntent().getStringExtra("userid");
        Log.i(TAG, "onCreate: "+userid);
        btnMap = findViewById(R.id.btnMap);
        btnCall = findViewById(R.id.btnCall);
        txtLoc = findViewById(R.id.txtLoc);
        txtProfile = findViewById(R.id.txtKarm);
        txtDetails = findViewById(R.id.txtDetails);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        mDatabaseReference.child(Constant.users).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.i("SA", "onDataChange: " + dataSnapshot1.getKey() + "::" + dataSnapshot1.getValue());
                    try {

                        /*
                        *    mfb.put("name", name);
            mfb.put("dob", dob);
            mfb.put("gender", Gen);
            mfb.put("city", city);
            mfb.put("pincode", pincode);
            mfb.put("state", st);
            mfb.put("blood_group", blod);
            mfb.put("mobile_no", number);
            mfb.put("email", email);
            mfb.put("password", password);
                        * */

                        if (dataSnapshot1.getKey().equals("name")) {
                            name = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("phone")) {
                            phone = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("latitude")) {
                            latitude = dataSnapshot1.getValue(Double.class);

                        }
                        if (dataSnapshot1.getKey().equals("longitude")) {
                            longitude = dataSnapshot1.getValue(Double.class);

                        }
                        if (dataSnapshot1.getKey().equals("location")) {
                            location = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("gender")) {
                            gender = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("city")) {
                            city = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("pincode")) {
                            pincode = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("state")) {
                            state = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("blood_group")) {
                            blood_group = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("email")) {
                            email = (String) dataSnapshot1.getValue();

                        }
                        if (dataSnapshot1.getKey().equals("url")) {
                            image = (String) dataSnapshot1.getValue();

                        }  if (dataSnapshot1.getKey().equals("usertype")) {
                            usertype = (String) dataSnapshot1.getValue();

                        }if (dataSnapshot1.getKey().equals("id")) {
                            roll = (String) dataSnapshot1.getValue();

                        }
                    } catch (Exception e) {
                        count = 0;
                    }


                }
                String color = "#cc0029";
                String text = "<font color=" + color + ">" + "Email: </font>" + email +
                        "<br><font color=" + color + ">Mobile No.:</font> " + phone +
                        "<br><font color=" + color + ">Blood Group:" + blood_group + "</font>"
                      +  "<br><font color=" + color + ">ID:" + roll + "</font>"
                        + "<br><font color=" + color + ">Usertype:" + usertype + "</font>";

                if (location == null) {
                    location = "Unknown";
                }
                txtDetails.setText(name);
                txtLoc.setText("State: " + state + "\nCity: " + city + "\nPincode: " + pincode );
                txtProfile.setText(Html.fromHtml(text));

                try {
                    Picasso.with(ViewProfile.this).load(image).into(imgUser);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         if (userid.equals(preferences.getString("userid",""))) {
        btnCall.setVisibility(View.GONE);
        btnMap.setVisibility(View.GONE);
        }
    }

    public void call(View v) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    public void map(View v) {
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setComponent((new ComponentName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity")));
        startActivity(intent);
    }
}
