package com.ngo.finder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAttendenceAdmin extends AppCompatActivity {
    ListView list;
    AdapterStatus adapter1;
    DatabaseReference databaseReference;
    //    ArrayList<AttendenceModel> lstAttendence = new ArrayList<>();
    ArrayList<AttendenceModel> lstAttendenceshow = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence_admin);
        list = findViewById(R.id.list);
        adapter1 = new AdapterStatus(this, R.layout.custum_list, lstAttendenceshow);
        list.setAdapter(adapter1);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        viewAttendece();
    }

    String TAG = "attend";

    public void viewAttendece() {


        Query query = databaseReference.child("users_attendence");
        ;
        Log.i(TAG, "sendNotification: query=" + query);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstAttendenceshow.clear();
                long total = dataSnapshot.getChildrenCount();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AttendenceModel model = dataSnapshot1.getValue(AttendenceModel.class);
                    lstAttendenceshow.add(model);

                }
                if (lstAttendenceshow.size() > 0) {
                    adapter1.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
