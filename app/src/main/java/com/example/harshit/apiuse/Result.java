package com.example.harshit.apiuse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Result extends AppCompatActivity implements View.OnClickListener {

    Integer REQUEST_CAMERA=1,REQUEST_FILE=0;    Button image_user_profile , upload_image;
    ImageView image_user_profile_view ;
    Bitmap upcomingimage ;
    String upcomingimagecode="";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12001;

    Button  festival_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        image_user_profile_view = (ImageView)findViewById(R.id.image_user_profile_view);

        image_user_profile = (Button)findViewById(R.id.image_user_profile);

        upload_image = (Button)findViewById(R.id.upload_image);

        festival_list=(Button)findViewById(R.id.festival_list);

        upload_image.setOnClickListener(this);

        image_user_profile.setOnClickListener(this);

        festival_list.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {



        if (view.getId() == R.id.festival_list)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);



            Call<ArrayList<FestivalModel>> result = api.getallfestivals();

            result.enqueue(new Callback<ArrayList<FestivalModel>>() {
                @Override
                public void onResponse(Call<ArrayList<FestivalModel>> call, Response<ArrayList<FestivalModel>> response) {
                    if (response.body() == null)
                    {
                        return;
                    }
                    else
                    {
                        ArrayList<FestivalModel> festival_list = response.body();


                        Intent intent = new Intent(getBaseContext() , Festivals.class);
                        intent.putExtra("festival_list", festival_list);
                        startActivity(intent);



                    }
                }

                @Override
                public void onFailure(Call<ArrayList<FestivalModel>> call, Throwable t) {

                }
            });


            return;
        }

        if(view.getId() == R.id.image_user_profile)
        {
        REQUEST_CAMERA=1;
        REQUEST_FILE=0;
        takeimagetask();}
        else{
            if (!upcomingimagecode.equals( ""))
            {


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);


                ImageRequest imageRequest = new ImageRequest();

                final String PREF_NAME = "com.data.wfi.userdetails";

                SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


                imageRequest.auth_token = sharedPreferences.getString("AUTH_TOKEN","");
                imageRequest.email = sharedPreferences.getString("EMAIL_ID","") ;
                imageRequest.image = upcomingimagecode;


                Call<ResultData> result = api.uploadimage(imageRequest);

                result.enqueue(new Callback<ResultData>() {
                    @Override
                    public void onResponse(Call<ResultData> call, Response<ResultData> response) {

                        ResultData resultData = response.body();

                        if (resultData.isvalid()) {




                            Toast.makeText(getApplicationContext(), "Upload Done", Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onFailure(Call<ResultData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }
    }

    public void takeimagetask()
    {
        final String[] menuforalert = {"Camera", "From Device"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setCancelable(true);
        builder.setItems(menuforalert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {


                    Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraintent.resolveActivity(getApplicationContext().getPackageManager()) != null)
                        startActivityForResult(cameraintent, REQUEST_CAMERA);


                } else if (which == 1) {


                    if(hasPermissions()) {

                        getfilefromexternalstorage();

                    }else{
                        requestPerms();
                    }

                }
            }

        });

        builder.show();

    }

    private void requestPerms(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private boolean hasPermissions(){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }
    public void getfilefromexternalstorage(){
        Intent fileintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileintent.setType("image/*");
        startActivityForResult(Intent.createChooser(fileintent, "Select File"), REQUEST_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
            getfilefromexternalstorage();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(getApplicationContext(), "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                Uri selectedimageuri = data.getData();
                Bitmap image;
                try {
                    image = decodeUri(getApplicationContext(), selectedimageuri, 100);
                } catch (FileNotFoundException e) {

                    Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                    if (REQUEST_FILE == 0) {

                    image_user_profile_view.setImageBitmap(image);


                    // converting the images to base64 string for easy transfer
                    if (image_user_profile_view.getDrawable() != null) {
                        upcomingimage = image;


                        LoaderManager.LoaderCallbacks<String> loaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
                            @Override
                            public Loader<String> onCreateLoader(int id, Bundle args) {
                                return new TaskToConvertImage(getApplicationContext().getApplicationContext(), upcomingimage);
                            }

                            @Override
                            public void onLoadFinished(Loader<String> loader, String data) {

                                upcomingimagecode = data;
                                getSupportLoaderManager().destroyLoader(R.id.loader_for_image_convert_upcom);
                            }

                            @Override
                            public void onLoaderReset(Loader<String> loader) {

                            }
                        };

                        getSupportLoaderManager().initLoader(R.id.loader_for_image_convert_upcom, null, loaderCallbacks);


                    }

                }

            }   else if (requestCode==REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap image = (Bitmap) bundle.get("data");


             if (REQUEST_CAMERA == 1) {
                    image_user_profile_view.setImageBitmap(ImageScalig.BITMAP_RESIZER(image, 150, 150));


                    // converting the images to base64 string for easy transfer
                    if (image_user_profile_view.getDrawable() != null) {
                        upcomingimage = image;


                        LoaderManager.LoaderCallbacks<String> loaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
                            @Override
                            public Loader<String> onCreateLoader(int id, Bundle args) {
                                return new TaskToConvertImage(getApplicationContext().getApplicationContext(), upcomingimage);
                            }

                            @Override
                            public void onLoadFinished(Loader<String> loader, String data) {

                                upcomingimagecode = data;
                                getSupportLoaderManager().destroyLoader(R.id.loader_for_image_convert_upcom);
                            }

                            @Override
                            public void onLoaderReset(Loader<String> loader) {

                            }
                        };

                        getSupportLoaderManager().initLoader(R.id.loader_for_image_convert_upcom, null, loaderCallbacks);


                    }
                }

            }
            }

        }


    }


