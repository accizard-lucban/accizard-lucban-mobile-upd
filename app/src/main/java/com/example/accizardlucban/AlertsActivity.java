package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class AlertsActivity extends AppCompatActivity {

    private Spinner filterSpinner;
    private ImageView profileIcon;
    private LinearLayout navHome, navChat, navReport, navMap, navAlerts;
    private RecyclerView announcementsRecyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList = new ArrayList<>();
    private List<Announcement> fullAnnouncementList = new ArrayList<>(); // For filtering
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        initViews();
        setupSpinner();
        setupClickListeners();
        setupAnnouncementsRecyclerView();
        fetchAnnouncements();
    }

    private void initViews() {
        filterSpinner = findViewById(R.id.filter_spinner);
        profileIcon = findViewById(R.id.profile_icon);
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navReport = findViewById(R.id.nav_report);
        navMap = findViewById(R.id.nav_map);
        navAlerts = findViewById(R.id.nav_alerts);
        announcementsRecyclerView = findViewById(R.id.announcementsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> fetchAnnouncements());
        }
    }

    private void setupSpinner() {
        String[] filterOptions = {"All", "Weather Warning", "Flood", "Landslide", "Earthquake", "Road Closure", "Evacuation Order", "Missing Person", "Informational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = filterOptions[position];
                filterAnnouncements(selected);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void filterAnnouncements(String filter) {
        announcementList.clear();
        if (filter.equals("All")) {
            announcementList.addAll(fullAnnouncementList);
        } else {
            for (Announcement ann : fullAnnouncementList) {
                if (ann.type != null && ann.type.equalsIgnoreCase(filter)) {
                    announcementList.add(ann);
                }
            }
        }
        announcementAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on home screen
                Intent intent = new Intent(AlertsActivity.this, MainDashboard.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Chat Activity
                Intent intent = new Intent(AlertsActivity.this, ChatActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Report Activity
                Intent intent = new Intent(AlertsActivity.this, ReportSubmissionActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Map Activity
                Intent intent = new Intent(AlertsActivity.this, MapViewActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on alerts/announcements screen
                Intent intent = new Intent(AlertsActivity.this, AlertsActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    private void setupAnnouncementsRecyclerView() {
        announcementAdapter = new AnnouncementAdapter(announcementList);
        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementsRecyclerView.setAdapter(announcementAdapter);
    }

    private void fetchAnnouncements() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("announcements")
                .orderBy("createdTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        int count = task.getResult().size();
                        android.util.Log.d("AlertsActivity", "Fetched " + count + " announcements");
                        if (count == 0) {
                            android.widget.Toast.makeText(this, "No announcements found.", android.widget.Toast.LENGTH_SHORT).show();
                        }
                        announcementList.clear();
                        fullAnnouncementList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Announcement ann = new Announcement(
                                    doc.getString("type"),
                                    doc.getString("priority"),
                                    doc.getString("description"),
                                    doc.getString("date")
                            );
                            announcementList.add(ann);
                            fullAnnouncementList.add(ann);
                        }
                        announcementAdapter.notifyDataSetChanged();
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        android.util.Log.e("AlertsActivity", "Error fetching announcements: " + errorMsg);
                        android.widget.Toast.makeText(this, "Error fetching announcements: " + errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Announcement model
    public static class Announcement {
        public String type, priority, message, date;
        public Announcement(String type, String priority, String message, String date) {
            this.type = type != null ? type : "Announcement";
            this.priority = priority != null ? priority : "Medium";
            this.message = message != null ? message : "";
            this.date = date != null ? date : "";
        }
    }

    // RecyclerView Adapter
    public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
        private List<Announcement> announcements;
        public AnnouncementAdapter(List<Announcement> announcements) {
            this.announcements = announcements;
        }
        @Override
        public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_announcement, parent, false);
            return new AnnouncementViewHolder(view);
        }
        @Override
        public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
            Announcement ann = announcements.get(position);
            holder.tvType.setText(ann.type);
            holder.tvPriority.setText(ann.priority);
            holder.tvMessage.setText(ann.message);
            holder.tvDate.setText(ann.date);
            // Optionally, set background color based on priority
            int bgRes = R.drawable.medium_priority_bg;
            if ("High".equalsIgnoreCase(ann.priority)) bgRes = R.drawable.high_priority_bg;
            else if ("Low".equalsIgnoreCase(ann.priority)) bgRes = R.drawable.low_priority_bg;
            holder.tvPriority.setBackgroundResource(bgRes);
        }
        @Override
        public int getItemCount() { return announcements.size(); }
        class AnnouncementViewHolder extends RecyclerView.ViewHolder {
            TextView tvType, tvPriority, tvMessage, tvDate;
            AnnouncementViewHolder(View itemView) {
                super(itemView);
                tvType = itemView.findViewById(R.id.tvAnnouncementType);
                tvPriority = itemView.findViewById(R.id.tvAnnouncementPriority);
                tvMessage = itemView.findViewById(R.id.tvAnnouncementMessage);
                tvDate = itemView.findViewById(R.id.tvAnnouncementDate);
            }
        }
    }
}