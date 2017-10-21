package com.example.martinruiz.shoppinglist.adapters;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.models.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MartinRuiz on 10/10/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private java.util.List<List> lists;
    private int layoutReference;
    private Activity activity ;
    private OnItemClickListener onItemClickListener;

    public ListAdapter(java.util.List<List> lists, int layoutReference, Activity activity, OnItemClickListener onItemClickListener) {
        this.lists = lists;
        this.layoutReference = layoutReference;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(layoutReference,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.bind(lists.get(position),onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItemListName) TextView textViewListName;
        @BindView(R.id.tvItemStoreName) TextView textViewStoreName;
        @BindView(R.id.itemList) CardView itemList;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final List list, final OnItemClickListener onItemClickListener){
            textViewListName.setText(list.getTitle());
            textViewStoreName.setText(list.getStore().getTitle());
            itemList.setOnClickListener(view -> {
                onItemClickListener.onItemClick(list,getAdapterPosition(),itemList);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(List list , int position, View view);
    }
}
