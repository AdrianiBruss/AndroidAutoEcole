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
import com.strongloop.android.loopback.callbacks.VoidCallback;

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

//        Student newStudent = studentRepo.createUser( "stormzy@email.fr", "password",
//                new Student().setData( "stormzy", "16 rue de sotmrzy", "username", "Stormzy", "phone", "iVBORw0KGgoAAAANSUhEUgAAECAAAAkSCAIAAACOxModAAAAA3NCSVQICAjb4U/gAAAgAElEQVR4nJy9W5MkO44mBoD0iMysqjO90zMjTdvOPkjPGv30+TeatV2T2bTtPqxJ25dz6mRlRDgJ6AEE/CM9qrpHbqerI/1CgrgTBEH+l3/5F2YmEmYuLKraWjOzQlxKKZWJSDsxcydj5t67iNRazUzJRKT3TkSlMDNb12/fvj0ej9YaqYrIrvdSSq2Xy+Wy1VdmZqn+ORERaylFqLTWVLWUQkRmxsxm5j9UVUT8DhH5b1XNp9ka3ifS3jszMzMR+VMzc+BVx1MiUlV/TUS8fSLypw6DiDCX1hozl1K8i8fjQUTX61VEHGPMBwzMvG2bIyfhREj8R2Iv4ffLkYwv+7/elIioqjfowPjLjr0Em4j2fS+l1FodDAfJH/kdhI2Za60+Fu8rMElm5o37le37V6WU4IHij5Jk+X4C5ogVEYckm8pmfTg+xvwqn4qIQ+Kd9t4fj0etNWFIHHr73t1gV9XsK9v31hKNSSZ/00fk//qjfd/3fb9er06my+XiN7E179ox0HvHNpOHk1ETUQ52ItDhTEK31mqtibeZOQeek6VzRMj5SeUEw+/UWpPrnCtqrS4jZkakwIeStHC6M1uyFrHe73d/dAYjYai1ClcfkZkRK8P1lMfyJjJ/EsiZLTkZcYJEdJSiDiGi1lprLaWemU0PMKQc3DtEwA6mHSOylqoDX0ZJFK4H3ann+wjt7Xa7XC7Owz5ApwsppzJxJYmayrkrO02eNyYzK0JExD40792s9y5SPz4+LpfLtm22t+SKHjivtSL8DNzyaO1yuaBmc3TlHX8zyZSYSZgTdUlQJ5wPxPigPrLrohlaa9u2oV511IkIG6HmNzMKTYI9OsOnPkeGSXOTj5zb3QTkcLx3Z+MkqFsEZy0XqKdj937Pcoo6c9s2RwsR7fvun6e+cjLVWh+PR5IAJcVx4pbOb27b5j+yEW/HaZfKyptNGuVrqLuwI9TPiUm/UpPs++5GzcxJUxjsWkLlyHQeLqWIiMhkAfd9z+5ccot7HmHKCeyg0xq6tjR/+RsB8PuorrNlZCf/7YhVVTZyI5tEd3vkL7uFen19TY5CHOYdbzABSwOB5HZpSmIFARSH5jzgNtEx78Ppcbmp+vTpk1su5PPslMDTcBvnyicsLOULDrlr0VJqcn6Cmm+iFk0csvtMgHbEs5kJkB5VQYzrcOqQFZOTSylETERSCjObaxhv3NEOlnoIsgMA/meyloiUIFz6h4dXRETEFpYxGVVEzA41OJTcZPEVhkZw38ysuwFQ0JZGOWSUO0dL2arf90Hd7/dkrZSIoTblcMZcmszMrbOZEWvvvcjmfW3b9vLy8u3bN23dR5eOTdIrHYnH42FheaVQinPaVlQgpRSjnhKhHbDJ7L7rWb2k9HVT94uQzVJGePY5k+GcRu7DE7ErqFprrcMcqCqxqiqZEJHxIX1DhAk8OlaHxMx0t9S93VREHAPWlZmLqIhQ7601Jq21FnPDagY+QxDw8GqY2fWcsD0eDzOttRZiVXXLXllaa+4eu5Qx877vslVVFRmGJhskcQ40Zta+O6+JSNPH+/v7+/v7n//8577r7Xbrj3a/30vZmLlbIaJGYmad2MxI6r7vu5EPkYiMpZSShpWIjEsS1Kyr6uXyQkSlVDNrbXdoHb2Px2Pfm4gMGzT0+ROjw7ONHp5qOYxgMENNT0mGpVUiUnbaHy0oD4/dgXF/j2KuRMK1Vuua8mV0TO7IDj88pN7MTFlSlyLw+MOnJ113ImLzkSq8P5CpqqYsIsKDvo/H49vXP/yn//Sffve7f/zd7363bdv1en19fU3+VyNVJbZt2wY2iIlI6dC3BFbYZv8nVVLwJJkZGeP7qUCGaPDkthER0zFhR1Q4AEa677t7Jv7maFmGS4OmJxFIYIAMDXpQIGl6qVt2ba1v29bJ9n1v2h+Px3/5r//373//+3/913+ttX7+/Pkf/uEffvsPf//b3/728a39t//23/7whz//6U9/utSNiFT7t2/ffn3/8z//8z//b//7P/3TP/3T7373v379+lVVX15eyna53++96b7vY5akpqoS+sfMlDpFLMW5K80WMxMrEan7jKbbtv3556+32+1f/+t/+f3vf99be319rbb97ne/+z//j3/++Pj4z//5//r111+//nrrvd8ej0+fPn366eWnn376/B++fPny5ePX92/fvmnvzjalFCP65Zdffvnl169fv16vV4ewtVZo87mzqiqxiEgtrbW//V/+/j/+x//4008//fLLL//z//l///jHP/7pf/6hlELcXYu6I2Fm23b9+PgodBiC9GTMTHkQ0X0kM+t9p/AqzY6ZaXzSiUjMpePw2AdZeQiX84GZsTOSexrUReQi5kr35eXl06e3z58/17q9vb1JLbXW7j6pDI9LRC5buV6vjqWylcNcliIiHbyLKuXwLW3ALCJsWmtlMREhLvu+Px6Pbds0dLhEfCZ48jBYQlZr3S7l559/fnl5+fnnn//wP/+07/vXr19vt9u+dzkiNnvv3T1n6/bt27fr9dUBaK05Bka8zrUiaWutcc/p2C8///rrr7/+/KevImLK4dcPDlRtqlpo9miIjMacdBHAIaomKIPHV/Ms71B3rMubPqfGHlkObcMzPPkmy6qaRkfQ2qTWWPFO/k7XyCKCR6Bb4jrGiJ/P76wvLDA8fQHMPflInzbytLV8edHGk67+jrb8914LKbNB7M4vhTjnGeDF0KQdXzT5uVkcyNP7w+mixYkiseMTnNefx3JubQF1AZKe8AktX51/YzuLG4CPzrAtwz/jLce4ALkM4S8CvJCV4co3l6c4CuwrDP7UwtzUmES4N9Vak3pxZwDDuakD06XMWYy17r6Qzzp96p2BaFfXrbVPnz4N6ssEjwdwehhKEfLZbkYSrtdrrdXdp5xKJ1H6vudwLpeLqu7W0tjd7t9aayOOQQXJbWaXUi+XS7OWc5l8QSLWhyNl5m3bWtOcrTtvsxEzs1jG+bXT+/v7y8tLrdXjkxZu6jEVooEinz25Na+1Si2rGif1ljWiea6pOFxlf4/DTHuEh8IlK6VILT4lxLhHTutYKjOrEjPHrKonj3n8Ybhvrb2+vrb97rNaMxOpl8vlfr/73IcOx3u0oKo+d554m45IeDdhZoXZBE45eY6qoWQtfJ6EO94M7J2E+rBQswbwBR1aHumsKtMbSTAOSMImEhHBqMfnTqi+qj7/t2zV8fx4PHSA6qshR5ztfr/70l44b8Y8XBpf46u1iqiI+O9ONhx7ZmK+XC5//vOfPVh0u93MIyqmCAnR8AccWiUx9xHNTI85Bf44BvhM5br3gkovSHaEPl5fXz99+vTy8pJ86NxOyqlDXKWQmPuErTUlY+bHbf/27du+7xSub6FJeeYP5cO4jPsylBsz+7zVZi3NzD5rcA9EqPCh8MdbLjVJfcNOS01ssLcA9oJDGESEZAI1AfAxUoR3Sin7fsc/zWx4R304mT6DcFVzv98vlyMCTGCCBeYLJFOQn2A5ZvDyHGhCQqeidg2Q7seY9cSbpZRt2x6PB5Emx3o8XyM4jC0z8/BsB/5XM+20phGslVSPoZfGvJVC10lcqdVBiNZocyKKcIGA1cxcb7MYM5t6bNP9Wx/stNyGtKbTZbP34iQTWE9EeFwK82URyZgMNpIYoFh9cPOXo7OT5kH1ij26BldrRMRhPbF9IjK3gDG/zxhdLmARLJNhv9mL0yhVQfayqJeELclHRHUrPAd7bcSjJtdlGS8bzPozDAKzYIp4o85rQ2bGBzirJTpDi6AeTVmvtdqwzvV42T1nW7075yhXx2ds+DQM7zRiESki7pUdsbWTv6pDdw1/gJl9XdjzTIDxAp4Rmi/btvXeW2sFxK1BvBcRkjxgpxUfmUPoSXQH2/mWw+zSPLXJT1BHGazd+DIQglHqtLjvGgZhhlFL/nYlliu/y"));
//
//        newStudent.save(new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                Log.e("message", "User created !");
//                startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e("message", t.toString());
//            }
//        });
//
//        User newStudent = studentRepo.createUser( "student1@mail.fr","password",
//                new Student().setData( "student1", "", "student1", "","", ""));


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
                    Constant.INSTRUCTOR = instructor;
                    Log.e("Constant.INSTRUCTOR", String.valueOf(Constant.INSTRUCTOR));
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

