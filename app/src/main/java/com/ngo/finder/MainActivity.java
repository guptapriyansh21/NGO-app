package com.ngo.finder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,AdvancedWebView.Listener {
    TextView txtLoc;

    SharedPreferences prefrences;

    AdapterImages adapter;
    LocationManager locationManager;
    ListView list;
    NavigationView navigationView;
    String userid;
    boolean start = true;
    AdvancedWebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        context = this;
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);
        mWebView=findViewById(R.id.webview);
//        String url=getIntent().getStringExtra("url");
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.loadUrl("file:///android_asset/Home - WINGS of Hope.html");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);

        if (!prefrences.getBoolean("login", false)) {
            prefrences.edit().putString("name", getIntent().getStringExtra("name"))
                    .putString("userid", getIntent().getStringExtra("userid"))
                    .putString("email", getIntent().getStringExtra("email"))
                    .putString("utype", getIntent().getStringExtra("utype"))
                    .putBoolean("login", true)
                    .putString("image", getIntent().getStringExtra("image")).commit();

        }

        userid = prefrences.getString("userid", "");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED
        ) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0,
                        0, this);
            } else if (locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0,
                        0, this);
            } else {


                Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }


        myRef.child(Constant.users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                lstUsersSearch.clear();
                String user="";
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    try {

                      if(dataSnapshot1.getKey().equalsIgnoreCase(prefrences.getString("userid",""))){
                          user=dataSnapshot1.getKey();
                      }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                if(!user.equalsIgnoreCase(prefrences.getString("userid",""))){
                    //user has been removed
                    prefrences.edit().clear().commit();

                    Toast.makeText(context, "You have been removed by admin", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(intent);
                userid=null;
                    finish();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("news");

    }



    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        mWebView.onActivityResult(requestCode, resultCode, intent);
//        // ...
//    }

//    @Override
//    public void onBackPressed() {
////        if (!mWebView.onBackPressed()) { return; }
//        // ...
//        super.onBackPressed();
//    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }


    FirebaseDatabase database;
    DatabaseReference myRef;


    public void updateProfile(View v) {
        Intent intent = new Intent(getBaseContext(), UpdateProfile.class);
        intent.putExtra("userid", userid);
        startActivity(intent);
    }

    public void askFaq(View v) {
        Intent intent = new Intent(getBaseContext(), GiveFeedback.class);
        startActivity(intent);
    }

    public void searchTeacher(View v) {
        Intent intent = new Intent(getBaseContext(), SearchTeacher.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: " + location.getLatitude() + "," + location.getLongitude() + "altitude=" + location.getAltitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        prefrences.edit().putString("latitude", Double.toString(latitude)).putString("longitude", Double.toString(longitude)).apply();
//        double distance = distance(Constant.latitude, latitude, Constant.longitude, longitude, Constant.altitude, 0);
      if(location.getLatitude()>0&&location.getLongitude()>0){
          if(userid!=null){
              myRef.child(Constant.users).child(userid).child("latitude").setValue(latitude);
              myRef.child(Constant.users).child(userid).child("longitude").setValue(longitude);
          }

      }

//        if (distance < 100) {
//            String current_date = Constant.getDate();
//            if (!current_date.equals(past_date)) {
//                String current_time = Constant.getTime();
//                AttendenceModel model = new AttendenceModel(auth.getUid(), auth.getCurrentUser().getDisplayName(), true, current_date, current_time);
//                myRef.child(Constant.user_attendence).push().setValue(model);
//                prefrences.edit().putString("past_date", current_date).apply();
//                Toast.makeText(context, "Attendence Marked Successfully", Toast.LENGTH_SHORT).show();
//
//            } else {
//                Log.i(TAG, "onLocationChanged: attendece already marked");
//            }
//        } else {
//            Log.i(TAG, "onLocationChanged: distance not applicable" + distance);
//        }
//        txtLoc.setText("Loc.."+latitude+","+longitude);
        GetCurrentAddress getCurrentAddress = new GetCurrentAddress();
        getCurrentAddress.execute();


//        Log.i(TAG, "onLocationChanged: distance=" + distance);
    }

    public String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                String locality = address.getLocality();
                String region_code = address.getCountryCode();
                result.append(""
                        + address.getAddressLine(0) + " "
                        // + address.getAddressLine(1) + " "
                        + address.getPostalCode() + " ");
                //result.append(locality + " ");

                //result.append(region_code);

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    String address1 = "";

    private class GetCurrentAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // this lat and log we can get from current location but here we
            // given hard coded
            address1 = "";
            System.out.println("lati=" + latitude + " longi=" + longitude);

            String address = getAddress(context, latitude, longitude);
            System.out.println("address=" + address);

            return address;
        }

        @Override
        protected void onPostExecute(String resultString) {
            // dialog.dismiss();
            address1 = address1 + resultString;
            myRef.child(Constant.users).child(userid).child("address").setValue(address1);
//txtLoc.setText(address1);
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    Context context;
    private static final int CAMERA_REQUEST = 1888;

    ArrayList<String> lstAttendence = new ArrayList<>();


    String token_id;

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        checkRegID();
        requestLocation();


        navigationView.getMenu().findItem(R.id.nav_sign_in).setTitle(prefrences.getString("name", ""));
        View hView = navigationView.getHeaderView(0);
        ImageView nav_user = (ImageView) hView.findViewById(R.id.imageView);
        TextView txtEmail = (TextView) hView.findViewById(R.id.txtEmail);
        TextView txtName = (TextView) hView.findViewById(R.id.txtName);
        txtEmail.setText(prefrences.getString("email", ""));
        txtName.setText(prefrences.getString("name", ""));
        if (prefrences.getString("utype", "").equalsIgnoreCase("student")) {
            navigationView.getMenu().findItem(R.id.nav_upload_image).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_upload_digital_materials).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_mark_attendence).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_upload_event).setVisible(false);
        }
        try {

            Picasso.with(this).load(prefrences.getString("image", "")).into(nav_user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED)) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0,
                        1, this);
            } else if (locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0,
                        1, this);
            } else {


                Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }


    }

    public void checkRegID() {
        if (playServicesAvailable()) {

            Log.i(TAG, "onCreate: " + token_id);
            token_id = FirebaseInstanceId.getInstance().getToken();
            if (prefrences.getBoolean("reg_status", true)) {
                myRef.child(Constant.users).child(userid).child("token_id").setValue(token_id);
                prefrences.edit().putString("token_id", token_id).commit();
                prefrences.edit().putBoolean("reg_status", false).commit();


            }
            Log.i(TAG, "onCreate: " + token_id);

        } else {
            Log.i(TAG, "sendNotificationToUser: no play services");
            // ... log error, or handle gracefully
        }
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean playServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
                if (!mWebView.onBackPressed()) { return; }
//         ...
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int RC_SIGN_IN = 123;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (prefrences.getString("utype", "").equalsIgnoreCase("student")) {

//    findViewById(R.id.nav_mark_attendence).setVisibility(View.GONE);
            if (id == R.id.nav_sign_in) {


                Intent intent1 = new Intent(getBaseContext(), ViewProfile.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                // already signed in

                // Handle the camera action
            } else if (id == R.id.nav_logout) {

                prefrences.edit().clear().commit();
                Intent intent1 = new Intent(getBaseContext(), LoginActivity.class);

                startActivity(intent1);
                finish();
                Toast.makeText(context, "Logout success", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_view_attendence) {


                Intent intent1 = new Intent(getBaseContext(), ViewAttendenceUser.class);

                startActivity(intent1);


            } else if (id == R.id.nav_gallery) {


                Intent intent1 = new Intent(getBaseContext(), Gallery.class);

                startActivity(intent1);


            }else if (id == R.id.nav_digital_learning) {


                Intent intent1 = new Intent(getBaseContext(), DigitalLearning.class);

                startActivity(intent1);


            } else if (id == R.id.nav_payment_donations) {


//                Intent intent1 = new Intent(getBaseContext(), PaymentOption.class);
//
//                startActivity(intent1);
                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "https://pmny.in/xIIiFFjAnvVP");
                startActivity(intent1);

            }
            else if (id == R.id.nav_events) {


                Intent intent1 = new Intent(getBaseContext(),EventLists.class);

                startActivity(intent1);


            }
            else if (id == R.id.nav_sponsors) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "https://goo.gl/forms/7yO5evdd9g0gpffu2");
                startActivity(intent1);


            }else if (id == R.id.nav_aboutus) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "file:///android_asset/aboutus.html");
                startActivity(intent1);


            }
            else if (id == R.id.nave_contactus) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "file:///android_asset/contactus.html");
                startActivity(intent1);


            } else if (id == R.id.nave_feedback) {


                Intent intent1 = new Intent(getBaseContext(), GiveFeedback.class);
                intent1.putExtra("url", "file:///android_asset/contactus.html");
                startActivity(intent1);


            }

        } else if (prefrences.getString("utype", "").equalsIgnoreCase("volunteer")) {
            if (id == R.id.nav_sign_in) {


                Intent intent1 = new Intent(getBaseContext(), ViewProfile.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                // already signed in

                // Handle the camera action
            }if (id == R.id.nav_upload_event) {


                Intent intent1 = new Intent(getBaseContext(), UploadEvent.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                // already signed in

                // Handle the camera action
            } else if (id == R.id.nav_logout) {

                prefrences.edit().clear().commit();
                Intent intent1 = new Intent(getBaseContext(), LoginActivity.class);

                startActivity(intent1);
                finish();
                Toast.makeText(context, "Logout success", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_mark_attendence) {


                Intent intent1 = new Intent(getBaseContext(), MarkAttendence.class);

                startActivity(intent1);


            } else if (id == R.id.nav_view_attendence) {


                Intent intent1 = new Intent(getBaseContext(), ViewAttendenceAdmin.class);

                startActivity(intent1);


            } else if (id == R.id.nav_upload_image) {


                Intent intent1 = new Intent(getBaseContext(), UploadImage.class);

                startActivity(intent1);


            } else if (id == R.id.nav_upload_digital_materials) {


                Intent intent1 = new Intent(getBaseContext(), UploadVideo.class);

                startActivity(intent1);


            }else if (id == R.id.nav_digital_learning) {


                Intent intent1 = new Intent(getBaseContext(), DigitalLearning.class);

                startActivity(intent1);


            } else if (id == R.id.nav_gallery) {


                Intent intent1 = new Intent(getBaseContext(), Gallery.class);

                startActivity(intent1);


            } else if (id == R.id.nav_payment_donations) {


//                Intent intent1 = new Intent(getBaseContext(), PaymentOption.class);
//
//                startActivity(intent1);
                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "https://pmny.in/xIIiFFjAnvVP");
                startActivity(intent1);

            }
            else if (id == R.id.nav_events) {

                Intent intent1 = new Intent(getBaseContext(),EventLists.class);

                startActivity(intent1);
            }
            else if (id == R.id.nav_sponsors) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "https://goo.gl/forms/7yO5evdd9g0gpffu2");
                startActivity(intent1);


            }else if (id == R.id.nav_aboutus) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "file:///android_asset/aboutus.html");
                startActivity(intent1);


            }
            else if (id == R.id.nave_contactus) {


                Intent intent1 = new Intent(getBaseContext(), WebView.class);
                intent1.putExtra("url", "file:///android_asset/contactus.html");
                startActivity(intent1);


            }else if (id == R.id.nave_feedback) {


                Intent intent1 = new Intent(getBaseContext(), GiveFeedback.class);
                intent1.putExtra("url", "file:///android_asset/contactus.html");
                startActivity(intent1);


            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    String path;
    String TAG = "MAINACTIVITY";

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {


                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

            showSnackbar(R.string.unknown_sign_in_response);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void showSnackbar(final int id) {
        Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();


    }

    Location location = null;

    public void requestLocation() {
        Log.i(TAG, "requestLocation: requesting location");

        try {
            LocationManager locationManager;
            String contex = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) getSystemService(contex);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locationManager.getBestProvider(criteria, false);
            int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

            if (rc == PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                //seconds and meter
                locationManager.requestLocationUpdates(provider, 0, 0,
                        locationListener);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            updateWithNewLongitude(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

            //updateWithNewLongitude(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    double latitude = 0.0, longitude = 0.0;

    private void updateWithNewLongitude(Location location) {

        // myLocationText = (TextView) findViewById(R.id.myLocationText);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(userid!=null){
                myRef.child(Constant.users).child(userid).child("latitude").setValue(latitude);
                myRef.child(Constant.users).child(userid).child("longitude").setValue(longitude);
            }

            //  Toast.makeText(context, "Current Loc="+latitude+","+longitude, Toast.LENGTH_SHORT).show();

        }

    }

}
