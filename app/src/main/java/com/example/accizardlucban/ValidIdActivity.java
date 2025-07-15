package com.example.accizardlucban;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ValidIdActivity extends AppCompatActivity {

    private static final String TAG = "ValidIdActivity";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    private ImageView ivValidId;
    private Button btnTakePhoto, btnUploadFromGallery, btnNext;
    private ImageButton btnBack;
    private TextView tvValidIdList;
    private String firstName, lastName, mobileNumber, email, password, province, cityTown, barangay;
    private Uri validIdUri;
    private boolean hasValidId = false;
    
    // Profile picture data
    private boolean hasProfilePicture = false;
    private String profileImageUriString;
    
    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_id);

        try {
            // Initialize Firebase
            mAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            
            initializeViews();
            getIntentData();
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading Valid ID activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            // Initialize all views
            ivValidId = findViewById(R.id.iv_valid_id);
            btnUploadFromGallery = findViewById(R.id.btn_upload_gallery);
            btnNext = findViewById(R.id.btn_next);
            btnBack = findViewById(R.id.btn_back);
            tvValidIdList = findViewById(R.id.tv_valid_ids_list);

            // Check if views are found
            if (btnTakePhoto == null) {
                Toast.makeText(this, "Error: Take Photo button not found", Toast.LENGTH_SHORT).show();
            }
            if (btnUploadFromGallery == null) {
                Toast.makeText(this, "Error: Upload Gallery button not found", Toast.LENGTH_SHORT).show();
            }
            if (btnNext == null) {
                Toast.makeText(this, "Error: Next button not found", Toast.LENGTH_SHORT).show();
            }
            if (btnBack == null) {
                Toast.makeText(this, "Error: Back button not found", Toast.LENGTH_SHORT).show();
            }
            if (tvValidIdList == null) {
                Toast.makeText(this, "Error: Valid ID list text not found", Toast.LENGTH_SHORT).show();
            }
            if (ivValidId == null) {
                Toast.makeText(this, "Error: Image view not found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                firstName = intent.getStringExtra("firstName");
                lastName = intent.getStringExtra("lastName");
                mobileNumber = intent.getStringExtra("mobileNumber");
                email = intent.getStringExtra("email");
                password = intent.getStringExtra("password");
                province = intent.getStringExtra("province");
                cityTown = intent.getStringExtra("cityTown");
                barangay = intent.getStringExtra("barangay");
                
                // Get profile picture data
                hasProfilePicture = intent.getBooleanExtra("hasProfilePicture", false);
                profileImageUriString = intent.getStringExtra("profileImageUri");

                // Debug: Check if data is received
                if (firstName == null || lastName == null) {
                    Toast.makeText(this, "Warning: Some user data is missing", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error getting intent data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        try {
            if (btnTakePhoto != null) {
                btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(ValidIdActivity.this, "Take Photo button clicked", Toast.LENGTH_SHORT).show();
                            if (checkCameraPermission()) {
                                openCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error accessing camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            if (btnUploadFromGallery != null) {
                btnUploadFromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(ValidIdActivity.this, "Upload Gallery button clicked", Toast.LENGTH_SHORT).show();
                            if (checkStoragePermission()) {
                                openGallery();
                            } else {
                                requestStoragePermission();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error accessing gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            if (btnNext != null) {
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (!hasValidId) {
                                Toast.makeText(ValidIdActivity.this, "Please upload a valid ID to continue", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // Create user account and save to Firestore
                            createUserAccount();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error creating account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            if (btnBack != null) {
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(ValidIdActivity.this, "Back button clicked", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error going back: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            if (tvValidIdList != null) {
                tvValidIdList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            showValidIdList();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error showing valid ID list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private boolean checkStoragePermission() {
        // For Android 13 and above, use READ_MEDIA_IMAGES
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void openCamera() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Gallery not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showValidIdList() {
        try {
            String validIds = "Valid IDs accepted:\n\n" +
                    "• Driver's License\n" +
                    "• Passport\n" +
                    "• National ID (PhilID)\n" +
                    "• SSS ID\n" +
                    "• PhilHealth ID\n" +
                    "• TIN ID\n" +
                    "• Voter's ID\n" +
                    "• Senior Citizen ID\n" +
                    "• PWD ID\n" +
                    "• Postal ID\n" +
                    "• Barangay ID\n" +
                    "• School ID (with signature)\n" +
                    "• Company ID (with signature)";

            new AlertDialog.Builder(this)
                    .setTitle("Valid IDs")
                    .setMessage(validIds)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error showing valid ID list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void enableNextButton() {
        if (btnNext != null) {
            btnNext.setEnabled(true);
            // You can also change the button appearance here if needed
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error handling permission result: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK && data != null) {
                if (requestCode == CAMERA_REQUEST_CODE) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (bitmap != null && ivValidId != null) {
                        ivValidId.setImageBitmap(bitmap);
                        ivValidId.setVisibility(View.VISIBLE);
                        hasValidId = true;
                        enableNextButton();
                        Toast.makeText(this, "Valid ID captured successfully", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == GALLERY_REQUEST_CODE) {
                    validIdUri = data.getData();
                    if (validIdUri != null && ivValidId != null) {
                        ivValidId.setImageURI(validIdUri);
                        ivValidId.setVisibility(View.VISIBLE);
                        hasValidId = true;
                        enableNextButton();
                        Toast.makeText(this, "Valid ID selected successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createUserAccount() {
        btnNext.setEnabled(false);
        btnNext.setText("Creating Account...");

        // Create user with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Generate custom userId (RID-[auto-incremented])
                                generateCustomUserIdAndContinue(user);
                            }
                        } else {
                            btnNext.setEnabled(true);
                            btnNext.setText("Next");
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ValidIdActivity.this,
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Generate custom userId in the format RID-[auto-incremented value]
    private void generateCustomUserIdAndContinue(FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .orderBy("userId", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    String newUserId = "RID-1";
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String lastUserId = doc.getString("userId");
                            if (lastUserId != null && lastUserId.startsWith("RID-")) {
                                try {
                                    int lastNum = Integer.parseInt(lastUserId.replace("RID-", ""));
                                    newUserId = "RID-" + (lastNum + 1);
                                } catch (NumberFormatException e) {
                                    // fallback to RID-1 if parsing fails
                                    newUserId = "RID-1";
                                }
                            }
                            break; // Only need the first (highest)
                        }
                    }
                    // Continue registration with newUserId
                    uploadImagesAndSaveUserData(newUserId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to generate user ID: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    btnNext.setEnabled(true);
                    btnNext.setText("Next");
                });
    }

    private void uploadImagesAndSaveUserData(String userId) {
        // Upload profile picture if exists
        if (hasProfilePicture && profileImageUriString != null) {
            uploadProfilePicture(userId, new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String profilePictureUrl) {
                    // Upload valid ID
                    uploadValidId(userId, profilePictureUrl);
                }
            });
        } else {
            // Upload valid ID only
            uploadValidId(userId, null);
        }
    }

    private void uploadProfilePicture(String userId, OnSuccessListener<String> onProfileSuccess) {
        try {
            Uri profileImageUri = Uri.parse(profileImageUriString);
            StorageReference profileImageRef = storageRef.child("profile_pictures/" + userId + ".jpg");

            // Convert image to bytes for upload
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            // Upload the image
            UploadTask uploadTask = profileImageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL
                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            onProfileSuccess.onSuccess(downloadUri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Failed to get profile picture download URL", e);
                            onProfileSuccess.onSuccess(null);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Failed to upload profile picture", e);
                    onProfileSuccess.onSuccess(null);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "Error processing profile picture", e);
            onProfileSuccess.onSuccess(null);
        }
    }

    private void uploadValidId(String userId, String profilePictureUrl) {
        if (validIdUri == null) {
            saveUserDataToFirestore(userId, profilePictureUrl, null);
            return;
        }

        try {
            StorageReference validIdRef = storageRef.child("valid_ids/" + userId + ".jpg");

            // Convert image to bytes for upload
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), validIdUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            // Upload the image
            UploadTask uploadTask = validIdRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL
                    validIdRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            saveUserDataToFirestore(userId, profilePictureUrl, downloadUri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Failed to get valid ID download URL", e);
                            saveUserDataToFirestore(userId, profilePictureUrl, null);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Failed to upload valid ID", e);
                    saveUserDataToFirestore(userId, profilePictureUrl, null);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "Error processing valid ID", e);
            saveUserDataToFirestore(userId, profilePictureUrl, null);
        }
    }

    private void saveUserDataToFirestore(String userId, String profilePictureUrl, String validIdUrl) {
        // Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("firebaseUid", mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "");
        userData.put("email", email);
        userData.put("fullName", firstName + " " + lastName);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);
        userData.put("phoneNumber", mobileNumber);
        userData.put("address", province + ", " + cityTown + ", " + barangay);
        userData.put("province", province);
        userData.put("cityTown", cityTown);
        userData.put("barangay", barangay);
        userData.put("profilePictureUrl", profilePictureUrl != null ? profilePictureUrl : "");
        userData.put("validIdUrl", validIdUrl != null ? validIdUrl : "");
        // Add createdDate and createdTime in requested formats (with AM/PM for time)
        String createdDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        String createdTime = new SimpleDateFormat("hh:mm:ss A", Locale.getDefault()).format(new Date());
        userData.put("createdDate", createdDate);
        userData.put("createdTime", createdTime);
        userData.put("isVerified", false);

        // Save to Firestore using FirestoreHelper with Firestore auto-ID
        FirestoreHelper.createUserWithAutoId(userData,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User data saved successfully");
                        // Navigate to success screen
                        proceedToSuccess();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnNext.setEnabled(true);
                        btnNext.setText("Next");
                        Log.w(TAG, "Error saving user data", e);
                        Toast.makeText(ValidIdActivity.this,
                                "Error saving user data: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void proceedToSuccess() {
        try {
            Intent intent = new Intent(ValidIdActivity.this, SuccessActivity.class);
            intent.putExtra("message", "Account created successfully!");
            intent.putExtra("nextActivity", "MainActivity");
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Registration completed successfully!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}