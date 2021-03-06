package com.emo.lkplayer.outerlayer.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.DynamicQueue;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.outerlayer.storage.daos.DBConstants;
import com.emo.lkplayer.outerlayer.storage.daos.DynamicQueueModelDao;
import com.emo.lkplayer.outerlayer.storage.daos.PlaylistModelDao;
import com.emo.lkplayer.outerlayer.storage.daos.PresetModelDao;
import com.emo.lkplayer.outerlayer.storage.daos.TypeConverter_IntegerArray;

@Database(entities = {EQPreset.UserDefPreset.class, Playlist.UserDefinedPlaylist.class, DynamicQueue.class}, version = 1,exportSchema = false)
@TypeConverters({TypeConverter_IntegerArray.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DBConstants.NAME_ROOM_DB)
                            .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract PresetModelDao presetModelDao();

    public abstract PlaylistModelDao playlistModelDao();

    public abstract DynamicQueueModelDao dynamicQueueModelDao();
}
