package com.freelancer.tvprograme2;

import android.content.Context;

import com.freelancer.tvprograme2.dto.TvProgramme;
import com.google.gson.Gson;

import okhttp3.CacheControl;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android 18 on 4/22/2016.
 */
public class ProgrammeRestController {
    static  final String URL = "https://whatsbeef.net/wabz/guide.php?";

    static final TvProgrammeGetInterface mTvProgrammeGetInterface = MainActivity.retrofit.create(TvProgrammeGetInterface.class);

    public static void getTvProgramme(Context context, int start, final OnDownloadDataCallback callback){

        //check first if there is cached data.
        try {
            TvProgramme cachedTvProgramme = getProgrammeFromCache(context, start);
            if(cachedTvProgramme != null){
                callback.onDownloadSuccess(cachedTvProgramme);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //if no cached data exists, start manual download from the url
        Call<TvProgramme> call = mTvProgrammeGetInterface.getTvProgramme(start);
        Callback<TvProgramme> tvProgrammeCallback = new Callback<TvProgramme>() {
            @Override
            public void onResponse(Call<TvProgramme> call, Response<TvProgramme> response) {
                callback.onDownloadSuccess(response.body());
            }

            @Override
            public void onFailure(Call<TvProgramme> call, Throwable t) {
                callback.onDownloadFailure();
            }
        };
        call.enqueue(tvProgrammeCallback);
    }

    private interface TvProgrammeGetInterface{
        @GET(URL)
        Call<TvProgramme> getTvProgramme(@Query("start") int start);
    }


    public static TvProgramme getProgrammeFromCache(Context context, int start) throws Exception {
        String cachedUrl = URL.concat("start=").concat(String.valueOf(start));
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .onlyIfCached()
                        .build())
                .url(cachedUrl)
                .build();
        okhttp3.Response forceCacheResponse = MainActivity.okHttpClient.newCall(request).execute();
        if (forceCacheResponse.code() != 504) {
            // The resource was cached! Show it.
            String responseBody = forceCacheResponse.body().toString();
            TvProgramme programme = new Gson().fromJson(responseBody, TvProgramme.class);

            return programme;
        } else {
            // The resource was not cached.
            return null;
        }
    }

    public static interface OnDownloadDataCallback {
        public void onDownloadFailure();

        public void onDownloadSuccess(TvProgramme programme);
    }

}
