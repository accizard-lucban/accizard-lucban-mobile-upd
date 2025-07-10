package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private ImageView sendButton, backButton, profileButton;
    private LinearLayout homeTab, chatTab, reportTab, mapTab, alertsTab;
    private LinearLayout takePhotoButton, openGalleryButton, referenceReportButton;
    private TextView statusText;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ChatActivity onCreate started");

        try {
            setContentView(R.layout.activity_chat);
            initializeViews();
            setupRecyclerView();
            setupClickListeners();
            loadInitialMessages();
            Log.d(TAG, "ChatActivity onCreate completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in ChatActivity onCreate", e);
            Toast.makeText(this, "Error loading chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            Log.d(TAG, "Initializing views");

            messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
            messageInput = findViewById(R.id.messageInput);
            sendButton = findViewById(R.id.sendButton);
            backButton = findViewById(R.id.backButton);
            statusText = findViewById(R.id.statusText);
            profileButton = findViewById(R.id.profileButton);

            // Bottom navigation
            homeTab = findViewById(R.id.homeTab);
            chatTab = findViewById(R.id.chatTab);
            reportTab = findViewById(R.id.reportTab);
            mapTab = findViewById(R.id.mapTab);
            alertsTab = findViewById(R.id.alertsTab);

            // Action buttons
            takePhotoButton = findViewById(R.id.takePhotoButton);
            openGalleryButton = findViewById(R.id.openGalleryButton);
            referenceReportButton = findViewById(R.id.referenceReportButton);

            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            Toast.makeText(this, "Error initializing chat views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        try {
            Log.d(TAG, "Setting up RecyclerView");

            messagesList = new ArrayList<>();
            chatAdapter = new ChatAdapter(messagesList);

            if (messagesRecyclerView != null) {
                messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                messagesRecyclerView.setAdapter(chatAdapter);
                Log.d(TAG, "RecyclerView setup completed");
            } else {
                Log.e(TAG, "messagesRecyclerView is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView", e);
            Toast.makeText(this, "Error setting up chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        try {
            Log.d(TAG, "Setting up click listeners");

            // Back button
            if (backButton != null) {
                backButton.setOnClickListener(v -> {
                    Log.d(TAG, "Back button clicked");
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                });
            }

            // Send button
            if (sendButton != null) {
                sendButton.setOnClickListener(v -> {
                    Log.d(TAG, "Send button clicked");
                    sendMessage();
                });
            }

            // Bottom navigation tabs
            setupBottomNavigationListeners();

            // Action buttons
            setupActionButtons();

            Log.d(TAG, "Click listeners setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigationListeners() {
        // Home tab
        if (homeTab != null) {
            homeTab.setOnClickListener(v -> {
                Log.d(TAG, "Home tab clicked");
                try {
                    Intent intent = new Intent(ChatActivity.this, MainDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to home", e);
                    Toast.makeText(ChatActivity.this, "Error navigating to home: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Chat tab
        if (chatTab != null) {
            chatTab.setOnClickListener(v -> {
                Log.d(TAG, "Chat tab clicked");
                Toast.makeText(ChatActivity.this, "Already on Chat", Toast.LENGTH_SHORT).show();
            });
        }

        // Report tab - Main implementation for navigation to ReportSubmissionActivity
        if (reportTab != null) {
            reportTab.setOnClickListener(v -> {
                Log.d(TAG, "Report tab clicked - navigating to ReportSubmissionActivity");
                try {
                    Intent intent = new Intent(ChatActivity.this, ReportSubmissionActivity.class);
                    intent.putExtra("from_activity", "ChatActivity");

                    Log.d(TAG, "Starting ReportSubmissionActivity");
                    startActivity(intent);

                    // Add smooth transition animation
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    Log.d(TAG, "Successfully navigated to ReportSubmissionActivity");

                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to ReportSubmissionActivity", e);
                    Toast.makeText(ChatActivity.this, "Error opening report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "reportTab is null - check your layout file for R.id.reportTab");
        }

        // Map tab
        if (mapTab != null) {
            mapTab.setOnClickListener(v -> {
                Log.d(TAG, "Map tab clicked");
                try {
                    Intent intent = new Intent(ChatActivity.this, MapViewActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to map", e);
                    Toast.makeText(ChatActivity.this, "Map feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Alerts tab
        if (alertsTab != null) {
            alertsTab.setOnClickListener(v -> {
                Log.d(TAG, "Alerts tab clicked");
                try {
                    Intent intent = new Intent(ChatActivity.this, AlertsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to alerts", e);
                    Toast.makeText(ChatActivity.this, "Alerts feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Profile tab
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                Log.d(TAG, "Profile tab clicked");
                try {
                    Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to profile", e);
                    Toast.makeText(ChatActivity.this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupActionButtons() {
        // Take photo button
        if (takePhotoButton != null) {
            takePhotoButton.setOnClickListener(v -> {
                Log.d(TAG, "Take photo button clicked");
                addMessage("📸 Taking a photo...", true, getCurrentTime());
                Toast.makeText(ChatActivity.this, "Camera feature coming soon", Toast.LENGTH_SHORT).show();
            });
        }

        // Open gallery button
        if (openGalleryButton != null) {
            openGalleryButton.setOnClickListener(v -> {
                Log.d(TAG, "Open gallery button clicked");
                addMessage("🖼️ Opening gallery...", true, getCurrentTime());
                Toast.makeText(ChatActivity.this, "Gallery feature coming soon", Toast.LENGTH_SHORT).show();
            });
        }

        // Reference report button
        if (referenceReportButton != null) {
            referenceReportButton.setOnClickListener(v -> {
                Log.d(TAG, "Reference report button clicked");
                addMessage("Reference to my report #REP-001", true, getCurrentTime());
            });
        }
    }

    private void loadInitialMessages() {
        try {
            Log.d(TAG, "Loading initial messages");
            addMessage("Hello! How can we assist you today?", false, "Today • 11:54 AM");
            addMessage("Welcome to ACCiZard Lucban Chat Support!", false, getCurrentTime());
            Log.d(TAG, "Initial messages loaded");
        } catch (Exception e) {
            Log.e(TAG, "Error loading initial messages", e);
            Toast.makeText(this, "Error loading initial messages", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage() {
        try {
            if (messageInput == null) {
                Log.e(TAG, "messageInput is null");
                return;
            }

            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                Log.d(TAG, "Sending message: " + message);
                addMessage(message, true, getCurrentTime());
                messageInput.setText("");

                // Simulate response after a delay
                if (messagesRecyclerView != null) {
                    messagesRecyclerView.postDelayed(() -> {
                        addMessage("Thank you for your message. We'll get back to you shortly.", false, getCurrentTime());
                    }, 1000);
                }
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending message", e);
            Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMessage(String content, boolean isUser, String timestamp) {
        try {
            if (messagesList == null || chatAdapter == null) {
                Log.e(TAG, "messagesList or chatAdapter is null");
                return;
            }

            ChatMessage message = new ChatMessage(content, isUser, timestamp);
            messagesList.add(message);
            chatAdapter.notifyItemInserted(messagesList.size() - 1);

            if (messagesRecyclerView != null) {
                messagesRecyclerView.scrollToPosition(messagesList.size() - 1);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding message", e);
            Toast.makeText(this, "Error adding message", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("Today • h:mm a", Locale.getDefault());
            return sdf.format(new Date());
        } catch (Exception e) {
            Log.e(TAG, "Error getting current time", e);
            return "Now";
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed called");
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ChatActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "ChatActivity onPause");
    }
}