package com.mpdam.ronald.autoecole.activities.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.models.Student;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Student student;

    private LinearLayout linearLayoutProfile;
    private TextView textViewFirstname, textViewLastname, textViewPhone, textViewAddress, textViewHours;


    public static ProfileFragment newInstance(Student user)
    {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            student = (Student) getArguments().get(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        linearLayoutProfile = (LinearLayout) view.findViewById(R.id.linearLayoutProfile);

        textViewFirstname = (TextView) view.findViewById(R.id.textViewFirstname);
        textViewLastname = (TextView) view.findViewById(R.id.textViewLastname);
        textViewPhone = (TextView) view.findViewById(R.id.textViewPhone);
        textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
        textViewHours = (TextView) view.findViewById(R.id.textViewHours);

        if(student != null)
        {

            if ( student.get("picture") != null  ) {

                Bitmap imageBitmap = base64ToBitmap(student.get("picture").toString());
                BitmapDrawable background = new BitmapDrawable(imageBitmap);
                linearLayoutProfile.setBackgroundDrawable(background);

            } else {

                linearLayoutProfile.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.studentpicture, null));

            }


            textViewFirstname.setText(student.get("firstname").toString());
            textViewLastname.setText(student.get("lastname").toString());
            textViewPhone.setText(student.get("phone").toString());
            textViewAddress.setText(student.get("address").toString());
            if(student.get("nbHours") != null){
                textViewHours.setText(String.format("%s hours", student.get("nbHours").toString()));
            }
        }

        return view;
    }



    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}
