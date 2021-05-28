package com.shyj.jianshen.utils;

import android.util.Log;

import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.bean.DaysCourseBean;

import java.math.BigDecimal;
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
    public static List<Integer>  getPlanDayList(List<DaysCourseBean> daysCourseBeanList){
        List<Integer> dayList = new ArrayList<>();
        if (daysCourseBeanList!=null && daysCourseBeanList.size()>0){
            for (int i = 0;i<daysCourseBeanList.size();i++){
                DaysCourseBean daysCourseBean = daysCourseBeanList.get(i);
                int day = daysCourseBean.getDay()+getNowDate()[2]-1;
                int mouthDays = getNowMouthDays();
                if (day>mouthDays){
                    dayList.add(day%mouthDays);
                }else {
                    dayList.add(day);
                }

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
        int nowDay = date[2]+day-1;
        if (nowDay>getNowMouthDays()){
            nowDay = nowDay % getNowMouthDays();
            String planDate = "";
            if (month+1<10&&nowDay<10){
                planDate = year+"0"+(month+1)+"0"+ nowDay + "";
            }else if (month+1>=10&&nowDay<10){
                planDate = year+""+(month+1)+"0"+ nowDay + "";
            }else if (month+1<10&&nowDay>=10){
                planDate = year+"0"+(month+1)+""+ nowDay + "";
            }else {
                planDate = year+""+(month+1)+""+ nowDay + "";
            }
            return planDate;
        }else {
            String planDate= "";
            if (month<10&&nowDay<10){
                planDate = year+"0"+month+"0"+ nowDay + "";
            }else if (month>=10&&nowDay<10){
                planDate = year+""+month+"0"+ nowDay + "";
            }else if (month<10&&nowDay>=10){
                planDate = year+"0"+month+""+ nowDay + "";
            }else {
                planDate = year+""+month+""+ nowDay + "";
            }
            return planDate;
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
        //周几
        int we = calendar.get(Calendar.DAY_OF_WEEK)-1;
//        Log.e("dateUtil", "getNowDate: "+we );
        return new int[]{year,month,day,we};
    }

    public static String getNowStringDate(){
        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = "";
        if (month<10&&day<10){
            date = year+"0"+month+"0"+ day + "";
        }else if (month>=10&&day<10){
            date = year+""+month+"0"+ day + "";
        }else if (month<10&&day>=10){
            date = year+"0"+month+""+ day + "";
        }else {
            date = year+""+month+""+ day + "";
        }
        return date;
    }


    private final static int KB_UNIT = 1024;
    private final static int KB_MIN_UNIT = KB_UNIT / 10;
    private final static int MB_UNIT = 1024 * 1024;
    private final static int MB_MIN_UNIT = MB_UNIT / 10;
    private final static int SECOND_UNIT = 1000;
    private final static int MINUTE_UNIT = SECOND_UNIT * 60;
    private final static int HOUR_UNIT = MINUTE_UNIT * 60;
    private final static int DAY_UNIT = HOUR_UNIT * 24;

    /**
     * 把以毫秒为单位的时间转换为中文格式显示的时间
     *
     * @param timeInMills 以毫秒为单位的时间
     */
    public static String doTransform2ReadableTime(long timeInMills) {
        StringBuilder sb = new StringBuilder();
        if (timeInMills < MINUTE_UNIT) {
            final int seconds = (int) (timeInMills / SECOND_UNIT);
            sb.append(seconds).append("秒");
        } else if (timeInMills < HOUR_UNIT) {
            final int minutes = (int) (timeInMills / MINUTE_UNIT);
            final int seconds = (int) ((timeInMills - minutes * MINUTE_UNIT) / SECOND_UNIT);
            sb.append(minutes).append("分钟").append(seconds).append("秒");
        } else if (timeInMills < DAY_UNIT) {
            final int hours = (int) (timeInMills / HOUR_UNIT);
            final int minutes = (int) ((timeInMills - hours * HOUR_UNIT) / MINUTE_UNIT);
            if (minutes > 0) {
                sb.append(hours).append("小时").append(minutes).append("分钟");
            } else {
                sb.append(hours).append("小时");
            }
        } else {
            final int days = (int) (timeInMills / DAY_UNIT);
            final int hours = (int) ((timeInMills - days * DAY_UNIT) / HOUR_UNIT);
            final int minutes = (int) ((timeInMills - days * DAY_UNIT - hours * HOUR_UNIT) / MINUTE_UNIT);
            if (hours > 0 && minutes > 0) {
                sb.append(days).append("天").append(hours).append("小时").append(minutes).append("分钟");
            } else {
                if (hours <= 0 && minutes > 0) {
                    sb.append(days).append("天").append(minutes).append("分钟");
                } else if (minutes <= 0 && hours > 0) {
                    sb.append(days).append("天").append(hours).append("小时");
                } else {
                    sb.append(days).append("天");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 大小单位的枚举类
     */
    public enum SizeUnit {
        KB, MB
    }

    /**
     * 把以字节为单位的数字转换为其他单位。
     *
     * @param sizeInByte 以字节为单位的数字
     */
    public static double transformSize2KBUnit(long sizeInByte) {
        double result = 0.0f;
        if (sizeInByte > KB_MIN_UNIT) {
            result = new BigDecimal(sizeInByte).divide(new BigDecimal(KB_UNIT)).setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        } else if (sizeInByte != 0L) {
            result = 0.1d;
        }
        return result;
    }
}
