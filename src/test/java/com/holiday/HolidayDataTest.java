package com.holiday;

import com.holiday.constants.VacationConstant;
import com.holiday.constants.WorkDayConstant;
import com.holiday.enums.HolidayEnum;
import com.holiday.utils.HolidayUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 节假日原始数据自检，防止录入时出现格式错误、年份断档、重复或冲突
 */
public class HolidayDataTest {

    @Test
    public void testKeysAreValidDates() {
        assertValidKeys(VacationConstant.vacationMap, "vacationMap");
        assertValidKeys(WorkDayConstant.workDayMap, "workDayMap");
    }

    private void assertValidKeys(Map<String, HolidayEnum> map, String name) {
        for (String key : map.keySet()) {
            assertEquals(name + " 中日期长度应为 8: " + key, 8, key.length());
            for (char c : key.toCharArray()) {
                if (!Character.isDigit(c)) {
                    fail(name + " 中日期包含非数字字符: " + key);
                }
            }
            try {
                LocalDate.parse(key, DateTimeFormatter.BASIC_ISO_DATE);
            } catch (RuntimeException e) {
                fail(name + " 中存在非法日期: " + key);
            }
        }
    }

    @Test
    public void testHolidayAndWorkDayAreDisjoint() {
        Set<String> both = new HashSet<>(VacationConstant.vacationMap.keySet());
        both.retainAll(WorkDayConstant.workDayMap.keySet());
        assertTrue("同一天不能既是节假日又是调休上班: " + both, both.isEmpty());
    }

    @Test
    public void testYearCoverageIsContinuous() {
        Set<Integer> holidayYears = years(VacationConstant.vacationMap);
        Set<Integer> workDayYears = years(WorkDayConstant.workDayMap);
        // 2004 年至 2027 年之间不允许出现空档
        for (int year = 2004; year <= 2027; year++) {
            assertTrue("缺少 " + year + " 年节假日数据", holidayYears.contains(year));
        }
        // 调休年份允许有空档（例如某年元旦不调休、春节不调休）
        for (Integer year : workDayYears) {
            assertTrue("调休数据超出节假日数据年份范围: " + year, holidayYears.contains(year));
        }
    }

    private Set<Integer> years(Map<String, HolidayEnum> map) {
        Set<Integer> years = new HashSet<>();
        for (String key : map.keySet()) {
            years.add(Integer.parseInt(key.substring(0, 4)));
        }
        return years;
    }

    @Test
    public void testEveryHolidayHasName() {
        for (Map.Entry<String, HolidayEnum> entry : VacationConstant.vacationMap.entrySet()) {
            HolidayEnum holidayEnum = entry.getValue();
            assertNotNull("日期没有对应节日: " + entry.getKey(), holidayEnum);
            assertNotNull(entry.getKey() + " 缺少中文名", holidayEnum.getHolidayCnName());
            assertNotNull(entry.getKey() + " 缺少英文名", holidayEnum.getHolidayEnName());
            assertEquals("getHolidayCnName 与数据不一致: " + entry.getKey(),
                    holidayEnum.getHolidayCnName(), HolidayUtil.getHolidayCnName(entry.getKey()));
            assertEquals("getHolidayEnName 与数据不一致: " + entry.getKey(),
                    holidayEnum.getHolidayEnName(), HolidayUtil.getHolidayEnName(entry.getKey()));
            assertEquals("getHolidayEnum 与数据不一致: " + entry.getKey(),
                    holidayEnum, HolidayUtil.getHolidayEnum(entry.getKey()));
        }
    }

    /**
     * 数据与 isWorkDay 结论必须一致，全量交叉校验
     */
    @Test
    public void testWorkDayConclusionMatchesData() {
        for (String day : VacationConstant.vacationMap.keySet()) {
            assertTrue("法定节假日不应是工作日: " + day, !HolidayUtil.isWorkDay(day));
            assertTrue("法定节假日应是休息日: " + day, HolidayUtil.isRestDay(day));
        }
        for (String day : WorkDayConstant.workDayMap.keySet()) {
            assertTrue("调休上班应是工作日: " + day, HolidayUtil.isWorkDay(day));
            assertFalse("调休上班不应是休息日: " + day, HolidayUtil.isRestDay(day));
            assertFalse("调休上班不应有节日名: " + day, HolidayUtil.isHoliday(day));
        }
    }

    /**
     * 普通周末既不放假也不是调休，isWorkDay 必须判为休息
     */
    @Test
    public void testOrdinaryWeekend() {
        for (String day : new String[]{"20250104", "20250712", "20261107", "20270703", "20270704"}) {
            assertFalse("普通周末不是法定节假日: " + day, HolidayUtil.isHoliday(day));
            assertFalse("普通周末不是调休: " + day, HolidayUtil.isExchangeWorkDay(day));
            assertFalse("普通周末不是工作日: " + day, HolidayUtil.isWorkDay(day));
            assertTrue("普通周末是休息日: " + day, HolidayUtil.isRestDay(day));
        }
    }
}
