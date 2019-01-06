package com.example.jigis.moviestage2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.Model.CastModel;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.ViewHolder.CastViewHolder;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder> {
    Context context;
    List<CastModel> castlist;

    public CastAdapter(Context context, List<CastModel> castlist) {
        this.context = context;
        this.castlist = castlist;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cast_item,parent,false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

        String casturl= ApiCallIng.IMAGE_URL+ApiCallIng.IMAGE_SIZE_185+castlist.get(position).getProfilePath();
        Log.e("url cast",""+casturl);
        Glide.with(context).load(casturl).apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)).into(holder.profileImage);
        holder.tvCastName.setText(castlist.get(position).getName());
        holder.tvchar.setText(castlist.get(position).getCharacter());

    }

    @Override
    public int getItemCount() {
        return castlist == null ? 0 : castlist.size();
    }
}
