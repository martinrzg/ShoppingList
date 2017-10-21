package com.example.martinruiz.shoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.adapters.ItemAdapter;
import com.example.martinruiz.shoppinglist.models.Category;
import com.example.martinruiz.shoppinglist.models.Item;
import com.example.martinruiz.shoppinglist.models.List;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class ItemsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Item>> {

    @BindView(R.id.recyclerViewItems) RecyclerView recyclerViewItems;
    private Spinner spinnerFilterCategory;

    private Realm realm;

    private List list;
    private Category selectedCategory;

    private RealmList<Item> items;
    private Object[] itemArray ;

    private int listID;


    private RealmResults<Category> categories;
    private ArrayAdapter<Category> adapterCategory;


    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        setTitle("Items");
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null) {
            listID = getIntent().getExtras().getInt("id");
            list = realm.where(List.class).equalTo("id",listID).findFirst();
            System.out.println("LIST:"+list.getTitle());

            items = list.getItems();
            //itemArray = list.getItems().toArray();

        }


        layoutManager = new LinearLayoutManager(this);
        adapter = new ItemAdapter(items, R.layout.item_product, this, (item, position, view) -> {
            Intent intent = new Intent(ItemsActivity.this,EditAddItemActivity.class);
            intent.putExtra("itemID",item.getId());
            intent.putExtra("listID",list.getId());
            startActivity(intent);
        });
        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItems.setLayoutManager(layoutManager);
        recyclerViewItems.setAdapter(adapter);





        //list = realm.where(List.class).equalTo("id", listID).findFirst();
        //CHECK LATER
        list.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                adapter.notifyDataSetChanged();
            }
        });

        //items = list.getItems();
    }
    @OnClick(R.id.fabAddItem)
    public void addItem() {
        Intent addItemIntent = new Intent(this,EditAddItemActivity.class);
        addItemIntent.putExtra("listID",list.getId());
        startActivity(addItemIntent);
    }

    @Override
    public void onChange(RealmResults<Item> items) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar_items_activity,menu);

        spinnerFilterCategory = (Spinner) menu.findItem(R.id.spinnerFilterCategory).getActionView();
        spinnerFilterCategory.setPopupBackgroundResource(R.color.colorPrimaryDark);

        categories = realm.where(Category.class).findAll();
        if(categories == null || categories.isEmpty()){

        }else {
            adapterCategory = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categories);
            spinnerFilterCategory.setAdapter(adapterCategory);
            spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    selectedCategory = (Category) adapterView.getItemAtPosition(pos);
                    if (selectedCategory.getTitle().equals("General")){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                //items.removeAll(items);
                                //items.addAll(list.getItems());
                            }
                        });
                    }else {
                        //RealmList<Item>  = realm.where(List.class).equalTo("id",listID).findFirst().getItems();
                                //.where().equalTo("id",selectedCategory.getId()).findAll();
                        //System.out.println(filterItems);

                        RealmList<Item> filterItems = new RealmList<>();

                        //where(Item.class).findAll().where(Category.class).equalTo("id",selectedCategory.getId());
                        /*for (int i = 0; i < items.size() ; i++) {
                            System.out.println("----------------------------> itemsCat: " + items.get(i).getCategory().getTitle() + " selectedCat: " + selectedCategory.getTitle());
                            System.out.println("[" + (items.get(i).getCategory().getId() == selectedCategory.getId()) + "]");
                            if (items.get(i).getCategory().getId() == selectedCategory.getId()) {
                                filterItems.add(items.get(i));
                            }
                        }*/
                    }

                    //adapter.notifyDataSetChanged();
                    //adapter.notifyDataSetChanged();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
