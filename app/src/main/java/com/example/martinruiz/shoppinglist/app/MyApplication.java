package com.example.martinruiz.shoppinglist.app;

import android.app.Application;

import com.example.martinruiz.shoppinglist.models.Category;
import com.example.martinruiz.shoppinglist.models.Item;
import com.example.martinruiz.shoppinglist.models.List;
import com.example.martinruiz.shoppinglist.models.ShopItem;
import com.example.martinruiz.shoppinglist.models.ShoppingCart;
import com.example.martinruiz.shoppinglist.models.Store;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by MartinRuiz on 10/10/2017.
 */

public class MyApplication extends Application {
    public static AtomicInteger ListID = new AtomicInteger();
    public static AtomicInteger ItemID = new AtomicInteger();
    public static AtomicInteger CategoryID = new AtomicInteger();
    public static AtomicInteger StoreID = new AtomicInteger();
    public static AtomicInteger ShoppingID = new AtomicInteger();
    public static AtomicInteger ShopItemID = new AtomicInteger();

    @Override
    public void onCreate() {
        initRealm();
        Realm realm = Realm.getDefaultInstance();

        ListID = getIdByTable(realm, List.class);
        ItemID = getIdByTable(realm, Item.class);
        CategoryID = getIdByTable(realm, Category.class);
        StoreID = getIdByTable(realm, Store.class);
        ShoppingID = getIdByTable(realm, ShoppingCart.class);
        ShopItemID = getIdByTable(realm, ShopItem.class);

        realm.close();


        super.onCreate();
    }


    private void initRealm(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable (Realm realm, Class <T> anyClass){
        RealmResults<T> results  = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }


}


