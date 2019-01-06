package com.droid.mobiotics.mobiotics;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ApiRequest {

    String Base_URL = "https://interview-e18de.firebaseio.com/";

    @GET("media.json")
    Call<List<Videos>> getDataFromVideos();

}
