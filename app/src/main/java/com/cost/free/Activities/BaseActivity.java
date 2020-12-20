package com.cost.free.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cost.free.DashboardActivity;
import com.cost.free.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class BaseActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    protected void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }
    protected void setActionBarTitleAndSubtitle(String title, String subtitle) {
        actionBar.setTitle(title);
        actionBar.setSubtitle(subtitle);
    }

    protected void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void startMainActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    protected void checkUserStatus() {
        if (currentUser==null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if (id == R.id.action_post) {
            startActivity(new Intent(getApplicationContext(), PostActivity.class));
            startActivity(new Intent(this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void initView();
}
