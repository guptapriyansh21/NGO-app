package com.ngo.finder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
EditText txtName,txtPhone,txtEMail,txtPassword;
String userid;
String TAG="Profile";
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
txtEMail=findViewById(R.id.txtEmail);
txtName=findViewById(R.id.txtName);
txtPassword=findViewById(R.id.txtPassword);
txtPhone=findViewById(R.id.txtPhone);
userid=getIntent().getStringExtra("userid");
       FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.users).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.i(TAG, "onDataChange: "+dataSnapshot1.getKey());
                    if(dataSnapshot1.getKey().equalsIgnoreCase("name")){
                        txtName.setText(dataSnapshot1.getValue().toString());
                    }  if(dataSnapshot1.getKey().equalsIgnoreCase("phone")){
                        txtPhone.setText(dataSnapshot1.getValue().toString());
                    }  if(dataSnapshot1.getKey().equalsIgnoreCase("password")){
                        txtPassword.setText(dataSnapshot1.getValue().toString());
                    }  if(dataSnapshot1.getKey().equalsIgnoreCase("email")){
                        txtEMail.setText(dataSnapshot1.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    String name,phone,email,password;
public void updateProfile(View v){
    name=txtName.getText().toString();
    phone=txtPhone.getText().toString();
    email=txtEMail.getText().toString();
    password=txtPassword.getText().toString();
    if(name.length()>0){
        if(email.contains("@")&&email.contains(".")){
            if(phone.length()==10){
                if(password.length()>5){
                    FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.users).child(userid).child("name").setValue(name);
                    FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.users).child(userid).child("phone").setValue(phone);
                    FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.users).child(userid).child("email").setValue(email);
                    FirebaseDatabase.getInstance().getReference(Constant.DB).child(Constant.users).child(userid).child("password").setValue(password);
                }else{
                    Toast.makeText(this, "Please check password. it must be more than 5 digit", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please check phone no.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please check email", Toast.LENGTH_SHORT).show();
        }
    }else{
        Toast.makeText(this, "Please check name", Toast.LENGTH_SHORT).show();
    }
}
}
