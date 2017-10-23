package com.coder.mvpframe.activity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by feng on 2017/10/23.
 */
public interface LoginModel {

    @POST("user")
    @FormUrlEncoded
    Call<Gson> login(@Field(value = "account") String account, @Field("password") String password);
}
