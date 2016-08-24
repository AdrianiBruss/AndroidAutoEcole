package com.mpdam.ronald.autoecole.activities.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.googleMap.LessonActivity;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button loginBtn;
    private EditText loginUsername, loginPassword;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        final RestAdapter adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        final InstructorRepository userRepo = adapter.createRepository(InstructorRepository.class);

//        userRepo.createUser("c@c.fr","ccc", new Instructor().setData("userC", "catherine", "", "")).save(new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                Log.e("message", "User created !");// Pencil now exists on the server!
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e("message", t.toString());// save failed, handle the error
//            }
//        });
//
//        userRepo.loginUser("b@b.fr", "konkak",
//
//                new InstructorRepository.LoginCallback() {
//
//                    @Override
//                    public void onSuccess(AccessToken token, Instructor currentUser) {
//                        Log.e("onSuccess",token.getUserId().toString());
//                    }
//
//                    @Override public void onError(Throwable t) {
//                        Log.e("onError",t.toString());
//                    }
//                }
//
//        );

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
//        userRepo.createObject(ImmutableMap.of("Modele", "Citroën","Marque", "C3")).save(new VoidCallback() {
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

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    public void goToMap(View view) {
        startActivity(new Intent(getApplicationContext(), LessonActivity.class));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
