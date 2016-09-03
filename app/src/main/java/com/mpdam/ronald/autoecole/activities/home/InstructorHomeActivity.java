package com.mpdam.ronald.autoecole.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

public class InstructorHomeActivity extends AppCompatActivity {

    private InstructorRepository instructorRepo;

    private RestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_home);

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        instructorRepo = adapter.createRepository(InstructorRepository.class);


//        Log.e("current user", Constant.USER.toString());
    }

    public void Logout(View view) {
        instructorRepo.logout(new VoidCallback() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onError(Throwable t) {
                Log.e("on error", "logout");
            }
        });
    }
}
