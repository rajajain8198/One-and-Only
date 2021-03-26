package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rajajainofficalproject.Class.Constant;
import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsDao;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int RC_SIGN_IN = 9001;
    String device_token;
    Button btnLogIn;
    EditText etUserID;
    EditText etPassword;
    CheckBox cbCheckBox;
    TextView tvSignUp, tvForgetPassword;
    VideoView videoview;
    ImageView FacebookLogin;
    CircleImageView GoogleLogin;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sh;
    UserDetails user_details;

    UserDetailsRoomDatabase userDetailsRoomDatabase;
    private GoogleSignInOptions googleSignInOptions;


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Constant.Firebase_Database_Reference_Path);
        userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(this);
        sharedPreferences = getSharedPreferences(Constant.Shared_Preferences, this.MODE_PRIVATE);
        sh = sharedPreferences.edit();

        btnLogIn = findViewById(R.id.btn_submit);
        etUserID = findViewById(R.id.et_user_id);
        etPassword = findViewById(R.id.et_password);
        cbCheckBox = findViewById(R.id.cb_check_box);
        tvSignUp = findViewById(R.id.tv_click_here);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        FacebookLogin = findViewById(R.id.civ_facebook);
        GoogleLogin = findViewById(R.id.civ_google);
        progressBar = findViewById(R.id.pb_progress_bar);


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Task Failed");
                    return;
                } else {
                    device_token = String.valueOf(task.getResult().getToken());
                    if (device_token != null) {
                        //  prefsUser.setDevice_token(device_token);
                        Log.d(TAG, "Device Token is : " + device_token);
                    }
                }

            }
        });

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserID.getText().toString().matches("")) {
                    etUserID.setError("User ID not be null");
                } else if (etPassword.getText().toString().matches("")) {
                    etPassword.setError("Password not be null");
                }
                if (cbCheckBox.isChecked() == false) {
                    Toast.makeText(LoginActivity.this, "Read Terms and Condition", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    String userEmail = etUserID.getText().toString();
                    String userPassword = etPassword.getText().toString();
                    userLogin(userEmail, userPassword);
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextClass(SignUpActivity.class);

            }
        });

        FacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "FaceBook", Toast.LENGTH_SHORT).show();
            }
        });

        GoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                Toast.makeText(LoginActivity.this, "Google", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextClass(ForgetPasswordActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void openNextClass(Class className) {
        Intent intent = new Intent(LoginActivity.this, className);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            progressBar.setVisibility(View.VISIBLE);


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //  firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                ;
                Toast.makeText(this, "  Not Equal \n  " + e + " \n " + data, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Log.w("raja", "Google sign in failed", e);

                updateUI(null);

            }


        }
    }

    private void s(GoogleSignInAccount account) {

        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        } else {
                            Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        progressBar.setVisibility(View.GONE);


                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

//        if (user != null) {
//            etEmail.setText(user.getEmail());
//            etPassword.setText(user.getUid());
///*
//            findViewById(R.id.signInButton).setVisibility(View.GONE);
//            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);*/
//        } else {
//            etEmail.setText(null);
//            etPassword.setText(null);
//
////            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
////            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
    }


    public void userLogin(String userID, String password) {

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(LoginActivity.this, "User Email Found ", Toast.LENGTH_SHORT).show();
                    String uniqueID = snapshot.child("unique_ID").getValue(String.class);
                    String Name = snapshot.child("name").getValue(String.class);
                    String Email = snapshot.child("email").getValue(String.class);
                    String Image = snapshot.child("image").getValue(String.class);
                    String Mobile_Number = snapshot.child("number").getValue(String.class);
                    String userID = snapshot.child("userID").getValue(String.class);
                    String Pass = snapshot.child("password").getValue(String.class);
                    if (etPassword.getText().toString().trim().equals(Pass)) {
                       firebaseUser = mAuth.getCurrentUser();
                        if (!uniqueID.isEmpty()) {
                            sh.putString(Constant.Shared_Preferences_User_Unique_ID, uniqueID);
                            sh.putString("ImageURL",snapshot.child("image").getValue(String.class));
                            sh.apply();
                            user_details = new UserDetails(uniqueID, userID, Name, Image, Email, Mobile_Number, Pass);
                            userDetailsRoomDatabase.productDao().insertDetails(user_details);
                            Toast.makeText(LoginActivity.this, "Unique ID Found : " + uniqueID, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "No Unique ID", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(LoginActivity.this, "Authentication Successful.",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        etUserID.getText().clear();
                        etPassword.getText().clear();
                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User Email and Password not Match", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "UserID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "User and Password not Match", Toast.LENGTH_SHORT).show();

            }
        });
    }
}