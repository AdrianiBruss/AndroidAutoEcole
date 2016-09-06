package com.mpdam.ronald.autoecole.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ronald on 11/08/2016.
 */
public class Student extends com.strongloop.android.loopback.User implements Serializable {

    public String username;
    public String address;
    public Integer nbHours;
    public String firstname;
    public String lastname;
    public String phone;
    public String picture;

    public Map<String, ? extends Object> setData(String login, String add, String firstn, String lastn, String tel, String pic) {
        HashMap ht = new HashMap();

        ht.put("username", login);
        ht.put("address", add);
        ht.put("nbHours", 0);
        ht.put("firstname", firstn);
        ht.put("lastname", lastn);
        ht.put("phone", tel);
        ht.put("picture", pic);

        return ht;
    }
}
