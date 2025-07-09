package com.example.accizardlucban;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.bind(report);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView typeTextView;
        private TextView descriptionTextView;
        private TextView locationTextView;
        private TextView timestampTextView;
        private TextView statusTextView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.reportTypeTextView);
            descriptionTextView = itemView.findViewById(R.id.reportDescriptionTextView);
            locationTextView = itemView.findViewById(R.id.reportLocationTextView);
            timestampTextView = itemView.findViewById(R.id.reportTimestampTextView);
            statusTextView = itemView.findViewById(R.id.reportStatusTextView);
        }

        public void bind(Report report) {
            typeTextView.setText(report.getReportType());
            descriptionTextView.setText(report.getDescription());
            locationTextView.setText(report.getLocation());
            timestampTextView.setText(report.getTimestamp());
            statusTextView.setText(report.getStatus());
        }
    }
}