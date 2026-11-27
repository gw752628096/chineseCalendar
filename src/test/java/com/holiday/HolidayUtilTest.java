package com.holiday;

import com.holiday.constants.VacationConstant;
import com.holiday.constants.WorkDayConstant;
import com.holiday.enums.HolidayEnum;
import com.holiday.utils.HolidayUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 节假日/调休数据测试，数据来源为国务院办公厅历年部分节假日安排通知。
 */
public class HolidayUtilTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    private final Map<String, HolidayEnum> expectedHolidays = new LinkedHashMap<>();
    private final Map<String, HolidayEnum> expectedWorkDays = new LinkedHashMap<>();

    private void holiday(String day, HolidayEnum holidayEnum) {
        assertNull("日期重复: " + day, expectedHolidays.put(day, holidayEnum));
    }

    private void holiday(String start, String end, HolidayEnum holidayEnum) {
        LocalDate date = LocalDate.parse(start, FORMATTER);
        LocalDate last = LocalDate.parse(end, FORMATTER);
        while (!date.isAfter(last)) {
            holiday(date.format(FORMATTER), holidayEnum);
            date = date.plusDays(1);
        }
    }

    private void workDay(String day, HolidayEnum holidayEnum) {
        assertNull("日期重复: " + day, expectedWorkDays.put(day, holidayEnum));
    }

    /**
     * 校验放假、调休数据是否与预期完全一致（数量、日期、节日名称）。
     */
    private void assertSchedule(String year) {
        for (Map.Entry<String, HolidayEnum> entry : expectedHolidays.entrySet()) {
            String day = entry.getKey();
            assertEquals("应放假: " + day, true, HolidayUtil.isHoliday(day));
            assertEquals("节日名称: " + day, entry.getValue().getHolidayCnName(), HolidayUtil.getHolidayCnName(day));
            assertEquals("节日英文名: " + day, entry.getValue().getHolidayEnName(), HolidayUtil.getHolidayEnName(day));
            assertFalse("放假日期不应是调休: " + day, HolidayUtil.isExchangeWorkDay(day));
        }
        for (Map.Entry<String, HolidayEnum> entry : expectedWorkDays.entrySet()) {
            String day = entry.getKey();
            assertEquals("应调休上班: " + day, true, HolidayUtil.isExchangeWorkDay(day));
            assertFalse("调休日期不应是节假日: " + day, HolidayUtil.isHoliday(day));
            assertNull("调休日期无节日名称: " + day, HolidayUtil.getHolidayCnName(day));
        }
        assertEquals(year + " 年放假天数", expectedHolidays.size(), countByYear(VacationConstant.vacationMap, year));
        assertEquals(year + " 年调休天数", expectedWorkDays.size(), countByYear(WorkDayConstant.workDayMap, year));
    }

    private int countByYear(Map<String, HolidayEnum> map, String year) {
        int count = 0;
        for (String day : map.keySet()) {
            if (day.startsWith(year)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void test2025() {
        // 元旦：1月1日放假1天，不调休
        holiday("20250101", HolidayEnum.NEW_YEARS_DAY);
        // 春节：1月28日至2月4日放假8天，1月26日、2月8日上班
        holiday("20250128", "20250204", HolidayEnum.SPRING_FESTIVAL);
        workDay("20250126", HolidayEnum.SPRING_FESTIVAL);
        workDay("20250208", HolidayEnum.SPRING_FESTIVAL);
        // 清明节：4月4日至6日放假3天
        holiday("20250404", "20250406", HolidayEnum.TOMB_SWEEPING_DAY);
        // 劳动节：5月1日至5日放假5天，4月27日上班
        holiday("20250501", "20250505", HolidayEnum.LABOUR_DAY);
        workDay("20250427", HolidayEnum.LABOUR_DAY);
        // 端午节：5月31日至6月2日放假3天
        holiday("20250531", "20250602", HolidayEnum.DRAGON_BOAT_FESTIVAL);
        // 国庆节、中秋节：10月1日至8日放假8天，9月28日、10月11日上班
        holiday("20251001", "20251005", HolidayEnum.NATIONAL_DAY);
        // 中秋节为10月6日，落在国庆假期内
        holiday("20251006", HolidayEnum.MID_AUTUMN_FESTIVAL);
        holiday("20251007", "20251008", HolidayEnum.NATIONAL_DAY);
        workDay("20250928", HolidayEnum.NATIONAL_DAY);
        workDay("20251011", HolidayEnum.NATIONAL_DAY);

        assertSchedule("2025");
        assertEquals(28, expectedHolidays.size());
        assertEquals(5, expectedWorkDays.size());
    }

    @Test
    public void test2026() {
        // 元旦：1月1日至3日放假3天，1月4日上班
        holiday("20260101", "20260103", HolidayEnum.NEW_YEARS_DAY);
        workDay("20260104", HolidayEnum.NEW_YEARS_DAY);
        // 春节：2月15日至23日放假9天，2月14日、2月28日上班
        holiday("20260215", "20260223", HolidayEnum.SPRING_FESTIVAL);
        workDay("20260214", HolidayEnum.SPRING_FESTIVAL);
        workDay("20260228", HolidayEnum.SPRING_FESTIVAL);
        // 清明节：4月4日至6日放假3天
        holiday("20260404", "20260406", HolidayEnum.TOMB_SWEEPING_DAY);
        // 劳动节：5月1日至5日放假5天，5月9日上班
        holiday("20260501", "20260505", HolidayEnum.LABOUR_DAY);
        workDay("20260509", HolidayEnum.LABOUR_DAY);
        // 端午节：6月19日至21日放假3天
        holiday("20260619", "20260621", HolidayEnum.DRAGON_BOAT_FESTIVAL);
        // 中秋节：9月25日至27日放假3天
        holiday("20260925", "20260927", HolidayEnum.MID_AUTUMN_FESTIVAL);
        // 国庆节：10月1日至7日放假7天，9月20日、10月10日上班
        holiday("20261001", "20261007", HolidayEnum.NATIONAL_DAY);
        workDay("20260920", HolidayEnum.NATIONAL_DAY);
        workDay("20261010", HolidayEnum.NATIONAL_DAY);

        assertSchedule("2026");
        assertEquals(33, expectedHolidays.size());
        assertEquals(6, expectedWorkDays.size());
    }

    @Test
    public void test2027() {
        // 元旦：1月1日至3日放假3天，不调休
        holiday("20270101", "20270103", HolidayEnum.NEW_YEARS_DAY);
        // 春节：2月5日至13日放假9天，1月31日、2月14日上班
        holiday("20270205", "20270213", HolidayEnum.SPRING_FESTIVAL);
        workDay("20270131", HolidayEnum.SPRING_FESTIVAL);
        workDay("20270214", HolidayEnum.SPRING_FESTIVAL);
        // 清明节：4月3日至5日放假3天
        holiday("20270403", "20270405", HolidayEnum.TOMB_SWEEPING_DAY);
        // 劳动节：5月1日至5日放假5天，5月8日上班
        holiday("20270501", "20270505", HolidayEnum.LABOUR_DAY);
        workDay("20270508", HolidayEnum.LABOUR_DAY);
        // 端午节：6月9日放假1天
        holiday("20270609", HolidayEnum.DRAGON_BOAT_FESTIVAL);
        // 中秋节：9月15日放假1天
        holiday("20270915", HolidayEnum.MID_AUTUMN_FESTIVAL);
        // 国庆节：10月1日至7日放假7天，9月26日、10月9日上班
        holiday("20271001", "20271007", HolidayEnum.NATIONAL_DAY);
        workDay("20270926", HolidayEnum.NATIONAL_DAY);
        workDay("20271009", HolidayEnum.NATIONAL_DAY);

        assertSchedule("2027");
        assertEquals(29, expectedHolidays.size());
        assertEquals(5, expectedWorkDays.size());
    }

    /**
     * 旧方法名（拼写错误版本）仍需保持可用
     */
    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedMethodName() {
        assertTrue(HolidayUtil.isExchangeWordDay("20250928"));
        assertFalse(HolidayUtil.isExchangeWordDay("20251001"));
    }

    /**
     * 历年数据回归，防止新增年份时误改历史数据。
     */
    @Test
    public void testHistory() {
        // 2004
        assertTrue(HolidayUtil.isHoliday("20040101"));
        assertTrue(HolidayUtil.isHoliday("20040122"));
        assertEquals("春节", HolidayUtil.getHolidayCnName("20040122"));
        // 2008 年取消五一黄金周
        assertEquals("劳动节", HolidayUtil.getHolidayCnName("20080501"));
        assertFalse(HolidayUtil.isHoliday("20080504"));
        assertTrue(HolidayUtil.isExchangeWorkDay("20080504"));
        // 2020 年春节延长
        assertTrue(HolidayUtil.isHoliday("20200202"));
        assertEquals("春节", HolidayUtil.getHolidayCnName("20200202"));
        // 2023 年中秋国庆相连
        assertEquals("中秋节", HolidayUtil.getHolidayCnName("20230929"));
        assertEquals("国庆节", HolidayUtil.getHolidayCnName("20230930"));
        // 2024
        assertTrue(HolidayUtil.isHoliday("20240210"));
        assertTrue(HolidayUtil.isExchangeWorkDay("20240204"));
        // 普通工作日
        assertFalse(HolidayUtil.isHoliday("20250102"));
        assertFalse(HolidayUtil.isExchangeWorkDay("20250102"));
        assertNull(HolidayUtil.getHolidayCnName("20250102"));
        // 边界日期
        assertFalse(HolidayUtil.isHoliday("20250431"));
        assertFalse(HolidayUtil.isHoliday("202504011"));
        assertFalse(HolidayUtil.isHoliday((String) null));
        assertFalse(HolidayUtil.isExchangeWorkDay((String) null));
        assertNull(HolidayUtil.getHolidayCnName((String) null));
        assertFalse(HolidayUtil.isHoliday((Date) null));
        assertFalse(HolidayUtil.isHoliday((Long) null));
        assertFalse(HolidayUtil.isExchangeWorkDay((Date) null));
        assertFalse(HolidayUtil.isExchangeWorkDay((Long) null));

        // Date 与时间戳重载
        Date nationalDay = Date.from(LocalDate.parse("20271001", FORMATTER).atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertTrue(HolidayUtil.isHoliday(nationalDay));
        assertTrue(HolidayUtil.isHoliday(nationalDay.getTime()));
        assertEquals("国庆节", HolidayUtil.getHolidayCnName(nationalDay));
        assertEquals("国庆节", HolidayUtil.getHolidayCnName(nationalDay.getTime()));
        assertEquals("National Day", HolidayUtil.getHolidayEnName(nationalDay));
    }
}
