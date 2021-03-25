package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Class.Constant;
import com.example.rajajainofficalproject.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText etEmail;
    Button btnSubmit;
    TextView tvTextView;
    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    String PhoneNumber,VerificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.Firebase_Database_Reference_Path);
        progressBar = findViewById(R.id.pb_progress);
        etEmail = findViewById(R.id.et_edit_text);
        btnSubmit = findViewById(R.id.btn_submit_email);
        tvTextView = findViewById(R.id.tv_text_normal);
        etEmail.setHint("Email ID");

        VerificationID = getIntent().getStringExtra("verificationID");
        PhoneNumber = getIntent().getStringExtra("phoneNumber");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = etEmail.getText().toString().trim();
                if (OTP != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    UserOTP(PhoneNumber, OTP);
                } else {
                    Toast.makeText(VerifyNumberActivity.this, "OTP not be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void UserOTP(String phoneNumber, String smsCode) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                5,
                TimeUnit.SECONDS,
                this, /* activity */
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...

                        Log.e("TAG", "OnVerificationNumber : " + credential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e("TAG", "OnVerificationNumber : " + e);

                    }

                    // ...
                });

        progressBar.setVisibility(View.GONE);


    }

}