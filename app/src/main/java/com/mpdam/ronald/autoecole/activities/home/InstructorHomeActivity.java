package com.mpdam.ronald.autoecole.activities.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.adapters.StudentAdapter;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.List;

public class InstructorHomeActivity extends SideMenuActivity {

    private StudentRepository studentRepo;
    private RestAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_instructor_home, (ViewGroup) findViewById(R.id.container));

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        studentRepo = adapter.createRepository(StudentRepository.class);

        listView = (ListView) findViewById(R.id.listViewStudents);

        studentRepo.findAll(new ListCallback<Student>() {
            @Override
            public void onSuccess(final List<Student> all) {
                StudentAdapter sa = new StudentAdapter(InstructorHomeActivity.this, all);
                listView.setAdapter(sa);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Constant.USER = all.get(position);
                        startActivity(new Intent(InstructorHomeActivity.this, StudentActivity.class));
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                Log.e("onError findAll", t.toString());
            }
        });

    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
