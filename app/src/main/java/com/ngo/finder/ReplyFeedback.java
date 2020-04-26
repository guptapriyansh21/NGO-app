package com.ngo.finder;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReplyFeedback extends AppCompatActivity {
EditText txtAnswer;
DatabaseReference mDatabaseReference;
String faqid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtAnswer=findViewById(R.id.txtAnswer);
        faqid=getIntent().getStringExtra("faqid");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mDatabaseReference= FirebaseDatabase.getInstance().getReference(Constant.DB);

    }

    String answer;
    public void replyFaq(View v) {
        answer = txtAnswer.getText().toString();

        if (answer.length() > 0) {



                mDatabaseReference.child(Constant.faq).child(faqid).child("answer").setValue(answer);
                Toast.makeText(this, "Reply sent successfully", Toast.LENGTH_SHORT).show();
                finish();

        } else {
            Toast.makeText(this, "Please check answer", Toast.LENGTH_SHORT).show();
        }
    }
}
