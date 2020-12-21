package com.cost.free.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.cost.free.Model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditPostActivity extends PostActivity{
    String imageStatus;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle("Edit Post");
        Intent intent = getIntent();
        String editPostId = intent.getStringExtra("editPostId");
        loadPostData(editPostId);

        uploadBtn.setText("Update");
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titlePost.getText().toString().trim();
                String description = desPost.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    makeToast("Enter Title");
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    makeToast("Type description");
                    return;
                }
                if (image_uri == null) {
                    updateData(editPostId, title, description, imageStatus);
                }
                else {
                    updateDataWithNewImage(editPostId, title, description, String.valueOf(image_uri));
                }
            }

        });
    }

    private void updateData(String editPostId, String title, String description, String imageStatus) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(editPostId);
        post.setpDescr(description);
        post.setpImage(imageStatus);
        post.setpTitle(title);
        HashMap<String, Object> map = post.toMap();
        databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                makeToast("Update post successfully!");
            }
        });
        startMainActivity();
    }

    private void updateDataWithNewImage(String editPostId, String title, String description, String imagePath) {
        String filePathAndName = "Posts/" + "post_" + editPostId;

        if (!imagePath.equals("noImage")) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.delete();
            ref.putFile(Uri.parse(imagePath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    String downloadUrl = uriTask.getResult().toString();
                    updateData(editPostId, title, description, downloadUrl);

                }
            });
        }

    }

    private void loadPostData(String editPostId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(editPostId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                titlePost.setText(post.getpTitle());
                desPost.setText(post.getpDescr());
                imageStatus = post.getpImage();
                if (!imageStatus.equals("noImage")) {
                    try {
                        Picasso.get().load(imageStatus).into(imagePost);
                    } catch (Exception e) {
                        makeToast("" + e.getMessage());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
