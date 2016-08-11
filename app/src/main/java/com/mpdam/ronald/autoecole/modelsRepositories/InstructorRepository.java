package com.mpdam.ronald.autoecole.modelsRepositories;

import com.mpdam.ronald.autoecole.models.Instructor;

/**
 * Created by Ronald on 28/07/2016.
 */
public class InstructorRepository extends com.strongloop.android.loopback.UserRepository<Instructor> {

    public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback<Instructor>
    {
    }

    public InstructorRepository() {
        super("instructor", Instructor.class);
    }

}
