package com.holiday.enums;

public enum HolidayEnum {
    NEW_YEARS_DAY("New Year's Day", "元旦", 1),
    SPRING_FESTIVAL("Spring Festival", "春节", 3),
    TOMB_SWEEPING_DAY("Tomb-sweeping Day", "清明节", 1),
    LABOUR_DAY("Labour Day", "劳动节", 1),
    DRAGON_BOAT_FESTIVAL("Dragon Boat Festival", "端午节", 1),
    MID_AUTUMN_FESTIVAL("Mid-autumn Festival", "中秋节", 1),
    NATIONAL_DAY("National Day", "国庆节", 3),
    ;
    private final String holidayEnName;
    private final String holidayCnName;
    private final Integer dayNum;

    HolidayEnum(String holidayEnName, String holidayCnName, Integer dayNum) {
        this.holidayEnName = holidayEnName;
        this.holidayCnName = holidayCnName;
        this.dayNum = dayNum;
    }

    public String getHolidayEnName() {
        return holidayEnName;
    }

    public String getHolidayCnName() {
        return holidayCnName;
    }

    public Integer getDayNum() {
        return dayNum;
    }


}
