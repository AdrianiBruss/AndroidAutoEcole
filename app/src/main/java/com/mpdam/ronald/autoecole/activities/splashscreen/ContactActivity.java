package com.mpdam.ronald.autoecole.activities.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.main.MainActivity;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    public void goToMain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
