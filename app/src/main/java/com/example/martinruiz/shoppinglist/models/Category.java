package com.example.martinruiz.shoppinglist.models;

import com.example.martinruiz.shoppinglist.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by MartinRuiz on 10/13/2017.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;


    public Category() {

    }

    public Category(String title) {
        this.id = MyApplication.CategoryID.incrementAndGet();
        this.title = title ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
