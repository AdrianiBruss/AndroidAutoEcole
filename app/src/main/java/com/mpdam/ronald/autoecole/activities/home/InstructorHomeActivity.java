package com.mpdam.ronald.autoecole.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.capture.CaptureActivity;
import com.mpdam.ronald.autoecole.activities.googleMap.LessonActivity;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

public class InstructorHomeActivity extends SideMenuActivity {

    private InstructorRepository instructorRepo;

    private RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_instructor_home);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_instructor_home, (ViewGroup) findViewById(R.id.container));

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        instructorRepo = adapter.createRepository(InstructorRepository.class);


//        Log.e("current user", Constant.USER.toString());
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

    public void goToLesson(View view) {
        startActivity(new Intent(getApplicationContext(), LessonActivity.class));
        finish();
    }
}
