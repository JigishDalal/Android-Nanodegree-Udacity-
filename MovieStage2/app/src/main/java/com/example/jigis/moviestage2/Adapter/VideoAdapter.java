package com.example.jigis.moviestage2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.Model.Video;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.ViewHolder.VideoHolder;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    List<Video> videoitem;
    Context ctx;
    String key;

    public VideoAdapter(Context ctx,List<Video> videoItem) {
        this.videoitem = videoItem;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.video_item,parent,false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

//        video Title name
        holder.tvVideotitle.setText(videoitem.get(position).getName());


        //thumb icon
        final String key=videoitem.get(position).getKey();
        String ImageTumb= ApiCallIng.VIDEO_THUMBNAIL+"/"+key+"/"+ApiCallIng.VIDEO_THUMBNAIL_RESOLUTION;
        Log.e("Image thumb",""+ImageTumb);
        Glide.with(ctx).load(ImageTumb).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)).into(holder.img_banner);


       //OnClick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String youtubevideo=ApiCallIng.VIDEOS_PATH+key;
                Log.e("Youtube Link",""+youtubevideo);
                Intent youtube=new Intent(Intent.ACTION_VIEW, Uri.parse(youtubevideo));
                if (youtube.resolveActivity(ctx.getPackageManager()) != null)
                    ctx.startActivity(youtube);
                else
                    Toast.makeText(ctx, "APP NOT FOUND", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.e("getItemCount: ",""+videoitem.size());
        return videoitem == null ? 0 : videoitem.size();
    }
}
