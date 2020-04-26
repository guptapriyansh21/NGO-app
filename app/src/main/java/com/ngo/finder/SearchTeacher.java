package com.ngo.finder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchTeacher extends AppCompatActivity {
    DatabaseReference myRef;
    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    Adapter1 adapter1;
    ListView listView;
    SharedPreferences prefrences;
    EditText txtRadius,txtSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employees);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter1 = new Adapter1(this, R.layout.custum_list, lstUsers);
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter1);
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);
        txtRadius = findViewById(R.id.txtRadius);
        txtSubject = findViewById(R.id.txtSubject);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                String id = prefrences.getString("userid", "") + lstUsersID.get(position);
                                Request request = new Request(prefrences.getString("userid", ""), lstUsersID.get(position), "pending", Constant.getDateTime(), prefrences.getString("name", ""), lstUsers.get(position));
                                FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.requests).child(id).setValue(request);
                                Toast.makeText(SearchTeacher.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:


                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchTeacher.this);
                builder.setMessage("Send Request for Detail").setPositiveButton("Yes!",
                        dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    String TAG = "SE";
    String distance = "";
String subject="";
    public void go(View v) {
        distance = txtRadius.getText().toString();
        subject = txtSubject.getText().toString();
        if (distance.length() > 0) {
            if(subject.length()>0){
                myRef = FirebaseDatabase.getInstance().getReference(Constant.DB);
                myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        lstUsers.clear();
                        lstUsersID.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            String name = (String) dataSnapshot1.child("name").getValue();
                            String usertype = (String) dataSnapshot1.child("usertype").getValue();
                            String subject1 = (String) dataSnapshot1.child("sub").getValue();
                            try {
                                if (usertype.equalsIgnoreCase("teacher")) {
                                    if(subject1.contains(subject)||subject.contains(subject1)){
                                        double lat1 = Double.parseDouble(prefrences.getString("latitude", ""));
                                        double lon1 = Double.parseDouble(prefrences.getString("longitude", ""));
                                        double lat2 = (double) dataSnapshot1.child("latitude").getValue(Long.class);
                                        double lon2 = (double) dataSnapshot1.child("longitude").getValue(Long.class);
                                        Log.i(TAG, "onDataChange: " + lat1 + "," + lat2);
                                        double dis = distance(lat1, lat2, lon1, lon2, 0, 0);
                                        if (dis <= (Double.parseDouble(distance) * 1000)) {

                                            lstUsers.add("Name: " + name + "\nSubject: " + dataSnapshot1.child("sub").getValue()
                                                    + "\nExperience: " + dataSnapshot1.child("exper").getValue()
                                                    + "\nQualification: " + dataSnapshot1.child("qual").getValue()
                                                    + "\nLocation: " + dataSnapshot1.child("location").getValue()
                                            );
                                            lstUsersID.add(dataSnapshot1.getKey());
                                        }
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            adapter1.notifyDataSetChanged();
                        }
                        if (lstUsers.size() == 0) {
                            Toast.makeText(SearchTeacher.this, "No Teachers found!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else {
                Toast.makeText(this, "Please check subject", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please enter radius", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
