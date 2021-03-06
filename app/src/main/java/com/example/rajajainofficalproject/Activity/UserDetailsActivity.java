package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sh;
    List<UserDetails> userDetailsList;

    String Name = null, Email= null, Password= null, Mobile_Number= null, Unique_ID= null, userID= null, Image= null;
    UserDetailsRoomDatabase userDetailsRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_Details");
        sharedPreferences = getSharedPreferences("user_details", this.MODE_PRIVATE);
        userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(this);
        userDetailsList = new ArrayList();
        Unique_ID = sharedPreferences.getString("user_unique_id","");
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

    private void readDetails(String user_unique_id) {

        if (user_unique_id.equals("")){
            Name = "Name";
            Email = "Email";
            Password = "Password";
            Mobile_Number = "Mobile Number";
        }else{
            userDetailsList = userDetailsRoomDatabase.productDao().getAllDetails(user_unique_id);
            userID = userDetailsList.get(0).getUserID();
            Image =userDetailsList.get(0).getImage();
            Name = userDetailsList.get(0).getName();
            Email = userDetailsList.get(0).getEmail();
            Password = userDetailsList.get(0).getPassword();
            Mobile_Number = userDetailsList.get(0).getNumber();
        }
        Toast.makeText(this, " Your unique ID : \n" + sharedPreferences.getString("user_unique_id",""), Toast.LENGTH_SHORT).show();
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
//        else if (etPassword.getText().toString() != etReEnterPassword.getText().toString()) {
//
//            Toast.makeText(UserDetailsActivity.this, "Password are different", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else {
            if (Email.equals(etEmail.getText().toString().trim())) {

            }else {
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

            tvName.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            tvContactNumber.setVisibility(View.GONE);
            tvPassword.setVisibility(View.GONE);
            btnSubmitButton.setText("Submit");
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

        databaseReference.child(userID).child("name").setValue(Name);
        databaseReference.child(userID).child("email").setValue(Email);
        databaseReference.child(userID).child("image").setValue(Image);
        databaseReference.child(userID).child("number").setValue(Mobile_Number);
        databaseReference.child(userID).child("password").setValue(Password);

    }

    void updateDetails() {

        Name = etName.getText().toString().trim();
        Email = etEmail.getText().toString().trim();
        Mobile_Number = String.valueOf(etMobileNumber.getText().toString().trim());
        Password = etPassword.getText().toString().trim();
        userDetailsRoomDatabase.productDao().updateDetails(Unique_ID,Name,Image,Email,Mobile_Number,Password);
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
}
