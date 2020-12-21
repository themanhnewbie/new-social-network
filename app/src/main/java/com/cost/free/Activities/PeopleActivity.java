package com.cost.free.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cost.free.Adapter.PostAdapter;
import com.cost.free.Model.Post;
import com.cost.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeopleActivity extends AppCompatActivity {

    ImageView avtProf, coverPrf;
    TextView nameProf, emailProf, phonePrf;
    FloatingActionButton fab;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    String uid;

    RecyclerView recyclerView;
    List<Post> postList;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        initView();
        fab.setVisibility(View.GONE);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        mRef.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String email = "" + snapshot.child("email").getValue();
                String phone = "" + snapshot.child("phone").getValue();
                String image = "" + snapshot.child("image").getValue();
                String cover = "" + snapshot.child("cover").getValue();

                //set data
                nameProf.setText(name);
                emailProf.setText(email);
                phonePrf.setText(phone);
                try {
                    Picasso.get().load(image).into(avtProf);
                }
                catch (Exception e) {
                    Picasso.get().load(R.drawable.ic_default).into(avtProf);
                }

                try {
                    Picasso.get().load(cover).into(coverPrf);
                }
                catch (Exception e) {
                    Picasso.get().load(R.drawable.ic_default).into(coverPrf);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.profile_post_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        loadPost(uid);
    }

    private void loadPost(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    if (post.getUid().equals(uid)) {
                        postList.add(post);
                    }

                    postAdapter = new PostAdapter(PeopleActivity.this, postList);

                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadUserInfo() {

    }


    public void initView() {
        avtProf = findViewById(R.id.avatar_prof);
        emailProf = findViewById(R.id.email_prof);
        nameProf = findViewById(R.id.name_prof);
        phonePrf = findViewById(R.id.phone_prof);
        coverPrf = findViewById(R.id.cover_pic);
        fab = findViewById(R.id.fab);
    }
}
