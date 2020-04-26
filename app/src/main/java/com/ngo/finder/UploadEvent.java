package com.ngo.finder;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class UploadEvent extends AppCompatActivity {
    Context context;
    EditText txtNotice, txtAlbum, txtLat, txtLon;
    TextView txtFile;
    MaterialButton btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        txtNotice = findViewById(R.id.txtNotice);
        btnUpload = findViewById(R.id.btnUpload);
        txtAlbum = findViewById(R.id.txtAlbum);
        txtFile = findViewById(R.id.txtFile);
        txtLat = findViewById(R.id.txtLat);
        txtLon = findViewById(R.id.txtLon);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.DB);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        dialog = findViewById(R.id.progressBar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    FirebaseDatabase database;
    DatabaseReference myRef;

    StorageReference mStorageReference;
    //    ArrayList<String> lstUploadedImage = new ArrayList<>();
    String album_name;
    MaterialProgressBar dialog;
    String latitude,longitude;

    public void sendNotification(View v) {
        final String notice = txtNotice.getText().toString();
        album_name = txtAlbum.getText().toString();
        latitude = txtLat.getText().toString();
        longitude = txtLon.getText().toString();
        if (notice.length() > 0) {
//            lstUploadedImage.clear();
            if (album_name.length() > 0) {
                int count = 0;
                Uri uri;
                btnUpload.setEnabled(false);

                while (count < mArrayUri.size()) {
                    dialog.setVisibility(View.VISIBLE);
                    dialog.setProgress(0);
                    uri = mArrayUri.get(count);
                    final int temp_count = count;
                    StorageReference sRef = mStorageReference.child(Constant.STORAGE_PATH_UPLOADS).child(album_name).child(album_name + "_" + System.currentTimeMillis());
                    sRef.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @SuppressWarnings("VisibleForTests")
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        dialog.dismiss();
//                                lstUploadedImage.add(taskSnapshot.getDownloadUrl().toString());
//                                    ImageUrl upload = new ImageUrl(taskSnapshot.getDownloadUrl().toString(), Constant.getDate(), album_name, notice, (-1 * System.currentTimeMillis()), "NA");
                                    EventData eventData=new EventData(taskSnapshot.getDownloadUrl().toString(), Constant.getDate(), album_name, notice, latitude,longitude,(-1 * System.currentTimeMillis()),"NA");
                                    myRef.child(Constant.eventData).push().setValue(eventData);

                                    Toast.makeText(context, "Image " + (temp_count + 1) + " Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    int progress = (int) (temp_count + 1) * 100 / mArrayUri.size();
                                    if ((temp_count + 1) == mArrayUri.size()) {
                                        dialog.setVisibility(View.GONE);
                                        txtAlbum.setText("");
                                        txtFile.setText("");
                                        txtNotice.setText("");
                                        txtLat.setText("");
                                        txtLon.setText("");
                                        btnUpload.setEnabled(true);
                                    } else {
                                        dialog.setProgress(progress);
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })


                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @SuppressWarnings("VisibleForTests")
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        dialog.setMessage("upload done=" + progress);

                                }
                            });


                    count++;
                }


            } else {
                Toast.makeText(context, "Please enter album name", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please set description", Toast.LENGTH_SHORT).show();
        }
    }

    int PICK_IMAGE_MULTIPLE = 11;

    public void selectFile(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
    }

    String path = null;


    String imageEncoded;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            mArrayUri.clear();
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from da
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri.add(mImageUri);


                    String displayName = "";

//                                uri = data.getData();
//                                Log.i("URI", "onActivityResult: "+uri.getPath());
                    String uriString = mArrayUri.get(0).toString();
                    Log.i("SINGLE", "onActivityResult: " + uriString);
                    File myFile = new File(uriString);
//                    path = myFile.getAbsolutePath();


//                    Log.i("MA", "onActivityResult: " + path);


                    if (uriString.contains("content://")) {
                        Cursor cursor1 = null;
                        try {
                            cursor1 = context.getContentResolver().query(mArrayUri.get(0), null, null, null, null);
                            if (cursor1 != null && cursor1.moveToFirst()) {
                                displayName = cursor1.getString(cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            }
                        } finally {
                            cursor1.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
//                            displayName=displayName+"\n"+displayName;

                    txtFile.setText(displayName);
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                        }
                        int count = 0;

                        String name = "";
                        while (count < mArrayUri.size()) {
                            String displayName = "";
//                                uri = data.getData();
//                                Log.i("URI", "onActivityResult: "+uri.getPath());
                            String uriString = mArrayUri.get(count).toString();
                            File myFile = new File(uriString);
//                            path = myFile.getAbsolutePath();
                            Log.i("MA", "onActivityResult: " + count + ":::" + uriString);
                            if (uriString.contains("content://")) {
                                Cursor cursor = null;
                                try {
                                    cursor = context.getContentResolver().query(mArrayUri.get(count), null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                        Log.i("UPLOADIMAGE", "onActivityResult: " + displayName);
                                    } else {
                                        Log.i("UPLOADIMAGE", "onActivityResult: no name found");
                                    }
                                } finally {
                                    cursor.close();
                                }
                            } else if (uriString.startsWith("file://")) {
                                displayName = myFile.getName();
                            }
                            name = name + "\n" + displayName;
                            count++;
                        }
                        txtFile.setText(name);
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


}
