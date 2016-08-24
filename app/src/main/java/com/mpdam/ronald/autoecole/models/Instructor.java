package com.mpdam.ronald.autoecole.models;

import com.google.common.collect.ImmutableMap;

/**
 * Created by Ronald on 28/07/2016.
 */

public class Instructor extends com.strongloop.android.loopback.User {

    public String username;
    public String firstame;
    public String lastname;
    public String phone;

    public ImmutableMap setData(String login, String firstn, String lastn, String tel){
        return ImmutableMap.of("Username", login, "firstname", firstn, "lastname", lastn, "phone", tel);
    }
}
