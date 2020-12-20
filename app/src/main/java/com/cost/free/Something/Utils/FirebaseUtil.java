package com.cost.free.Something.Utils;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil implements IFirebaseUtil{
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();

    public static void updateUserName(String name) {
        UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        user.updateProfile(upcr);
    }

    public static void updateUserPhoto(Uri uri) {
        UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(upcr);
    }

    public static void updateUserInfo(String name, Uri uri) {
        updateUserName(name);
        updateUserPhoto(uri);
    }

    public static boolean isUserSignedIn() {
        return user != null;
    }

    public static String getCurrentUserPhoto() {
        return user.getPhotoUrl().toString();
    }

    public static String getCurrentUserName() {
        return user.getDisplayName();
    }

    @Override
    public String getUserName(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(uid).child("name");
        return null;
    }

    @Override
    public String getUserPhoto() {
        return null;
    }
}
