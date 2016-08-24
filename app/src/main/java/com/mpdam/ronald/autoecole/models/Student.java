package com.mpdam.ronald.autoecole.models;

import com.google.common.collect.ImmutableMap;

/**
 * Created by Ronald on 11/08/2016.
 */
public class Student extends com.strongloop.android.loopback.User {

    public String username;
    public String firstname;
    public String lastname;
    public String address;
    public String nbHours;
    public String phone;
    public String picture;
    public Lesson lessons;

    public ImmutableMap setData(String login, String firstn, String lastn, String add, String tel, String pic) {
        return ImmutableMap.of("firstname", firstn, "lastname", lastn, "address", add, "phone", tel, "picture", pic);
    }
}
