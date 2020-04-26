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

public class AdapterDigitalLearning extends RecyclerView.Adapter<AdapterDigitalLearning.ViewHolder> {
    private ArrayList<ImageUrl> imageUrls;
    private Context context;

    public AdapterDigitalLearning(Context context, ArrayList<ImageUrl> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;

    }

    @Override
    public AdapterDigitalLearning.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custum_list_digital_learning, viewGroup, false);
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
        viewHolder.txtDesc.setText(imageUrls.get(i).getAlbum_name()+"\n"+imageUrls.get(i).getDescription()+"\n"+imageUrls.get(i).getDated());

        viewHolder.txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DigitalMaterialDownloadPanel.class);
                intent.putExtra("id",imageUrls.get(i).getAlbum_name());
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

//        ImageViewZoom img;
        TextView txtDesc;

        public ViewHolder(View view) {
            super(view);
//            img = view.findViewById(R.id.imageView);
            txtDesc = view.findViewById(R.id.tvDisplayName);

        }
    }

}