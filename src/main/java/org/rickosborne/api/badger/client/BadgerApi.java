package org.rickosborne.api.badger.client;

import org.rickosborne.api.badger.data.User;
import retrofit.http.*;

import java.util.Collection;

public interface BadgerApi {

    public static final String PATH_USER = "/user";
    public static final String PARAM_USERID = "userId";
    public static final String PATH_ID = "/{userId}";
    public static final String PATH_CHECKIN = "/checkin";
    public static final String PATH_PATIENTS = "/patients";

    @GET(PATH_USER)
    public Collection<User> getUsers();

    @GET(PATH_USER + PATH_ID)
    public User getUserById(@Path(PARAM_USERID) long userId);

    @GET(PATH_USER + PATH_ID + PATH_PATIENTS)
    public Collection<User> getPatientsForUser(@Path(PARAM_USERID) long userId);

    @POST(PATH_USER)
    public User addUser(@Body User user);

    @PUT(PATH_USER + PATH_ID)
    public User updateUser(@Body User user);

    @POST(PATH_USER + PATH_ID + PATH_CHECKIN)
    public void userCheckin(@Path(PARAM_USERID) long userId);

}
