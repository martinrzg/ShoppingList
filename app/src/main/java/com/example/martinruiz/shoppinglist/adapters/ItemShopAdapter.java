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

public class ItemShopAdapter extends RecyclerView.Adapter<ItemShopAdapter.ViewHolder> {


    private List<Item> items;
    private int layoutReference;
    private Activity activity;

    public ItemShopAdapter(List<Item> items, int layoutRefence, Activity activity) {
        this.items = items;
        this.layoutReference = layoutRefence;
        this.activity = activity;
    }



    @Override
    public ItemShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(layoutReference,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemShopAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageViewShoppingItem) ImageView imageViewItem;
        @BindView(R.id.textViewShoppingItemName) TextView textViewItemName;
        @BindView(R.id.textViewItemDetails) TextView textViewItemCategory;
        @BindView(R.id.textViewItemSubtotal) TextView textViewItemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bind(final Item item ){
            textViewItemName.setText(item.getName());
            textViewItemCategory.setText(item.getQuantity() + " of "+item.getPrice());

            textViewItemPrice.setText("$"+String.format("%.2f",(item.getPrice() * item.getQuantity())));
            if(item.getImageURL()==null || item.getImageURL().isEmpty()){

            }else{
                File file = new File(item.getImageURL());
                Picasso.with(activity).load(file).transform(new CropCircleTransformation()).into(imageViewItem);
            }

        }
    }

}
