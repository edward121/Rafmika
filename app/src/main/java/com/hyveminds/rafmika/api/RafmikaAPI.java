package com.hyveminds.rafmika.api;

import com.hyveminds.rafmika.response.BaseResponse;
import com.hyveminds.rafmika.response.GetChallengeResponse;
import com.hyveminds.rafmika.response.GetEventResponse;
import com.hyveminds.rafmika.response.LoginResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by storm on 12/19/2016.
 */

public interface RafmikaAPI {
    String baseURL = "http://rafmika.com/";

    @GET("webservice.php?operation=getchallenge")
    Call<GetChallengeResponse> getChallengeDetail(@Query("username") String username);


    @FormUrlEncoded
    @POST("/webservice.php")
    Call<LoginResponse> login(@Field("operation") String operation, @Field("username") String username, @Field("accessKey") String accessKey);

    @GET("/webservice.php?operation=query")
    Call<GetEventResponse> getEventList(@Query("sessionName") String sessionName, @Query("query") String query);

    @FormUrlEncoded
    @POST("/webservice.php")
    Call<BaseResponse> update(@Field("operation") String operation, @Field("sessionName") String sessionName, @Field("element") String element);
}
