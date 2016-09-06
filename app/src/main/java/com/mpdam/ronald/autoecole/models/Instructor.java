package com.mpdam.ronald.autoecole.models;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ronald on 28/07/2016.
 */

public class Instructor extends com.strongloop.android.loopback.User {

    public String username;
    public String firstame;
    public String lastname;
    public String phone;

    public Map<String, ? extends Object> setData(String login, String firstn, String lastn, String tel){
        HashMap ht = new HashMap();

        ht.put("username", login);
        ht.put("firstname", firstn);
        ht.put("lastname", lastn);
        ht.put("phone", tel);

        return ht;
    }
}
