package com.mpdam.ronald.autoecole.activities.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    private InstructorRepository instructorRepo;
    private StudentRepository studentRepo;

    private RestAdapter adapter;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Button loginBtn;
    private EditText loginUsername, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("AUTHENTICATION_DATA", Context.MODE_PRIVATE);
        editor = preferences.edit();

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
                Constant.INSTRUCTOR = currentInstructor;
                Constant.CURRENT_USER = "INSTRUCTOR";
                editor.putString("Authentication_Token", token.getId().toString());
                editor.apply();

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
                Constant.STUDENT = currentStudent;
                Constant.CURRENT_USER = "STUDENT";
                editor.putString("Authentication_Token", token.getId().toString());
                editor.apply();

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

}
