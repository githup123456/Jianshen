package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class CourseActionBean extends LitePalSupport {
    /**id	字符串	动作唯一标识
     name	字符串	动作名称
     description	字符串	动作描述
     type	字符串	countdown 表示按时间做，times 表示按个数做
     pergroup	整型	需要做的个数
     duration	长整型	需要做的时间，单位（ms）
     gap	长整型	休息时长，单位（ms）
     mp4Time	长整型	文件播放时长，单位（ms）    * */

    @Column(unique = true)
    private String id;
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
    @Column(nullable = true)
    private long mp4Time;

    @Column(nullable = true)
    private CourseBean courseBean;

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
}
