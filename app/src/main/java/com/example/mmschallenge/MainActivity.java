package com.example.mmschallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int IMAGE_PICK = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermission();
    }


    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, getString(R.string.failure_permission));

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                enablePicButton();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enablePicButton();
                } else {

                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(MainActivity.this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();

                    disablePicButton();
                }
            }
        }
    }


    private void disablePicButton() {
        Toast.makeText(this, R.string.button_disabled, Toast.LENGTH_LONG).show();
        (findViewById(R.id.button_photo)).setVisibility(View.INVISIBLE);
    }

    private void enablePicButton() {
        (findViewById(R.id.button_photo)).setVisibility(View.VISIBLE);
    }

    public void choosePic(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_PICK);
    }

    @Override
    protected void onActivityResult
    (int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == IMAGE_PICK) {
            if (resultCode == RESULT_OK) {

                Uri mSelectedImage = imageReturnedIntent.getData();
                Log.d(TAG, "onActivityResult: " + mSelectedImage.toString());
                Intent smsIntent = new Intent(Intent.ACTION_SEND);
                smsIntent.putExtra(Intent.EXTRA_STREAM, mSelectedImage);
                smsIntent.setType("image/*");
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Log.d(TAG, "Can't resolve app for ACTION_SEND Intent.");
                }
            }
        }
    }
}
