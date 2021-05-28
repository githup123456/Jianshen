package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class DaysCourseBean extends LitePalSupport {

    @Column(unique = true)
    private int id;

    @Column(index = true, nullable = false)
    private int day;

    @Column(nullable = false)
    private String Date;

    @Column(nullable = true)
    private PlanBean planBean;
    @Column(nullable = false)
    private int weekDay;
    @Column(nullable = false)
    private int mouthDay;
    private List<CourseBean> courseList = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public PlanBean getPlanBean() {
        return planBean;
    }

    public void setPlanBean(PlanBean planBean) {
        this.planBean = planBean;
    }

    public void setCourseList(List<CourseBean> courseList) {
        this.courseList = courseList;
    }

    public List<CourseBean> getCourseList() {
        return courseList;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public int getMouthDay() {
        return mouthDay;
    }

    public void setMouthDay(int mouthDay) {
        this.mouthDay = mouthDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
}
