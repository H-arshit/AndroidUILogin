package com.example.harshit.apiuse;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


public class TaskToConvertImage extends AsyncTaskLoader<String> {

    private Bitmap imagebitmap;
    public TaskToConvertImage(Context context , Bitmap imagebitmap) {
        super(context);
        this.imagebitmap = imagebitmap;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }
        catch (Exception e){
            return "";
        }

    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}