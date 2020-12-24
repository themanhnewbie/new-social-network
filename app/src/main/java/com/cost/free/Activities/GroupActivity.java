package com.cost.free.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GroupActivity extends CreateGroupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        fabPost.setVisibility(View.VISIBLE);
        databaseReference.child("Groups").child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adminId = (String) snapshot.child("adminId").getValue();
                if (adminId != currentUser.getUid()) fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(GroupActivity.this, PostToGroupActivity.class);
                postIntent.putExtra("groupId", groupId);
                startActivity(postIntent);
            }
        });
    }
}
