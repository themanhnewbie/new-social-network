package com.cost.free.Something.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class PickImageUtil {

    private Uri imageUri;

    public String getImageTitle(String imageId) {
        return imageId + new Date().getTime();
    }

    public Uri getPickedImageUri() {
        openGallery();
        return imageUri;
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imageUri = intent.getData();
    }

    public void loadImage(String imagePath, ImageView view) throws IOException {
        URL url = new URL(imagePath);
        Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        view.setImageBitmap(bm);
    }

    public void loadImage(Uri uri, ImageView view) {
        view.setImageURI(uri);
    }

}
