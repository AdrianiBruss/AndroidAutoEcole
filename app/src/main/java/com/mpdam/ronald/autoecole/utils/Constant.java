package com.mpdam.ronald.autoecole.utils;


import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.User;

import java.net.URI;

/**
 * Created by Ronald on 08/08/2016.
 */
public class Constant {

    public static final String URL          = "http://autoecoleandroid-cloudbruss.rhcloud.com/api/";
    public static Student STUDENT           = null;
    public static Instructor INSTRUCTOR     = null;
    public static Uri PhotoURI              = null;
    public static String encodedImage       = null;

}
