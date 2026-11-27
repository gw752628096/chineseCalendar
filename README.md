# ChineseCalendar
![CI](https://github.com/gw752628096/chineseCalendar/actions/workflows/ci.yml/badge.svg)

判断是否是法定节假日，git 上只看到了 python 版本（[LKI/chinese-calendar](https://github.com/LKI/chinese-calendar)）。  
为了调用方便,自己实现一个java版本。

该项目用来判断某年某月某一天是不是工作日/节假日。支持<text style="color:red;font-size:20px;font-weight:bold">2004年</text>至<text style="color:red;font-size:20px;font-weight:bold">2027年</text>，包括2020年的春节延长。  

由于次年的节假日安排，取决于国务院发布的日程。所以本项目一般会在国务院更新以后，发布新的版本。  

按照以往的经验，一般是每年的<text style="color:red;font-size:20px;font-weight:bold">11月</text>前后发布新版本。  

安装
====
maven
----
    <dependency>
        <groupId>io.github.gw752628096</groupId>
        <artifactId>chinese-calendar</artifactId>
        <version>2027.1</version>
    </dependency>
Gradle
----
    implementation group: 'io.github.gw752628096', name: 'chinese-calendar', version: '2027.1'

使用
====
    //是否是法定节假日（不含普通周末）
    HolidayUtil.isHoliday("20271001");
    HolidayUtil.isHoliday(LocalDate.of(2027, 10, 1));
    HolidayUtil.isHoliday(new Date());
    HolidayUtil.isHoliday(System.currentTimeMillis());

    //是否是调休上班
    HolidayUtil.isExchangeWorkDay("20271009");          // true：周六，但调休上班

    //是否是工作日 / 休息日（周末、法定节假日、调休一起判断）
    HolidayUtil.isWorkDay("20271009");                  // true：调休上班
    HolidayUtil.isWorkDay("20271004");                  // false：国庆假期
    HolidayUtil.isRestDay("20271010");                  // true：周日

    //工作日推算
    HolidayUtil.getNextWorkDay("20271007");             // 20271008
    HolidayUtil.getPrevWorkDay("20271001");             // 20270930
    HolidayUtil.countWorkDay("20271001", "20271007");   // 0

    //获取节日中文名、英文名、枚举
    HolidayUtil.getHolidayCnName("20271001");
    HolidayUtil.getHolidayEnName(LocalDate.of(2027, 10, 1));
    HolidayUtil.getHolidayEnum("20271001");

查询类方法（`isHoliday`、`isExchangeWorkDay`、`isWorkDay`、`isRestDay`、`getHolidayCnName`、`getHolidayEnName`）均提供 `String`(yyyyMMdd)、`LocalDate`、`Date`、时间戳四种入参重载；`getNextWorkDay`、`getPrevWorkDay`、`countWorkDay` 提供 `String` 与 `LocalDate` 两种重载。  
`isExchangeWordDay` 为早期拼写错误的方法名，已标记 `@Deprecated`，请改用 `isExchangeWorkDay`。  
`isWorkDay` / `isRestDay` 已收录 2004 年—2027 年的安排，超出该范围仅按周末判断。

测试
====
    mvn test

- `HolidayUtilTest`：各年份放假/调休逐日断言、年度总数校验、历史数据回归
- `HolidayUtilWorkDayTest`：工作日/休息日判断、工作日推算、四种入参重载、非法入参
- `HolidayDataTest`：数据自检（日期格式、放假与调休冲突、年份断档、与 `isWorkDay` 结论交叉校验）

CI 会在 JDK 8/11/17/21 上执行 `mvn test`。

贡献代码
====
1.Fork + Clone项目到本地  
2.修改[节假日定义](https://github.com/gw752628096/chineseCalendar/blob/main/src/main/java/com/holiday/constants/VacationConstant.java)  
3.提交PR  

更新日志
====
1.0.0-RELEASE 2020-04-24 发布第一个版本  
2027.1 2026-11-27 补充2025年-2027年节假日及调休安排；新增 isWorkDay/isRestDay、工作日推算、LocalDate 重载、getHolidayEnum；isExchangeWordDay 更名为 isExchangeWorkDay；新增单元测试与 CI

问题反馈
====
欢迎提交issue，或者直接联系我(<text style="color:blue;font-size:16px">752628096@qq.com</text>)