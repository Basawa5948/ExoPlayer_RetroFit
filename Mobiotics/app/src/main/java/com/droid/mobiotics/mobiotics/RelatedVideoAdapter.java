package com.droid.mobiotics.mobiotics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RelatedVideoAdapter extends RecyclerView.Adapter<RelatedVideoAdapter.ViewHolder> {
    private List<Videos> videosList;
    private Context context;

    public RelatedVideoAdapter(List<Videos> videosList, Context context) {
        this.videosList = videosList;
        this.context = context;
    }

    @NonNull
    @Override
    public RelatedVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_related_videos,parent,false);

        return new RelatedVideoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedVideoAdapter.ViewHolder holder, int position) {
        final Videos videos = videosList.get(position);

        holder.description.setText(videos.getDescription());
        Picasso.with(context).load(videos.getThumb()).into(holder.thumb);
        holder.title.setText(videos.getTitle());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , PlayVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title",videos.getTitle());
                bundle.putString("Description",videos.getDescription());
                bundle.putString("URL",videos.getUrl());
                bundle.putInt("Touch",1);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public ImageView thumb;
        public TextView title;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.description_related);
            thumb = (ImageView) itemView.findViewById(R.id.thumb_related);
            title = (TextView) itemView.findViewById(R.id.title_related);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_related);
        }
    }
}


