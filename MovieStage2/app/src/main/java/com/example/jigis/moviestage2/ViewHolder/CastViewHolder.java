package com.example.jigis.moviestage2.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jigis.moviestage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CastViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.profile_image)
    public
    CircleImageView profileImage;
    @BindView(R.id.tv_castname)
    public
    TextView tvCastName;
    @BindView(R.id.charater_tv)
    public
    TextView tvchar;

    public CastViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
}
