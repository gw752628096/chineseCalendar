package com.holiday.utils;

import com.holiday.constants.VacationConstant;
import com.holiday.constants.WorkDayConstant;
import com.holiday.enums.HolidayEnum;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;

public class HolidayUtil {
    /**
     * 字符串判断是否是节假日
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否节假日
     */
    public static boolean isHoliday(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return false;
        }
        Map<String, HolidayEnum> vacationMap = VacationConstant.vacationMap;
        HolidayEnum holidayEnum = vacationMap.get(yyyyMMdd);
        if (null == holidayEnum) {
            return false;
        }

        return true;
    }

    /**
     * 日期判断是否是节假日
     *
     * @param date 日期
     * @return 是否节假日
     */
    public static boolean isHoliday(Date date) {
        if (date == null) {
            return false;
        }

        String yyyyMMdd = new DateTime(date.getTime()).toString("yyyyMMdd");
        return isHoliday(yyyyMMdd);
    }

    /**
     * 时间戳判断是否是节假日
     *
     * @param timestamp 时间戳
     * @return 是否节假日
     */
    public static boolean isHoliday(Long timestamp) {
        if (null == timestamp) {
            return false;
        }

        String yyyyMMdd = new DateTime(timestamp).toString("yyyyMMdd");
        return isHoliday(yyyyMMdd);
    }

    /**
     * 字符串判断是否是调休
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否调休
     */
    public static boolean isExchangeWordDay(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return false;
        }
        Map<String, HolidayEnum> workDayMap = WorkDayConstant.workDayMap;
        HolidayEnum holidayEnum = workDayMap.get(yyyyMMdd);
        if (null == holidayEnum) {
            return false;
        }

        return true;
    }

    /**
     * 日期判断是否是调休
     *
     * @param date 日期
     * @return 是否调休
     */
    public static boolean isExchangeWordDay(Date date) {
        if (date == null) {
            return false;
        }

        String yyyyMMdd = new DateTime(date.getTime()).toString("yyyyMMdd");
        return isExchangeWordDay(yyyyMMdd);
    }

    /**
     * 时间戳判断是否是调休
     *
     * @param timestamp 时间戳
     * @return 是否调休
     */
    public static boolean isExchangeWordDay(Long timestamp) {
        if (null == timestamp) {
            return false;
        }

        String yyyyMMdd = new DateTime(timestamp).toString("yyyyMMdd");
        return isExchangeWordDay(yyyyMMdd);
    }

    /**
     * 字符串获取节假日英文名
     *
     * @param yyyyMMdd 日期字符串
     * @return 节假日英文名
     */
    public static String getHolidayEnName(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return null;
        }
        Map<String, HolidayEnum> vacationMap = VacationConstant.vacationMap;
        HolidayEnum holidayEnum = vacationMap.get(yyyyMMdd);
        if (null == holidayEnum) {
            return null;
        }

        return holidayEnum.getHolidayEnName();
    }

    /**
     * 日期获取节假日英文名
     *
     * @param date 日期
     * @return 节假日英文名
     */
    public static String getHolidayEnName(Date date) {
        if (date == null) {
            return null;
        }

        String yyyyMMdd = new DateTime(date.getTime()).toString("yyyyMMdd");
        return getHolidayEnName(yyyyMMdd);
    }

    /**
     * 时间戳获取节假日英文名
     *
     * @param timestamp 时间戳
     * @return 节假日英文名
     */
    public static String getHolidayEnName(Long timestamp) {
        if (null == timestamp) {
            return null;
        }

        String yyyyMMdd = new DateTime(timestamp).toString("yyyyMMdd");
        return getHolidayEnName(yyyyMMdd);
    }

    /**
     * 字符串获取节假日中文名
     *
     * @param yyyyMMdd 日期字符串
     * @return 节假日中文名
     */
    public static String getHolidayCnName(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return null;
        }
        Map<String, HolidayEnum> vacationMap = VacationConstant.vacationMap;
        HolidayEnum holidayEnum = vacationMap.get(yyyyMMdd);
        if (null == holidayEnum) {
            return null;
        }

        return holidayEnum.getHolidayCnName();
    }

    /**
     * 日期获取节假日中文名
     *
     * @param date 日期
     * @return 节假日中文名
     */
    public static String getHolidayCnName(Date date) {
        if (date == null) {
            return null;
        }

        String yyyyMMdd = new DateTime(date.getTime()).toString("yyyyMMdd");
        return getHolidayCnName(yyyyMMdd);
    }

    /**
     * 时间戳获取节假日中文名
     *
     * @param timestamp 时间戳
     * @return 节假日中文名
     */
    public static String getHolidayCnName(Long timestamp) {
        if (null == timestamp) {
            return null;
        }

        String yyyyMMdd = new DateTime(timestamp).toString("yyyyMMdd");
        return getHolidayCnName(yyyyMMdd);
    }
}
