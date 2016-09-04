package com.mpdam.ronald.autoecole.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.adapters.StudentAdapter;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.List;
import java.util.ListIterator;

public class InstructorHomeActivity extends SideMenuActivity {

    private StudentRepository studentRepo;
    private InstructorRepository instructorRepo;
    private RestAdapter adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_instructor_home);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_instructor_home, (ViewGroup) findViewById(R.id.container));

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        studentRepo = adapter.createRepository(StudentRepository.class);
//        instructorRepo = adapter.createRepository(InstructorRepository.class);

        mListView = (ListView) findViewById(R.id.listViewStudents);

//        Instructor user = Constant.INSTRUCTOR;
        studentRepo.findAll(new ListCallback<Student>() {
            @Override
            public void onSuccess(List<Student> all) {
                StudentAdapter adapter = new StudentAdapter(InstructorHomeActivity.this, all);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("onError findAll", t.toString());
            }
        });

    }

    public void logout(View view) {
        instructorRepo.logout(new VoidCallback() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("on error", "logout");
            }
        });
    }

//    private List<Student> genererStudents(){
//        List<Student> students = new ArrayList<Student>();
//        students.add(new Student("", "153A rue Cardinet", "Florent", "Philippon", "0102030405", ""));
//        students.add(new Student("", "19 avenue Jean Jaur�s", "Jean", "Dupont", "0899451369", ""));
//        students.add(new Student("", "175 rue Cardinet", "Jacques", "Dutreix", "0714236541", ""));
//        students.add(new Student("", "15 rue Clichy", "Luc", "Michelin", "0555041082", ""));
//        students.add(new Student("", "39 bis boulevard des Batignolles", "Thomas", "Hammerschmidt", "0987652412", ""));
//        students.add(new Student("", "153A rue des Clochards", "Julie", "Renaud", "0659724047", ""));
//        return students;
//    }

//    public void capturePicture(View view) {
//        startActivity(new Intent(getApplicationContext(), CaptureActivity.class));
//        finish();
//    }
}
