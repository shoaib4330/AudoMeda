package com.emo.lkplayer.model.content_providers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.emo.lkplayer.model.entities.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/22/17.
 */

public final class FoldersProvider implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface MediaProviderEventsListener {
        void onListCreated(List<Folder> pFolderList);
    }

    //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private static final Uri MEDIA_FOLDERS_URI = MediaStore.Files.getContentUri("external");
    private static final int ID_LOADER_Folder = 0;

    private MediaProviderEventsListener mediaProviderEventsListener;
    private boolean dataRequestMade = false;
    private List<Folder> foldersList;

    private Context context;
    private LoaderManager loaderManager;

    /* shoaib: Old cursor, only kept to be given back to loader when we receive new cursor */
    private Cursor cursor;

    public FoldersProvider(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    public void requestFoldersData() {
        dataRequestMade = true;
        init();
    }

    private void init() {
        loaderManager.initLoader(ID_LOADER_Folder, null, this);
    }

    public void register(MediaProviderEventsListener mediaProviderEventsListener) {
        this.mediaProviderEventsListener = mediaProviderEventsListener;
        if (dataRequestMade)
            this.mediaProviderEventsListener.onListCreated(this.foldersList);
    }

    public void unRegister() {
        this.mediaProviderEventsListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{"COUNT(" + MediaStore.Files.FileColumns.DATA + ") AS totalFiles",
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME
                };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO +
                " OR "+ MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO +
                ") GROUP BY (" + MediaStore.Files.FileColumns.PARENT;

        String sortOrder = MediaStore.Files.FileColumns.DISPLAY_NAME + " ASC";

        CursorLoader cursorLoader = new CursorLoader(context, MEDIA_FOLDERS_URI, projection, selection, null, sortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        foldersList = new ArrayList<>();
        if (data.moveToFirst()) {
            Folder newFolder;
            do {
                String absFolderPath = data.getString(data.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String fileDisplayName = data.getString(data.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));

                newFolder = new Folder();
                newFolder.setPath(extractFolderPath(absFolderPath, fileDisplayName));
                newFolder.setDiplayName(extractFolderTitle(absFolderPath));
                newFolder.setCountFiles(data.getInt(data.getColumnIndex("totalFiles")));
                foldersList.add(newFolder);
            }
            while (data.moveToNext());
        }
        if (this.mediaProviderEventsListener != null)
            this.mediaProviderEventsListener.onListCreated(foldersList);
        /* swap the cursor */
        this.swapCursor(data, data = cursor);
    }

    private String extractFolderTitle(String absolutePathToFile) {
        File file = new File(new File(absolutePathToFile).getParent());
        String name = file.getName();
        return name;
    }

    private String extractFolderPath(String absolutePathToFile, String fileName) {
        int absPathLength = absolutePathToFile.length();
        int fileNameLength = fileName.length();
        String folderPathWithoutFileName = absolutePathToFile.substring(0, absPathLength - fileNameLength);
        return folderPathWithoutFileName;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null, null);
    }

    private void swapCursor(Cursor cursorNew, Cursor dummy) {
        /* shoaib: we keep reference of the old cursor, it will be swapped with new one */
        this.cursor = cursorNew;
    }

}
