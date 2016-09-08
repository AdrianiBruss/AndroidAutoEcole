package com.mpdam.ronald.autoecole.models;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.collect.ImmutableMap;
import com.mpdam.ronald.autoecole.adapters.LessonAdapter;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.Model;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ronald on 11/08/2016.
 */
public class Lesson extends Model {

    public Date date;
    public String during;
    public Double distance;
    public JSONArray geoPoints;

    public Lesson(){}

    public Lesson(Date da, String du, Double di, JSONArray points){
        this.date = da;
        this.during = du;
        this.distance = di;
        this.geoPoints = points;
    }

    public ImmutableMap setData(String studentId, Date da, String du, Double di, JSONArray arrayPoints) {
        return ImmutableMap.of("studentId", studentId, "date", da, "during", du, "distance", di, "geoPoints", arrayPoints);
    }

    public Double getItinerary(List<Location> list, GoogleMap map){

        Double distance = 0.0;

        int i = 0;

        while (i < list.size())
        {
            Location firstPoint;
            Location secondPoint = list.get(i);

            if(i < 1)
            {
                firstPoint = list.get(0);
                distance += list.get(0).distanceTo(list.get(1));
            }
            else
            {
                firstPoint = list.get(i-1);
                distance += list.get(i-1).distanceTo(list.get(i));
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

        return (distance/1000);

    }

    public JSONArray convertToJSONArray(List<Location> listLocation){

        int i = 0;

        JSONArray allGeopoints = new JSONArray() ;
        JSONObject oneGeopoint = new JSONObject();

        while (i < listLocation.size())
        {
            Double latitude = listLocation.get(i).getLatitude();
            Double longitude = listLocation.get(i).getLongitude();

            try {
                oneGeopoint.put("latitude", latitude);
                oneGeopoint.put("longitude", longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            allGeopoints.put(oneGeopoint);

            i++;
        }

        return allGeopoints;

    }

    public String getDurationBetweenTwoDates(Date firstDate, Date secondDate){

        // duration
        long diff = secondDate.getTime() - firstDate.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        SimpleDateFormat durationFormatter = new SimpleDateFormat("HH:mm:ss");
        String duration = diffHours + ":" + diffMinutes + ":" + diffSeconds;

        try {
            Date durationFormat = durationFormatter.parse(duration);
            duration = durationFormatter.format(durationFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(duration);

    }

}
