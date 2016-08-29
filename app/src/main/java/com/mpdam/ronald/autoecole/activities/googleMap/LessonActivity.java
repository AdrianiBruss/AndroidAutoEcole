package com.mpdam.ronald.autoecole.activities.googleMap;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.utils.GPSLocation;
import com.mpdam.ronald.autoecole.utils.GoogleAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LessonActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private List<Location> allLocations = new ArrayList<Location>();
    private LocationRequest locationRequest;
    private LocationManager locationManager;

    private Date startTime;
    private Date endTime;

    private Boolean startLocationUpdates = false;
    private Boolean locationUpdates = false;

    private Button buttonStartLocation;
    private Button buttonStopLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        buttonStartLocation = (Button) findViewById(R.id.butttonStartLocation);
        buttonStopLocation = (Button) findViewById(R.id.buttonStopLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (googleApiClient == null)
        {
            googleApiClient = new GoogleAPI().getGoogleApiClient(this, this, this);
        }

        if (locationRequest == null)
        {
            locationRequest = new GoogleAPI().getLocationRequest(500,100,LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart()
    {
        if(googleApiClient != null)
        {
            googleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        if(googleApiClient != null)
        {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    public void startLocation(View view)
    {
        startTime = Calendar.getInstance().getTime();
        startLocationUpdates = true;
        buttonStartLocation.setVisibility(View.GONE);
        buttonStopLocation.setVisibility(View.VISIBLE);
    }

    public void stopLocation(View view)
    {
        int i = 0;
        Double distance = 0.0;
        endTime = Calendar.getInstance().getTime();
        startLocationUpdates = false;

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        if(allLocations.size() > 1)
        {
            while (i < allLocations.size())
            {
                Location firstPoint;
                Location secondPoint = allLocations.get(i);

                if(i < 1)
                {
                    firstPoint = allLocations.get(0);
                    distance += allLocations.get(0).distanceTo(allLocations.get(1));
                }
                else
                {
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
        }
        else
        {
            Toast.makeText(this, "You have to move if you want to save itinerary", Toast.LENGTH_SHORT).show();
        }

        long diffInMilis = endTime.getTime() - startTime.getTime();
        Log.e("diffInMilis", String.valueOf(diffInMilis));
        long diffInSecond = diffInMilis / 1000;
        Log.e("diffInSecond", String.valueOf(diffInSecond));
//        long diffInMinute = diffInMilis / (60 * 1000);
//        long diffInHour = diffInMilis / (60 * 60 * 1000);
//        long diffInDays = diffInMilis / (24 * 60 * 60 * 1000);

        Date date = new Date(diffInSecond);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String dateText = df2.format(date);
        System.out.println(dateText);

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        locationUpdates = new GoogleAPI().checkGPS(this, locationManager, googleApiClient, locationRequest, map);

        if(locationUpdates)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        new GoogleAPI().onConnectionFailed(connectionResult);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        new GPSLocation().focusOnUser(location, map);

        if (startLocationUpdates)
        {
            allLocations.add(location);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

}
