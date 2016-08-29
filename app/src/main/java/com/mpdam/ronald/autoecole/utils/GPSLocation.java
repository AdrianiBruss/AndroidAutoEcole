package com.mpdam.ronald.autoecole.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Ronald on 28/08/2016.
 */
public class GPSLocation {

    private Location currentLocation;

    public boolean startLocationUpdates(Activity activity, GoogleApiClient api, LocationRequest request, GoogleMap map)
    {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasPermission = ContextCompat.checkSelfPermission(activity, locationPermission);
        String[] permissions = new String[]{locationPermission};

        if (hasPermission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, permissions, 0);
            return false;
        }
        else
        {
            Toast.makeText(activity, "We already have persmission", Toast.LENGTH_SHORT).show();
            map.setMyLocationEnabled(true);
            return true;
        }
    }

    public void showGPSDisabledAlertToUser(final Context ctx, final Class activityClass)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ctx.startActivity(
                        new Intent(ctx, activityClass));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void focusOnUser(Location location, GoogleMap map)
    {
        currentLocation = location;

        map.moveCamera(
                CameraUpdateFactory.newLatLngZoom( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ),
                        15)
        );

    }
}
