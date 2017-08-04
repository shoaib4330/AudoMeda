package com.emo.lkplayer.outerlayer.storage.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by shoaibanwar on 8/2/17.
 */
@Dao
public interface PlaylistModelDao {

    @Query("select * from "+ DBConstants.TABLE_PLAYLIST_NAME)
    LiveData<List<Playlist.UserDefinedPlaylist>> getUserDefPlaylists();

    @Query("select * from "+ DBConstants.TABLE_PLAYLIST_NAME+ " where playlistName = :playlistName")
    Playlist.UserDefinedPlaylist getUserDefPlaylistbyName(String playlistName);

    @Insert(onConflict = REPLACE)
    void addNewPlaylist(Playlist.UserDefinedPlaylist playlist);

    @Delete
    void deletePlaylist(Playlist.UserDefinedPlaylist playlist);

}