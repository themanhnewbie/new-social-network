package com.cost.free.Activities;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cost.free.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostToGroupActivity extends PostActivity {
    @Override
    protected void uploadData(String title, String description, String uri) {
        progressDialog.setMessage("Post....");
        progressDialog.show();

        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Group/" + "post_" + timeStamp;
        if (!uri.equals("noImage")) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                Post post = new Post(timeStamp, title, description, downloadUri, uid, "0", "0");
                                HashMap<String, Object> hashMap = post.toMap();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference().child("Groups").child(groupId);
                                ref.child("post").child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                makeToast("Post successful!");
                                                onBackPressed();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        makeToast("" + e.getMessage());

                                    }
                                });
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
        else {
            Post post = new Post(timeStamp, title, description, "noImage", uid, "0", "0");
            HashMap<String, Object> hashMap = post.toMap();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupId);
            ref.child("post").child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            makeToast("Post successful!");
                            startMainActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    makeToast("" + e.getMessage());
                }
            });

        }
    }
}
