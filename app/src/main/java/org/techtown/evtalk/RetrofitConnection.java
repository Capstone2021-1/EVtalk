package org.techtown.evtalk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnection {
    String URL = "http://ec2-3-35-11-225.ap-northeast-2.compute.amazonaws.com";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Service server = retrofit.create(Service.class);
}
