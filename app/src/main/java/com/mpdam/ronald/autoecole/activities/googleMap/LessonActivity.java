package com.mpdam.ronald.autoecole.activities.googleMap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.utils.Constant;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class LessonActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location currentLocation;
    private List<Location> allLocations = new ArrayList<Location>();
    private LocationRequest locationRequest;

    private TextView latitudeTextView, longitudeTextView, lastUpdateTimeTextView;
    private String lastUpdateTime;

    private Integer permissionInt;
    private LatLng beaubreuilCoord, feytiatCoord, limogesCoord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if(locationRequest == null){
            locationRequest = new LocationRequest()
                    .setInterval(10000)
                    .setFastestInterval(5000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


//        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
//        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
//        lastUpdateTimeTextView = (TextView) findViewById(R.id.lastUpdateTimeTextView);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("on connected", "good");
        Log.e("googleAPI", googleApiClient.toString());

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("on connection failed", "bad");
    }

    protected void startLocationUpdates() {
        Log.e("startLocationUpdates", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));

        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasPermission = ContextCompat.checkSelfPermission(this, locationPermission);
        String[] permissions = new String[]{locationPermission};

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, 0);

        } else {

            map.setMyLocationEnabled(true);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);

            Toast.makeText(this, "We already have persmission", Toast.LENGTH_SHORT).show();

        }

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

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        allLocations.add(currentLocation);
        lastUpdateTime = DateFormat.getTimeInstance().format(new Date());

//        Log.e("on location changed", location.toString() + ", " + lastUpdateTime.toString());
//        updateUI();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),
                15));

//        map.addPolyline((new PolylineOptions())
//            .add(   new LatLng(previousLocation.getLatitude(), previousLocation.getLongitude()),
//                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())
//            )
//            .width(5)
//            .color(Color.RED)
//            .geodesic(false));
    }

    private void updateUI() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasPermission = ContextCompat.checkSelfPermission(this, locationPermission);
        String[] permissions = new String[]{locationPermission};

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

    public void stopLocation(View view) {

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        int i = 0;

        while (i < allLocations.size()) {

            Location firstPoint;
            Location secondPoint = allLocations.get(i);

            if(i < 1){
                firstPoint = allLocations.get(0);
            }
            else{
                firstPoint = allLocations.get(i-1);
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
    }
}
