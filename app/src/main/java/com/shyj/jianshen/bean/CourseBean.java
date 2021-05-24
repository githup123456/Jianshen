package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class CourseBean extends LitePalSupport {
    /**
     * id	字符串	课程唯一标识
     * name	字符串	课程名称
     * indexs	整型	课程序号
     * grade	整型	课程等级，分为1,2,3,4等级
     * duration	长整型	课程时长，不包括休息动作休息时长，单位（ms）
     * calorie	整型	预计消耗的卡路里，单位千卡
     * equipments	Array	字符串数组，需要的器械列表
     * actionTypes	Array	课程动作列表，见附录2
     */

    @Column(unique = true)
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, index = true)
    private int indexs;
    @Column(nullable = false)
    private int grade;
    @Column(nullable = false)
    private long duration;
    private int calorie;

    @Column(defaultValue = "false")
    private boolean isCompleted;
    @Column(defaultValue = "false")
    private boolean isCollect;

    private DaysCourseBean daysCourseBean;

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public boolean isCollect() {
        return isCollect;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


    private List<CourseActionBean> actionTypes = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIndexs(int indexs) {
        this.indexs = indexs;
    }

    public int getIndexs() {
        return indexs;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public void setActionTypes(List<CourseActionBean> actionTypes) {
        this.actionTypes = actionTypes;
    }

    public List<CourseActionBean> getActionTypes() {
        return actionTypes;
    }

    public void setDaysCourseBean(DaysCourseBean daysCourseBean) {
        this.daysCourseBean = daysCourseBean;
    }

    public DaysCourseBean getDaysCourseBean() {
        return daysCourseBean;
    }
}
