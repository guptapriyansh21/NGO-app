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


public class AdapterVideoDownload extends ArrayAdapter<ImageUrl> {
ArrayList<ImageUrl> imageUrls;
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public AdapterVideoDownload(Context context, int resID, ArrayList<ImageUrl> items) {
        super(context, resID, items);
        imageUrls=items;
    }

    @SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setBackgroundColor(Color.argb(220, 74,121, 181));
        v.setBackgroundResource(R.drawable.bg_button);
       
       // if (position == 1) {
            ((TextView) v).setTextColor(Color.GREEN);
            ((TextView) v).setText(imageUrls.get(position).getAlbum_name()+"\n"+imageUrls.get(position).getDescription());

       // }s
        return v;
    }

}