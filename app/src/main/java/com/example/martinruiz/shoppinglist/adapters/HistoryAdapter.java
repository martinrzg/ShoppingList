package com.example.martinruiz.shoppinglist.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.models.ShoppingCart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MartinRuiz on 10/11/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {


    private List<ShoppingCart> shoppingCarts;
    private int layoutReference;
    private Activity activity;
    private OnItemClickListener onItemClickListener;


    public HistoryAdapter(List<ShoppingCart> items, int layoutRefence, Activity activity, OnItemClickListener onItemClickListener) {
        this.shoppingCarts = items;
        this.layoutReference = layoutRefence;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }



    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(layoutReference,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        holder.bind(shoppingCarts.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return shoppingCarts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textViewShoppingDate) TextView textViewShoppingDate;
        @BindView(R.id.textViewShoppingElements) TextView textViewShoppingElements;
        @BindView(R.id.textViewShoppingTotal) TextView textViewShoppingTotal;
        @BindView(R.id.cardViewHistory) CardView cardViewHistory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final ShoppingCart shoppingCart , final OnItemClickListener onItemClickListener ){
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            textViewShoppingDate.setText("Compra "+sdf.format(shoppingCart.getDate()));
            textViewShoppingTotal.setText("$"+String.format("%.2f",shoppingCart.getTotal()));
            int elements  = 0;
            for (int i = 0; i < shoppingCart.getItems().size(); i++) {
                elements += shoppingCart.getItems().get(i).getQuantity();
            }
            textViewShoppingElements.setText(elements+" elementos");
            cardViewHistory.setOnClickListener(view -> {
                onItemClickListener.onItemClick(shoppingCart ,getAdapterPosition(),cardViewHistory);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ShoppingCart shoppingCart, int position, View view);
    }
}


