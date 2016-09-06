package com.mpdam.ronald.autoecole.activities.account;

import android.content.Context;
import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.home.InstructorHomeActivity;
import com.mpdam.ronald.autoecole.activities.home.StudentHomeActivity;
import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.User;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

public class LoginActivity extends AppCompatActivity {

    private InstructorRepository instructorRepo;
    private StudentRepository studentRepo;

    private RestAdapter adapter;

    private Button loginBtn;
    private EditText loginUsername, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        instructorRepo = adapter.createRepository(InstructorRepository.class);
        studentRepo = adapter.createRepository(StudentRepository.class);

        loginBtn = (Button) findViewById(R.id.loginButton);
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);


        //Login click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = loginUsername.getText().toString();
                String password = loginPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter your username and your password", Toast.LENGTH_SHORT).show();
                } else {
                    instructorConnection(username, password, getApplicationContext());
                }

            }
        });

    }

    protected void instructorConnection(final String id, final String pass, final Context context){

        instructorRepo.loginUser(id , pass , new InstructorRepository.LoginCallback(){

            @Override
            public void onSuccess(AccessToken token, Instructor currentInstructor) {
                Log.e("token", token.toString());
                Constant.INSTRUCTOR = currentInstructor;

                Intent intent = new Intent(context, InstructorHomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable t) {
                studentConnection(id, pass, context);
            }
        });

    }

    protected void studentConnection(String id, String pass, final Context context){
        
        studentRepo.loginUser(id , pass , new StudentRepository.LoginCallback(){
            @Override
            public void onSuccess(AccessToken token, Student currentStudent) {
                Log.e("token", token.toString());
                Constant.STUDENT = currentStudent;

                Intent intent = new Intent(context, StudentHomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("error",t.toString());
                Toast.makeText(context, "Sorry but login fails... Please check your username and password", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void goToRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

}
