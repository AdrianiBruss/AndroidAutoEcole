package com.mpdam.ronald.autoecole.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.models.Lesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ronald on 04/09/2016.
 */
public class LessonAdapter extends ArrayAdapter<Lesson> {

    //lessons est la liste des models à afficher
    public LessonAdapter(Context context, List<Lesson> lessons) {
        super(context, 0, lessons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_lesson,parent, false);
        }

        LessonViewHolder viewHolder = (LessonViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new LessonViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.textViewLesson);
            viewHolder.date = (TextView) convertView.findViewById(R.id.textViewDate);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Lesson> lessons
        Lesson lesson = getItem(position);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(lesson.date);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText("Cours");
        viewHolder.date.setText(date);

        return convertView;
    }

    private class LessonViewHolder{
        public TextView title;
        public TextView date;
    }

}
