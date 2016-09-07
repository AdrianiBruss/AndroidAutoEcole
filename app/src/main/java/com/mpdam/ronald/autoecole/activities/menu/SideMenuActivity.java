package com.mpdam.ronald.autoecole.activities.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.view.menu.MenuView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.account.LoginActivity;
import com.mpdam.ronald.autoecole.activities.account.RegisterActivity;
import com.mpdam.ronald.autoecole.utils.Constant;

public class SideMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView menuImageView;
    private TextView menuFirstname;
    private TextView menuLastname;
    private MenuItem navLesson;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        menuImageView   = (ImageView) headerLayout.findViewById(R.id.menuImageView);
        menuFirstname   = (TextView) headerLayout.findViewById(R.id.menuFirstname);
        menuLastname    = (TextView) headerLayout.findViewById(R.id.menuLastname);


        getUserForMenu();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.side_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_students) {
            // Handle the camera action
        } else if (id == R.id.nav_lessons) {

        } else if (id == R.id.nav_register) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            // logout
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserForMenu() {

        Log.e("Constant.STUDENT", String.valueOf(Constant.STUDENT));
        Log.e("Constant.INSTRUCTOR", String.valueOf(Constant.INSTRUCTOR));

        if ( Constant.CURRENT_USER == "STUDENT" ) {

            navigationView.getMenu().findItem(R.id.nav_register).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_students).setVisible(false);

            menuImageView.setImageBitmap(base64ToBitmap(Constant.STUDENT.get("picture").toString()));
            menuFirstname.setText(Constant.STUDENT.get("firstname").toString());
            menuLastname.setText(Constant.STUDENT.get("lastname").toString());

        } else if ( Constant.CURRENT_USER == "INSTRUCTOR" ) {

            navigationView.getMenu().findItem(R.id.nav_lessons).setVisible(false);

            menuImageView.setImageResource(R.drawable.studentpicture);
            menuFirstname.setText(Constant.INSTRUCTOR.get("firstname").toString());
            menuLastname.setText(Constant.INSTRUCTOR.get("lastname").toString());

        } else {
            Log.e("USER", "NULL");
        }

    }



    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}