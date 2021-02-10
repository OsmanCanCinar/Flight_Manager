package com.occ.flightmanager.drawer.togo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.occ.flightmanager.R;
import java.util.ArrayList;

public class AdapterForRecyclerView extends RecyclerView.Adapter<AdapterForRecyclerView.PlaceHolder> {

    ArrayList<String> destinations;
    ArrayList<String> countries;
    ArrayList<Integer> ids;

    public AdapterForRecyclerView(ArrayList<String> destinations, ArrayList<String> countries, ArrayList<Integer> ids) {
        this.destinations = destinations;
        this.countries = countries;
        this.ids = ids;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_row_detail,parent,false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, final int position) {
        final String s = holder.itemView.getResources().getString(R.string.edit);
        holder.destinationDisplay.setText(destinations.get(position));
        holder.countryDisplay.setText(countries.get(position));
        holder.recordIdDisplay.setText(String.valueOf(ids.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_tg_listDirections.ActionFragmentTgListToFragmentTgListElement navAction = Fragment_tg_listDirections.actionFragmentTgListToFragmentTgListElement(s);
                navAction.setRecordType(s);
                navAction.setRecordId(ids.get(position));
                Navigation.findNavController(view).navigate(navAction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder {

        TextView destinationDisplay, countryDisplay, recordIdDisplay;

        public PlaceHolder(@NonNull View view){
            super(view);

            recordIdDisplay = view.findViewById(R.id.recordID);
            destinationDisplay = view.findViewById(R.id.destinationName);
            countryDisplay = view.findViewById(R.id.countryName);
        }
    }
}