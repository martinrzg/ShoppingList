package com.example.martinruiz.shoppinglist.models;

import com.example.martinruiz.shoppinglist.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MartinRuiz on 10/20/2017.
 */

public class ShoppingCart extends RealmObject {

    @PrimaryKey
    private int id;
    private Date date;
    private RealmList<ShopItem> items;
    private double total;

    public ShoppingCart() {

    }

    public ShoppingCart(int id, Date date, RealmList<ShopItem> items, double total) {
        this.id = MyApplication.ShoppingID.incrementAndGet();
        this.date = date;
        this.items = items;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<ShopItem> getItems() {
        return items;
    }

    public void setItems(RealmList<ShopItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
