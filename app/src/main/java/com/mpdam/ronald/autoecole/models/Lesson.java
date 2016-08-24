package com.mpdam.ronald.autoecole.models;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Model;

import java.util.Date;

/**
 * Created by Ronald on 11/08/2016.
 */
public class Lesson extends Model {

    public String name;
    public Date date;
    public Float during;
    public Float distance;
    public String geoPoints;

    public ImmutableMap setData(String n, Date da, Float du, Float di) {
        return ImmutableMap.of("name", n, "date", da, "during", du, "distance", di);
    }
}
