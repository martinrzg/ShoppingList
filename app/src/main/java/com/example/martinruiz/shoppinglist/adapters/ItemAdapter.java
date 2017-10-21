package com.example.martinruiz.shoppinglist.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.models.Item;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by MartinRuiz on 10/11/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private List<Item> items;
    private int layoutReference;
    private Activity activity;
    private OnItemClickListener onItemClickListener;

    public ItemAdapter(List<Item> items, int layoutRefence, Activity activity, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.layoutReference = layoutRefence;
        this.activity = activity;
        this.onItemClickListener = onItemClickListener;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }



    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(layoutReference,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position),onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageViewItem) ImageView imageViewItem;
        @BindView(R.id.textViewItemName) TextView textViewItemName;
        @BindView(R.id.textViewItemCategory) TextView textViewItemCategory;
        @BindView(R.id.textViewItemPrice) TextView textViewItemPrice;
        @BindView(R.id.cardViewItem) CardView cardViewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bind(final Item item, OnItemClickListener onItemClickListener ){
            textViewItemName.setText(item.getName());
            textViewItemCategory.setText(item.getCategory().getTitle());

            textViewItemPrice.setText("$"+String.format("%.2f",item.getPrice()));
            if(item.getImageURL()==null || item.getImageURL().isEmpty()){

            }else{
                File file = new File(item.getImageURL());
                Picasso.with(activity).load(file).transform(new CropCircleTransformation()).into(imageViewItem);
            }


            cardViewItem.setOnClickListener(view ->
                    onItemClickListener.onItemClick(item,getAdapterPosition(),cardViewItem));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item , int position, View view);
    }
}
