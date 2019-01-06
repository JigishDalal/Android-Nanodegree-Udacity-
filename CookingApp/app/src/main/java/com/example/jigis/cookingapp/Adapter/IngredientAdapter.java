package com.example.jigis.cookingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jigis.cookingapp.Model.Ingredient;
import com.example.jigis.cookingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    List<Ingredient> ingredientList;
    Context context;

    public IngredientAdapter(Context context,List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        this.context=context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_ingredient,parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
    holder.measure.setText(ingredientList.get(position).getMeasure());
    holder.quantity.setText(ingredientList.get(position).getQuantity());
    holder.description.setText(ingredientList.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientList== null ? 0:ingredientList.size();
    }

    public class  IngredientViewHolder extends RecyclerView.ViewHolder{
      @BindView(R.id.tv_quantity)
        TextView quantity;
      @BindView(R.id.tv_measure)
      TextView measure;
      @BindView(R.id.tv_description)
      TextView description;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
