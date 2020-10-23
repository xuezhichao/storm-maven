package com.xzc.storm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aston on 2017/7/11.
 */
public class DateUtil {
    public static String getDateByFormat(String dateStr ,String fmt) throws ParseException {
        SimpleDateFormat sdf = null;
        if(dateStr.contains("/")) {
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else if(dateStr.contains("T")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        } else if(dateStr.contains("-")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        }
        return new SimpleDateFormat(fmt).format(sdf.parse(dateStr));
    }

    public static int getIntervalDays(Date fDate, Date eDate){
        if (null == fDate || null == eDate) {
            return -1;
        }

        Long intervalMilli = eDate.getTime() - fDate.getTime();

        return (int)(intervalMilli / (24 * 60 * 60 * 1000));
    }

    public static int getIntervalDays(String fDateStr, String eDateStr ,String fmt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date fDate = sdf.parse(fDateStr);
        Date eDate = sdf.parse(eDateStr);
        Long intervalMilli = eDate.getTime() - fDate.getTime();

        return (int)(intervalMilli / (24 * 60 * 60 * 1000) + 1);
    }

    public static int getIntervalDays(String fDateStr, String eDateStr) throws ParseException {
        return getIntervalDays(fDateStr ,eDateStr ,"yyyy-MM-dd");
    }


    public static int getTtlSec(){
        return getTtlSec(1);
    }

    /**
     * 获取距离今天 daycnt 天到今天的存活时间
     * @param dayCnt 距离今天天数（正数）
     * @return
     */
    public static int getTtlSec(int dayCnt){
        Calendar cal = Calendar.getInstance();
        int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        return dayCnt * 24*60*60 - (hour * 60*60 + minute * 60 + second) + 1;
    }

    /**
     * 获取距离今天 daycnt 天的当天时间戳（粒度天）
     * @param dayCnt 距离今天天数（负数表示过去）
     * @return
     */
    public static Long getTtlDeadTimestamp(int dayCnt){
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.DATE, dayCnt);
        //System.out.println(cal.getTime());

        cal.set(Calendar.HOUR ,0);
        cal.set(Calendar.MINUTE ,0);
        cal.set(Calendar.SECOND ,0);
        cal.set(Calendar.MILLISECOND ,0);

        return cal.getTime().getTime() / 1000;
    }

    public static Date getDate(String dateStr) throws ParseException {
        String fmt = "yyyyMMdd HH:mm:ss";
        if(dateStr.contains("/")) {
            fmt = "yyyy/MM/dd HH:mm:ss";
        }
        else if(dateStr.contains("T")) {
            fmt = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        }
        else if(dateStr.contains("-")) {
            fmt = "yyyy-MM-dd HH:mm:ss";
        }else {
            fmt = "yyyyMMdd";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.parse(dateStr);
    }

    public static String getFormatDate(Date date ,String fmt){
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }

    public static String getDateStr(int dayCnt){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, dayCnt);
        return getFormatDate(cal.getTime() ,"yyyy-MM-dd");
    }

    public static String getDateStrByFmt(int dayCnt ,String fmt){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, dayCnt);
        return getFormatDate(cal.getTime() ,fmt);
    }

    public static Long getTimestamp(String dateStr) throws ParseException {
       return getDate(dateStr).getTime() / 1000;
    }

    public static String timestampToDateStr(Long ts ,String fmt)throws ParseException{
        DateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(ts);
    }

    public static String timestampToDateStr(Long ts)throws ParseException{
        return timestampToDateStr(ts ,"yyyy-MM-dd HH:mm:ss");
    }

    public static String addDay(String dt ,int n){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        Date dat = null;
        try {
            dat = fmt.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);

        cal.add(Calendar.DATE, n);
        return fmt.format(cal.getTime());
    }

}
