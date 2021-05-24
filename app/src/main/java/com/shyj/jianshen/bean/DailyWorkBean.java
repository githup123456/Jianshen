package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class DailyWorkBean extends LitePalSupport {

    @Column(unique = true,nullable = false)
    private String date;
    @Column(defaultValue = "0",nullable = false)
    private int kcal;
    @Column(defaultValue = "0",nullable = false)
    private int duration;
    @Column(defaultValue = "0",nullable = false)
    private int completed;


    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getKcal() {
        return kcal;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCompleted() {
        return completed;
    }
}
