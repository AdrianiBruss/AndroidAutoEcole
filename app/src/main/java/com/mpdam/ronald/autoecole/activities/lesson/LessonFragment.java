package com.mpdam.ronald.autoecole.activities.lesson;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mpdam.ronald.autoecole.R;
import com.mpdam.ronald.autoecole.adapters.LessonAdapter;
import com.mpdam.ronald.autoecole.adapters.StudentAdapter;
import com.mpdam.ronald.autoecole.models.Lesson;
import com.mpdam.ronald.autoecole.models.Student;
import com.mpdam.ronald.autoecole.modelsRepositories.LessonRepository;
import com.mpdam.ronald.autoecole.modelsRepositories.StudentRepository;
import com.mpdam.ronald.autoecole.utils.Constant;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.remoting.adapters.Adapter;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class LessonFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private List<Lesson> lessons;

    private ListView listView;

    public static LessonFragment newInstance(ArrayList lessons) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, lessons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            lessons = (List<Lesson>) getArguments().get(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        listView = (ListView) view.findViewById(R.id.listViewLessons);

        LessonAdapter ladapter = new LessonAdapter(getContext(), lessons);
        listView.setAdapter(ladapter);

        return view;
    }

}
