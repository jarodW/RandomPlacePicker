package com.example.myapplication.Splash;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.Main.MainActivity;

/**
 * Created by jarod on 10/15/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setMessage("Need to get Location to retrieve resturants")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onLocationPermissionClicked();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            } else {
                onLocationPermissionClicked();
            }
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onLocationPermissionClicked() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);


    }
}