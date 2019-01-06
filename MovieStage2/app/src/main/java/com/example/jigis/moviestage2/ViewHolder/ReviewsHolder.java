package com.example.jigis.moviestage2.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jigis.moviestage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsHolder  extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_author)
   public
    TextView tvAuthor;
    public
    @BindView(R.id.tv_review)
    TextView tvReview;


    public ReviewsHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
