package com.emo.lkplayer.innerlayer.model.entities.retroModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class retro_JsonTrack {
    @SerializedName("track_id")
    @Expose
    public Integer trackId;
    @SerializedName("track_mbid")
    @Expose
    public String trackMbid;
    @SerializedName("track_isrc")
    @Expose
    public String trackIsrc;
    @SerializedName("track_spotify_id")
    @Expose
    public String trackSpotifyId;
    @SerializedName("track_soundcloud_id")
    @Expose
    public String trackSoundcloudId;
    @SerializedName("track_xboxmusic_id")
    @Expose
    public String trackXboxmusicId;
    @SerializedName("track_name")
    @Expose
    public String trackName;
    @SerializedName("track_name_translation_list")
    @Expose
    public List<Object> trackNameTranslationList = null;
    @SerializedName("track_rating")
    @Expose
    public Integer trackRating;
    @SerializedName("track_length")
    @Expose
    public Integer trackLength;
    @SerializedName("commontrack_id")
    @Expose
    public Integer commontrackId;
    @SerializedName("instrumental")
    @Expose
    public Integer instrumental;
    @SerializedName("explicit")
    @Expose
    public Integer explicit;
    @SerializedName("has_lyrics")
    @Expose
    public Integer hasLyrics;
    @SerializedName("has_subtitles")
    @Expose
    public Integer hasSubtitles;
    @SerializedName("num_favourite")
    @Expose
    public Integer numFavourite;
    @SerializedName("lyrics_id")
    @Expose
    public Integer lyricsId;
    @SerializedName("subtitle_id")
    @Expose
    public Integer subtitleId;
    @SerializedName("album_id")
    @Expose
    public Integer albumId;
    @SerializedName("album_name")
    @Expose
    public String albumName;
    @SerializedName("artist_id")
    @Expose
    public Integer artistId;
    @SerializedName("artist_mbid")
    @Expose
    public String artistMbid;
    @SerializedName("artist_name")
    @Expose
    public String artistName;
    @SerializedName("album_coverart_100x100")
    @Expose
    public String albumCoverart100x100;
    @SerializedName("album_coverart_350x350")
    @Expose
    public String albumCoverart350x350;
    @SerializedName("album_coverart_500x500")
    @Expose
    public String albumCoverart500x500;
    @SerializedName("album_coverart_800x800")
    @Expose
    public String albumCoverart800x800;
    @SerializedName("track_share_url")
    @Expose
    public String trackShareUrl;
    @SerializedName("track_edit_url")
    @Expose
    public String trackEditUrl;
    @SerializedName("commontrack_vanity_id")
    @Expose
    public String commontrackVanityId;
    @SerializedName("restricted")
    @Expose
    public Integer restricted;
    @SerializedName("first_release_date")
    @Expose
    public String firstReleaseDate;
    @SerializedName("updated_time")
    @Expose
    public String updatedTime;
}
