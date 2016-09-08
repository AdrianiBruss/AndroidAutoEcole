package com.mpdam.ronald.autoecole.utils;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.mpdam.ronald.autoecole.activities.main.MainActivity;

/**
 * Created by Ronald on 28/08/2016.
 */
public class GoogleAPI {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    public GoogleApiClient getGoogleApiClient (Context ctx, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener listener)
    {
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(listener)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        return googleApiClient;
    }

    public LocationRequest getLocationRequest (Integer interval, Integer fatestInterval, Integer priority)
    {
        locationRequest = new LocationRequest()
                .setInterval(interval)
                .setFastestInterval(fatestInterval)
                .setPriority(priority);

        return locationRequest;
    }

    public boolean checkGPS(Activity activity, LocationManager locMng, GoogleApiClient api, LocationRequest request, GoogleMap map)
    {
        if (locMng.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return new GPSLocation().startLocationUpdates(activity, api, request, map);
        }
        else
        {
            new GPSLocation().showGPSDisabledAlertToUser(activity, MainActivity.class);
            return false;
        }
    }

    public void onConnectionSuspended(ConnectionResult connectionResult)
    {
        Log.e("on connection suspended", connectionResult.toString());
    }

    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.e("on connection failed", connectionResult.toString());
    }

}
