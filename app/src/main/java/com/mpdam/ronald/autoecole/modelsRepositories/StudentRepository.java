package com.mpdam.ronald.autoecole.modelsRepositories;

import com.mpdam.ronald.autoecole.models.Student;

/**
 * Created by Ronald on 11/08/2016.
 */
public class StudentRepository extends com.strongloop.android.loopback.UserRepository<Student> {

    public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback<Student>
    {
    }

    public StudentRepository() {
        super("student", Student.class);
    }

}
