package com.shyj.jianshen.utils;

import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.bean.DaysCourseBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xhu_ww on 2018/6/1.
 * description:
 */
public class DateUtil {

    public static String formatDateToMD(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM/dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String formatDateToYMD(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy/MM/dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String  MouthToEnglish(int mouth){
        String mouthString = "Jan";
        switch (mouth){
            case 1:
                mouthString = "Jan";
                break;
            case 2:
                mouthString = "Feb";
                break;
            case 3:
                mouthString = "Mar";
                break;
            case 4:
                mouthString = "Apr";
                break;
            case 5:
                mouthString = "May";
                break;
            case 6:
                mouthString = "Jun";
                break;
            case 7:
                mouthString = "Jul";
                break;
            case 8:
                mouthString = "Aug";
                break;
            case 9:
                mouthString = "Sept";
                break;
            case 10:
                mouthString = "Oct";
                break;
            case 11:
                mouthString = "Nov";
                break;
            case 12:
                mouthString = "Dec";
                break;
        }
        return mouthString;
    }

    /** 获取计划天数列表*/
    public static List<String>  getPlanDayList(List<DaysCourseBean> daysCourseBeanList){
        List<String> dayList = new ArrayList<>();
        if (daysCourseBeanList!=null && daysCourseBeanList.size()>0){
            for (int i = 0;i<daysCourseBeanList.size();i++){
                DaysCourseBean daysCourseBean = daysCourseBeanList.get(i);
                int day = daysCourseBean.getDay();
                int mouthDays = getNowMouthDays();
                dayList.add(day%mouthDays+"");
            }
        }
        return dayList;
    }

    /** 获取 计划对应的日期*/
    public static String getPlanDate(int day){
        int[] date = getNowDate();
        //年
        int year = date[0];
        //月
        int month = date[1];
        int nowDay = date[2]+day;
        if (nowDay>getNowMouthDays()){
            nowDay = nowDay % getNowMouthDays();
            String planDate = year+""+(month+1)+""+ nowDay + "";
            return planDate;
        }else {
            return year+""+month+""+ nowDay + "";
        }
    }

    // todo 获取当前月份天数
    public static int getNowMouthDays(){
        int[] date = getNowDate();
        //年
        int year = date[0];
        //月
        int month = date[1];
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (year%4==0){
                    return 29;
                }else {
                    return 28;
                }
            default:return 31;
        }
    }

    /**
     * 获取当前日期
     * */
    public static int[] getNowDate(){
        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{year,month,day};
    }
}
