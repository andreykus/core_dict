package com.bivgroup.common.utils.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by bush on 15.08.2016.
 * конвертер даты
 */
public class DataConverter {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /** параметр GMT*/
    final public static String GMT_NAME = "GMT";
    /** начальная дата*/
    final public static String START_DATE = "25569.0";
    final public static Integer DELTA_CONSTANT = 86400000;
    final public static Integer ROUND_DIGIT = 15;
    /** параметр дата*/
    final public static String END_NAME_FIELD = "DATE";

    /**
     * текущая дата
     * @return - текущая дата
     */
    public  BigDecimal getCurrentDate() {
        long currentDateMs = (new Date()).getTime();
        long timeOffset = (long) TimeZone.getDefault().getOffset(currentDateMs);
        return convertDate(new Date(currentDateMs + timeOffset));
    }

    /**
     * дата без времени
     * @param time -  дата
     * @return - дата без времени
     */
    public  Date getDateWithoutTime(Date time) {
        Calendar date;
        if (time.toString().contains(GMT_NAME)) {
            date = Calendar.getInstance(TimeZone.getTimeZone(GMT_NAME));
        } else {
            date = Calendar.getInstance();
        }

        date.setTime(time);
        Calendar dateGMT = Calendar.getInstance(TimeZone.getTimeZone(GMT_NAME));
        dateGMT.set(1, date.get(1));
        dateGMT.set(2, date.get(2));
        dateGMT.set(5, date.get(5));
        dateGMT.set(11, 0);
        dateGMT.set(12, 0);
        dateGMT.set(13, 0);
        dateGMT.set(14, 0);
        return dateGMT.getTime();
    }


    /**
     * @deprecated
     */
    @Deprecated
    public  Date convertDate(Float time) {
        return new Date((long) (((double) time.floatValue() - (new Double(START_DATE)).doubleValue()) * DELTA_CONSTANT.doubleValue()));
    }

    /**
     * конвертер даты из типа Date в тип  BigDecimal
     * @param time - дата
     * @return - дата
     */
    public  BigDecimal convertDate(Date time) {
        return BigDecimal.valueOf((double) time.getTime() / DELTA_CONSTANT.doubleValue()).add(BigDecimal.valueOf((new Double(START_DATE)).doubleValue()));
    }

    /**
     * конвертер даты из типа Date в тип  Double
     * @param time- дата
     * @return - дата
     */
    public static Double convertDateToDuble(Date time) {
        return (Double) ((time.getTime() / DELTA_CONSTANT.doubleValue()) + new Double(START_DATE));
    }

    /**
     * конвертер даты в формат с Gmt
     * @param date - дата
     * @return - дата
     */
    public  Date convertToGmt(Date date) {
        return convert(date, TimeZone.getDefault(), TimeZone.getTimeZone(GMT_NAME));
    }

    /**
     * Конверт даты по зонам
     * @param date -  дата
     * @param sourceTimeZone - исходная зона
     * @param targetTimeZone - конечная зона
     * @return - дата
     */
    public  Date convert(Date date, TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int sourceOffset = sourceTimeZone.getOffset(date.getTime());
        int targetOffset = targetTimeZone.getOffset(date.getTime());
        int delta = targetOffset - sourceOffset;
        calendar.add(14, -delta);
        return calendar.getTime();
    }

    /**
     * Конверт даты из BigDecimal в Date
     * @param time - дата
     * @return -  дата
     */
    public  Date convertDate(BigDecimal time) {
        return new Date(time.subtract(new BigDecimal(START_DATE)).multiply(new BigDecimal(DELTA_CONSTANT)).round(new MathContext(ROUND_DIGIT)).longValue());
    }

    /**
     * конвертер даты из типа Double в тип  Date
     * @param time - дата
     * @return -  дата
     */
    public  Date convertDate(Double time) {
        return new Date((long) ((time.doubleValue() - (new Double(START_DATE)).doubleValue()) * DELTA_CONSTANT.doubleValue()));
    }

    /**
     * конвертер map с датой в BigDecimal
     * @param date - map с датой
     */
    public  void convertDateToFloat(Map<String, Object> date) {
        Iterator i = date.entrySet().iterator();

        while (i.hasNext()) {
            Entry item = (Entry) i.next();
            if (item.getValue() instanceof Date) {
                item.setValue(convertDate((Date) item.getValue()));
            }
        }
    }

    /**
     * конвертер map с датой в BigDecimal
     * @param date - map с датой
     * @param isTrunc - обрезать
     */
    public  void convertDateToString(Map<String, Object> date, boolean isTrunc) {
        Iterator i = date.entrySet().iterator();

        while (i.hasNext()) {
            Entry item = (Entry) i.next();
            if (item.getValue() instanceof Date) {
                if (isTrunc) {
                    item.setValue(Long.toString(convertDate((Date) item.getValue()).longValue()));
                } else {
                    item.setValue(convertDate((Date) item.getValue()).toString());
                }
            }
        }

    }

    /**
     * конверт из float в Date в map параметров
     * @param date - map с датой
     */
    public  void convertFloatToDate(Map<String, Object> date) {
        Iterator i = date.entrySet().iterator();

        while (true) {
            while (i.hasNext()) {
                Map.Entry item = (Map.Entry) i.next();
                if (((String) item.getKey()).toUpperCase().endsWith(END_NAME_FIELD)) {
                    if (item.getValue() instanceof Double) {
                        item.setValue(convertDate((Double) item.getValue()));
                    } else if (item.getValue() instanceof Float) {
                        item.setValue(convertDate((Float) item.getValue()));
                    } else if (item.getValue() instanceof BigDecimal) {
                        item.setValue(convertDate((BigDecimal) item.getValue()));
                    } else if (item.getValue() instanceof Integer) {
                        item.setValue(convertDate(Double.valueOf(((Integer) item.getValue()).doubleValue())));
                    }
                } else if (item.getValue() instanceof List) {
                    Iterator i1 = ((List) item.getValue()).iterator();

                    while (i1.hasNext()) {
                        Object obj = i1.next();
                        if (obj instanceof Map) {
                            convertFloatToDate((Map) obj);
                        }
                    }
                } else if (item.getValue() instanceof Map) {
                    convertFloatToDate((Map) item.getValue());
                }
            }
            return;
        }
    }

    public  <T> T defaultMapKeyConvert(Map<String, T> map, String key, T obj) {
        if (map.get(key) == null) {
            map.put(key, obj);
        }
        return (T) map.get(key);
    }

    public  <T> void setDefaultMapKeyConvert(Map<String, T> map, String key, T obj) {
        if (map.get(key) == null) {
            map.put(key, obj);
        }
    }

    /**
     * если пустая сирока
     * @param s - строка
     * @return -  если пустая строка true
     */
    public  boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }
}
