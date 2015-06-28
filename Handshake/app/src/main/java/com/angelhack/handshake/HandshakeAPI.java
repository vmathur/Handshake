package com.angelhack.handshake;

import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author curtiskroetsch
 */
public interface HandshakeAPI {

    @FormUrlEncoded 
    @POST("/user/signup")
    void signup(@Field("user_id") String userId,
                @Field("first_name") String fname,
                @Field("last_name") String lname,
                @Field("picture_url") String picUrl,
                @Field("tag_line") String tagline,
                Callback<Void> cb);

    @GET("/user/{id}/connections")
    void getConnections(@Path("id") String id, Callback<List<PersonProfile>> cb);

}
