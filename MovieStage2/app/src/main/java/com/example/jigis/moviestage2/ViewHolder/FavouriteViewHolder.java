package com.example.jigis.moviestage2.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jigis.moviestage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.movie_image)
    public
    ImageView movieList;
    @BindView(R.id.movie_title)
    public
    TextView title;


    public FavouriteViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
