package com.ngo.finder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MarkAttendence extends AppCompatActivity {
    LinearLayout linearMain;
    CheckBox checkBox;
    Button btnSaveAttendence;
    Iterator<?> i;
    Set<?> set;
    ProgressDialog dialog;
//    Spinner spnBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendence);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        spnBatch = (Spinner) findViewById(R.id.spnBatch);
        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        btnSaveAttendence = new MaterialButton(this);
        btnSaveAttendence.setText("Submit");
        /**
         * * create linked hash map for store item you can get value from
         * database * or server also
         */
        getDetails();

    }

    View.OnClickListener getOnClickDoSomething(final CheckBox button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("*************id******" + button.getId());
                System.out.println("and text***" + button.getText().toString());
                if (button.isChecked()) {
                    System.out.println("and text*** selected"
                            + button.getText().toString());
                } else {
                    System.out.println("and text*** not selected"
                            + button.getText().toString());
                }
            }
        };

    }

    ArrayList<Integer> lstUserid = new ArrayList<>();
    ArrayList<String> lstUserFireID = new ArrayList<>();
    ArrayList<String> lstUserFireName = new ArrayList<>();
    String TAG = "MA";

    int count = 0;

    public void getDetails() {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constant.DB);
        Query query = myRef.getRef().child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = 0;
                lstUserid.clear();
                lstUserFireID.clear();
                lstUserFireName.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try{
                        if(dataSnapshot1.child("usertype").getValue().toString().equals("student")){
                            checkBox = new CheckBox(MarkAttendence.this);
                            checkBox.setId(count);
                            checkBox.setText(dataSnapshot1.child("name").getValue().toString());
                            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                            linearMain.addView(checkBox);
                            lstUserFireName.add(dataSnapshot1.child("name").getValue().toString());
                            lstUserFireID.add(dataSnapshot1.getKey());
                            lstUserid.add(count);

                        }
                    }catch (Exception e){e.printStackTrace();}
                    count++;

                }
                if (count > 0) {
                    linearMain.addView(btnSaveAttendence);
                    btnSaveAttendence.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int temp = 0;
                            while (temp < lstUserid.size()) {
                                checkBox = findViewById(lstUserid.get(temp));
                                Log.i(TAG, "onClick: " + checkBox.isChecked());

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(Constant.DB);
                                String date=getDate();
                                Log.i(TAG, "onClick: "+date);
                                String time=Constant.getTime();
                                Log.i(TAG, "onClick: "+time);

                                AttendenceModel model = new AttendenceModel(lstUserFireID.get(temp), lstUserFireName.get(temp),date ,time, checkBox.isChecked());
                                myRef.child("users_attendence").push().setValue(model);


                                temp++;

                            }
                            if(temp>0){
                                Toast.makeText(MarkAttendence.this, "Attendence Marked Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//
//
//	int count = 0;
//	JSONArray jArray;
//	final LinkedHashMap<String, String> alphabet = new LinkedHashMap<String, String>();
//	try {
//		jArray = new JSONArray(server_data);
//		while (count < jArray.length()) {
//
//			JSONObject jObj = jArray.getJSONObject(count);
//			alphabet.put(jObj.getString("userid"),
//					jObj.getString("name"));
//
//			count++;
//		}
//		set = alphabet.entrySet();
//		// Get an iterator
//		i = set.iterator();
//		// Display elements
//
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//	runOnUiThread(new Runnable() {
//		public void run() {
//			while (i.hasNext()) {
//				@SuppressWarnings("rawtypes")
//				Map.Entry me = (Map.Entry) i.next();
//				System.out.print(me.getKey() + ": ");
//				System.out.println(me.getValue());
//				checkBox = new CheckBox(MarkAttendence.this);
//				checkBox.setId(Integer.parseInt(me.getKey().toString()));
//				checkBox.setText(me.getValue().toString());
//				checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
//				linearMain.addView(checkBox);
//			}
//			if(alphabet.size()>0)
//				linearMain.addView(btnSaveAttendence);
//			btnSaveAttendence
//					.setOnClickListener(new View.OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//
//							i = set.iterator();
//							jArrayData= new JSONArray();
//							while (i.hasNext()) {
//								@SuppressWarnings("rawtypes")
//								Map.Entry me = (Map.Entry) i.next();
//								System.out.print(me.getKey() + ": ");
//								System.out.println(me.getValue());
//								checkBox = (CheckBox) findViewById(Integer
//										.parseInt(me.getKey()
//												.toString().trim()));
//								JSONObject jObj = new JSONObject();
//								try {
//									if (checkBox.isChecked()) {
//										System.out.println("checked");
//										jObj.put(
//												me.getKey().toString(),
//												"P");
//
//									} else {
//
//										jObj.put(
//												me.getKey().toString(),
//												"A");
//										System.out.println("unchecked");
//									}
//									jArrayData.put(jObj);
//								} catch (Exception e) {
//
//								}
//
//							}
//
//							Log.i("WSP", jArrayData.toString());
//							SaveAttendence mark = new SaveAttendence();
//							mark.execute();
//						}
//
//
//
//					});
//		}
//	});

    }

    //	private String url_get_students = URL.get_stu_name_attendence_BATCH;
//	private String server_data;
//	JSONArray jArrayData;
//String batch;
//
//
//
//	public class SaveAttendence extends AsyncTask<Void, Void, Boolean> {
//
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			List<NameValuePair> param = new ArrayList<NameValuePair>();
//			param.add(new BasicNameValuePair("batch", batch));
//			param.add(new BasicNameValuePair("jArray", jArrayData.toString()));
//
//			try {
//				JSONParser jp = new JSONParser();
//				server_data = jp.makeHttpRequest(url_SaveUser, "GET", param);
//
//				Log.i("WPS", server_data);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			dialog.dismiss();
//			server_data = server_data.trim();
//			runOnUiThread(new Runnable() {
//				public void run() {
//					if (server_data.equalsIgnoreCase("success")) {
//						check = 1;
//					} else {
//						check = 0;
//					}
//					System.out.println(check);
//					if (check == 0) {
//						Toast.makeText(getBaseContext(), "An error occured!!",
//								Toast.LENGTH_SHORT).show();
//					} else if (check > 0) {
//
//						Toast.makeText(getBaseContext(),
//								"Attendence Registered successfully!!",
//								Toast.LENGTH_SHORT).show();
//						finish();
//					}
//				}
//			});
//
//		}
//	}
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy:MM:dd");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }
}
