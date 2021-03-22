package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Interface.CallMethodInterface;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText etNumber;
    Button btnSubmit;
    TextView tvTextView, tvResendOTP;
    FirebaseAuth auth;
    CallMethodInterface callMethodInterface;
    private String mVerificationId;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pb_progress);
        etNumber = findViewById(R.id.et_edit_text);
        btnSubmit = findViewById(R.id.btn_submit_email);
        tvTextView = findViewById(R.id.tv_text_normal);
        tvResendOTP = findViewById(R.id.tv_resend_otp);
        etNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        tvResendOTP.setVisibility(View.VISIBLE);

        etNumber.setHint("Enter OTP Here");
        tvTextView.setText("Please enter your OTP for verification");

        Intent intent = getIntent();
        number  = intent.getStringExtra("phone_number");
        Toast.makeText(OtpVerificationActivity.this, "Your Number : "  + number, Toast.LENGTH_SHORT).show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = String.valueOf(etNumber.getText().toString().trim());
                if (otp.equals("")) {
                    Toast.makeText(OtpVerificationActivity.this, "OTP not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    verifyVerificationCode(otp);                }
            }

        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp(number);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("name", " : " + number);
        getOtp(number);
    }

    public void getOtp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,
                5,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(OtpVerificationActivity.this, "Successfully" + phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpVerificationActivity.this, "Failed : " + e, Toast.LENGTH_SHORT).show();
                        Log.e("raja", " : " +e);

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mVerificationId = s;
                        Toast.makeText(OtpVerificationActivity.this, "Code Sent" + s, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OtpVerificationActivity.this, LoginActivity.class));
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(OtpVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(OtpVerificationActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(OtpVerificationActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(OtpVerificationActivity.this, "OTP Verified Failed", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}