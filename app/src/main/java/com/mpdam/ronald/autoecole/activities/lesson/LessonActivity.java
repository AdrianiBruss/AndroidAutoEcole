package com.mpdam.ronald.autoecole.activities.lesson;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.home.StudentActivity;
import com.mpdam.ronald.autoecole.activities.home.StudentHomeActivity;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.modelsRepositories.LessonRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.mpdam.ronald.autoecole.utils.GPSLocation;
import com.mpdam.ronald.autoecole.utils.GoogleAPI;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LessonActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;
    private LocationManager locationManager;

    private Date startTime;
    private Date endTime;
    private List<Location> allLocations = new ArrayList<Location>();
    private Boolean startLocationUpdates = false;
    private Boolean locationUpdates = false;
    private String userId;
    private Lesson lesson;

    private TextView textViewDistance, textViewDuring, textViewDate;
    private Button buttonStartLocation, buttonStopLocation, backToLessons;
    private LinearLayout linearLayoutInfos;

    private RestAdapter adapter;
    private LessonRepository lrepo;
    private StudentRepository srepo;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        textViewDuring = (TextView) findViewById(R.id.textViewDuring);
        linearLayoutInfos = (LinearLayout) findViewById(R.id.linearLayoutInfos);
        buttonStartLocation = (Button) findViewById(R.id.butttonStartLocation);
        buttonStopLocation = (Button) findViewById(R.id.buttonStopLocation);
        backToLessons = (Button) findViewById(R.id.backToLessons);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        lrepo = adapter.createRepository(LessonRepository.class);
        srepo = adapter.createRepository(StudentRepository.class);

        userId = Constant.STUDENT.getId().toString();

        backToLessons.setVisibility(View.GONE);

        // create instance of google api
        if (googleApiClient == null) {
            googleApiClient = new GoogleAPI().getGoogleApiClient(this, this, this);
        }

        // create instance of location request to get user locations
        if (locationRequest == null) {
            locationRequest = new GoogleAPI().getLocationRequest(500, 100, LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        // if user is looking for lesson details
        if(Constant.CURRENT_USER == "STUDENT"){
            lesson = Constant.LESSON;

            buttonStartLocation.setVisibility(View.INVISIBLE);
            buttonStopLocation.setVisibility(View.INVISIBLE);
            backToLessons.setVisibility(View.VISIBLE);
            linearLayoutInfos.setVisibility(View.VISIBLE);

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String date = df.format(lesson.date);

            DecimalFormat numberFormat = new DecimalFormat("#0.000");
            String distance = numberFormat.format(lesson.distance).toString();

            String duration = lesson.during.toString();

            textViewDate.setText(textViewDate.getText() + date);
            textViewDistance.setText(textViewDistance.getText() + distance);
            textViewDuring.setText(textViewDuring.getText() + duration);
        }

        // create the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    public void startLocation(View view) {
        startTime = Calendar.getInstance().getTime();

        startLocationUpdates = true;

        buttonStartLocation.setVisibility(View.GONE);
        buttonStopLocation.setVisibility(View.VISIBLE);
        backToLessons.setVisibility(View.GONE);

    }

    public void stopLocation(View view) {
        JSONArray jsonArray = new JSONArray();

        startLocationUpdates = false;

        buttonStopLocation.setVisibility(View.GONE);

        // stop requestLocationUpdates
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        //check if locations array is empty
        // if true -> draw lines between geopoints, display date, duration and distance of a travel
        // else show a message
        if (allLocations.size() > 1) {

            endTime = Calendar.getInstance().getTime();

            backToLessons.setVisibility(View.VISIBLE);
            linearLayoutInfos.setVisibility(View.VISIBLE);

            // API needs json array to create a lesson so we convert our location list to JSONArray
            jsonArray = new Lesson().convertToJSONArray(allLocations);

            // draw itinerary on map and get the distance
            Double distance = new Lesson().getItinerary(allLocations, map);
            // distance in km with 3 decimal places
            DecimalFormat numberFormat = new DecimalFormat("#0.000");
            textViewDistance.setText(textViewDistance.getText() + numberFormat.format(distance));

            String duration = new Lesson().getDurationBetweenTwoDates(startTime, endTime);
            textViewDuring.setText(textViewDuring.getText() + duration);

            // format date
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            String date = dateFormatter.format(endTime);
            textViewDate.setText(textViewDate.getText() + date);

            lrepo.createObject(
                new Lesson().setData(
                        userId,
                        endTime,
                        duration,
                        distance,
                        jsonArray
                )
            )
            .save(new VoidCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Le cours a bien été créé !", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable t) {
                }
            });

        } else {
            buttonStartLocation.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Aucun itinéraire enregistré", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // check all permissions (GPS, Manifest) on google api connection -> return boolean
        locationUpdates = new GoogleAPI().checkGPS(this, locationManager, googleApiClient, locationRequest, map);

        // if true, start requestLocationUpdates to get all user locations
        if (locationUpdates) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        new GoogleAPI().onConnectionFailed(connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        // when user location changes, center map on user and save location in array if button "Start" is pressed
        new GPSLocation().focusOnUser(location, map);

        if (startLocationUpdates) {
            allLocations.add(location);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // initialize the map created
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(Constant.CURRENT_USER == "STUDENT"){

            int i = 0;
            JSONArray list = Constant.LESSON.geoPoints;

            while (i < list.length())
            {
                JSONObject firstPoint = null;
                JSONObject secondPoint = null;

                try {
                    secondPoint = (JSONObject) list.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(i < 1)
                {
                    try {
                        firstPoint = (JSONObject) list.get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        firstPoint = (JSONObject) list.get(i-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    map.addPolyline((new PolylineOptions())
                            .add(   new LatLng(firstPoint.getDouble("latitude"), firstPoint.getDouble("longitude")),
                                    new LatLng(secondPoint.getDouble("latitude"), secondPoint.getDouble("longitude"))
                            )
                            .width(5)
                            .color(Color.RED)
                            .geodesic(false));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i++;
            }

        }
    }

    public void backToLessons(View view) {
        // depends if we are on lesson details or create a lesson
        if(lesson != null){
            startActivity(new Intent(this, StudentHomeActivity.class));
        }
        else{
            startActivity(new Intent(this, StudentActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        // depends if we are on lesson details or create a lesson
        if(lesson != null){
            startActivity(new Intent(this, StudentHomeActivity.class));
        }
        else{
            startActivity(new Intent(this, StudentActivity.class));
        }
    }
}
