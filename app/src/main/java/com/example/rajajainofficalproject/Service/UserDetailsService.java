package com.example.rajajainofficalproject.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rajajainofficalproject.Activity.DashBoardActivity;
import com.example.rajajainofficalproject.Activity.LoginActivity;
import com.example.rajajainofficalproject.Database.UserDetails;
import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDetailsService extends Service {
    IBinder iBinder = new MyBinder();
    String uniqueID ="", Name, Email, Image, Mobile_Number, UserID, Pass;
    int UserDetails = 1 ;
    DatabaseReference myRef;
    FirebaseDatabase database;
    UserDetails user_details;
    FirebaseAuth mAuth;
    UserDetailsRoomDatabase userDetailsRoomDatabase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinder extends Binder{
        public UserDetailsService getService(){
            return UserDetailsService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User_Details");
        userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserDetails = intent.getIntExtra("input_type", 1);
        final String ID = intent.getStringExtra("userID");
        final String password = intent.getStringExtra("password");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if ( UserDetails == 1){
                    if (!ID.equals("") || !password.equals("")){
                        getUserAllData(ID, password);
                    }
                }else if(UserDetails == 2){
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getUserAllData(String userID, final String password){

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "User Email Found ", Toast.LENGTH_SHORT).show();
                    uniqueID = snapshot.child("unique_ID").getValue(String.class);
                    setUniqueID(uniqueID);
                    Email = snapshot.child("email").getValue(String.class);
                    setEmail(Email);
                    Name = snapshot.child("name").getValue(String.class);
                    Image = snapshot.child("image").getValue(String.class);
                    Mobile_Number = snapshot.child("number").getValue(String.class);
                    UserID = snapshot.child("userID").getValue(String.class);
                    Pass = snapshot.child("password").getValue(String.class);
                    if (password.equals(Pass)) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (!uniqueID.isEmpty()) {

                            user_details = new UserDetails(uniqueID, UserID, Name, Image, Email, Mobile_Number, Pass);
                            userDetailsRoomDatabase.productDao().insertDetails(user_details);
                            Toast.makeText(getApplicationContext(), "Unique ID Found : " + uniqueID, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "No Unique ID", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getApplicationContext(), "Authentication Successful.",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "User Email and Password not Match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "UserID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "User and Password not Match", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

