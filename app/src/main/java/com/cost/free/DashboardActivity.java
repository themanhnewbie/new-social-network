package com.cost.free;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.cost.free.Activities.BaseActivity;
import com.cost.free.Fragment.FriendFragment;
import com.cost.free.Fragment.GroupFragment;
import com.cost.free.Fragment.HomeFragment;
import com.cost.free.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends BaseActivity {

    FirebaseAuth mAuth;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        navigationView = findViewById(R.id.navigation);

        setActionBarTitle("Home");
        loadFragment(new HomeFragment());

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {

        BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    HomeFragment homeFragment = new HomeFragment();
                    ProfileFragment profileFragment = new ProfileFragment();
                    FriendFragment friendFragment = new FriendFragment();
                    GroupFragment groupFragment = new GroupFragment();
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                setActionBarTitle("Home");
                                loadFragment(homeFragment);
                                return true;
                            case R.id.nav_profile:
                                setActionBarTitle("Profile");
                                loadFragment(profileFragment);
                                return true;
                            case R.id.nav_friend:
                                setActionBarTitle("People");
                                loadFragment(friendFragment);
                                return true;
                            case R.id.nav_group:
                                setActionBarTitle("Group");
                                loadFragment(groupFragment);
                                return true;

                        }
                        return false;
                    }
                };
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        super.onResume();
    }

    @Override
    public void initView() {
        navigationView = findViewById(R.id.navigation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }


}