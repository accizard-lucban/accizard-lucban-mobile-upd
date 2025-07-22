package com.example.accizardlucban;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    private final List<String> provinces;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(String province);
    }

    public ProvinceAdapter(List<String> provinces, OnItemClickListener listener) {
        this.provinces = provinces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String province = provinces.get(position);
        holder.textView.setText(province);
        holder.itemView.setOnClickListener(v -> listener.onClick(province));
    }

    @Override
    public int getItemCount() {
        return provinces.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
} 