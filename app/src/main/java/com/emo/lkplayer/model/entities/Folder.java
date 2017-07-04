package com.emo.lkplayer.model.entities;

/**
 * Created by shoaibanwar on 6/20/17.
 */

public final class Folder {

    private String path;
    private int countFiles;
    private String diplayName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public void setCountFiles(int countFiles) {
        this.countFiles = countFiles;
    }

    public String getDiplayName() {
        return diplayName;
    }

    public void setDiplayName(String diplayName) {
        this.diplayName = diplayName;
    }

}
