package com.mpdam.ronald.autoecole.activities.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.menu.SideMenuActivity;
import com.mpdam.ronald.autoecole.activities.main.MainActivity;

public class ContactActivity extends SideMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

    }

    public void goToMain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
