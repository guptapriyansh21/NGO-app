package com.ngo.finder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdapterStatus extends ArrayAdapter<AttendenceModel> {
ArrayList<AttendenceModel>attendenceModels;
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public AdapterStatus(Context context, int resID, ArrayList items) {
        super(context, resID, items);
        attendenceModels=items;
    }

    @SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setBackgroundColor(Color.argb(220, 74,121, 181));
        v.setBackgroundResource(R.drawable.bg_button);

       // if (position == 1) {
        if(attendenceModels.get(position).isStatus()){
            ((TextView) v).setTextColor(Color.GREEN);
            ((TextView) v).setText(attendenceModels.get(position).getName()
                    +"\n"+attendenceModels.get(position).getRdate()
                    +" "+attendenceModels.get(position).getTime()

            );
        }else {
            ((TextView) v).setTextColor(Color.RED);
            ((TextView) v).setText(attendenceModels.get(position).getName()
                    +"\n"+attendenceModels.get(position).getRdate()
                    +" "+attendenceModels.get(position).getTime()

            );
        }

       // }s
        return v;
    }

}