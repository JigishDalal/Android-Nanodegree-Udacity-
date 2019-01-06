package com.example.jigis.cookingapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.RecipeHomeHolder>{
    Context context;
    List<Recipes> recipesList;
    String ImagePath;
    int[] drawableIds = {R.drawable.cookies, R.drawable.cookies, R.drawable.cookies, R.drawable.cookies};


    //Click Item
    final private RecipesItemOnClickListener mOnClickListener;


    public HomeAdapter(Context context, List<Recipes> recipesList, RecipesItemOnClickListener mOnClickListener) {
        this.context=context;
        this.recipesList=recipesList;
        this.mOnClickListener = mOnClickListener;
    }
    public interface RecipesItemOnClickListener {
        void onItemClick(int clickedPosition);
    }

    @Override
    public RecipeHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_card_recipe,parent,false);
        return new RecipeHomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHomeHolder holder, final int position) {
        holder.tvReipeName.setText(recipesList.get(position).getName().toString());
        holder.tvServing.setText("Servings: "+recipesList.get(position).getServings());
        ImagePath=recipesList.get(position).getImage();
        if(ImagePath!=null && !ImagePath.isEmpty()){
            Glide.with(holder.itemView.getContext()).load(ImagePath).listener(new RequestListener<Drawable>() {
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
                    .into(holder.imageView);

        }
        else {
            recipesList.get(position).setImages(drawableIds[position]);
        }

    }

    @Override
    public int getItemCount() { return recipesList== null ? 0:recipesList.size();
    }

    public class RecipeHomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.img_recipe)
        ImageView imageView;
        @BindView(R.id.tv_RecipeName)
        TextView tvReipeName;
        @BindView(R.id.tv_serving)
        TextView tvServing;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;


        public RecipeHomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(clickedPosition);
        }

    }
}
