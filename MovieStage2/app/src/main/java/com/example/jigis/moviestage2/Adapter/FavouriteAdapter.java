package com.example.jigis.moviestage2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.example.jigis.moviestage2.DataBase.MovieModelDatabase;
import com.example.jigis.moviestage2.DetailsActivity;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.ViewHolder.FavouriteViewHolder;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder>  {
    Context context;
    List<MovieModelDatabase> movieModelDatabases;


    public FavouriteAdapter(Context context,List<MovieModelDatabase> movieModelDatabases) {
        this.movieModelDatabases = movieModelDatabases;
        this.context=context;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie,parent,false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {

        holder.title.setText(movieModelDatabases.get(position).getTitle());


        //full image path url +size+image original path
        Glide.with(holder.itemView.getContext()).load(movieModelDatabases.get(position).getPoster_path()).listener(new RequestListener<Drawable>() {
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
                .into(holder.movieList);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailsActivity.class);
                intent.putExtra("Favourite", movieModelDatabases.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.movieList, "bookmarkPoster");
                context.startActivity(intent, optionsCompat.toBundle());
            }
        });


    }

    @Override
    public int getItemCount() {
        if (movieModelDatabases == null) {
            return 0;
        }

        return  movieModelDatabases.size();
    }


    public void setMovies(List<MovieModelDatabase> movieEntries) {
        movieModelDatabases = movieEntries;
        notifyDataSetChanged();
    }
}
