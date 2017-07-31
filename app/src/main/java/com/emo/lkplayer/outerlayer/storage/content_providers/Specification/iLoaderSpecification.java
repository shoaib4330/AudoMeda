package com.emo.lkplayer.outerlayer.storage.content_providers.Specification;

import android.database.Cursor;
import android.net.Uri;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shoaibanwar on 6/25/17.
 */
public interface iLoaderSpecification<T> extends Serializable {
    Uri getUriForLoader();

    String[] getProjection();

    String getSelection();

    String[] getSelectionArgs();

    String getSortOrder();

    List<T> returnMappedList(Cursor cursor);
}


