package com.mpdam.ronald.autoecole.activities.googleMap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LessonActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private Location currentLocation;
    private Location lastLocation;
    private List<Location> allLocations = new ArrayList<Location>();
    private LocationRequest locationRequest;
    private LocationManager locationManager;

    private Date startTime;
    private Date endTime;

    private Boolean startLocationUpdates = false;

    private Button buttonStartLocation;
    private Button buttonStopLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        buttonStartLocation = (Button) findViewById(R.id.butttonStartLocation);
        buttonStopLocation = (Button) findViewById(R.id.buttonStopLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

        }else{

            showGPSDisabledAlertToUser();

        }

        if (googleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        if (locationRequest == null) {
            locationRequest = new LocationRequest()
                    .setInterval(500)
                    .setFastestInterval(100)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void showGPSDisabledAlertToUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                startLocationUpdates();
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(
                        new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void startLocation(View view) {

        startTime = Calendar.getInstance().getTime();
        startLocationUpdates = true;
        buttonStartLocation.setVisibility(View.GONE);
        buttonStopLocation.setVisibility(View.VISIBLE);

    }

    public void startLocationUpdates() {
//        Log.e("startLocationUpdates", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));

        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasPermission = ContextCompat.checkSelfPermission(this, locationPermission);
        String[] permissions = new String[]{locationPermission};

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, 0);

        } else {

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);

            if (lastLocation != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()),
                        15));
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);

            Toast.makeText(this, "We already have persmission", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (startLocationUpdates){
            currentLocation = location;
//        Log.e("location",location.toString());
            allLocations.add(currentLocation);
//        Log.e("on location changed", location.toString() + ", " + lastUpdateTime.toString());
        }

    }

    public void stopLocation(View view) {

        int i = 0;
        Double distance = 0.0;
        endTime = Calendar.getInstance().getTime();
        startLocationUpdates = false;

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        while (i < allLocations.size()) {

            Location firstPoint;
            Location secondPoint = allLocations.get(i);

            if(i < 1){
                firstPoint = allLocations.get(0);
                distance += allLocations.get(0).distanceTo(allLocations.get(1));
            }
            else{
                firstPoint = allLocations.get(i-1);
                distance += allLocations.get(i-1).distanceTo(allLocations.get(i));
            }

            map.addPolyline((new PolylineOptions())
                .add(   new LatLng(firstPoint.getLatitude(), firstPoint.getLongitude()),
                        new LatLng(secondPoint.getLatitude(), secondPoint.getLongitude())
                )
                .width(5)
                .color(Color.RED)
                .geodesic(false));

            i++;
        }

        long diffInMilis = endTime.getTime() - startTime.getTime();
//
        long diffInSecond = diffInMilis / 1000;
//        long diffInMinute = diffInMilis / (60 * 1000);
//        long diffInHour = diffInMilis / (60 * 60 * 1000);
//        long diffInDays = diffInMilis / (24 * 60 * 60 * 1000);

        Date date = new Date(diffInSecond);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);
        System.out.println(dateText);

    }

    protected void onStart() {

        if(googleApiClient != null){
            googleApiClient.connect();
        }

        super.onStart();
    }

    protected void onStop() {

        if(googleApiClient != null){
            googleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    protected void onPause() {
        if (startLocationUpdates) {
            startLocationUpdates();
        }
//        Log.e("on pause", "on pause");
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Log.e("on connected", "good");
//        Log.e("googleAPI", googleApiClient.toString());

    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("on connection failed", "bad");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//            Log.e("permission", "permission");

//        beaubreuilCoord = new LatLng(45.879493, 1.292750);
//        feytiatCoord = new LatLng(45.812902, 1.323138);
//        limogesCoord = new LatLng(45.830365, 1.254312);
//
//        MarkerOptions marker1 = new MarkerOptions().position(beaubreuilCoord).title("Marker 1 : Cora Centre Commercial Beaubreuil");
//        MarkerOptions marker2 = new MarkerOptions().position(feytiatCoord).title("Marker 2 : Super U Centre Commercial Feytiat");
//        MarkerOptions marker3 = new MarkerOptions().position(limogesCoord).title("Marker 3 : Tribunal Centre Ville Limoges");

        // Add a marker in Sydney and move the camera

//        map.addMarker(marker1);
//        map.addMarker(marker2);
//        map.addMarker(marker3);

//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "LOCATION_FINE GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "LOCATION_FINE DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
