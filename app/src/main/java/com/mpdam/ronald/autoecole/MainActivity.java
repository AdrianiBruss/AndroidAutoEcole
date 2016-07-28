package com.mpdam.ronald.autoecole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.mpdam.ronald.autoecole.models.Instructor;
import com.mpdam.ronald.autoecole.models.InstructorRepository;
import com.mpdam.ronald.autoecole.models.UserRepository;
import com.mpdam.ronald.autoecole.models.User;
import com.mpdam.ronald.autoecole.models.VoitureRepository;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.RestAdapter;


public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText loginUsername, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RestAdapter adapter = new RestAdapter(getApplicationContext(), "http://autoecoleandroid-cloudbruss.rhcloud.com/api");
//        final VoitureRepository repository = adapter.createRepository(VoitureRepository.class);
        final InstructorRepository userRepo = adapter.createRepository(InstructorRepository.class);

        userRepo.loginUser("instructor@inscructor.com", "instructor",

                new InstructorRepository.LoginCallback() {

                    @Override
                    public void onSuccess(AccessToken token, Instructor currentUser) {
                        Log.e("onSuccess",token.getUserId().toString());
                    }

                    @Override public void onError(Throwable t) {
                        Log.e("onError",t.toString());
                    }
                }

        );

//        loginBtn = (Button) findViewById(R.id.loginButton);
//        loginUsername = (EditText) findViewById(R.id.loginUsername);
//        loginPassword = (EditText) findViewById(R.id.loginPassword);
//
//        //Login click listener
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String username = loginUsername.getText().toString();
//                String password = loginPassword.getText().toString();
//
//                final UserRepository userRepo = adapter.createRepository(UserRepository.class);
//
//                System.out.println(username + " : " + password);
//
//                if(username != "" && password != "")
//                {
//                    userRepo.loginUser(username , password , new UserRepository.LoginCallback<ser>() {
//                        @Override
//                        public void onSuccess(AccessToken token, User currentUser) {
////                            Intent goToMain = new Intent(getApplicationContext(), Main.class);
////                            startActivity(goToMain);
////                            finish();
//                                System.out.println(token.getUserId() + ":" + currentUser.getId());
//                        }
//
//                        @Override
//                        public void onError(Throwable t) {
//                            Log.e("ErrorMessage", "Login E", t);
//                        }
//                    });
//                }
//
//            }
//        });

//
//
//        repository.createObject(ImmutableMap.of("Modele", "Citroën","Marque", "C3")).save(new VoidCallback() {
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
}
