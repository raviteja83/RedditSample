package com.rt12148.redditsample;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyApplication extends Application {

    private static MyApplication instance;
    private Retrofit retrofit;
    private ApiService apiService;
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/r/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Realm.init(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public ApiService getRetrofit() {
        if(null == apiService) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public Realm getRealm() {
        if(realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
