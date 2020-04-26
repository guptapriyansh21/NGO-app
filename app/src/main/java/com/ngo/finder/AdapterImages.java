package com.ngo.finder;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//Adapter class extends with BaseAdapter and implements with OnClickListener
public class AdapterImages extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> data;

    private static LayoutInflater inflater=null;

     Context ctx;
    public AdapterImages(Activity a, ArrayList<User> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         ctx=a.getApplicationContext();
        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache

    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
     
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
          
        public TextView txtDetails;



        public ImageView image;
        public ImageView imgCall;

    }
     
    public View getView(final int position, View convertView, ViewGroup parent) {
         
        View vi=convertView;
        ViewHolder holder;
          
        if(convertView==null){
              
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.listview_row_images, null);
            /****** View Holder Object to contain tabitem.xml file elements ******/
 
            holder = new ViewHolder();
            holder.txtDetails = (TextView) vi.findViewById(R.id.txtDetails);

            holder.image=(ImageView) vi.findViewById(R.id.image);
            holder.imgCall=(ImageView) vi.findViewById(R.id.imgCall);

           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();
         
         

        holder.txtDetails.setText(data.get(position).getName()
                +"\n"+data.get(position).getPost()
                +"\n"+data.get(position).getLocation()


        );

        ImageView image = holder.image;
        Log.i("ADApter", "getView: "+data.get(position).getUrl());
        Picasso.with(ctx).load(data.get(position).getUrl()).into(image);
        //DisplayImage function from ImageLoader Class


         image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub


				Intent intent = new Intent(ctx,ImageZoomer.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("url",data.get(position).getUrl() );

				ctx.startActivity(intent);
			}
		});
        holder.imgCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get(position).getPhone()));

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);

                } else {
//                    ActivityCompat.requestPermissions(ctx, new String[]{
//                                    Manifest.permission.CALL_PHONE
//                            },
//                            1);
                }

            }
        });
        return vi;
    }
 
   
  
}