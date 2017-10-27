package com.example.harshit.apiuse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by harshit on 27/10/17.
 */

class FestivalModel {
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("place")
    @Expose
    private String place;


    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;









}
