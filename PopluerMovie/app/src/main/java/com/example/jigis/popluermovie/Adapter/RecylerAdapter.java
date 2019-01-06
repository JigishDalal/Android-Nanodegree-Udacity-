package com.example.jigis.popluermovie.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.popluermovie.ApiCall.ApiCall;
import com.example.jigis.popluermovie.DetailsActivity;
import com.example.jigis.popluermovie.MainActivity;
import com.example.jigis.popluermovie.Model.Movie;
import com.example.jigis.popluermovie.Model.MovieResponse;
import com.example.jigis.popluermovie.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MovieHolder> {

    private  Context ctx;
    private List<Movie> movieList;
    DetailsActivity detailsActivity;

    public RecylerAdapter(Context context, List<Movie> moviesList) {
        this.movieList = moviesList;
        this.ctx = context;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_list_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, final int position) {

        holder.movietitle.setText(movieList.get(position).getTitle());

        //full image path url +size+image original path
        String imagepath = ApiCall.IMAGE_URL + ApiCall.IMAGE_SIZE_185 + movieList.get(position).getPoster_path();
        Glide.with(ctx).load(imagepath).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Bitmap bitmap = ((BitmapDrawable) resource.getCurrent()).getBitmap();
                Palette palette = Palette.generate(bitmap);
                int defaultColor = 0xFF333333;
                int color = palette.getVibrantColor(defaultColor);
                holder.itemView.setBackgroundColor(color);
                holder.movietitle.setTextColor(Color.WHITE);
                return false;
            }
        }).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(holder.movieImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx,DetailsActivity.class);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.putExtra("movie", movieList.get(position));
                    Pair<View, String> image=Pair.create((View)holder.movieImg,holder.movieImg.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) ctx, image);
                    ctx.startActivity(intent, options.toBundle());
                }
                else {

                }
            }
        });


    }

    @Override
    public int getItemCount() {
            return movieList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_image)
        ImageView movieImg;
        @BindView(R.id.movie_title)
        TextView movietitle;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
