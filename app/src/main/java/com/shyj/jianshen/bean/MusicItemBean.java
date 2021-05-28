package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public  class MusicItemBean extends LitePalSupport {

    @Column(unique = true, index = true)
    private long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String musicFile;
    @Column(nullable = false)
    private String musicBg;

    @Column(defaultValue = "false")
    private boolean isDownload;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMusicBg() {
        return musicBg;
    }

    public void setMusicBg(String musicBg) {
        this.musicBg = musicBg;
    }

    public void setMusicFile(String musicFile) {
        this.musicFile = musicFile;
    }

    public String getMusicFile() {
        return musicFile;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public boolean isDownload() {
        return isDownload;
    }


}
