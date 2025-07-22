package com.example.accizardlucban;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitialLetterAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private final List<String> originalData;
    private final CustomFilter customFilter = new CustomFilter();

    public InitialLetterAutoCompleteAdapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(Arrays.asList(objects)));
        this.originalData = new ArrayList<>(Arrays.asList(objects));
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return customFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No filter, return all suggestions
                suggestions.addAll(originalData);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : originalData) {
                    // Split the item by spaces to check each word
                    String[] words = item.split(" ");
                    for (String word : words) {
                        if (word.toLowerCase().startsWith(filterPattern)) {
                            suggestions.add(item);
                            break; // Match found, no need to check other words in this item
                        }
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            clear();
            if (results.values != null) {
                addAll((List<String>) results.values);
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return (String) resultValue;
        }
    }
} 