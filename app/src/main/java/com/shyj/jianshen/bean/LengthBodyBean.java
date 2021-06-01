package com.shyj.jianshen.bean;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class LengthBodyBean extends LitePalSupport {

    @Column(nullable = false)
    private String date;
    @Column(nullable = false,defaultValue = "0")
    private float lengthChest;
    @Column(nullable = false,defaultValue = "0")
    private float lengthHip;
    @Column(nullable = false,defaultValue = "0")
    private float lengthArm;
    @Column(nullable = false,defaultValue = "0")
    private float lengthWaist;
    @Column(nullable = false,defaultValue = "0")
    private float lengthThigh;
    @Column(nullable = false,defaultValue = "0")
    private float lengthCalf;





    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


    public float getLengthArm() {
        return lengthArm;
    }

    public void setLengthArm(float lengthArm) {
        this.lengthArm = lengthArm;
    }

    public float getLengthCalf() {
        return lengthCalf;
    }

    public void setLengthCalf(float lengthCalf) {
        this.lengthCalf = lengthCalf;
    }

    public float getLengthChest() {
        return lengthChest;
    }

    public void setLengthChest(float lengthChest) {
        this.lengthChest = lengthChest;
    }

    public float getLengthHip() {
        return lengthHip;
    }

    public void setLengthHip(float lengthHip) {
        this.lengthHip = lengthHip;
    }

    public float getLengthThigh() {
        return lengthThigh;
    }

    public void setLengthThigh(float lengthThigh) {
        this.lengthThigh = lengthThigh;
    }

    public float getLengthWaist() {
        return lengthWaist;
    }

    public void setLengthWaist(float lengthWaist) {
        this.lengthWaist = lengthWaist;
    }

    @Override
    public String toString() {
        return "LengthBodyBean{" +
                "date='" + date + '\'' +
                ", lengthChest=" + lengthChest +
                ", lengthHip=" + lengthHip +
                ", lengthArm=" + lengthArm +
                ", lengthWaist=" + lengthWaist +
                ", lengthThigh=" + lengthThigh +
                ", lengthCalf=" + lengthCalf +
                '}';
    }
}
