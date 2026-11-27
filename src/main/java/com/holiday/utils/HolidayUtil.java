package com.holiday.utils;

import com.holiday.constants.VacationConstant;
import com.holiday.constants.WorkDayConstant;
import com.holiday.enums.HolidayEnum;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class HolidayUtil {
    /**
     * yyyyMMdd，严格模式解析，非法日期（如 20250431）不会被顺延成合法日期
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final int DATE_LENGTH = 8;
    /**
     * 查找上/下一个工作日的最大跨度，防止脏数据导致死循环
     */
    private static final int MAX_SEARCH_DAYS = 400;

    /**
     * 把 yyyyMMdd 解析为 LocalDate
     *
     * @param yyyyMMdd 日期字符串
     * @return 日期，格式非法时返回 null
     */
    private static LocalDate parse(String yyyyMMdd) {
        if (null == yyyyMMdd || yyyyMMdd.length() != DATE_LENGTH) {
            return null;
        }
        try {
            return LocalDate.parse(yyyyMMdd, FORMATTER);
        } catch (DateTimeException e) {
            return null;
        }
    }

    private static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static LocalDate toLocalDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 是否法定节假日（不含普通周末）
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否节假日
     */
    public static boolean isHoliday(String yyyyMMdd) {
        if (null == yyyyMMdd || yyyyMMdd.length() != DATE_LENGTH) {
            return false;
        }
        Map<String, HolidayEnum> vacationMap = VacationConstant.vacationMap;
        HolidayEnum holidayEnum = vacationMap.get(yyyyMMdd);
        return null != holidayEnum;
    }

    /**
     * 是否法定节假日（不含普通周末）
     *
     * @param date 日期
     * @return 是否节假日
     */
    public static boolean isHoliday(Date date) {
        LocalDate localDate = toLocalDate(date);
        return null != localDate && isHoliday(localDate);
    }

    /**
     * 是否法定节假日（不含普通周末）
     *
     * @param timestamp 时间戳
     * @return 是否节假日
     */
    public static boolean isHoliday(Long timestamp) {
        LocalDate localDate = toLocalDate(timestamp);
        return null != localDate && isHoliday(localDate);
    }

    /**
     * 是否法定节假日（不含普通周末）
     *
     * @param date 日期
     * @return 是否节假日
     */
    public static boolean isHoliday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return isHoliday(date.format(FORMATTER));
    }

    /**
     * 是否调休上班（把周末调整为工作日）
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否调休
     */
    public static boolean isExchangeWorkDay(String yyyyMMdd) {
        if (null == yyyyMMdd || yyyyMMdd.length() != DATE_LENGTH) {
            return false;
        }
        Map<String, HolidayEnum> workDayMap = WorkDayConstant.workDayMap;
        HolidayEnum holidayEnum = workDayMap.get(yyyyMMdd);
        return null != holidayEnum;
    }

    /**
     * 是否调休上班（把周末调整为工作日）
     *
     * @param date 日期
     * @return 是否调休
     */
    public static boolean isExchangeWorkDay(Date date) {
        LocalDate localDate = toLocalDate(date);
        return null != localDate && isExchangeWorkDay(localDate);
    }

    /**
     * 是否调休上班（把周末调整为工作日）
     *
     * @param timestamp 时间戳
     * @return 是否调休
     */
    public static boolean isExchangeWorkDay(Long timestamp) {
        LocalDate localDate = toLocalDate(timestamp);
        return null != localDate && isExchangeWorkDay(localDate);
    }

    /**
     * 是否调休上班（把周末调整为工作日）
     *
     * @param date 日期
     * @return 是否调休
     */
    public static boolean isExchangeWorkDay(LocalDate date) {
        if (date == null) {
            return false;
        }
        return isExchangeWorkDay(date.format(FORMATTER));
    }

    /**
     * 是否调休上班
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否调休
     * @deprecated 方法名拼写有误，请使用 {@link #isExchangeWorkDay(String)}
     */
    @Deprecated
    public static boolean isExchangeWordDay(String yyyyMMdd) {
        return isExchangeWorkDay(yyyyMMdd);
    }

    /**
     * 是否调休上班
     *
     * @param date 日期
     * @return 是否调休
     * @deprecated 方法名拼写有误，请使用 {@link #isExchangeWorkDay(Date)}
     */
    @Deprecated
    public static boolean isExchangeWordDay(Date date) {
        return isExchangeWorkDay(date);
    }

    /**
     * 是否调休上班
     *
     * @param timestamp 时间戳
     * @return 是否调休
     * @deprecated 方法名拼写有误，请使用 {@link #isExchangeWorkDay(Long)}
     */
    @Deprecated
    public static boolean isExchangeWordDay(Long timestamp) {
        return isExchangeWorkDay(timestamp);
    }

    /**
     * 是否调休上班
     *
     * @param date 日期
     * @return 是否调休
     * @deprecated 方法名拼写有误，请使用 {@link #isExchangeWorkDay(LocalDate)}
     */
    @Deprecated
    public static boolean isExchangeWordDay(LocalDate date) {
        return isExchangeWorkDay(date);
    }

    /**
     * 是否工作日：调休上班算工作日，法定节假日不算，其余按周一至周五判断。
     * 已收录 2004 年至 2027 年的安排，超出范围的年份仅按周末判断。
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否工作日
     */
    public static boolean isWorkDay(String yyyyMMdd) {
        LocalDate localDate = parse(yyyyMMdd);
        return null != localDate && isWorkDay(localDate);
    }

    /**
     * 是否工作日：调休上班算工作日，法定节假日不算，其余按周一至周五判断。
     *
     * @param date 日期
     * @return 是否工作日
     */
    public static boolean isWorkDay(Date date) {
        LocalDate localDate = toLocalDate(date);
        return null != localDate && isWorkDay(localDate);
    }

    /**
     * 是否工作日：调休上班算工作日，法定节假日不算，其余按周一至周五判断。
     *
     * @param timestamp 时间戳
     * @return 是否工作日
     */
    public static boolean isWorkDay(Long timestamp) {
        LocalDate localDate = toLocalDate(timestamp);
        return null != localDate && isWorkDay(localDate);
    }

    /**
     * 是否工作日：调休上班算工作日，法定节假日不算，其余按周一至周五判断。
     *
     * @param date 日期
     * @return 是否工作日
     */
    public static boolean isWorkDay(LocalDate date) {
        if (date == null) {
            return false;
        }
        String yyyyMMdd = date.format(FORMATTER);
        if (WorkDayConstant.workDayMap.containsKey(yyyyMMdd)) {
            return true;
        }
        if (VacationConstant.vacationMap.containsKey(yyyyMMdd)) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * 是否休息日，即非法定调休上班的工作日之外的日期（含普通周末）
     *
     * @param yyyyMMdd 日期字符串
     * @return 是否休息日
     */
    public static boolean isRestDay(String yyyyMMdd) {
        LocalDate localDate = parse(yyyyMMdd);
        return null != localDate && !isWorkDay(localDate);
    }

    /**
     * 是否休息日，即非法定调休上班的工作日之外的日期（含普通周末）
     *
     * @param date 日期
     * @return 是否休息日
     */
    public static boolean isRestDay(Date date) {
        LocalDate localDate = toLocalDate(date);
        return null != localDate && !isWorkDay(localDate);
    }

    /**
     * 是否休息日，即非法定调休上班的工作日之外的日期（含普通周末）
     *
     * @param timestamp 时间戳
     * @return 是否休息日
     */
    public static boolean isRestDay(Long timestamp) {
        LocalDate localDate = toLocalDate(timestamp);
        return null != localDate && !isWorkDay(localDate);
    }

    /**
     * 是否休息日，即非法定调休上班的工作日之外的日期（含普通周末）
     *
     * @param date 日期
     * @return 是否休息日
     */
    public static boolean isRestDay(LocalDate date) {
        return date != null && !isWorkDay(date);
    }

    /**
     * 获取下一个工作日（不包含当天）
     *
     * @param yyyyMMdd 日期字符串
     * @return 下一个工作日，入参格式非法时返回 null
     */
    public static String getNextWorkDay(String yyyyMMdd) {
        LocalDate localDate = getNextWorkDay(parse(yyyyMMdd));
        return null == localDate ? null : localDate.format(FORMATTER);
    }

    /**
     * 获取下一个工作日（不包含当天）
     *
     * @param date 日期
     * @return 下一个工作日，入参为 null 时返回 null
     */
    public static LocalDate getNextWorkDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate next = date;
        for (int i = 0; i < MAX_SEARCH_DAYS; i++) {
            next = next.plusDays(1);
            if (isWorkDay(next)) {
                return next;
            }
        }
        return null;
    }

    /**
     * 获取上一个工作日（不包含当天）
     *
     * @param yyyyMMdd 日期字符串
     * @return 上一个工作日，入参格式非法时返回 null
     */
    public static String getPrevWorkDay(String yyyyMMdd) {
        LocalDate localDate = getPrevWorkDay(parse(yyyyMMdd));
        return null == localDate ? null : localDate.format(FORMATTER);
    }

    /**
     * 获取上一个工作日（不包含当天）
     *
     * @param date 日期
     * @return 上一个工作日，入参为 null 时返回 null
     */
    public static LocalDate getPrevWorkDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate prev = date;
        for (int i = 0; i < MAX_SEARCH_DAYS; i++) {
            prev = prev.minusDays(1);
            if (isWorkDay(prev)) {
                return prev;
            }
        }
        return null;
    }

    /**
     * 统计区间内的工作日天数（含首尾）
     *
     * @param start 开始日期，yyyyMMdd
     * @param end   结束日期，yyyyMMdd
     * @return 工作日天数，入参非法或开始日期晚于结束日期时返回 0
     */
    public static int countWorkDay(String start, String end) {
        return countWorkDay(parse(start), parse(end));
    }

    /**
     * 统计区间内的工作日天数（含首尾）
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 工作日天数，入参为 null 或开始日期晚于结束日期时返回 0
     */
    public static int countWorkDay(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            return 0;
        }
        int count = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (isWorkDay(date)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取节假日中文名
     *
     * @param yyyyMMdd 日期字符串
     * @return 节假日中文名，非节假日返回 null
     */
    public static String getHolidayCnName(String yyyyMMdd) {
        HolidayEnum holidayEnum = getHolidayEnum(yyyyMMdd);
        return null == holidayEnum ? null : holidayEnum.getHolidayCnName();
    }

    /**
     * 获取节假日中文名
     *
     * @param date 日期
     * @return 节假日中文名，非节假日返回 null
     */
    public static String getHolidayCnName(Date date) {
        return getHolidayCnName(toLocalDate(date));
    }

    /**
     * 获取节假日中文名
     *
     * @param timestamp 时间戳
     * @return 节假日中文名，非节假日返回 null
     */
    public static String getHolidayCnName(Long timestamp) {
        return getHolidayCnName(toLocalDate(timestamp));
    }

    /**
     * 获取节假日中文名
     *
     * @param date 日期
     * @return 节假日中文名，非节假日返回 null
     */
    public static String getHolidayCnName(LocalDate date) {
        return null == date ? null : getHolidayCnName(date.format(FORMATTER));
    }

    /**
     * 获取节假日英文名
     *
     * @param yyyyMMdd 日期字符串
     * @return 节假日英文名，非节假日返回 null
     */
    public static String getHolidayEnName(String yyyyMMdd) {
        HolidayEnum holidayEnum = getHolidayEnum(yyyyMMdd);
        return null == holidayEnum ? null : holidayEnum.getHolidayEnName();
    }

    /**
     * 获取节假日英文名
     *
     * @param date 日期
     * @return 节假日英文名，非节假日返回 null
     */
    public static String getHolidayEnName(Date date) {
        return getHolidayEnName(toLocalDate(date));
    }

    /**
     * 获取节假日英文名
     *
     * @param timestamp 时间戳
     * @return 节假日英文名，非节假日返回 null
     */
    public static String getHolidayEnName(Long timestamp) {
        return getHolidayEnName(toLocalDate(timestamp));
    }

    /**
     * 获取节假日英文名
     *
     * @param date 日期
     * @return 节假日英文名，非节假日返回 null
     */
    public static String getHolidayEnName(LocalDate date) {
        return null == date ? null : getHolidayEnName(date.format(FORMATTER));
    }

    /**
     * 获取日期对应的节假日枚举
     *
     * @param yyyyMMdd 日期字符串
     * @return 节假日枚举，非节假日返回 null
     */
    public static HolidayEnum getHolidayEnum(String yyyyMMdd) {
        if (null == yyyyMMdd || yyyyMMdd.length() != DATE_LENGTH) {
            return null;
        }
        return VacationConstant.vacationMap.get(yyyyMMdd);
    }
}
