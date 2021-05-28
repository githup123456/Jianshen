package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class PlanBean extends LitePalSupport {

    /*
    id	字符串	计划唯一标识
name	字符串	计划名称
mark	字符串	计划标识，封面图使用
grade	整型	计划等级，分为1,2,3等级
dayNum	整型	计划天数
isVip	整型	是否需要VIP，1需要，默认1
courses	Array	每天的课程列表，见附录2
   */

    @Column(ignore = true)
    private String id;
    @Column(nullable = false)
    private String plan;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String mark;
    @Column(nullable = false)
    private int grade;
    @Column(nullable = false)
    private int dayNum;
    @Column(nullable = false)
    private int isVip;


    private List<DaysCourseBean> courses = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlanId(String planId) {
        this.plan = planId;
    }

    public String getPlanId() {
        return plan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsVip() {
        return isVip;
    }



    public List<DaysCourseBean> getCourses() {
        return courses;
    }

    public void setCourses(List<DaysCourseBean> courses) {
        this.courses = courses;
    }
}
