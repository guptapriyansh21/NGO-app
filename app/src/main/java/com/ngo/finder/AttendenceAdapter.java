package com.ngo.finder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//Adapter class extends with BaseAdapter and implements with OnClickListener
public class AttendenceAdapter extends BaseAdapter {


	private Activity activity;
    private ArrayList<AttendenceModel> data;
    Context ctx;
    private static LayoutInflater inflater=null;
   // public ImageLoader imageLoader;

	int font_size=0,font_size_title=18;
    public AttendenceAdapter(Activity a, ArrayList<AttendenceModel> d) {
        activity = a;
        data=d;
        ctx=activity.getApplicationContext();
        inflater = (LayoutInflater)activity.
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);



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
          
        public TextView txtID;

        CheckBox checkBox;

  
    }
     
    @SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custum_list_attendence, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtID =  vi.findViewById(R.id.txtName);
            holder.checkBox =  vi.findViewById(R.id.cbxUser);



            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();


        holder.txtID.setText(data.get(position).getName());



    return vi;
    }
     


}
