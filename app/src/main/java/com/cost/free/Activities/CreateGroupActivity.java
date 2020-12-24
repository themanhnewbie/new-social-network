package com.cost.free.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.cost.free.Model.Group;
import com.cost.free.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CreateGroupActivity extends BaseActivity {

    Uri pickedImageUri;

    ImageView ivGroupImage, ivGroupCover;
    TextView tvGroupName, tvGroupAdminEmail;
    FloatingActionButton fab, fabPost;
    Button btn;
    ProgressDialog progressDialog;

    String imageOrCover;

    String groupImagePath, groupCoverPath;
    Uri imageUri, coverUri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        groupImagePath = "noImage";
        groupCoverPath = "noImage";
        initView();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setActionBarTitle("Create new group");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

    }

    protected void uploadData() {
        String groupName = tvGroupName.getText().toString();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        StorageReference groupPhotoRef = storageReference.child("Group").child(timeStamp);
        if (imageUri != null) {
//            groupPhotoRef.child("image").putFile(imageUri).ad
        }
        if (coverUri != null) {
            groupPhotoRef.child("cover").putFile(coverUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    groupCoverPath = uriTask.getResult().toString();
                }
            });
        }
        Log.d("AAAA1", groupImagePath);
        Group group = new Group(timeStamp, groupName, groupImagePath, groupCoverPath, currentUser.getUid());
        HashMap<String, Object> hashMap = group.toMap();
        databaseReference.child("Groups").child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                makeToast("Create group successfully!");
                onBackPressed();
            }
        });

    }

    @Override
    public void initView() {
        ivGroupImage = findViewById(R.id.group_image);
        ivGroupCover = findViewById(R.id.group_cover);
        tvGroupName = findViewById(R.id.group_name);
        tvGroupAdminEmail = findViewById(R.id.group_admin_id);
        fab = findViewById(R.id.group_fab);
        fabPost = findViewById(R.id.group_fab_post);
        fabPost.setVisibility(View.INVISIBLE);
        btn = findViewById(R.id.group_button_confirm);
        tvGroupAdminEmail.setText(currentUser.getEmail());
        progressDialog = new ProgressDialog(this);
    }

    protected void showEditProfileDialog() {
        String[] options = {"Edit Group Picture", "Edit Group Cover Photo", "Edit Group Name"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Action");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    progressDialog.setMessage("Update Group picture");
                    imageOrCover = "image";
                    showImagePicDialog();
                }
                else if (which == 1) {
                    //edit cover
                    progressDialog.setMessage("Update Group Cover Photo");
                    imageOrCover = "cover";
                    showImagePicDialog();
                }
                else if (which == 2) {
                    //edit name
                    progressDialog.setMessage("Update Group Name ");
                    showNameUpdateDialog();
                }
            }
        });
        builder.create().show();
    }

    protected void showNameUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Name");
        EditText editText = new EditText(this);
        editText.setHint("Enter Name: ");
        builder.setView(editText);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                if (!value.isEmpty()) {
                    progressDialog.show();
                    tvGroupName.setText(value);
                    progressDialog.dismiss();
                }
                else {
                    makeToast("Enter name");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });

        builder.create().show();
    }

    protected void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pick Image From");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openCamera();
                }
                else if (which == 1) {
                    openGallery();
                }
            }
        });
        builder.create().show();
    }
    protected void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        pickedImageUri = CreateGroupActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pickedImageUri);
        startActivityForResult(cameraIntent, 103);
    }

    protected void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 101 || requestCode == 103) && resultCode == RESULT_OK && data != null) {

            if (imageOrCover.equals("image")) {
                pickedImageUri = data.getData();
                ivGroupImage.setImageURI(pickedImageUri);
                imageUri = pickedImageUri;
            }
            if (imageOrCover.equals("cover")){
                ivGroupCover.setImageURI(pickedImageUri);
                coverUri = pickedImageUri;
            }

        }
    }
}