package com.rt12148.redditsample;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rt12148 on 23/3/17.
 **/

interface ApiService {
    @GET("{subreddit}/.json")
    Call<JsonObject> getSubreddit(@Path("subreddit") String subreddit);
}
