package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText etEmail;
    Button btnSubmit;
    TextView tvTextView;
    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

       // auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User_Details");
        progressBar = findViewById(R.id.pb_progress);
        etEmail = findViewById(R.id.et_edit_text);
        btnSubmit = findViewById(R.id.btn_submit_email);
        tvTextView = findViewById(R.id.tv_text_normal);
        etEmail.setHint("Email ID");
        tvTextView.setText("Please enter your Email for Reset your password");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserID = etEmail.getText().toString().trim();
                if (UserID != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin(UserID);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Email not be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
        finish();
    }

    public void verificationDetails(final String Mobile_Number) {
//        auth.sendPasswordResetEmail(Mobile_Number)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ForgetPasswordActivity.this, "Reset Password Link Send\n To Your Email", Toast.LENGTH_SHORT).show();
//
//                            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(ForgetPasswordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            etEmail.getText().clear();
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + Mobile_Number,
                5,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        Toast.makeText(ForgetPasswordActivity.this, "onVerificationCompleted", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onVerificationCompleted:" + credential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgetPasswordActivity.this, "onVerificationFailed : " + e, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onVerificationFailed : " + e);

                    }


                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Toast.makeText(ForgetPasswordActivity.this, "onVerification Code Sent : ", Toast.LENGTH_SHORT).show();

                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.e("TAG", "onCodeSent:" + verificationId);
                        Intent intent = new Intent(ForgetPasswordActivity.this, VerifyNumberActivity.class);
                        intent.putExtra("verificationID", verificationId);
                        intent.putExtra("phoneNumber", Mobile_Number);
                        startActivity(intent);

//                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, "code");
//
//                        if (credential.getSmsCode().isEmpty()) {
//                            Log.e("TAG", "Credential : " + credential.getSmsCode());
//
//                        }

                    }
                }
        );

    }

//    public void UserOTP(String phoneNumber, String smsCode) {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
//
//        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);
//
//        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
//        phoneAuthProvider.verifyPhoneNumber(
//                phoneNumber,
//                60L,
//                TimeUnit.SECONDS,
//                this, /* activity */
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential credential) {
//                        // Instant verification is applied and a credential is directly returned.
//                        // ...
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                    }
//
//                    // ...
//                });
//
//    }


    public void userLogin(String userID) {

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(ForgetPasswordActivity.this, "User Email Found ", Toast.LENGTH_SHORT).show();
                    String uniqueID = snapshot.child("unique_ID").getValue(String.class);
                    String Name = snapshot.child("name").getValue(String.class);
                    String Email = snapshot.child("email").getValue(String.class);
                    String Mobile_Number = snapshot.child("number").getValue(String.class);
                    String userID = snapshot.child("userID").getValue(String.class);
                    verificationDetails(Mobile_Number);

                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "UserID not found", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgetPasswordActivity.this, "User and Password not Match", Toast.LENGTH_SHORT).show();

            }
        });
    }

}