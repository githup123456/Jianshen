package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class CourseActionBean extends LitePalSupport implements Serializable {
    /**id	字符串	动作唯一标识
     name	字符串	动作名称
     description	字符串	动作描述
     type	字符串	countdown 表示按时间做，times 表示按个数做
     pergroup	整型	需要做的个数
     duration	长整型	需要做的时间，单位（ms）
     gap	长整型	休息时长，单位（ms）
     mp4Time	长整型	文件播放时长，单位（ms）    * */

    @Column(ignore = true)
    private String id;
    @Column(nullable = false)
    private String actionID;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private int pergroup;
    @Column(nullable = false)
    private long gap;
    @Column(nullable = false)
    private long duration;
    @Column(nullable = false)
    private long mp4Time;
    @Column(nullable = false)
    private String actionFile;

    @Column(nullable = true)
    private CourseBean courseBean;

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPergroup(int pergroup) {
        this.pergroup = pergroup;
    }

    public int getPergroup() {
        return pergroup;
    }

    public void setGap(long gap) {
        this.gap = gap;
    }

    public long getGap() {
        return gap;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setMp4Time(long mp4Time) {
        this.mp4Time = mp4Time;
    }

    public long getMp4Time() {
        return mp4Time;
    }

    public void setCourseBean(CourseBean courseBean) {
        this.courseBean = courseBean;
    }

    public CourseBean getCourseBean() {
        return courseBean;
    }

    public String getActionFile() {
        return actionFile;
    }

    public void setActionFile(String actionFile) {
        this.actionFile = actionFile;
    }

    @Override
    public String toString() {
        return "CourseActionBean{" +
                "id='" + id + '\'' +
                ", actionID='" + actionID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", pergroup=" + pergroup +
                ", gap=" + gap +
                ", duration=" + duration +
                ", mp4Time=" + mp4Time +
                ", courseBean=" + courseBean +
                '}';
    }
}
