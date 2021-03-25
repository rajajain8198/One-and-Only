package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rajajainofficalproject.Class.Constant;
import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class UserDetailsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imgUserPitcher, imgUpdateUserPitcher;
    EditText etName, etEmail, etMobileNumber, etPassword, etReEnterPassword;
    TextView tvName, tvEmail, tvContactNumber, tvPassword, tvEmailVerified, tvNumberVerified;
    Button btnSubmitButton;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    StorageReference storageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sh;
    List<UserDetails> userDetailsList;

    Uri selectedImageUri = null;
    String Name = null, Email = null, Password = null, Mobile_Number = null, Unique_ID = null, userID = null, Image = null;
    UserDetailsRoomDatabase userDetailsRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initViews();

        sharedPreferences = getSharedPreferences(Constant.Shared_Preferences, this.MODE_PRIVATE);
        sh = sharedPreferences.edit();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constant.Firebase_Database_Reference_Path);
        storageRef = FirebaseStorage.getInstance().getReference();
        userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(this);
        userDetailsList = new ArrayList();
        Unique_ID = sharedPreferences.getString(Constant.Shared_Preferences_User_Unique_ID, "");
        Image = sharedPreferences.getString("ImageURL", "");
        readDetails(Unique_ID);

        btnSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSubmitButton.getText() == "Update Record") {
                    progressBar.setVisibility(View.VISIBLE);
                    readDetails(Unique_ID);
                    setVisibility();
                } else {
                    if (validation()) {
                        progressBar.setVisibility(View.VISIBLE);
                        updateDetails();
                        updateUserDetailsFirebase(userID);
                        readDetails(Unique_ID);
                        setVisibility();
                    } else {
                        Toast.makeText(UserDetailsActivity.this, "Please Fill Details", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        imgUpdateUserPitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                startActivityForResult(intent, 2);

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);

            }
        });

        setVisibility();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Image = sharedPreferences.getString("ImageURL", "");
        if (selectedImageUri != null) {
            Glide.with(this).load(selectedImageUri).into(imgUserPitcher);
        } else {
            Glide.with(this).load(Image).into(imgUserPitcher);
        }
    }

    private void readDetails(String user_unique_id) {

        if (user_unique_id.equals("")) {
            Name = "Name";
            Email = "Email";
            Password = "Password";
            Mobile_Number = "Mobile Number";
        } else {
            userDetailsList = userDetailsRoomDatabase.productDao().getAllDetails(user_unique_id);
            userID = userDetailsList.get(0).getUserID();
            Image = userDetailsList.get(0).getImage();
            Name = userDetailsList.get(0).getName();
            Email = userDetailsList.get(0).getEmail();
            Password = userDetailsList.get(0).getPassword();
            Mobile_Number = userDetailsList.get(0).getNumber();
        }
        Log.d("TAG", " Image From Firebase : " + Image);
        Toast.makeText(this, " Your unique ID : \n" + sharedPreferences.getString(Constant.Shared_Preferences_User_Unique_ID, ""), Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_bar);
        tvName = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_user_email);
        tvContactNumber = findViewById(R.id.tv_user_number);
        tvPassword = findViewById(R.id.tv_user_passwrod);
        tvEmailVerified = findViewById(R.id.tv_verified);
        tvNumberVerified = findViewById(R.id.tv_verified_number);
        etName = findViewById(R.id.et_user_new_name);
        etEmail = findViewById(R.id.et_user_new_email);
        etMobileNumber = findViewById(R.id.et_user_new_number);
        etPassword = findViewById(R.id.et_user_new_password);
        etReEnterPassword = findViewById(R.id.et_user_new_reenter_password);
        btnSubmitButton = findViewById(R.id.btn_submit);
        imgUpdateUserPitcher = findViewById(R.id.img_update_user_pitcher);
        imgUserPitcher = findViewById(R.id.civ_user_picture);
    }

    Boolean validation() {
        if (etName.getText().toString().isEmpty() || etMobileNumber.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() ||
                etPassword.getText().toString().isEmpty() || etReEnterPassword.getText().toString().isEmpty()) {
            Toast.makeText(UserDetailsActivity.this, "Enter Details Properly", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if (!etPassword.getText().toString().equals(etReEnterPassword.getText())) {
//
//            Toast.makeText(UserDetailsActivity.this, "Password are different", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else {
            if (Email.equals(etEmail.getText().toString().trim())) {

            } else {
                //updateEmail();
            }
            return true;
        }

    }

    void setVisibility() {
        if (btnSubmitButton.getText() == "Update Record") {
            etName.setVisibility(View.VISIBLE);
            etMobileNumber.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            etEmail.setVisibility(View.VISIBLE);
            etReEnterPassword.setVisibility(View.VISIBLE);
            imgUpdateUserPitcher.setVisibility(View.VISIBLE);

            tvName.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            tvContactNumber.setVisibility(View.GONE);
            tvPassword.setVisibility(View.GONE);
            btnSubmitButton.setText("SUBMIT");
            progressBar.setVisibility(View.GONE);

            tvNumberVerified.setVisibility(View.GONE);
            tvEmailVerified.setVisibility(View.GONE);

            etName.setText(Name);
            etEmail.setText(Email);
            etMobileNumber.setText(Mobile_Number);
            etPassword.setText(Password);
            etReEnterPassword.setText(Password);

        } else {
            etName.setVisibility(View.GONE);
            etMobileNumber.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            etReEnterPassword.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
            imgUpdateUserPitcher.setVisibility(View.GONE);

            tvName.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvContactNumber.setVisibility(View.VISIBLE);
            tvPassword.setVisibility(View.VISIBLE);
            btnSubmitButton.setText("Update Record");
            progressBar.setVisibility(View.GONE);

            tvNumberVerified.setVisibility(View.GONE);
            tvEmailVerified.setVisibility(View.GONE);

            tvName.setText(Name);
            tvEmail.setText(Email);
            tvContactNumber.setText(Mobile_Number);
            tvPassword.setText(Password);
        }

    }


    public void updateUserDetailsFirebase(String userID) {
        progressBar.setVisibility(View.VISIBLE);
        UploadImageFileToFirebaseStorage(selectedImageUri);
    }

    void updateDetails() {
        Name = etName.getText().toString().trim();
        Email = etEmail.getText().toString().trim();
        Mobile_Number = String.valueOf(etMobileNumber.getText().toString().trim());
        Password = etPassword.getText().toString().trim();
        userDetailsRoomDatabase.productDao().updateDetails(Unique_ID, Name, Image, Email, Mobile_Number, Password);
    }

    public void verifyEmail() {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        if (task.isSuccessful()) {
                            Toast.makeText(UserDetailsActivity.this,
                                    "Verification email sent to " + firebaseUser.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("TAG", "sendEmailVerification", task.getException());
                            Toast.makeText(UserDetailsActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateEmail() {
        AuthCredential credential = EmailAuthProvider
                .getCredential(etEmail.getText().toString().trim(), etPassword.getText().toString().trim()); // Current Login Credentials \\
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "User re-authenticated.");

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail("user@example.com")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User email address updated.");
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();
            imgUserPitcher.setImageURI(selectedImageUri);

        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void UploadImageFileToFirebaseStorage(Uri FilePathUri) {

        if (FilePathUri != null) {
            String Storage_Path = Constant.Firebase_Storage_Path_For_Image;

            final StorageReference storageReference2nd = storageRef.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.d("TAG", " Upload Image URL : " + url);
                                    Image = url;

                                    sh.putString("ImageURL", url);
                                    sh.apply();
                                    databaseReference.child(userID).child("name").setValue(Name);
                                    databaseReference.child(userID).child("email").setValue(Email);
                                    databaseReference.child(userID).child("image").setValue(Image);
                                    databaseReference.child(userID).child("number").setValue(Mobile_Number);
                                    databaseReference.child(userID).child("password").setValue(Password);

                                }
                            });

                            String TempImageName = Name + " Profile Pic ";
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(UserDetailsActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "Exception : " + exception + ", Exception Message : " + exception.getLocalizedMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

        } else {

            Toast.makeText(UserDetailsActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }
        selectedImageUri = null;
        progressBar.setVisibility(View.GONE);
    }
}