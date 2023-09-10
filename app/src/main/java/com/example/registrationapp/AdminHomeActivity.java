package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.registrationapp.fragments.HomeFragment;
import com.example.registrationapp.fragments.LogoutFragment;
import com.example.registrationapp.fragments.ProfileFragment;
import com.example.registrationapp.fragments.ReportFragment;
import com.google.android.material.navigation.NavigationView;


public class AdminHomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    final int menuProfile =1, menuReport=2, menuHome=3, menuLogout=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        // navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar = (Toolbar) findViewById(R.id.toolBar);


        // toggle.getDrawerArrowDrawable().setColor(getResources().getColor());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return false;
            }
        });


//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//            navigationView.setCheckedItem(R.id.menu_profile);
//        }
//    }


//    @SuppressLint("SuspiciousIndentation")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_profile) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//        } else if (id == R.id.menu_logout) {
//            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
//        } else if (id == R.id.menu_home) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//        } else if (id == R.id.menu_report) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
//        } else {
//
//        }
//
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;

//        switch (item.getItemId()) {
//            case menuProfile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//                break;
//            case menuReport:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
//                break;
//            case menuHome:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                break;
//            case menuLogout:
//                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }


    }
}