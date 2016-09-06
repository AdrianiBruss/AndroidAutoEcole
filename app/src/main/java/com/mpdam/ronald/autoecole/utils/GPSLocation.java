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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.mpdam.ronald.autoecole.activities.home.StudentActivity;


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
            //display current location on map
            map.setMyLocationEnabled(true);
            return true;
        }
    }

    public void showGPSDisabledAlertToUser(final Context ctx, final Class activityClass)
    {
        // check if GPS is activated, if not, start mobile settings activity
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setTitle("Activer votre GPS");
        builder.setMessage("Activer votre GPS s'il vous plaît");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Activer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignorer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ctx.startActivity(
                        new Intent(ctx, StudentActivity.class));
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
