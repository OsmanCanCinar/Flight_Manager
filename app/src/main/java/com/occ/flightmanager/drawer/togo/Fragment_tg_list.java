package com.occ.flightmanager.drawer.togo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.occ.flightmanager.R;
import java.util.ArrayList;

public class Fragment_tg_list extends Fragment {

    SQLiteDatabase database;
    AdapterForRecyclerView adapter;
    ArrayList<String> destinations;
    ArrayList<String> countries;
    ArrayList<Integer> ids;
    FloatingActionButton floatingActionButton;

    public Fragment_tg_list() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tg_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_list);
        destinations = new ArrayList<>();
        countries = new ArrayList<>();
        ids = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterForRecyclerView(destinations,countries,ids);
        recyclerView.setAdapter(adapter);
        populateWithData();

        final String create = getResources().getString(R.string.create);
        floatingActionButton = view.findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_tg_listDirections.ActionFragmentTgListToFragmentTgListElement navAction = Fragment_tg_listDirections.actionFragmentTgListToFragmentTgListElement(create);
                Navigation.findNavController(view).navigate(navAction);
            }
        });
    }

    public void populateWithData(){
        int idIndex, destinationIndex, countryIndex;
        String db = getResources().getString(R.string.db_name);
        String query = getResources().getString(R.string.query_list);
        String id = getResources().getString(R.string.col_id);
        String dName = getResources().getString(R.string.col_dName);
        String cName = getResources().getString(R.string.col_cName);

        try {
            database = requireActivity().openOrCreateDatabase(db, Context.MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery(query, null);
            idIndex = cursor.getColumnIndex(id);
            destinationIndex = cursor.getColumnIndex(dName);
            countryIndex = cursor.getColumnIndex(cName);
            while (cursor.moveToNext()) {
                destinations.add(cursor.getString(destinationIndex));
                countries.add(cursor.getString(countryIndex));
                ids.add(cursor.getInt(idIndex));
            }
            adapter.notifyDataSetChanged();
            cursor.close();
            database.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}