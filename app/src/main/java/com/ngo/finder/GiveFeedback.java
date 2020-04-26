package com.ngo.finder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GiveFeedback extends AppCompatActivity {

    DatabaseReference myRef;
    SharedPreferences preferences;
    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    Adapter1 adapter1;
    ListView listView;
    EditText txtName,txtEmail,txtContact,txtSubject,txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        myRef = FirebaseDatabase.getInstance().getReference(Constant.DB);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        txtSubject = findViewById(R.id.txtSubject);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtContact = findViewById(R.id.txtContact);
        txtDesc = findViewById(R.id.txtDesc);
        listView=findViewById(R.id.list);
        adapter1=new Adapter1(this,R.layout.custum_list,lstUsers);
        listView.setAdapter(adapter1);

        myRef.child(Constant.faq).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUsers.clear();
                lstUsersID.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String userid = (String) dataSnapshot1.child("userid").getValue();
                    String name = (String) dataSnapshot1.child("name").getValue();
                    String subject = (String) dataSnapshot1.child("subject").getValue();
                    String description = (String) dataSnapshot1.child("description").getValue();
                    String reply = (String) dataSnapshot1.child("answer").getValue();

                    if (preferences.getString("userid", "").equalsIgnoreCase(userid)) {
                        lstUsers.add("Name: " + name + "\nSubject: " + subject
                                + "\nDescription: " + description
                                + "\nReply: " + reply

                        );
                        lstUsersID.add(dataSnapshot1.getKey());
                    }


                    adapter1.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String subject, desc,name,mobile,email;

    public void askFaq(View v) {
        subject = txtSubject.getText().toString();
        desc = txtDesc.getText().toString();
        name = txtDesc.getText().toString();
        mobile = txtDesc.getText().toString();
        email = txtDesc.getText().toString();
        if (subject.length() > 0) {
            if (desc.length() > 0) {
                Map map = new HashMap();
                map.put("name", name);
                map.put("mobile", mobile);
                map.put("email", email);
                map.put("subject", subject);
                map.put("description", desc);
                map.put("answer", "");
                map.put("userid", preferences.getString("userid", ""));
                map.put("name", preferences.getString("name", ""));
                map.put("dated", Constant.getDate());

                myRef.child(Constant.faq).push().setValue(map);
                Toast.makeText(this, "Feedback sent successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please check description", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Please check subject", Toast.LENGTH_SHORT).show();
        }
    }
}
