package com.mpdam.ronald.autoecole.activities.lesson;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.mpdam.ronald.autoecole.activities.home.InstructorHomeActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.utils.GPSLocation;
import com.mpdam.ronald.autoecole.utils.GoogleAPI;

import java.text.ParseException;
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
    private Button backToLessons;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        buttonStartLocation     = (Button) findViewById(R.id.butttonStartLocation);
        buttonStopLocation      = (Button) findViewById(R.id.buttonStopLocation);
        backToLessons           = (Button) findViewById(R.id.backToLessons);

        backToLessons.setVisibility(View.GONE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // create instance of google api
        if (googleApiClient == null)
        {
            googleApiClient = new GoogleAPI().getGoogleApiClient(this, this, this);
        }

        // create instance of location request to get user locations
        if (locationRequest == null)
        {
            locationRequest = new GoogleAPI().getLocationRequest(500,100,LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        // create the map
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
        backToLessons.setVisibility(View.GONE);

    }

    public void stopLocation(View view)
    {
        startLocationUpdates = false;

        buttonStopLocation.setVisibility(View.GONE);
        buttonStartLocation.setVisibility(View.GONE);
        backToLessons.setVisibility(View.VISIBLE);

        // stop requestLocationUpdates
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        //check if locations array is empty
            // if true -> draw lines between geopoints, display date, duration and distance of a travel
            // else show a message
        if(allLocations.size() > 1)
        {
            int i = 0;
            Double distance = 0.0;
            endTime = Calendar.getInstance().getTime();

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

            //make date, distance and duration readable for users (format)

            // duration
            long diff = endTime.getTime() - startTime.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;

            SimpleDateFormat durationFormatter = new SimpleDateFormat("HH:mm:ss");
            String duration = diffHours + ":" + diffMinutes + ":" + diffSeconds;

            // date
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

            try {

                Date durationFormat = durationFormatter.parse(duration);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("YOUR TRIP");
                builder.setMessage( "Date : " + dateFormatter.format(endTime) + "\n" + "\n" +
                        "Duration : " + durationFormatter.format(durationFormat) + "\n" + "\n" +
                        "Distance (en km): " + String.format("%.3g%n", (distance / 1000) )); // distance in km with 3 decimal places
                builder.create().show();

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else
        {
            Toast.makeText(this, "Aucun itinéraire enregistré", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        // check all permissions (GPS, Manifest) on google api connection -> return boolean
        locationUpdates = new GoogleAPI().checkGPS(this, locationManager, googleApiClient, locationRequest, map);

        // if true, start requestLocationUpdates to get all user locations
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
        // when user location changes, center map on user and save location in array if button "Start" is pressed
        new GPSLocation().focusOnUser(location, map);

        if (startLocationUpdates)
        {
            allLocations.add(location);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // initialize the map created
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void backToLessons(View view) {
        startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
    }

}
