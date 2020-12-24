package com.cost.free.Activities;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cost.free.Model.User;
import com.cost.free.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends BaseActivity {

    EditText email, password, confirm_pass;
    Button create_btn;
    TextView create_mess;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        setupUI(findViewById(R.id.layout_register));
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setActionBarTitle("Register");

        mAuth = FirebaseAuth.getInstance();

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input email, password
                String user_email = email.getText().toString().trim();
                String user_pass = password.getText().toString().trim();
                String user_confirm = confirm_pass.getText().toString().trim();

                //Validate
                if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    email.setError("Email is required");
                    email.setFocusable(true);
                }
                else if (user_pass.length() < 6) {
                    password.setError("Password length at least 6 characters");
                    password.setFocusable(true);
                }
                else if (!user_pass.equals(user_confirm)) {
                    confirm_pass.setError("Password mismatched");
                    confirm_pass.setFocusable(true);
                }
                else {
                    register(user_email, user_pass);
                }
            }
        });

        create_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void initView() {
        email = findViewById(R.id.res_email);
        password = findViewById(R.id.res_password);
        confirm_pass = findViewById(R.id.confirm_pass);
        create_btn = findViewById(R.id.create_btn);
        create_mess = findViewById(R.id.login_mess);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
    }

    private void register(final String email,  String password) {

        //email, pass pattern is valid, start registering user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            currentUser = mAuth.getCurrentUser();

                            String email = currentUser.getEmail();
                            String uid = currentUser.getUid();

                            User mUser = new User("", email, "", "", "", "", uid);
                            HashMap<String, String> hashMap = mUser.toMap();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            makeToast("Registered...\n" + currentUser.getEmail());

                            startMainActivity();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            makeToast("Can't register with this email or password");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                makeToast("" + e.getMessage());

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}