package com.example.displayprogram.Network;

import com.example.displayprogram.Model.ModelClass;
import com.example.displayprogram.Network.Response.CheckPasswordResponse;
import com.example.displayprogram.Network.Response.ServerTimeResponse;
import com.example.displayprogram.Network.Response.TimeTableResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface ApiInterface {
    @FormUrlEncoded // annotation used in POST type requests
    @POST("/syncservertime")     // API's endpoints
    public void SyncServerTime(@Field("response") String name,
                             Callback<ServerTimeResponse> callback);

    @FormUrlEncoded // annotation used in POST type requests
    @POST("/CheckPassword")     // API's endpoints
    public void CheckPassword(@Field("tabletpassword") String name,
                               Callback<CheckPasswordResponse> callback);

    @FormUrlEncoded // annotation used in POST type requests
    @POST("/checktimetable")     // API's endpoints
    public void CheckTimeTable(@Field("tabletserialnumber") String name,
                               Callback<ArrayList<ModelClass>> callback);
}
