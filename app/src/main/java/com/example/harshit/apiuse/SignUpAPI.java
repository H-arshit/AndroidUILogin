package com.example.harshit.apiuse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by harshit on 27/10/17.
 */

public interface SignUpAPI {


    String BASE_URL = "http://192.168.43.182:3000/new/";


    @POST("user")
    Call<ResultData> getresult(@Body RequestData requestData);




}
