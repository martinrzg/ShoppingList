package com.example.martinruiz.shoppinglist.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.activities.ItemsActivity;
import com.example.martinruiz.shoppinglist.adapters.ListAdapter;
import com.example.martinruiz.shoppinglist.models.List;
import com.example.martinruiz.shoppinglist.models.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.example.martinruiz.shoppinglist.R.layout.support_simple_spinner_dropdown_item;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListsFragment extends Fragment implements RealmChangeListener<RealmResults<List>> ,AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.recyclerViewLists) RecyclerView recyclerViewList;

    private Realm realm;
    private RealmResults<List> lists;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RealmResults<Store> stores;
    private ArrayAdapter<Store> adapterStore;

    private Store selectedStore;



    public ListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListsFragment newInstance(String param1, String param2) {
        ListsFragment fragment = new ListsFragment();
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

        //realm.executeTransaction((realm)-> realm.deleteAll() );

        lists = realm.where(List.class).findAll();
        printLists();
        lists.addChangeListener(this);

        stores = realm.where(Store.class).findAll();
        if(stores == null || stores.isEmpty()){
            createNewStore("General");
        }
    }

    private void printLists(){

        for (int i = 0; i < lists.size(); i++) {
            System.out.println(lists.get(i).getTitle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        //ButterKnife binding here...

        ButterKnife.bind(this,view);

        /*Recycler View */

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ListAdapter(lists, R.layout.item_list, getActivity(), new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List list, int position, View view) {
                //Toast.makeText(getContext(), "Click: "+list.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intentDetail  = new Intent(getActivity(), ItemsActivity.class);
                intentDetail.putExtra("id",list.getId());
                System.out.println("SEND ID: "+list.getId());
                startActivity(intentDetail);
            }
        });
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewList.setLayoutManager(layoutManager);
        recyclerViewList.setAdapter(adapter);
        //TODO Add OnScrollStateListener

        return view;
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
                    + " must implement OnFragmentIntera  ctionListener");
            */
            //Toast.makeText(context,"Lists Fragment Attached",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onChange(RealmResults<List> lists) {
        adapter.notifyDataSetChanged();
        //Toast.makeText(getContext(), "SOMETHING CHANGED!", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.fabAddList)
    public void fabAddList(){
        showDialogCreateList("Add new List",null);
    }


    //TODO STORE SELECTED
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        selectedStore = (Store) adapterView.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //selectedStore = new Store("General");
    }

    private void showDialogAddStore(String title, String message) {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("Add",null)
                .setNegativeButton("Cancel",null)
                .create();
        if(title != null) dialog.setTitle(title);
        if(message != null) dialog.setMessage(message);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_store,null);
        dialog.setView(viewInflated);
        final EditText editTextStoreName = viewInflated.findViewById(R.id.editTextStoreName);
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            button.setOnClickListener(view -> {
                if (editTextStoreName == null || editTextStoreName.getText().toString().isEmpty()){
                    editTextStoreName.setError("Enter a name");
                }else{
                    String shopName = editTextStoreName.getText().toString().trim();
                    createNewStore(shopName);
                    adapterStore.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            buttonNegative.setOnClickListener(view -> {dialog.dismiss();});
        });
        dialog.create();
        dialog.show();
    }

    private void showDialogCreateList(String title, String message){
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setPositiveButton("Add",null)
                .setNegativeButton("Cancel",null)
                .create();
        if(title != null) dialog.setTitle(title);
        if(message != null) dialog.setMessage(message);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_list,null);
        dialog.setView(viewInflated);
        final EditText inputListName = viewInflated.findViewById(R.id.editTextListName);
        final Spinner spinner = viewInflated.findViewById(R.id.spinnerStores);
        final ImageView imageViewAddStore = viewInflated.findViewById(R.id.imageViewAddStore);

        imageViewAddStore.setOnClickListener(view -> showDialogAddStore("Add store",null));

        //TODO Spinner Adapter and consult from DB!
        stores = realm.where(Store.class).findAll();
        adapterStore = new ArrayAdapter<Store>(getContext(), support_simple_spinner_dropdown_item,stores);
        spinner.setAdapter(adapterStore);
        spinner.setOnItemSelectedListener(this);

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            button.setOnClickListener(view -> {
                if (inputListName == null || inputListName.getText().toString().isEmpty()){
                    inputListName.setError("Enter a name");
                }else{
                    String listName = inputListName.getText().toString().trim();
                    createNewList(listName);
                    adapterStore.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            buttonNegative.setOnClickListener(view -> {dialog.dismiss();});
        });
        dialog.create();
        dialog.show();
    }

    /* CRUD ACTION */

    private void createNewList(String listName){

        final List list = new List(listName, selectedStore);
        //System.out.println(list.getTitle());
        realm.executeTransaction((realm) -> realm.copyToRealm(list));
    }
    private void createNewStore(String name){
        final Store store = new Store(name);
        realm.executeTransaction((realm) -> realm.copyToRealm(store) );
    }


    /**/

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
