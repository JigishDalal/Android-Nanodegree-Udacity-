package com.example.jigis.moviestage2.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.Model.Movie;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.ViewHolder.PopularMovieHolder;

import java.util.List;

public class PopularMovieAdapter  extends RecyclerView.Adapter<PopularMovieHolder> {
    private Context ctx;
    private List<Movie> movieList;
//    click on movie
    private final MovieClickListener mMovieClickListener;

    public PopularMovieAdapter(List<Movie> moviesList, MovieClickListener mMovieClickListener) {
        this.movieList = moviesList;
//        this.ctx = context;
        this.mMovieClickListener = mMovieClickListener;
    }
    @NonNull
    @Override
    public PopularMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false);
        return new PopularMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final PopularMovieHolder holder, final int position) {
        holder.movietitle.setText(movieList.get(position).getTitle());
        //full image path url +size+image original path
        String imagepath = ApiCallIng.IMAGE_URL + ApiCallIng.IMAGE_SIZE_185 + movieList.get(position).getPoster_path();
        Glide.with(holder.itemView.getContext()).load(imagepath).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.movieImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMovieClickListener.onMoviePosterClick(movieList.get(position),holder.movieImg,holder.movieImg.getTransitionName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }


}
