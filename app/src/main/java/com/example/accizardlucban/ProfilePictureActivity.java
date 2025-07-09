package com.example.accizardlucban;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProfilePictureActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 103;

    private ImageView ivProfilePicture;
    private Button btnTakePhoto, btnUploadFromGallery, btnNext, btnBack;
    private String firstName, lastName, mobileNumber, email, password, province, cityTown, barangay;
    private Uri profileImageUri;
    private boolean hasProfilePicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        try {
            initializeViews();
            getIntentData();
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading profile picture activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            ivProfilePicture = findViewById(R.id.ivProfilePicture);
            btnTakePhoto = findViewById(R.id.btnTakePhoto);
            btnUploadFromGallery = findViewById(R.id.btnUploadFromGallery);
            btnNext = findViewById(R.id.btnNext);
            btnBack = findViewById(R.id.btnBack);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            firstName = intent.getStringExtra("firstName");
            lastName = intent.getStringExtra("lastName");
            mobileNumber = intent.getStringExtra("mobileNumber");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            province = intent.getStringExtra("province");
            cityTown = intent.getStringExtra("cityTown");
            barangay = intent.getStringExtra("barangay");

            // Debug: Check if data is received
            if (firstName == null || lastName == null) {
                Toast.makeText(this, "Warning: Some user data is missing", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error getting intent data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupClickListeners() {
        if (btnTakePhoto != null) {
            btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (checkCameraPermission()) {
                            openCamera();
                        } else {
                            requestCameraPermission();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfilePictureActivity.this, "Error accessing camera", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (btnUploadFromGallery != null) {
            btnUploadFromGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (checkStoragePermission()) {
                            openGallery();
                        } else {
                            requestStoragePermission();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfilePictureActivity.this, "Error accessing gallery", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (btnNext != null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Optional: Check if profile picture is uploaded
                        if (!hasProfilePicture) {
                            Toast.makeText(ProfilePictureActivity.this, "Please upload a profile picture or skip to continue", Toast.LENGTH_SHORT).show();
                        }
                        proceedToValidId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfilePictureActivity.this, "Error proceeding to next step", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfilePictureActivity.this, "Error going back", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
            Toast.makeText(this, "Error opening camera", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Error opening gallery", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Error handling permission result", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK && data != null) {
                if (requestCode == CAMERA_REQUEST_CODE) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (bitmap != null && ivProfilePicture != null) {
                        ivProfilePicture.setImageBitmap(bitmap);
                        hasProfilePicture = true;
                        Toast.makeText(this, "Profile picture captured successfully", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == GALLERY_REQUEST_CODE) {
                    profileImageUri = data.getData();
                    if (profileImageUri != null && ivProfilePicture != null) {
                        ivProfilePicture.setImageURI(profileImageUri);
                        hasProfilePicture = true;
                        Toast.makeText(this, "Profile picture selected successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void proceedToValidId() {
        try {
            Intent intent = new Intent(ProfilePictureActivity.this, ValidIdActivity.class);

            // Pass all the data to ValidIdActivity
            intent.putExtra("firstName", firstName != null ? firstName : "");
            intent.putExtra("lastName", lastName != null ? lastName : "");
            intent.putExtra("mobileNumber", mobileNumber != null ? mobileNumber : "");
            intent.putExtra("email", email != null ? email : "");
            intent.putExtra("password", password != null ? password : "");
            intent.putExtra("province", province != null ? province : "");
            intent.putExtra("cityTown", cityTown != null ? cityTown : "");
            intent.putExtra("barangay", barangay != null ? barangay : "");

            startActivity(intent);
            Toast.makeText(this, "Proceeding to Valid ID verification", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error proceeding to Valid ID: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}