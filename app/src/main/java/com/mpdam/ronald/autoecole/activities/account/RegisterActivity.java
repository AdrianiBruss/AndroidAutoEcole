package com.mpdam.ronald.autoecole.activities.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private StudentRepository studentRepo;

    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private Button registerButton;
    private EditText registerUsername;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerAddress;
    private EditText registerPhone;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        RestAdapter adapter = new RestAdapter(getApplicationContext(), Constant.URL);
        studentRepo = adapter.createRepository(StudentRepository.class);


        mImageView          = (ImageView) findViewById(R.id.imageViewCapture);
        registerButton      = (Button) findViewById(R.id.registerButton);
        registerUsername    = (EditText) findViewById(R.id.registerUsername);
        registerEmail       = (EditText) findViewById(R.id.registerEmail);
        registerPassword    = (EditText) findViewById(R.id.registerPassword);
        registerAddress     = (EditText) findViewById(R.id.registerAddress);
        registerPhone       = (EditText) findViewById(R.id.registerPhone);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username     = registerUsername.getText().toString();
                String email        = registerEmail.getText().toString();
                String password     = registerPassword.getText().toString();
                String address      = registerAddress.getText().toString();
                String phone        = registerPhone.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter your username and your password", Toast.LENGTH_SHORT).show();
                } else {

                    Student newStudent = studentRepo.createUser( email, password,
                            new Student().setData( username, address, username, "", phone, encodedImage));

                    newStudent.save(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Log.e("message", "User created !");
                            startActivity(new Intent(getApplicationContext(), InstructorHomeActivity.class));
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.e("message", t.toString());
                        }
                    });
                }

            }
        });

    }

//    private void userCreation(String username, String email, String password, String address, String phone) {
//
//        Log.e("username", username);
//        Log.e("email", email);
//        Log.e("password", password);
//        Log.e("address", address);
//        Log.e("phone", phone);
//
////        User newStudent = studentRepo.createUser(
////                email, password,
////                new Student().setData(username, address, "", "", phone, ""));
//
//
//    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        Log.e("message", "createImageFile");
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
////        galleryAddPic();
//        return image;
//    }

//    private void galleryAddPic() {
//        Log.e("message", "sauvegarde de la photo dans la gallerie");
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        // ici il ya l'image qu'on peut récuperer
//        Log.e("contentUri", String.valueOf(contentUri));
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }

    public void capturePicture(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                Log.e("error", "Eroor Capturing picture");
//            }
//            if (photoFile != null) {
//
//                Log.e("photoFile", String.valueOf(photoFile));
//
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.mpdam.ronald.autoecole.fileprovider",
//                        photoFile);
//
//                Log.e("photoURI", String.valueOf(photoURI));
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//
//                setResult(RESULT_OK, takePictureIntent);
//
//
//
//            }
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("requestCode", String.valueOf(REQUEST_IMAGE_CAPTURE));
        Log.e("resultCode", String.valueOf(RESULT_OK));

        Log.e("meaggse", String.valueOf(data));


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (data != null) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                encodedImage = encodeImage(imageBitmap);

                Log.e("data != null", encodedImage);

                mImageView.setImageBitmap(imageBitmap);
            }
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }


}
