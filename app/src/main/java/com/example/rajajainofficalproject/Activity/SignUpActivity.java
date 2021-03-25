package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Class.Constant;
import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.Interface.CallMethodInterface;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Realm mRealm;
    ProgressBar progressBar;
    EditText etUserId, etName, etEmail, etMobileNumber, etPassword, etReEnterPassword;
    TextView tvSignIn;
    String userID;
    Button btnSubmitButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sh;
    Map<String, Object> user;
    ImageView imgPassword, imgReenterPassword;
    UserDetails user_details;
    String Name = null, Email = null, Password = null, Mobile_Number = null, UniqueId = null;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;
    private CallMethodInterface inter;
    UserDetailsRoomDatabase userDetailsRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Constant.Shared_Preferences, this.MODE_PRIVATE);
        sh = sharedPreferences.edit();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(Constant.Firebase_Database_Reference_Path);
        userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(this);

        progressBar = findViewById(R.id.progress_bar);
        etUserId = findViewById(R.id.et_user_ID);
        etName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_user_email);
        etMobileNumber = findViewById(R.id.et_user_number);
        etPassword = findViewById(R.id.et_user_password);
        etReEnterPassword = findViewById(R.id.et_user_reenter_password);
        btnSubmitButton = findViewById(R.id.btn_submit_details);
        imgPassword = findViewById(R.id.img_password);
        imgReenterPassword = findViewById(R.id.img_renter_password);
        tvSignIn = findViewById(R.id.sing_in);


        btnSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Toast.makeText(SignUpActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    userID = etUserId.getText().toString().trim();
                    checkUserID(userID);
                }

            }
        });

        imgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT);

            }
        });

        imgReenterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etReEnterPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextClass(LoginActivity.class);
            }
        });

    }

    public void checkUserID(String userID) {
        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "user ID already exists \n please select another Id", Toast.LENGTH_SHORT).show();
                } else {
                    userSignUp(etEmail.getText().toString().trim(), String.valueOf(etPassword.getText().toString().trim()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openNextClass(LoginActivity.class);
    }

    Boolean validation() {
        if (etUserId.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter Details Properly", Toast.LENGTH_SHORT).show();
            etUserId.setError("User ID not be null");
            return false;
        } else if (etName.getText().toString().isEmpty()) {
            etName.setError("Name not be null");
            return false;
        } else if (etEmail.getText().toString().isEmpty()) {
            return false;
        } else if (etMobileNumber.getText().toString().isEmpty()) {
            etMobileNumber.setError("Mobile Number not be null");
            return false;
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password not be null");
            return false;
        } else if (etReEnterPassword.getText().toString().isEmpty()) {
            etReEnterPassword.setError("ReEnter Password not be null");
            return false;
        } else if (etPassword.getText().toString().trim().length() <= 5 || etReEnterPassword.getText().toString().trim().length() <= 5) {
            Toast.makeText(SignUpActivity.this, "Password must be" + "\n" + "minimum 6 letter and same ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (etPassword.getText().toString().length() < 1) {
            imgPassword.setVisibility(View.GONE);
        } else {
            imgPassword.setVisibility(View.VISIBLE);
        }
        if (etReEnterPassword.getText().toString().length() < 1) {
            imgReenterPassword.setVisibility(View.GONE);
        } else {
            imgPassword.setVisibility(View.VISIBLE);
        }
    }

    private void userSignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            user = new HashMap<>();

                            Name = etName.getText().toString().trim();
                            Email = etEmail.getText().toString().trim();
                            Mobile_Number = String.valueOf(etMobileNumber.getText().toString().trim());
                            Password = etPassword.getText().toString().trim();

                            user.put("name", Name);
                            user.put("email", Email);
                            user.put("password", Mobile_Number);
                            user.put("mobile", Password);

                            firebaseUser = mAuth.getCurrentUser();
                            //sendDate(firebaseUser.getUid());
                            sendDate(firebaseUser.getUid());
                            UniqueId = firebaseUser.getUid();
                            sh.putString(Constant.Shared_Preferences_User_Unique_ID, firebaseUser.getUid());
                            sh.commit();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.w("TAG", "createUserWithEmail:failure" + task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendDate(String userid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Add a new document with a generated ID
        DocumentReference documentReference = firestore
                .collection("NEW")
                .document(userID);

        documentReference.set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sendDataToFirebase(userID, firebaseUser.getUid());
                        Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void sendDataToFirebase(String userID, String uniqueID) {

        //Details saved in firebase

        user_details = new UserDetails(uniqueID, userID, Name, "", Email, Mobile_Number, Password);
        myRef.child(userID).setValue(user_details);
        Toast.makeText(SignUpActivity.this, "Save Firebase Database", Toast.LENGTH_SHORT).show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userDetailsRoomDatabase.productDao().insertDetails(user_details);
                Log.e("database", " : " + user_details.toString());
                openNextClass(DashBoardActivity.class);
                Toast.makeText(SignUpActivity.this, "Save Room Database", Toast.LENGTH_SHORT).show();
                verifyEmail();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("raja", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void openNextClass(Class className) {
        Intent intent = new Intent(SignUpActivity.this, className);
        startActivity(intent);
        finish();
    }

    public void verifyEmail() {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Verification email sent to " + firebaseUser.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}