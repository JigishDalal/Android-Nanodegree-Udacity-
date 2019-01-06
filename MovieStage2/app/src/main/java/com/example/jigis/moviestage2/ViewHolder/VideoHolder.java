package com.example.jigis.moviestage2.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jigis.moviestage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_banner)
    public
    ImageView img_banner;
    @BindView(R.id.video_title)
    public
    TextView tvVideotitle;


    public VideoHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
