package com.mpdam.ronald.autoecole.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.ProfileFragment;
import com.mpdam.ronald.autoecole.activities.lesson.LessonActivity;
import com.mpdam.ronald.autoecole.activities.lesson.LessonFragment;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.adapters.LessonAdapter;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentActivity extends SideMenuActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private RestAdapter adapter;

    private RequestQueue queue;

    private SharedPreferences preferences;

    private ArrayList lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_student, (ViewGroup) findViewById(R.id.container));

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // get token stored in Preferences for API request /lessons of one student
        preferences = getSharedPreferences("AUTHENTICATION_DATA", Context.MODE_PRIVATE);
        String token = preferences.getString("Authentication_Token","");
        String userId = Constant.STUDENT.getId().toString();

        queue = Volley.newRequestQueue(this);

        String url = Constant.URL + "Students/" + userId + "/lessons?access_token=" + token;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        lessons = new ArrayList();
                        int i = 0;

                        //convert response to List<Lesson> that we can handle easily
                        while(i < response.length())
                        {
                            try
                            {
                                JSONObject object = response.getJSONObject(i);

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                                Date dateLesson = new Date();

                                try {
                                    Date date = formatter.parse(object.get("date").toString());
                                    dateLesson = date;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String duringLesson = (String) object.get("during");
                                Double distanceLesson = object.getDouble("distance");
                                JSONArray geopointsLesson = object.getJSONArray("geoPoints");

                                Lesson lesson = new Lesson(dateLesson, duringLesson, distanceLesson, geopointsLesson);
                                lessons.add(lesson);

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            i++;
                        }

                        //setup TapBar
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment().newInstance(Constant.STUDENT), "");
        adapter.addFragment(new LessonFragment().newInstance(lessons), "");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title=" ";
            switch (position){
                case 0:
                    title="Profil";
                    break;
                case 1:
                    title="Cours";
                    break;
            }

            return title;
        }
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, LessonActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, InstructorHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
