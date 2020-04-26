package com.ngo.finder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BroadCastMessageActivity extends AppCompatActivity {
    EditText txtMessage;
    SharedPreferences prefrences;
    Context context;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<User> lstUsersSearch = new ArrayList<>();
    String userid;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        txtMessage = findViewById(R.id.txtMessage);
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);

        userid = prefrences.getString("userid", "");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);
        myRef.child(Constant.subject_teacher).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstUsersSearch.clear();
                jsonArray = new JSONArray();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        if (dataSnapshot1.child("fuserid").getValue().toString().equalsIgnoreCase(userid)) {

                            User user = new User();
                            user.setName(dataSnapshot1.child("name").getValue().toString());
                            user.setUrl(dataSnapshot1.child("url").getValue().toString());
                            user.setPhone(dataSnapshot1.child("phone").getValue().toString());

                            user.setPost(dataSnapshot1.child("subject").getValue().toString());
                            user.setLocation(dataSnapshot1.child("location").getValue().toString());
                            user.setToken_id(dataSnapshot1.child("fuserid").getValue().toString());
                            user.setFtoken(dataSnapshot1.child("token_id").getValue().toString());
                            user.setUserid(dataSnapshot1.getKey());
                            lstUsersSearch.add(user);
                            jsonArray.put(dataSnapshot1.child("token_id").getValue().toString());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    String message;

    public void sendTopicMessage(View v) {
        message = txtMessage.getText().toString();
        if (message.length() > 0) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", "New Notification from Faculty Finder");
                jsonObject.put("msg", message);
                jsonObject.put("from", prefrences.getString("userid", ""));
                String arr[] = {jsonArray.get(0).toString(), jsonObject.toString()};
                SENDFCM sendfcm = new SENDFCM();
                sendfcm.execute(arr);
//                Toast.makeText(this, "Message Broadcasted sussessfully", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
        }
    }

    class SENDFCM extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {


            try {

                FireBase fireBase = new FireBase();
                String response = fireBase.send(params[0], params[1]);

                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jObj = new JSONObject(aVoid);
                if (jObj.getInt("success") == 1) {
                    Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to send notification", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
