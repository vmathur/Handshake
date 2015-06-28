package com.angelhack.handshake;

import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author curtiskroetsch
 */
public interface HandshakeAPI {

    @GET("/user/signup")
    void signup(@Body PersonProfile profile, Callback<Void> cb);

    @POST("/user/{id}/connections")
    void getConnections(@Path("id") String id, Callback<List<PersonProfile>> cb);

}
