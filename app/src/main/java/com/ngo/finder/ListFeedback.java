package com.ngo.finder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFeedback extends AppCompatActivity {
    DatabaseReference myRef;
    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    Adapter1 adapter1;
    ListView listView;
SharedPreferences prefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employees);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter1 = new Adapter1(this, R.layout.custum_list, lstUsers);
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter1);
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent=new Intent(getBaseContext(),ReplyFeedback.class);
                                intent.putExtra("faqid",lstUsersID.get(position));
                                startActivity(intent);


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:


                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ListFeedback.this);
                builder.setMessage("What do you want?").setPositiveButton("Reply!",
                        dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });
        myRef= FirebaseDatabase.getInstance().getReference(Constant.DB);
        myRef.child(Constant.faq).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUsers.clear();
                lstUsersID.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String name = (String) dataSnapshot1.child("name").getValue();
                    String subject = (String) dataSnapshot1.child("subject").getValue();
                    String description = (String) dataSnapshot1.child("description").getValue();



                        lstUsers.add("Name: "+name+"\nSubject: "+subject
                                +"\nDescription: "+description

                        );
                        lstUsersID.add(dataSnapshot1.getKey());



                    adapter1.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
