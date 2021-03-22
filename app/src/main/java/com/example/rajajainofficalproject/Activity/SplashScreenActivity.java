package com.example.rajajainofficalproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.rajajainofficalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sh;
    String Unique_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences("user_details", this.MODE_PRIVATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Unique_ID = sharedPreferences.getString("user_unique_id","");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentUser = mAuth.getInstance().getCurrentUser();
                if (Unique_ID.isEmpty()){
                    openNextPage(LoginActivity.class);
                }else {
                    openNextPage(DashBoardActivity.class);
                }
            }
        }, 2000);
    }

    public void openNextPage(Class className){
        Intent intent = new Intent(this, className);
        startActivity(intent);
        finish();
    }
}