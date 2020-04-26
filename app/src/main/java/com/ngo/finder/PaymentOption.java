package com.ngo.finder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class PaymentOption extends Activity implements OnItemSelectedListener {
    Spinner bank_name;
    Button pay, pick_date;
    String userid, amount;
    EditText card_number, name, show_date, cvv_number;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    static final int DATE_PICKER_ID = 1111;
    EditText txtAmount;

    TextView tvCurrent;
    String total_sum;

SharedPreferences preference;
String TAG="PAyment";
Context ctx;
DatabaseReference mDatabaseReference;
String booking_id,ftoken_id;
Context context;
String initial_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_option);
        bank_name = (Spinner) findViewById(R.id.select_bank);
        pay = (Button) findViewById(R.id.payment);
        card_number = (EditText) findViewById(R.id.cardnumber);
        name = (EditText) findViewById(R.id.name_on_card);
        show_date = (EditText) findViewById(R.id.date);
        cvv_number = (EditText) findViewById(R.id.cvv_number);
        pick_date = (Button) findViewById(R.id.pick_date);
        txtAmount = (EditText) findViewById(R.id.txtamount);
        tvCurrent = (TextView) findViewById(R.id.tvCurrentAMount);
        total_sum = getIntent().getStringExtra("total_cost");
        booking_id = getIntent().getStringExtra("request_id");
        ftoken_id = getIntent().getStringExtra("ftoken_id");
        context=this;
        Log.i(TAG, "onCreate: "+total_sum);
        ctx=this;


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                PaymentOption.this, R.array.bank_name,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_name.setAdapter(adapter);
        bank_name.setOnItemSelectedListener(this);

preference= PreferenceManager.getDefaultSharedPreferences(this);
        userid = preference.getString("userid","");

//        txtAmount.setText(total_sum);


        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
total_sum=txtAmount.getText().toString();
if(total_sum.length()>0){
    if ((card_number.getText().toString().equals("") || card_number
            .getText().toString().length() < 16)
            || name.getText().toString().equals("")
            || show_date.getText().toString().equals("")
            || cvv_number.getText().toString().equals("")
            || cvv_number.getText().toString().length() < 3) {
        Toast.makeText(getApplicationContext(),
                "Please fill all the details with valid Entry!",
                Toast.LENGTH_SHORT).show();
    } else {



        Toast.makeText(getApplicationContext(),
                "Amount Paid successfully",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}else {
    Toast.makeText(ctx, "Please enter amount to add", Toast.LENGTH_SHORT).show();
}

            }
        });

        pick_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position,
                               long arg3) {
        switch (parent.getId()) {
            case R.id.select_bank:

                bank_name.setSelection(position);

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            show_date.setText(new StringBuilder().append(month + 1).append("-")
                    .append(day).append("-").append(year).append(" "));

        }
    };

}
