package com.app.tuskan.moviestage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tuskan.moviestage2.R;
import com.app.tuskan.moviestage2.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Yakup on 29.6.2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String BASE_THUMBNAIL_URL = "https://img.youtube.com/vi/";
    private static final String QUERY = "/0.jpg";

    private Context context;
    private ArrayList<Video> videos;

    public VideoAdapter(Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    public static class videoHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public videoHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.video_thumbnail_imageview);
            textView = itemView.findViewById(R.id.video_title_textview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new VideoAdapter.videoHolder(layoutInflater.inflate(R.layout.video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Video item = videos.get(position);

        final VideoAdapter.videoHolder container = (videoHolder) holder;

        Picasso.with(context)
                .load(BASE_THUMBNAIL_URL + item.getVideoKey() + QUERY)
                .error(R.mipmap.ic_launcher)
                .into(container.imageView);

        container.textView.setText(item.getVideoTitle());

        container.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + item.getVideoKey()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

}
