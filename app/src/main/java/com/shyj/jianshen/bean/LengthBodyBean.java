package com.shyj.jianshen.bean;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class LengthBodyBean extends LitePalSupport {
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private float length;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
