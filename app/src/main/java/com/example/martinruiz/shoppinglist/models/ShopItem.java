package com.example.martinruiz.shoppinglist.models;

import com.example.martinruiz.shoppinglist.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MartinRuiz on 10/10/2017.
 */

public class ShopItem extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private double price;
    private String imageURL;
    private int quantity;
    private String note;
    private Category category;
    //name, price, image,qty,

    public ShopItem() {

    }

    public ShopItem(String name, Category category, double price, String imageURL, int quantity, String note) {
        this.id = MyApplication.ShopItemID.incrementAndGet();
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageURL = imageURL;
        this.quantity = quantity;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return getName();
    }
}
