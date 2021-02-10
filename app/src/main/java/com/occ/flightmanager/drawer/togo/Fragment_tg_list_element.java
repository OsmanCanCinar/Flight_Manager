package com.occ.flightmanager.drawer.togo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.occ.flightmanager.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Fragment_tg_list_element extends Fragment {

    EditText destName, coName;
    ImageView destImg;
    Button btnChose, btnSave, btnUpdate, btnDelete;
    String query, category, destination, country;
    int id;

    SQLiteDatabase database;
    SQLiteStatement sqLiteStatement;

    Bitmap imgChosen, shrank;
    ByteArrayOutputStream oStream;
    byte[] imgToBytes;

    SaveBitmap saveBitmap;
    private static final String NAME = "saveBitmap";

    public Fragment_tg_list_element() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tg_list_element, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        destImg = view.findViewById(R.id.selectedImg);
        destName = view.findViewById(R.id.destination_text);
        coName = view.findViewById(R.id.country_text);
        btnChose = view.findViewById(R.id.chooseImageBtn);
        btnSave = view.findViewById(R.id.saveBtn);
        btnUpdate = view.findViewById(R.id.updateBtn);
        btnDelete = view.findViewById(R.id.deleteBtn);
        oStream= new ByteArrayOutputStream();
        category = getResources().getString(R.string.empty);

        if(getArguments() != null) {
            category = Fragment_tg_list_elementArgs.fromBundle(getArguments()).getRecordType();
        }
        else
            category = getResources().getString(R.string.create);

        if(category.matches(getResources().getString(R.string.create))){
            setBlank();
        }else{
            btnSave.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnChose.setVisibility(View.GONE);
            id = Fragment_tg_list_elementArgs.fromBundle(getArguments()).getRecordId();
            displayData(id);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToList(view);
            }
        });

        btnChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromGallery();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view,id);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(view,id);
            }
        });

        startSavedBitmapFragment();
        loadBitmap();
    }

    public void delete(final View view, final int id){
        final String query = getResources().getString(R.string.query_delete);
        final String db = getResources().getString(R.string.db_name);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getResources().getString(R.string.wanna_delete));
        alert.setMessage(destName.getText().toString() + getResources().getString(R.string.r_u_sure));
        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
                    database.execSQL(query,new String[]{String.valueOf(id)});
                    Toast.makeText(getContext(),getResources().getString(R.string.delete_successful),Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(getContext(),getResources().getString(R.string.cannot_delete),Toast.LENGTH_SHORT).show();
                }
                database.close();
                navigateBackToList(view);
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),getResources().getString(R.string.not_saved),Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

    public void displayData(int id){
        final String db = getResources().getString(R.string.db_name);
        String query = getResources().getString(R.string.query_display);
        String picture = getResources().getString(R.string.col_picture);
        String desName = getResources().getString(R.string.col_dName);
        String cName = getResources().getString(R.string.col_cName);
        try {
            database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery(query,new String[]{String.valueOf(id)});
            int imgIndex = cursor.getColumnIndex(picture);
            int destinationIndex = cursor.getColumnIndex(desName);
            int countryIndex = cursor.getColumnIndex(cName);

            while (cursor.moveToNext()) {
                destName.setText(cursor.getString(destinationIndex));
                coName.setText(cursor.getString(countryIndex));

                byte[] bytes = cursor.getBlob(imgIndex);
                Bitmap img = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                destImg.setImageBitmap(img);
            }
            cursor.close();
            database.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBlank(){
        Bitmap defaultImg = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.circle);
        destImg.setImageBitmap(defaultImg);
        destName.setText(getResources().getString(R.string.empty));
        coName.setText(getResources().getString(R.string.empty));
        btnSave.setVisibility(View.VISIBLE);
        btnChose.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
    }

    public void update(View view,int id){
        String query = getResources().getString(R.string.query_update);
        final String db = getResources().getString(R.string.db_name);
        destination = destName.getText().toString();
        country = coName.getText().toString();

        btnSave.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
        btnChose.setVisibility(View.GONE);

        try{
           database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
           SQLiteStatement sqLiteStatement = database.compileStatement(query);
           sqLiteStatement.bindString(1,destination);
           sqLiteStatement.bindString(2,country);
           sqLiteStatement.bindString(3,String.valueOf(id));
           sqLiteStatement.execute();
           Toast.makeText(getContext(),getResources().getString(R.string.updated),Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
             Toast.makeText(getContext(),getResources().getString(R.string.noway_2_update),Toast.LENGTH_SHORT).show();
        }
        database.close();
        navigateBackToList(view);
    }

    public void addToList(View view){
        final String db = getResources().getString(R.string.db_name);
        destination = destName.getText().toString();
        country = coName.getText().toString();
        String dbQuery = getResources().getString(R.string.query_element);
        query = getResources().getString(R.string.query_insert);

        if(imgChosen == null){
            Toast.makeText(getContext(),getResources().getString(R.string.must_select),Toast.LENGTH_SHORT).show();
        }
        else{
            shrank = shrinkIt(400);
            shrank.compress(Bitmap.CompressFormat.JPEG,50,oStream);
            imgToBytes = oStream.toByteArray();
            try{
                database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
                database.execSQL(dbQuery);
                sqLiteStatement = database.compileStatement(query);
                sqLiteStatement.bindString(1,destination);
                sqLiteStatement.bindString(2,country);
                sqLiteStatement.bindBlob(3,imgToBytes);
                sqLiteStatement.execute();
                Toast.makeText(getContext(),getResources().getString(R.string.successful_msg),Toast.LENGTH_SHORT).show();
            }catch (Exception exception){
                exception.printStackTrace();
                Toast.makeText(getContext(),getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
            }
            database.close();
            navigateBackToList(view);
        }
    }

    public void navigateBackToList(View view){
        NavDirections directions = Fragment_tg_list_elementDirections.actionFragmentTgListElementToFragmentTgList();
        Navigation.findNavController(view).navigate(directions);
    }

    public Bitmap shrinkIt(int size){
        float rate;
        int width, height;

        width = imgChosen.getWidth();
        height = imgChosen.getHeight();
        rate = (float) width / (float) height;

        if(rate <= 1 ){
            width = (int) (size * rate);
            return Bitmap.createScaledBitmap(imgChosen,width,size,true);
        }else{
            height = (int) (size/rate);
            return Bitmap.createScaledBitmap(imgChosen,size,height,true);
        }
    }

    public void startSavedBitmapFragment(){
        FragmentManager fragmentManager = getChildFragmentManager();
         this.saveBitmap = (SaveBitmap) fragmentManager.findFragmentByTag(NAME);
         if(this.saveBitmap == null){
             this.saveBitmap = new SaveBitmap();
             fragmentManager.beginTransaction().add(this.saveBitmap,NAME).commit();
         }
    }

    public void loadBitmap(){
        if (this.saveBitmap == null)
            return;

        imgChosen = this.saveBitmap.getBitmap();
        if (imgChosen == null)
            return;

        destImg.setImageBitmap(imgChosen);
    }

    public void chooseFromGallery(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent toMediaStore = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(toMediaStore,124);
        }
        else
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 123){
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Intent toMediaStore = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(toMediaStore,124);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(!(data ==null) && resultCode == Activity.RESULT_OK && (requestCode == 124)){
            Uri uriOfImg = data.getData();

            try {
                if (Build.VERSION.SDK_INT < 28)
                    //noinspection deprecation
                    imgChosen = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uriOfImg);
                else {
                    /*  when user tries to save a record without selecting a picture, I used have error caused empty uri(address of data in device.
                        I handled this problem with if clause in line 226
                     */
                    ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), uriOfImg);
                    imgChosen = ImageDecoder.decodeBitmap(source);
                }
                this.saveBitmap.setBitmap(imgChosen);
                destImg.setImageBitmap(imgChosen);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}