package com.mpdam.ronald.autoecole.activities.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mpdam.ronald.autoecole.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private ImageView mImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mImageView = (ImageView) this.findViewById(R.id.imageViewCapture);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        Log.e("message", "createImageFile");
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
//        galleryAddPic();
        return image;
    }
    private void galleryAddPic() {
        Log.e("message", "sauvegarde de la photo dans la gallerie");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        // ici il ya l'image qu'on peut récuperer
        Log.e("contentUri", String.valueOf(contentUri));
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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


            if(data != null)
            {

                Log.e("message", "data != null");

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                mImageView.setImageBitmap(imageBitmap);
            }
        }
    }


    public void capturePicture(View view) {
        dispatchTakePictureIntent();
    }

}
