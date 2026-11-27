package com.holiday;

import com.holiday.utils.HolidayUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 工作日/休息日判断、工作日推算测试
 */
public class HolidayUtilWorkDayTest {

    @Test
    public void testIsWorkDay() {
        // 法定节假日
        assertFalse(HolidayUtil.isWorkDay("20250101"));
        assertFalse(HolidayUtil.isWorkDay("20250129"));
        assertFalse(HolidayUtil.isWorkDay("20251001"));
        // 调休上班（周末，但需要上班）
        assertTrue(HolidayUtil.isWorkDay("20250126"));
        assertTrue(HolidayUtil.isWorkDay("20251011"));
        assertTrue(HolidayUtil.isWorkDay("20270214"));
        // 普通周末
        assertFalse(HolidayUtil.isWorkDay("20250104"));
        assertFalse(HolidayUtil.isWorkDay("20270703"));
        assertFalse(HolidayUtil.isWorkDay("20270704"));
        // 普通工作日
        assertTrue(HolidayUtil.isWorkDay("20250102"));
        assertTrue(HolidayUtil.isWorkDay("20250127"));
        assertTrue(HolidayUtil.isWorkDay("20270705"));
        // 未收录年份：仅按周末判断
        assertTrue(HolidayUtil.isWorkDay("20030101"));
        assertFalse(HolidayUtil.isWorkDay("20030104"));
        assertTrue(HolidayUtil.isWorkDay("20300101"));
    }

    @Test
    public void testIsRestDay() {
        assertTrue(HolidayUtil.isRestDay("20250101"));
        assertTrue(HolidayUtil.isRestDay("20250104"));
        assertFalse(HolidayUtil.isRestDay("20250102"));
        assertFalse(HolidayUtil.isRestDay("20250126"));
    }

