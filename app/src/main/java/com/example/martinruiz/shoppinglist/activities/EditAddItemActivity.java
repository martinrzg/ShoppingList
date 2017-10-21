package com.example.martinruiz.shoppinglist.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.martinruiz.shoppinglist.R;
import com.example.martinruiz.shoppinglist.models.Category;
import com.example.martinruiz.shoppinglist.models.Item;
import com.example.martinruiz.shoppinglist.models.List;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class EditAddItemActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO    = 1;
    private static final int REQUEST_GALLERY_PHOTO = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static final String FILE_PROVIDER = "com.martinruiz.shoppinglist.fileprovider" ;


    @BindView(R.id.famChoosePhoto) FloatingActionMenu famChoosePhoto;
    @BindView(R.id.imageViewItemPhoto) ImageView imageViewItemPhoto;
    @BindView(R.id.editTextItemName) EditText editTextItemName;
    @BindView(R.id.spinnerCategories) Spinner spinnerCategories;
    @BindView(R.id.editTextItemPrice) EditText editTextItemPrice;
    @BindView(R.id.editTextItemQuantity) EditText editTextItemQuantity;
    @BindView(R.id.editTextItemNote) EditText editTextItemNote;

    private ArrayAdapter<Category> adapterCategory;
    private RealmResults<Category> categories;

    private Realm realm;
    private String mCurrentPhotoPath;
    private Category selectedCategory;
    private int itemID = Integer.MIN_VALUE;
    private int listID = Integer.MIN_VALUE;

    private Item itemToEdit = null;
    private List list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        categories = realm.where(Category.class).findAll();

        if(categories == null || categories.isEmpty()){
            createNewCategory("General");
        }

        adapterCategory = new ArrayAdapter<Category>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        spinnerCategories.setAdapter(adapterCategory);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedCategory = (Category) adapterView.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().size() == 2){//EDIT
                setTitle("Edit Item");
                itemID = getIntent().getExtras().getInt("itemID");
                itemToEdit = realm.where(Item.class).equalTo("id",itemID).findFirst();
                listID = getIntent().getExtras().getInt("listID");
                list = realm.where(List.class).equalTo("id",listID).findFirst();

                setEditData(itemToEdit);
            }
            if(getIntent().getExtras().size()==1){//ADD
                setTitle("New Item");
                listID = getIntent().getExtras().getInt("listID");
                list = realm.where(List.class).equalTo("id",listID).findFirst();
            }
        }
    }

    public void setEditData(Item item) {
        editTextItemName.setText(item.getName());
        editTextItemPrice.setText(String.format("%.2f",item.getPrice()));
        editTextItemQuantity.setText(item.getQuantity()+"");
        spinnerCategories.setSelection(categories.indexOf(item.getCategory()));
        if(item.getImageURL()==null || item.getImageURL().isEmpty()){

        }else{
            mCurrentPhotoPath = item.getImageURL();
            File file = new File(item.getImageURL());
            Picasso.with(this).load(file).fit().into(imageViewItemPhoto);
        }
    }

    @OnClick(R.id.fabSaveItem)
    public void fabSaveItem(){
        String itemName;
        Double itemPrice = 0d;
        int itemQuantity = 0;
        String imageURL;
        String itemNote;
        boolean hasErrors = false;

        itemName = editTextItemName.getText().toString().trim();
        if(itemName.isEmpty()){
            editTextItemName.setError("Enter a name");
            hasErrors = true;
        }
        if(editTextItemPrice.getText().toString().trim().isEmpty()){
            editTextItemPrice.setError("Enter a price");
            hasErrors = true;
        }else {
            itemPrice = Double.parseDouble(editTextItemPrice.getText().toString().trim());
        }
        if(editTextItemQuantity.getText().toString().isEmpty()){
            editTextItemQuantity.setError("Enter a quantity");
            hasErrors = true;
        }else{
            itemQuantity = Integer.parseInt(editTextItemQuantity.getText().toString().trim());
        }
        if(mCurrentPhotoPath == null){
            imageURL = null;
        }else{
            imageURL = mCurrentPhotoPath;
        }
        itemNote = editTextItemNote.getText().toString().trim();

        if(!hasErrors){
            if(itemToEdit == null){
                itemToEdit = new Item(itemName,selectedCategory,itemPrice,imageURL,itemQuantity,itemNote);
                saveItem(itemToEdit);
            }else{
                Double finalItemPrice = itemPrice;
                int finalItemQuantity = itemQuantity;
                realm.executeTransaction((Realm realm) -> {
                    itemToEdit.setName(itemName);
                    itemToEdit.setCategory(selectedCategory);
                    itemToEdit.setPrice(finalItemPrice);
                    itemToEdit.setImageURL(imageURL);
                    itemToEdit.setQuantity(finalItemQuantity);
                    itemToEdit.setNote(itemNote);
                });
                onBackPressed();
            }
        }
    }
    @OnClick(R.id.fabCameraPhoto)
    public void takePhoto(){
        famChoosePhoto.close(true);
        dispatchTakePictureIntent();
    }
    @OnClick(R.id.fabGalleryPhoto)
    public void choosePhoto(){
        famChoosePhoto.close(true);
        validateStoragePermission();
    }
    @OnClick(R.id.imageViewAddCategory)
    public void addCategory(){
        showDialogAddStore("Add new Category",null);
    }

    private void validateStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "You did not grant access to gallery",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        ,REQUEST_EXTERNAL_STORAGE);
            }
        }else {
            intentToGallery();
        }
    }

    private void intentToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUEST_GALLERY_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    intentToGallery();
                }else {
                    Toast.makeText(this, "You did not grant access to gallery",Toast.LENGTH_LONG).show();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showDialogAddStore(String title, String message) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("Add",null)
                .setNegativeButton("Cancel",null)
                .create();
        if(title != null) dialog.setTitle(title);
        if(message != null) dialog.setMessage(message);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_category,null);
        dialog.setView(viewInflated);
        final EditText editTextCategoryName = viewInflated.findViewById(R.id.editTextCategoryName);
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            button.setOnClickListener(view -> {
                if (editTextCategoryName == null || editTextCategoryName.getText().toString().isEmpty()){
                    editTextCategoryName.setError("Enter a name");
                }else{
                    String shopName = editTextCategoryName.getText().toString().trim();
                    createNewCategory(shopName);
                    adapterCategory.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            buttonNegative.setOnClickListener(view -> {dialog.dismiss();});
        });
        dialog.create();
        dialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,FILE_PROVIDER,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == RESULT_OK ) {
                System.out.println("ADD GALLERY");
                //galleryAddPic();
                File f = new File(mCurrentPhotoPath);
                //Picasso.with(this).load(f).transform(new CropCircleTransformation()).into();
                Picasso.with(this).load(f).fit().into(imageViewItemPhoto);
            }
        }
        if(requestCode == REQUEST_GALLERY_PHOTO){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();

                String[] projection = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex); // returns null
                cursor.close();
                File f = new File(mCurrentPhotoPath);
                Picasso.with(this).load(f).fit().into(imageViewItemPhoto);
            }
        }
    }

    /* CRUD OPERATIONS */
    private void createNewCategory(String name){
        final Category category = new Category(name);
        realm.executeTransaction((realm) -> realm.copyToRealm(category) );
    }
    private void saveItem (Item item){
        realm.executeTransaction(realm -> {
            realm.copyToRealmOrUpdate(item);
            list.getItems().add(item);
        });
        onBackPressed();
    }



}
