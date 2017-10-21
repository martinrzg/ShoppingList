package com.example.martinruiz.shoppinglist.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.fragments.HistoryFragment;
import com.example.martinruiz.shoppinglist.fragments.ListsFragment;
import com.example.martinruiz.shoppinglist.fragments.ShoppingFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle("Lists");
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayoutContainer,new ListsFragment()).commit();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_lists:
                    setTitle("Lists");
                    transaction.replace(R.id.frameLayoutContainer,new ListsFragment()).commit();
                    return true;
                case R.id.navigation_shopping:
                    setTitle("Shopping Cart");
                    transaction.replace(R.id.frameLayoutContainer,new ShoppingFragment()).commit();
                    return true;
                case R.id.navigation_history:
                    setTitle("History");
                    transaction.replace(R.id.frameLayoutContainer,new HistoryFragment()).commit();
                    return true;
            }
            return false;
        }
    };

}
