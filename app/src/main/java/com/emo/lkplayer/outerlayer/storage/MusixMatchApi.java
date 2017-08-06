package com.emo.lkplayer.outerlayer.storage;



import com.emo.lkplayer.innerlayer.model.entities.retroModels.retro_LyricsResponse;
import com.emo.lkplayer.innerlayer.model.entities.retroModels.retro_TrackSearchResponse;

import retrofit2.http.*;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MusixMatchApi {

    @GET("track.search?")
    Call<retro_TrackSearchResponse> QueryTracksWithLyrics(@Query("q_track") String q_track, @Query("q_artist")String q_artist);

    //@GET("track.lyrics.get?")
    //Call<retro_LyricsResponse> getLyrics (@Query("track_id") Integer track_id);

    @GET("matcher.lyrics.get?apikey=59f3060c4ab34f175347668c1de73dde&")
    Call<retro_LyricsResponse> getLyrics (@Query("q_track") String q_track, @Query("q_artist")String q_artist);
}
