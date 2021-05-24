package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class DaysCourseBean extends LitePalSupport {

    @Column(index = true,nullable = false)
    private int day;

    @Column(unique = true)
    private String Date;

    private PlanBean planBean;

    private List<CourseBean> courseList = new ArrayList<>();

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
}
