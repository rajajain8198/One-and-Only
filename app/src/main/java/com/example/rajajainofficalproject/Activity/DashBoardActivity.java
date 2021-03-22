package com.example.rajajainofficalproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajajainofficalproject.Fragment.HomeFragment;
import com.example.rajajainofficalproject.Fragment.DownloadedFragment;
import com.example.rajajainofficalproject.Fragment.RecentlyOpenFragment;
import com.example.rajajainofficalproject.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;
    TextView tvHome, tvRecentOpen, tvDownload;
    ImageView imgMenu;
    DrawerLayout mDrawerLayout;
    View view;
    NavigationView navigationView;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        progressBar = findViewById(R.id.progress_bar);
        navigationView = findViewById(R.id.nav_view ) ;
        tvHome = findViewById(R.id.tv_Home);
        tvRecentOpen = findViewById(R.id.tv_recent_open);
        tvDownload = findViewById(R.id.tv_download);
        imgMenu = findViewById(R.id.menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(DashBoardActivity.this) ;

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment(DashBoardActivity.this));
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
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer , menu) ;
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
    onBackPressed () {
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            super .onBackPressed() ;
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId() ;
        if (id == R.id. action_settings ) {
            return true;
        }
        return super .onOptionsItemSelected(item) ;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId() ;
        if (id == R.id. nav_profile ) {
            openNextClass(UserDetailsActivity.class);
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id. nav_map ) {
            openNextClass(MapsActivity.class);
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id. nav_setting ) {
            openNextClass(SettingActivity.class);
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        } else if (id == R.id. nav_sign_out ) {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        }
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat. START ) ;
        return true;
    }

    public void openNextClass(Class className){
        Intent intent = new Intent(DashBoardActivity.this, className);
        startActivity(intent);
    }

    public void userSignOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}