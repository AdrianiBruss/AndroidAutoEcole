package com.mpdam.ronald.autoecole.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.models.Student;

import java.util.List;

/**
 * Created by Ronald on 04/09/2016.
 */
public class StudentAdapter extends ArrayAdapter<Student> {

    //students est la liste des models à afficher
    public StudentAdapter(Context context, List<Student> students) {
        super(context, 0, students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_student,parent, false);
        }

        StudentViewHolder viewHolder = (StudentViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StudentViewHolder();
            viewHolder.firstname = (TextView) convertView.findViewById(R.id.textViewFirstname);
            viewHolder.lastname = (TextView) convertView.findViewById(R.id.textViewLastname);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.textViewPhone);
            viewHolder.address = (TextView) convertView.findViewById(R.id.textViewAddress);
//            viewHolder.photo = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Student> students
        Student student = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.firstname.setText(student.get("firstname").toString());
        viewHolder.lastname.setText(student.get("lastname").toString());
        viewHolder.phone.setText(student.get("phone").toString());
        viewHolder.address.setText(student.get("address").toString());
//        viewHolder.picture.setImageDrawable(new ColorDrawable(student.getColor()));

        return convertView;
    }

    private class StudentViewHolder{
        public TextView firstname;
        public TextView lastname;
        public TextView phone;
        public TextView address;
//        public ImageView picture;
    }

}
