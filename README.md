# ChineseCalendar
判断是否是法定节假日，git上只看到了python版本(https://github.com/LKI/chinese-calendar)。  
为了调用方便,自己实现一个java版本。

该项目用来判断某年某月某一天是不是工作日/节假日。支持<text style="color:red;font-size:20px;font-weight:bold">2004年</text>至<text style="color:red;font-size:20px;font-weight:bold">2024年</text>，包括2020年的春节延长。  

由于次年的节假日安排，取决于国务院发布的日程。所以本项目一般会在国务院更新以后，发布新的版本。  

按照以往的经验，一般是每年的<text style="color:red;font-size:20px;font-weight:bold">11月</text>前后发布新版本。  

安装
====
maven
----
    <dependency>
        <groupId>io.github.gw752628096</groupId>
        <artifactId>chinese-calendar</artifactId>
        <version>1.0.0-RELEASE</version>
    </dependency>
Gradle
----
    implementation group: 'io.github.gw752628096', name: 'chinese-calendar', version: '1.0.0-RELEASE'

使用
====
    //是否是节假日
    HolidayUtil.isHoliday("20241001");
    HolidayUtil.isHoliday(new Date());
    HolidayUtil.isHoliday(19241920293L);
    
    //是否是调休
    HolidayUtil.isExchangeWordDay("20241001");
    HolidayUtil.isExchangeWordDay(new Date());
    HolidayUtil.isExchangeWordDay(19241920293L);

    //获取节日中文名
    HolidayUtil.getHolidayCnName("20241001");
    HolidayUtil.getHolidayCnName(new Date());
    HolidayUtil.getHolidayCnName(19241920293L);

    //获取节日英文名
    HolidayUtil.getHolidayEnName("20241001");
    HolidayUtil.getHolidayEnName(new Date());
    HolidayUtil.getHolidayEnName(19241920293L);

贡献代码
====
1.Fork + Clone项目到本地  
2.修改[节假日定义](https://github.com/gw752628096/chineseCalendar/blob/main/src/main/java/com/holiday/constants/VacationConstant.java)  
3.提交PR  

更新日志
====
1.0.0-RELEASE 2020-04-24 发布第一个版本

迭代
====
1.增加判断是否需要上班的功能  
2.增加节气相关功能  
3.增加生肖相关功能  
4.其他

问题反馈
====
欢迎提交issue，或者直接联系我(<text style="color:blue;font-size:16px">752628096@qq.com</text>)