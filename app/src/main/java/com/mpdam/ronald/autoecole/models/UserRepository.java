package com.mpdam.ronald.autoecole.models;

public class UserRepository extends com.strongloop.android.loopback.UserRepository {

        public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback
        {

        }

        public UserRepository() {
                super("user", User.class);
        }

}
