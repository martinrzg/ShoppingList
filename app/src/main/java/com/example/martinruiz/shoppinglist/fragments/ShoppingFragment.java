package com.example.martinruiz.shoppinglist.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.adapters.ItemShopAdapter;
import com.example.martinruiz.shoppinglist.models.Item;
import com.example.martinruiz.shoppinglist.models.Store;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recyclerViewShoppingItem) RecyclerView recyclerViewShoppingItem;
    @BindView(R.id.textViewItemSubtotal) TextView textViewItemSubtotal;
    @BindView(R.id.textViewItemElements) TextView textViewItemElements;
    @BindView(R.id.spinnerItems) Spinner spinnerItems;
    @BindView(R.id.imageViewAddItem) ImageView imageViewAddItem;
    @BindView(R.id.editTextShoppingQuantity) EditText editTextQuantity;

    private ArrayList<Item> shoppingItems ;
    private RealmResults<Item> items;
    private ArrayAdapter<Item> adapterItem;
    private Item selectedItem;
    private double subtotal;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Realm realm;

    private OnFragmentInteractionListener mListener;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        realm = Realm.getDefaultInstance();
        items = realm.where(Item.class).findAll();
        shoppingItems = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        //ButterKnife binding here...
        ButterKnife.bind(this,view);

        adapterItem = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, items);
        spinnerItems.setAdapter(adapterItem);
        spinnerItems.setOnItemSelectedListener(this);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ItemShopAdapter(shoppingItems, R.layout.item_shopping,getActivity());

        recyclerViewShoppingItem.setHasFixedSize(true);
        recyclerViewShoppingItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShoppingItem.setLayoutManager(layoutManager);
        recyclerViewShoppingItem.setAdapter(adapter);

        return view;
    }

    @OnClick(R.id.imageViewAddItem)
    public void addItemImage() {
        int quantity;
        System.out.println(editTextQuantity.getText().toString());

        if(editTextQuantity.getText().toString().isEmpty()){
            editTextQuantity.setError("Valid quantity");
        }else {
            try{
                quantity = Integer.parseInt(editTextQuantity.getText().toString());
                editTextQuantity.setError(null);
                System.out.println(quantity+10);
                selectedItem.setQuantity(quantity);
                subtotal += selectedItem.getPrice();
                shoppingItems.add(selectedItem);
                textViewItemSubtotal.setText("$"+subtotal);
            }catch (Exception e){
                editTextQuantity.setError("Valid number");
            }
        }
    }
    @OnClick(R.id.fabSaveShoppingCart)
    public void fabSaveShoppingCart(){

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
            //Toast.makeText(context,"Shopping Fragment Attached",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItem = (Item) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
