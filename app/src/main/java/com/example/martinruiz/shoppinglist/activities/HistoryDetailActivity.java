package com.example.martinruiz.shoppinglist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.adapters.ItemShopAdapter;
import com.example.martinruiz.shoppinglist.models.ShoppingCart;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class HistoryDetailActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewShoppingDetailItem) RecyclerView recyclerView;
    @BindView(R.id.textViewItemDetailTotal) TextView textViewTotal;
    @BindView(R.id.textViewDetailElements) TextView textViewElements;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Realm realm;
    private ShoppingCart shoppingCart;
    private int elements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null) {
            int shoppingCardID = 0;
            shoppingCardID = getIntent().getExtras().getInt("id");
            shoppingCart = realm.where(ShoppingCart.class).equalTo("id",shoppingCardID).findFirst();
        }
        for (int i = 0; i < shoppingCart.getItems().size(); i++) {
            elements += shoppingCart.getItems().get(i).getQuantity();
        }
        textViewTotal.setText("$"+shoppingCart.getTotal());
        textViewElements.setText(elements+" elements");
        layoutManager = new LinearLayoutManager(this);
        adapter = new ItemShopAdapter(shoppingCart.getItems(), R.layout.item_shopping,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
