package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class CalorieMouthBean extends LitePalSupport {

    @Column(nullable = false)
    private String day;
    @Column(nullable = false)
    private float calorie;
    @Column(nullable = true)
    private String mouth;

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getMouth() {
        return mouth;
    }
}
