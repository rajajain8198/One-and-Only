package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Database.UserDetailsRoomDatabase;
import com.example.rajajainofficalproject.Fragment.HomeFragment;
import com.example.rajajainofficalproject.Fragment.DownloadedFragment;
import com.example.rajajainofficalproject.Fragment.RecentlyOpenFragment;
import com.example.rajajainofficalproject.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;
    TextView tvHome, tvRecentOpen, tvDownload;
    ImageView imgMenu;
    DrawerLayout mDrawerLayout;
    View view;
    Checkout checkout;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        progressBar = findViewById(R.id.progress_bar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tvHome = findViewById(R.id.tv_Home);
        tvRecentOpen = findViewById(R.id.tv_recent_open);
        tvDownload = findViewById(R.id.tv_download);
        imgMenu = findViewById(R.id.menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(DashBoardActivity.this);

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  loadFragment(new HomeFragment(DashBoardActivity.this));
            }
        });
        tvRecentOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new RecentlyOpenFragment(DashBoardActivity.this));
            }
        });
        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new DownloadedFragment(DashBoardActivity.this));
            }
        });
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DashBoardActivity.this, UserDetailsActivity.class);
//                startActivity(intent);
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        if (mDrawerLayout.isDrawerOpen()){
////
////        }Das
//
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
//
//    }

    void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        progressBar.setVisibility(View.GONE);

    }


    @Override
    protected void onStart() {
        super.onStart();
        loadFragment(new HomeFragment(DashBoardActivity.this));
    }

    @Override
    public void
    onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_profile:
                openNextClass(UserDetailsActivity.class);
                break;
            case R.id.nav_payment:
                testOnlinePayment();
                break;
            case R.id.nav_map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.nav_search:
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);
                break;
            case R.id.nav_setting:
                openNextClass(SettingActivity.class);
                break;
            case R.id.nav_sign_out:
                userSignOut();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openNextClass(Class className) {
        Intent intent = new Intent(DashBoardActivity.this, className);
        startActivity(intent);
    }

    public void userSignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user_details", getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor sh = sharedPreferences.edit();

                        UserDetailsRoomDatabase userDetailsRoomDatabase = UserDetailsRoomDatabase.getDatabase(getApplicationContext());
                        userDetailsRoomDatabase.productDao().deleteDetails(sharedPreferences.getString("user_unique_id",""));
                        sh.putString("user_unique_id", "");
                        sh.apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }

    public void testOnlinePayment() {

        checkout = new Checkout();
        checkout.setKeyID("<rzp_test_fKQEgqJu9l8wPm>");
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("name", "Razorpay Corp");
            orderRequest.put("description", "Demoing Charges");
            orderRequest.put("amount", 1); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("prefill.email", "rajajain8198@gmail.com");
            orderRequest.put("prefill.contact", "8889338396");
            orderRequest.put("payment_capture", false);
            checkout.open( this, orderRequest);

        } catch (Exception e) {
            // Handle Exception
            Toast.makeText(this, " try error : " + e, Toast.LENGTH_SHORT).show();
        }
    }
}