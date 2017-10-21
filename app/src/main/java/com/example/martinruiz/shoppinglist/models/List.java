package com.example.martinruiz.shoppinglist.models;

import com.example.martinruiz.shoppinglist.app.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MartinRuiz on 10/10/2017.
 */

public class List extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private RealmList<Item> items;
    private Store store;



    public List() {

    }

    public List(String title, Store store) {
        this.id = MyApplication.ListID.incrementAndGet();
        this.title = title;
        this.items = new RealmList<Item>();
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }


}
