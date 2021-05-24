package com.shyj.jianshen.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class UsersBean extends LitePalSupport{

    @Column(nullable = false,unique = true)
    private int sex;
    @Column(unique = true, nullable = false)
    private String sexS;
    @Column(nullable = false)
    private int purpose;
    @Column(nullable = false)
    private String goal;
    @Column(nullable = false)
    private int bodyParts;
    @Column(nullable = false)
    private String focusParts;
    @Column(nullable = false)
    private int grade;
    @Column(nullable = false)
    private String level;
    @Column(nullable = true)
    private String height;
    @Column(nullable = true)
    private String weight;


    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getFocusParts() {
        return focusParts;
    }

    public void setFocusParts(String focusParts) {
        this.focusParts = focusParts;
    }

    public String getSexS() {
        return sexS;
    }

    public void setSexS(String sexS) {
        this.sexS = sexS;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setBodyParts(int bodyParts) {
        this.bodyParts = bodyParts;
    }

    public int getBodyParts() {
        return bodyParts;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return weight;
    }
}
