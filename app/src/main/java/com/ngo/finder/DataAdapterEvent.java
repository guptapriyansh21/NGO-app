package com.ngo.finder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class DataAdapterEvent extends RecyclerView.Adapter<DataAdapterEvent.ViewHolder> {
    private ArrayList<EventData> imageUrls;
    private Context context;

    public DataAdapterEvent(Context context, ArrayList<EventData> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;

    }

    @Override
    public DataAdapterEvent.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtDesc.setText(imageUrls.get(i).getAlbum_name());
//        Picasso.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);
//        ImageViewZoomConfig imageViewZoomConfig=new ImageViewZoomConfig();
//        imageViewZoomConfig.saveProperty(true);
//        ImageViewZoomConfig.ImageViewZoomConfigSaveMethod imageViewZoomConfigSaveMethod = ImageViewZoomConfig.ImageViewZoomConfigSaveMethod.onlyOnDialog; // You can use always
//        imageViewZoomConfig.setImageViewZoomConfigSaveMethod(imageViewZoomConfigSaveMethod);

        try{
            Picasso.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);
        }catch (Exception e){
            e.printStackTrace();

        }
//        viewHolder.img.setConfig(imageViewZoomConfig);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EventDetails.class);
                intent.putExtra("title",imageUrls.get(i).getAlbum_name());
                intent.putExtra("details",imageUrls.get(i).getDescription());
                intent.putExtra("latitude",imageUrls.get(i).getLatitude());
                intent.putExtra("longitude",imageUrls.get(i).getLongitude());
                intent.putExtra("url",imageUrls.get(i).getImageUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
//        Glide.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageViewZoom img;
        TextView txtDesc;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            txtDesc = view.findViewById(R.id.txtDesc);
        }
    }

}