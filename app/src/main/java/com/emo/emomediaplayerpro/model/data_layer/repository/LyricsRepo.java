package com.emo.emomediaplayerpro.model.data_layer.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.emo.emomediaplayerpro.model.domain.retro_entities.retro_LyricsResponse;
import com.emo.emomediaplayerpro.model.data_layer.MusixMatchApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LyricsRepo {

    private static final String BASE_URL = "http://api.musixmatch.com/ws/1.1/";
    private Retrofit retrofit;
    private MusixMatchApi musixMatchApi;
    private Call callLyricSearch;

    private LiveData<String> observable_Lyrics = new MutableLiveData<>();

    public LyricsRepo()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        musixMatchApi = retrofit.create(MusixMatchApi.class);
    }

    public LiveData<String> getLyrics (String trackTitle,String trackArtist)
    {
        callLyricSearch = musixMatchApi.getLyrics(trackTitle,trackArtist);
        callLyricSearch.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response)
            {
                Log.d("response",response.message());
                retro_LyricsResponse lyricsResponse = (retro_LyricsResponse) response.body();
                Log.d("response",lyricsResponse.message.body.lyrics.lyricsBody);
                Log.d("response",response.toString());
                ((MutableLiveData)observable_Lyrics).setValue(lyricsResponse.message.body.lyrics.lyricsBody);
            }

            @Override
            public void onFailure(Call call, Throwable t)
            {
                ((MutableLiveData)observable_Lyrics).setValue("Lyrics not available...");
            }
        });
        return observable_Lyrics;
    }
}
