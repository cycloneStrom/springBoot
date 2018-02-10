package org.poem.common.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by poem on 2016/5/7.
 * 日期管理
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     *yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * yyyy-MM
     */
    public static final String MONTH_FORMAT = "yyyy-MM";
    /**
     * yyyy年MM月dd日
     */
    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日";
    /**
     * yyyy年MM月
     */
    public static final String MONTH_FORMAT_CN = "yyyy年MM月";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String DATETIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    /**
     * MM-dd
     */
    public static final String SIMPLE_DATE_FORMAT = "MM-dd";
    /**
     * HH:mm
     */
    public static final String TIME_FORMAT = "HH:mm";
    /**
     * HH:mm:ss
     */
    public static final String TIME_FORMAT_ALL = "HH:mm:ss";
    /**
     * yyyy年MM月dd日 HH:mm
     */
    private static final String LOCAL_FORMAT_DATE_TIME = "yyyy年MM月dd日 HH:mm";

    /**
     * yyyy-MM-dd HH:mm
     * @param str
     * @return
     */
    public static Date parseDateTimeMinute(final String str) {
        try {
            return parseDate(str, DATETIME_MINUTE_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDateTimeMinute(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_MINUTE_FORMAT);
        return sdf.format(date);
    }

    public static Date parseDateTime(final String str) {
        try {
            return parseDate(str, DATETIME_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatDateTime(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatTime(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.TIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatTimeAll(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.TIME_FORMAT_ALL);
        return sdf.format(date);
    }

    public static String formatDate(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        return sdf.format(date);
    }

    public static String formatMonth(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.MONTH_FORMAT);
        return sdf.format(date);
    }


    public static String format(final Date date, String patterns) {
        SimpleDateFormat sdf = new SimpleDateFormat(patterns);
        return sdf.format(date);
    }

    public static Date parseDate(final String str) {
        try {
            return parseDate(str, DATE_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseTime(final String str) {
        try {
            return parseDate(str, TIME_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseTimeAll(final String str) {
        try {
            return parseDate(str, TIME_FORMAT_ALL);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSimpleDateFormat(final Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.SIMPLE_DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date getSimpleDateParse(final String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.SIMPLE_DATE_FORMAT);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date parseMonth(final String str) {
        try {
            return parseDate(str, MONTH_FORMAT);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取两个时间之间相差的月份数组
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 两个时间的相差的月份数组
     */
    public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    public static Date formatDate(Date date, int field) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (field) {
            case Calendar.MONTH:
                calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;

            case Calendar.DAY_OF_MONTH:
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.HOUR_OF_DAY:
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.MINUTE:
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case Calendar.SECOND:
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            default:
                break;
        }
        return calendar.getTime();
    }

    public static Date formatDateStr(String date, String DATE_FORMAT) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Time formatTime(String time, String DATE_FORMAT) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return new Time(sdf.parse(time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 对时间分钟操作
     *
     * @param date   时间
     * @param number 加 或者 减的大小
     * @return
     */
    public static Date getMinuteAddOrSub(Date date, Integer number) {
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, number);
        return calendar.getTime();
    }

    /**
     * 得到时间的年，月， 日
     *
     * @param date
     * @param flag
     * @return
     */
    public static Integer getDatefor(Date date, int flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer result = null;
        switch (flag) {
            case Calendar.YEAR:
                result = calendar.get(Calendar.YEAR);
                break;
            case Calendar.MONTH:
                result = calendar.get(Calendar.MONTH) + 1;
                break;
            case Calendar.DATE:
                result = calendar.get(Calendar.DATE);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 得到月份和日期
     *
     * @param date
     * @return
     */
    public static String getDatefor(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }


    /* 计算两个日期之间相差的天数
     * @param smallDate 较小的时间
     * @param bigDate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smallDate, Date bigDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smallDate = sdf.parse(sdf.format(smallDate));
        bigDate = sdf.parse(sdf.format(bigDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smallDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bigDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 求两个时间相差的时间单位
     *
     * @param smallDate
     * @param bigDate
     * @param type      分别为  Calendar.YEAR,Calendar.MONTH,Calendar.DATE,Calendar.HOUR,Calendar.MINUTE
     * @return
     */
    public static int minusTime(Date smallDate, Date bigDate, int type) {
        if (smallDate == null || bigDate == null) {
            return Integer.MAX_VALUE;
        }
        Calendar smallCalendar = Calendar.getInstance();
        smallCalendar.setTime(smallDate);
        Calendar bigCalendar = Calendar.getInstance();
        bigCalendar.setTime(bigDate);
        smallCalendar.set(smallCalendar.get(Calendar.YEAR), smallCalendar.get(Calendar.MONTH), smallCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        smallCalendar.set(Calendar.MILLISECOND, 0);
        bigCalendar.set(bigCalendar.get(Calendar.YEAR), bigCalendar.get(Calendar.MONTH), bigCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        bigCalendar.set(Calendar.MILLISECOND, 0);
        long smallTime = smallCalendar.getTimeInMillis();
        long bigTime = bigCalendar.getTimeInMillis();
        int bigTotalMonth = (bigCalendar.get(Calendar.YEAR) - 1) * 12 + bigCalendar.get(Calendar.MONTH);
        int smallTotalMonth = (smallCalendar.get(Calendar.YEAR) - 1) * 12 + smallCalendar.get(Calendar.MONTH);
        switch (type) {
            case Calendar.YEAR:
                return bigCalendar.get(Calendar.YEAR) - smallCalendar.get(Calendar.YEAR);
            case Calendar.MONTH:
                return bigTotalMonth - smallTotalMonth;
            case Calendar.DATE:
                return (int) ((bigTime - smallTime) / (1000l * 3600 * 24)) + 1;
            case Calendar.HOUR:
                return (int) ((bigTime - smallTime) / (1000l * 3600));
            case Calendar.MINUTE:
                return (int) ((bigTime - smallTime) / (1000l * 60));
            default:
                return Integer.MAX_VALUE;
        }
    }


    public static Date getStartTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getBeforeOfNowDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取周次
     *
     * @param date
     * @param weekStartDate
     * @param loopDays
     * @return
     */
    public static int[] getWeekBySignedDate(Date date, Date weekStartDate, int loopDays) {
        int[] weekAndDay = new int[2];
        int minusDay = minusTime(weekStartDate, date, Calendar.DATE);
        //如果相差少于一天，按一天算
        minusDay = minusDay < 1 ? 1 : minusDay;
        //week
        weekAndDay[0] = minusDay / loopDays;
        weekAndDay[0] = minusDay % loopDays == 0 ? weekAndDay[0] : weekAndDay[0] + 1;
        //weekDay
        weekAndDay[1] = minusDay % loopDays;
        weekAndDay[1] = weekAndDay[1] == 0 ? loopDays : weekAndDay[1];
        return weekAndDay;
    }

    /**
     * 日期管理
     *
     * @param dateTime
     * @return
     */
    public static String formatLocalDateTime(Date dateTime) {
        if (dateTime == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LOCAL_FORMAT_DATE_TIME);
        return simpleDateFormat.format(dateTime);
    }
}
