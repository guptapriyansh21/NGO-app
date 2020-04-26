package com.ngo.finder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DigitalMaterialDownloadPanel extends AppCompatActivity {
ListView list;
AdapterVideoDownload adapter1;
ArrayList<ImageUrl>lstMaterials=new ArrayList<>();
DatabaseReference databaseReference;
String id;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_material_download_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
id=getIntent().getStringExtra("id");
      list=findViewById(R.id.list);
      adapter1=new AdapterVideoDownload(this,R.layout.custum_list,lstMaterials);
      list.setAdapter(adapter1);
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//              String file_url=lstMaterials.get(position).getImageUrl();
              String file_url[]={lstMaterials.get(position).getImageUrl(),lstMaterials.get(position).getDisplay_name()};
              new DownloadFileFromURL().execute(file_url);
          }
      });
    databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
    databaseReference.child(Constant.upload_video).orderByChild("upload_time").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            lstMaterials.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                ImageUrl imageUrl = dataSnapshot1.getValue(ImageUrl.class);
                if(imageUrl.getAlbum_name().equalsIgnoreCase(id)){
                    lstMaterials.add(imageUrl);
                }


            }
            Toast.makeText(DigitalMaterialDownloadPanel.this, "Click on any item to download", Toast.LENGTH_SHORT).show();
            adapter1.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


}
    /**
     * Showing Dialog
     * */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String name;
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String filepath=null;
            try {
                URL url = new URL(f_url[0]);
                 name = f_url[1];
                Log.i("FILENAME", "doInBackground: "+name);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
               filepath= Environment
                        .getExternalStorageDirectory().toString()
                        + "/"+name;
                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/"+name);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return filepath;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            open_file(file_url);
        }
        public void open_file(String filename) {
            File file=new File(filename);
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            String ext2 = FilenameUtils.getExtension(name);
            String mimeType = myMime.getMimeTypeFromExtension(ext2);
            Log.i("FILE", "open_file: "+mimeType);
            newIntent.setDataAndType(Uri.fromFile(file),mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(newIntent);
            } catch (ActivityNotFoundException e){
                e.printStackTrace();
            }
        }
    }

}
