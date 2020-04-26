package com.ngo.finder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    ArrayList<String> lstUsers = new ArrayList<>();
    ArrayList<String> lstUsersID = new ArrayList<>();
    FirebaseAuth auth;
    String TAG = "ADMIN";

    EditText txtSearch;
    Context context;
    StorageReference mStorageReference;
    ListView listView;
    String data = "";
    Adapter1 adapter1;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        txtSearch = findViewById(R.id.txtSearch);
        listView = findViewById(R.id.list);
        adapter1 = new Adapter1(this, R.layout.custum_list, lstUsers);
        listView.setAdapter(adapter1);
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Admin", Snackbar.LENGTH_LONG)
                        .setAction("Click Here", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminPanel.this, RegisterActivity.class);
                                intent.putExtra("usertype", "admin");
                                startActivity(intent);

                            }
                        }).show();
            }
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);
        mStorageReference = FirebaseStorage.getInstance().getReference();


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, lstUsers);
//        spnUsers.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(getBaseContext(), ViewProfile.class);
                                intent.putExtra("userid", lstUsersID.get(position));
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                myRef.child(Constant.users).child(lstUsersID.get(position)).removeValue();
                                Toast.makeText(context, "User removed successfully", Toast.LENGTH_SHORT).show();

                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                //No button clicked

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("What do you want?").setPositiveButton("View Profile", dialogClickListener)
                        .setNeutralButton("Cancel", dialogClickListener)
                        .setNegativeButton("Remove User", dialogClickListener)
                        .show();
            }
        });
        getUserList(data);
        if (!preferences.getBoolean("login", false)) {
            preferences.edit().putString("name", getIntent().getStringExtra("name"))
                    .putString("userid", getIntent().getStringExtra("userid"))
                    .putString("email", getIntent().getStringExtra("email"))
                    .putString("utype", getIntent().getStringExtra("utype"))
                    .putBoolean("login", true)
                    .putString("image", getIntent().getStringExtra("image")).commit();

        }
    }

    public void search(View v) {
        data = txtSearch.getText().toString();
        if (data.length() > 0) {
            getUserList(data);

        } else {
            Toast.makeText(context, "Please check input", Toast.LENGTH_SHORT).show();
        }
    }

    public void newEmployee(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("usertype", "teacher");
        startActivity(intent);

    }

    public void viewFeedback(View v) {
        Intent intent = new Intent(this, ListFeedback.class);
        intent.putExtra("usertype", "teacher");
        startActivity(intent);

    }

    public void getUserList(final String data) {
        myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lstUsers.clear();
                lstUsersID.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                try{
                    String name = (String) dataSnapshot1.child("name").getValue();
                    if (data.length() > 0) {
                        if (name.contains(data)) {

                            lstUsers.add(name + "\n--" + dataSnapshot1.child("usertype").getValue());
                            lstUsersID.add(dataSnapshot1.getKey());
                        }
                    } else {
                        lstUsers.add(name + "\n--" + dataSnapshot1.child("usertype").getValue());
                        lstUsersID.add(dataSnapshot1.getKey());
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

                }
                adapter1.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {
            preferences.edit().clear().commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
