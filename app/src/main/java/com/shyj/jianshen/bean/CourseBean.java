package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseBean extends LitePalSupport implements Serializable {
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

    @Column(ignore = true)
    private String id;
    @Column(nullable = false)
    private String courseID;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int indexs;
    @Column(nullable = false)
    private int grade;
    @Column(nullable = false)
    private long duration;
    @Column(nullable = false)
    private int calorie;
    @Column(nullable = false)
    private String bgUrl;
    @Column(nullable = false,defaultValue = "0")
    private int days;

    @Column(defaultValue = "false")
    private boolean isCompleted;
    @Column(defaultValue = "false")
    private boolean isCollect;

    @Column(nullable = true)
    private String[] equipments;

    private DaysCourseBean daysCourseBean;

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
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

    public void setEquipments(String[] equipments) {
        this.equipments = equipments;
    }

    public String[] getEquipments() {
        return equipments;
    }


    @Override
    public String toString() {
        return "CourseBean{" +
                "id='" + id + '\'' +
                ", courseID='" + courseID + '\'' +
                ", name='" + name + '\'' +
                ", indexs=" + indexs +
                ", grade=" + grade +
                ", duration=" + duration +
                ", calorie=" + calorie +
                ", bgUrl='" + bgUrl + '\'' +
                ", days=" + days +
                ", isCompleted=" + isCompleted +
                ", isCollect=" + isCollect +
                ", equipments=" + Arrays.toString(equipments) +
                ", daysCourseBean=" + daysCourseBean +
                ", actionTypes=" + actionTypes +
                '}';
    }
}
