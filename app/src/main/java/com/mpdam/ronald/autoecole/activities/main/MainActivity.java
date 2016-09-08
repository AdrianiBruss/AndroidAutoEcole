package com.mpdam.ronald.autoecole.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.JsonArray;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.home.InstructorHomeActivity;
import com.mpdam.ronald.autoecole.activities.home.StudentHomeActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.LessonRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private InstructorRepository instructorRepo;
    private StudentRepository studentRepo;

    private RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        instructorRepo = adapter.createRepository(InstructorRepository.class);
        studentRepo = adapter.createRepository(StudentRepository.class);

        //get current user from cache
        findInstructor();

    }


    protected void findInstructor() {

        instructorRepo.findCurrentUser(new ObjectCallback<Instructor>() {
            @Override
            public void onSuccess(Instructor instructor) {
                if (instructor == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Constant.INSTRUCTOR = instructor;
                    Constant.CURRENT_USER = "INSTRUCTOR";
                    startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e("on error instructor",t.toString());
                findStudent();
            }
        });
    }

    protected void findStudent() {

        studentRepo.findCurrentUser(new ObjectCallback<Student>() {
            @Override
            public void onSuccess(Student student) {
                if (student == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Constant.STUDENT = student;
                    Constant.CURRENT_USER = "STUDENT";
                    startActivity(new Intent(getApplicationContext(), StudentHomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e("on error student", t.toString());
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}

