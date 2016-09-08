package com.mpdam.ronald.autoecole.activities.account;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.activities.home.InstructorHomeActivity;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.InstructorRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.User;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private StudentRepository studentRepo;

    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private Button registerButton;
    private EditText registerUsername;
    private EditText registerFirstname;
    private EditText registerLastname;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerAddress;
    private EditText registerPhone;
    private RelativeLayout registerSplash;

    private String encodedImage;
    private Uri photoURI;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        RestAdapter adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        studentRepo = adapter.createRepository(StudentRepository.class);


        mImageView          = (ImageView) findViewById(R.id.imageCaptured);
        registerButton      = (Button) findViewById(R.id.registerButton);
        registerUsername    = (EditText) findViewById(R.id.registerUsername);
        registerFirstname   = (EditText) findViewById(R.id.registerFirstname);
        registerLastname    = (EditText) findViewById(R.id.registerLastname);
        registerEmail       = (EditText) findViewById(R.id.registerEmail);
        registerPassword    = (EditText) findViewById(R.id.registerPassword);
        registerAddress     = (EditText) findViewById(R.id.registerAddress);
        registerPhone       = (EditText) findViewById(R.id.registerPhone);
        registerSplash      = (RelativeLayout) findViewById(R.id.registerSplash);


        // Submit form
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerSplash.setVisibility(View.VISIBLE);

                String username     = registerUsername.getText().toString();
                String firstname    = registerFirstname.getText().toString();
                String lastname     = registerLastname.getText().toString();
                String email        = registerEmail.getText().toString();
                String password     = registerPassword.getText().toString();
                String address      = registerAddress.getText().toString();
                String phone        = registerPhone.getText().toString();


            // Check if email and password are not empty
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Please enter your username and your password", Toast.LENGTH_SHORT).show();
            } else {

                // Create new Student user method
                Student newStudent = studentRepo.createUser( email, password,
                        new Student().setData( username, address, firstname, lastname, phone, Constant.encodedImage));

                    // Save Student callback
                    newStudent.save(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            registerSplash.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
                        }

                        @Override
                        public void onError(Throwable t) {
                            registerSplash.setVisibility(View.GONE);
                            Log.e("message", t.toString());
                        }
                    });
                }

            }

        });

    }

    // Call Camera Intent
    public void capturePicture(View view) {
        dispatchTakePictureIntent();
    }

    
    private void dispatchTakePictureIntent() {

        // Init camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("error", "Error occurred while creating the File");
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.mpdam.ronald.autoecole.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Constant.PhotoURI = photoURI;
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    // Callback called when picture has been shot
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            photoURI = Constant.PhotoURI;

            try {

                // Getting the bitmap image from URI
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                //Scaling down the bitmap
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 640, 360, false);

                // Rotate Image
                Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, 640, 360, matrix, true);

                // Converting bitmap to base46
                encodedImage = bitmapToBase64(rotatedBitmap);

                Constant.encodedImage = encodedImage;

                mImageView.setBackgroundResource(R.drawable.takepictureok);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


}
