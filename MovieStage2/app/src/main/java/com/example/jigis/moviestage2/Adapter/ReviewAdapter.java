package com.example.jigis.moviestage2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jigis.moviestage2.Model.Reviews;
import com.example.jigis.moviestage2.Model.ReviewsResponse;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.ViewHolder.ReviewsHolder;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewsHolder> {

    List<Reviews> reviewsList;
    Context context;
    public ReviewAdapter(Context context, List<Reviews> mReviewList) {
        this.context = context;
        this.reviewsList = mReviewList;
    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_item,parent,false);
        return new ReviewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsHolder holder, int position) {
        String author=reviewsList.get(position).getAuthor();
        if(!author.isEmpty()&& author.length()!=0 )
        {

            holder.tvAuthor.setText(reviewsList.get(position).getAuthor());
        }else {
            holder.tvAuthor.setText("NO DATA FOUND");
        }
        holder.tvReview.setText(reviewsList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
//        Log.e( "getItemCount: ",""+reviewsList.size() );
        return reviewsList == null ? 0 : reviewsList.size();
    }
}
