package com.mpdam.ronald.autoecole.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.home.InstructorHomeActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

public class MainActivity extends AppCompatActivity {

    private InstructorRepository instructorRepo;
    private StudentRepository studentRepo;

    private RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        instructorRepo = adapter.createRepository(InstructorRepository.class);
        studentRepo = adapter.createRepository(StudentRepository.class);
//
        //get current user from cache
        findInstructor();

//        Instructor newInstructor = instructorRepo.createUser( "instructor1@mail.fr","password",
//                new Instructor().setData( "Paul", "Samaré","", ""));
//
//        newInstructor.save(new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                Log.e("message", "User created !");
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e("message", t.getLocalizedMessage()+ ", " + t.getMessage() + ", " +  t.toString());
//            }
//        });

//        instructorRepo.createObject(ImmutableMap.of("Modele", "Citroën","Marque", "C3")).save(new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                Log.e("message", "save !");// Pencil now exists on the server!
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e("message", t.toString());// save failed, handle the error
//            }
//        });

//        repository.findById("579743118df0ad1990d60e95", new ObjectCallback<Voiture>() {
//            @Override
//            public void onSuccess(Voiture v) {
//                Log.e("message", v.getMarque());// found!
//                Log.e("message", v.getModele());// found!
//            }
//
//            public void onError(Throwable t) {
//                Log.e("message", "error !");// handle the error
//            }
//        });

    }


    protected void findInstructor() {

        instructorRepo.findCurrentUser(new ObjectCallback<Instructor>() {
            @Override
            public void onSuccess(Instructor instructor) {
                if (instructor == null) {
                    Log.e("onSuccess","instructor null");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
//                    Constant.INSTRUCTOR = instructor;
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
                    Log.e("onSuccess","student null");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Constant.STUDENT = student;
                    startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
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

    public void logout(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}

