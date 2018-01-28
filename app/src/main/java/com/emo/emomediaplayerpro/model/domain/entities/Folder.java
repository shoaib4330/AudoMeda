package com.emo.emomediaplayerpro.model.domain.entities;

import java.io.Serializable;

public final class Folder implements Serializable{

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
