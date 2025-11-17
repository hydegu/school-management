package com.example.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期时间工具类
 */
public class DateUtils {

    /**
     * 常用日期格式
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATE_COMPACT = "yyyyMMdd";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";

    /**
     * 常用格式化器
     */
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(PATTERN_DATETIME);
    public static final DateTimeFormatter FORMATTER_DATETIME_FULL = DateTimeFormatter.ofPattern(PATTERN_DATETIME_FULL);

    private DateUtils() {
        // 工具类，禁止实例化
    }

    // ==================== 获取当前时间 ====================

    /**
     * 获取当前日期
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间
     */
    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     */
    public static long currentTimeSeconds() {
        return Instant.now().getEpochSecond();
    }

    // ==================== 格式化 ====================

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return 格式化后的字符串 (yyyy-MM-dd)
     */
    public static String formatDate(LocalDate date) {
        return date == null ? null : date.format(FORMATTER_DATE);
    }

    /**
     * 格式化日期时间
     *
     * @param dateTime 日期时间
     * @return 格式化后的字符串 (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(FORMATTER_DATETIME);
    }

    /**
     * 格式化日期时间（自定义格式）
     *
     * @param dateTime 日期时间
     * @param pattern  格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期（自定义格式）
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 解析 ====================

    /**
     * 解析日期字符串
     *
     * @param dateStr 日期字符串 (yyyy-MM-dd)
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return LocalDate.parse(dateStr, FORMATTER_DATE);
    }

    /**
     * 解析日期时间字符串
     *
     * @param dateTimeStr 日期时间字符串 (yyyy-MM-dd HH:mm:ss)
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, FORMATTER_DATETIME);
    }

    /**
     * 解析日期时间字符串（自定义格式）
     *
     * @param dateTimeStr 日期时间字符串
     * @param pattern     格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtils.isBlank(dateTimeStr) || StringUtils.isBlank(pattern)) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 转换 ====================

    /**
     * Date 转 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Date 转 LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * LocalDateTime 转 Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate 转 Date
     */
    public static Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 时间戳（毫秒）转 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转时间戳（毫秒）
     */
    public static long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    // ==================== 计算 ====================

    /**
     * 计算两个日期之间的天数
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的小时数
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * 计算两个日期时间之间的分钟数
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * 计算两个日期时间之间的秒数
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(start, end);
    }

    // ==================== 日期操作 ====================

    /**
     * 增加天数
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date == null ? null : date.plusDays(days);
    }

    /**
     * 增加月数
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        return date == null ? null : date.plusMonths(months);
    }

    /**
     * 增加年数
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        return date == null ? null : date.plusYears(years);
    }

    /**
     * 减少天数
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date == null ? null : date.minusDays(days);
    }

    /**
     * 获取月份的第一天
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取月份的最后一天
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取年份的第一天
     */
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取年份的最后一天
     */
    public static LocalDate getLastDayOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfYear());
    }

    // ==================== 判断 ====================

    /**
     * 判断日期是否在范围内
     */
    public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    /**
     * 判断是否为周末
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 判断是否为工作日
     */
    public static boolean isWorkday(LocalDate date) {
        return !isWeekend(date);
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(LocalDate date) {
        return date != null && date.isLeapYear();
    }
}
