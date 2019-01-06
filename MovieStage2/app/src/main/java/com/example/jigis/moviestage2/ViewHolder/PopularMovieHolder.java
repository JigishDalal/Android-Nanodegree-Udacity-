package com.example.jigis.moviestage2.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jigis.moviestage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularMovieHolder extends RecyclerView.ViewHolder  {
    @BindView(R.id.movie_image)
    public
    ImageView movieImg;
    @BindView(R.id.movie_title)
    public
    TextView movietitle;

    public PopularMovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
