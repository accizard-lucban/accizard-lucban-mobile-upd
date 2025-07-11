package com.example.accizardlucban;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SimpleSearchAdapter extends RecyclerView.Adapter<SimpleSearchAdapter.ViewHolder> {

    private List<SearchPlace> searchPlaces;
    private OnSearchPlaceClickListener listener;

    public static class SearchPlace {
        private String name;
        private String address;
        private double latitude;
        private double longitude;

        public SearchPlace(String name, String address, double latitude, double longitude) {
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() { return name; }
        public String getAddress() { return address; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }

    public interface OnSearchPlaceClickListener {
        void onSearchPlaceClick(SearchPlace place);
    }

    public SimpleSearchAdapter(List<SearchPlace> searchPlaces, OnSearchPlaceClickListener listener) {
        this.searchPlaces = searchPlaces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < searchPlaces.size()) {
            SearchPlace place = searchPlaces.get(position);
            holder.text1.setText(place.getName());
            holder.text2.setText(place.getAddress());
            
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSearchPlaceClick(place);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return searchPlaces != null ? searchPlaces.size() : 0;
    }

    public void updatePlaces(List<SearchPlace> newPlaces) {
        this.searchPlaces = newPlaces;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
} 