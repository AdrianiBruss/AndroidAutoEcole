package com.mpdam.ronald.autoecole.modelsRepositories;

import com.mpdam.ronald.autoecole.models.Lesson;
import com.strongloop.android.loopback.ModelRepository;

import java.io.Serializable;

/**
 * Created by Ronald on 11/08/2016.
 */
public class LessonRepository extends ModelRepository implements Serializable {

    public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback<Lesson>
    {
    }

    public LessonRepository() {
        super("lesson", Lesson.class);
    }
}
