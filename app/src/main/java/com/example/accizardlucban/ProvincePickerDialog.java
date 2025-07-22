package com.example.accizardlucban;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class ProvincePickerDialog extends Dialog {
    private final List<String> provinces;
    private final OnProvinceSelectedListener listener;

    public interface OnProvinceSelectedListener {
        void onProvinceSelected(String province);
    }

    public ProvincePickerDialog(@NonNull Context context, OnProvinceSelectedListener listener) {
        super(context);
        this.listener = listener;
        this.provinces = Arrays.asList(context.getResources().getStringArray(R.array.province_list));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_province_picker);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewProvinces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProvinceAdapter adapter = new ProvinceAdapter(provinces, province -> {
            listener.onProvinceSelected(province);
            dismiss();
        });
        recyclerView.setAdapter(adapter);
        // TODO: Integrate fast scroller library for full A-Z index bar
    }
} 