package com.example.myfirstapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import android.hardware.camera2;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 101;
    Button captureButton;
    ImageView picTaken;
    private static final int Permission_Code = 1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      declaring captureButton and picTaken
        captureButton = findViewById(R.id.button);
        picTaken = findViewById(R.id.imageView);

//       Adding a clicklistener to the button
        captureButton.setOnClickListener(new View.OnClickListener()

        {

            public void onClick(View v)
            {
                // Code here executes on main thread after user presses button
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED)
                    {
//                        Permission not enabled so we ask for it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        Show pop up to request permission
                        requestPermissions(permission, Permission_Code);

                    }
                    else
                    {
                       // permission granted
                        openCamera();

                    }

                }
                else
                {
                    // System os < marshmallow
                    openCamera();
                }

            }
        });
    }

    public void openCamera()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case Permission_Code:
            {
                if (grantResults.length>0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                }
                else
                {
                    Toast.makeText( this,  "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    // When image is captured from the camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            picTaken.setImageURI(image_uri);

        }
    }

}

//    public void clickPicture(View v)
//    {
//        Toast myToast =  Toast.makeText(getApplicationContext(), "Wait", Toast.LENGTH_LONG);
//        myToast.show();
//    }
//
//    public void TakePictureIntent(View v)
//    {
//        Intent pic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pic.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(pic, REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//
//
//}
