package com.example.jigis.cookingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jigis.cookingapp.Model.Steps;
import com.example.jigis.cookingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{
    Context context;
    List<Steps> stepsList;
    //Click Item
    final private stepsItemOnClickListener mOnClickListener;

    public interface stepsItemOnClickListener {
        void onItemClick(Steps steps);
    }

    public StepsAdapter(Context context, List<Steps> stepsList,stepsItemOnClickListener mOnClickListener) {
        this.context = context;
        this.stepsList = stepsList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_steps,parent,false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {

        holder.tviewNumber.setText(String.valueOf(stepsList.get(position).getId()));
        holder.tviewSteps.setText(stepsList.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return stepsList== null ? 0:stepsList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_step)
        TextView tviewSteps;

        @BindView(R.id.tv_number)
        TextView tviewNumber;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Steps step = stepsList.get(adapterPosition);
            mOnClickListener.onItemClick(step);
        }
    }
}