    @Test
    public void testWorkDayAndRestDayAreComplementary() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        while (date.getYear() == 2025) {
            assertEquals("日期: " + date,
                    HolidayUtil.isWorkDay(date), !HolidayUtil.isRestDay(date));
            date = date.plusDays(1);
        }
    }

    @Test
    public void testLocalDateOverloads() {
        LocalDate nationalDay = LocalDate.of(2027, 10, 1);
        assertTrue(HolidayUtil.isHoliday(nationalDay));
        assertEquals("国庆节", HolidayUtil.getHolidayCnName(nationalDay));
        assertEquals("National Day", HolidayUtil.getHolidayEnName(nationalDay));
        assertFalse(HolidayUtil.isWorkDay(nationalDay));
        assertTrue(HolidayUtil.isRestDay(nationalDay));

        LocalDate workDay = LocalDate.of(2027, 10, 9);
        assertTrue(HolidayUtil.isExchangeWorkDay(workDay));
        assertTrue(HolidayUtil.isWorkDay(workDay));
        assertNull(HolidayUtil.getHolidayCnName(workDay));

        LocalDate weekEnd = LocalDate.of(2027, 7, 3);
        assertFalse(HolidayUtil.isHoliday(weekEnd));
        assertFalse(HolidayUtil.isWorkDay(weekEnd));
        assertTrue(HolidayUtil.isRestDay(weekEnd));
    }

    @Test
    public void testDateAndTimestampOverloads() {
        Date date = Date.from(LocalDate.of(2025, 1, 26).atStartOfDay(ZoneId.systemDefault()).toInstant());
        long timestamp = date.getTime();
        assertTrue(HolidayUtil.isWorkDay(date));
        assertTrue(HolidayUtil.isWorkDay(timestamp));
        assertTrue(HolidayUtil.isExchangeWorkDay(date));
        assertTrue(HolidayUtil.isExchangeWorkDay(timestamp));
        assertFalse(HolidayUtil.isRestDay(date));
        Date saturday = Date.from(LocalDate.of(2025, 1, 25).atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertTrue(HolidayUtil.isRestDay(saturday));
        assertTrue(HolidayUtil.isRestDay(saturday.getTime()));
    }

    @Test
    public void testInvalidInput() {
        assertFalse(HolidayUtil.isWorkDay((String) null));
        assertFalse(HolidayUtil.isWorkDay(""));
        assertFalse(HolidayUtil.isWorkDay("2025041"));
        assertFalse(HolidayUtil.isWorkDay("202504011"));
        assertFalse(HolidayUtil.isWorkDay("2025-04-01"));
        assertFalse(HolidayUtil.isWorkDay("20250431"));
        assertFalse(HolidayUtil.isWorkDay("20251301"));
        assertFalse(HolidayUtil.isRestDay("20250431"));
        assertFalse(HolidayUtil.isWorkDay((Date) null));
        assertFalse(HolidayUtil.isRestDay((Date) null));
        assertFalse(HolidayUtil.isWorkDay((Long) null));
        assertFalse(HolidayUtil.isRestDay((Long) null));
        assertNull(HolidayUtil.getNextWorkDay("20250431"));
        assertNull(HolidayUtil.getPrevWorkDay((String) null));
        assertEquals(0, HolidayUtil.countWorkDay("20250431", "20250501"));
        assertEquals(0, HolidayUtil.countWorkDay("20250501", "20250431"));
    }

    @Test
    public void testGetNextWorkDay() {
        // 周五 -> 下一个工作日（普通周末跳过）
        assertEquals("20250103", HolidayUtil.getNextWorkDay("20250102"));
        // 调休的周日需要上班
        assertEquals("20250126", HolidayUtil.getNextWorkDay("20250124"));
        // 国庆假期之后
        assertEquals("20251009", HolidayUtil.getNextWorkDay("20250930"));
        assertEquals("20251009", HolidayUtil.getNextWorkDay("20251008"));
        // 跨年：2027 年元旦不调休，1 月 4 日（周一）即工作日
        assertEquals("20270104", HolidayUtil.getNextWorkDay("20270103"));
        // LocalDate 重载
        assertEquals(LocalDate.of(2025, 10, 9), HolidayUtil.getNextWorkDay(LocalDate.of(2025, 10, 1)));
    }

    @Test
    public void testGetPrevWorkDay() {
        // 周一 -> 上一个工作日（普通周末跳过）
        assertEquals("20250103", HolidayUtil.getPrevWorkDay("20250106"));
        // 调休上班的周日
        assertEquals("20250126", HolidayUtil.getPrevWorkDay("20250127"));
        // 国庆假期第一天 -> 节前最后一个工作日
        assertEquals("20250930", HolidayUtil.getPrevWorkDay("20251001"));
        assertEquals("20250930", HolidayUtil.getPrevWorkDay("20251009"));
        // 假期中间的日期
        assertEquals("20250930", HolidayUtil.getPrevWorkDay("20251005"));
        // 跨年
        assertEquals("20261231", HolidayUtil.getPrevWorkDay("20270104"));
    }

    private int countRestDay(LocalDate start, LocalDate end) {
        int count = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (HolidayUtil.isRestDay(date)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testCountWorkDay() {
        // 国庆假期
        assertEquals(0, HolidayUtil.countWorkDay("20251001", "20251008"));
        // 含调休上班
        assertEquals(3, HolidayUtil.countWorkDay("20250928", "20250930"));
        // 单日
        assertEquals(1, HolidayUtil.countWorkDay("20251009", "20251009"));
        assertEquals(0, HolidayUtil.countWorkDay("20250104", "20250104"));
        // 普通一周
        assertEquals(5, HolidayUtil.countWorkDay("20270705", "20270711"));
        // 开始日期晚于结束日期
        assertEquals(0, HolidayUtil.countWorkDay("20250106", "20250103"));
        // 全年工作日数：365 天 - 104 个周末 - 落在工作日的法定节假日 + 调休上班
        assertEquals(248, HolidayUtil.countWorkDay("20250101", "20251231"));
        assertEquals(248, HolidayUtil.countWorkDay("20260101", "20261231"));
        assertEquals(248, HolidayUtil.countWorkDay("20270101", "20271231"));
        // 全年休息日 117 天 = 104 个周末 + 落在工作日的假期 - 调休上班
        assertEquals(117, countRestDay(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)));
        assertEquals(117, countRestDay(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)));
        assertEquals(117, countRestDay(LocalDate.of(2027, 1, 1), LocalDate.of(2027, 12, 31)));
        // LocalDate 重载
        assertEquals(248, HolidayUtil.countWorkDay(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)));
    }
}
