package com.example.accizardlucban;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class ValidIdActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_id);

        try {
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
            ivValidId = findViewById(R.id.iv_valid_id);;
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
                            proceedToSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ValidIdActivity.this, "Error proceeding to next step: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void proceedToSuccess() {
        try {
            // Check if SuccessActivity exists, if not, just show a success message
            Intent intent = new Intent(ValidIdActivity.this, SuccessActivity.class);

            // Pass user data to SuccessActivity
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("email", email);
            intent.putExtra("mobileNumber", mobileNumber);
            intent.putExtra("province", province);
            intent.putExtra("cityTown", cityTown);
            intent.putExtra("barangay", barangay);

            startActivity(intent);
            Toast.makeText(this, "Registration completed successfully!", Toast.LENGTH_SHORT).show();

            // Clear the activity stack to prevent going back
            finishAffinity();

        } catch (Exception e) {
            e.printStackTrace();
            // If SuccessActivity doesn't exist, just show success message
            Toast.makeText(this, "Registration completed successfully! " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}