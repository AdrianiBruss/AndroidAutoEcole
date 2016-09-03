package com.mpdam.ronald.autoecole.activities.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final long TIMER = 2000;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // lancer l'ecran home activity

                // Intent
                Intent intent = new Intent(SplashActivity.this, ContactActivity.class);
                startActivity(intent);

//                finish(); ou noHistory dans le manifest
            }
        }, TIMER);
    }
}
