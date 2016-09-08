package com.mpdam.ronald.autoecole.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.lesson.LessonActivity;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.adapters.LessonAdapter;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentHomeActivity extends SideMenuActivity {

    private StudentRepository studentRepo;

    private RestAdapter adapter;

    private SharedPreferences preferences;

    private List<Lesson> lessons;

    private GridView gridView;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_student_home, (ViewGroup) findViewById(R.id.container));
        gridView = (GridView) findViewById(R.id.student_gridViewLessons);

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        studentRepo = adapter.createRepository(StudentRepository.class);

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
                    public ArrayList onResponse(JSONArray response)
                    {
                        int i = 0;
                        lessons = new ArrayList<>();

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

                        if(lessons != null){
                            LessonAdapter ladapter = new LessonAdapter(getApplicationContext(), lessons);
                            gridView.setAdapter(ladapter);

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Constant.LESSON = lessons.get(position);
                                    startActivity(new Intent(StudentHomeActivity.this, LessonActivity.class));
                                }
                            });
                        }

                        return null;
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
