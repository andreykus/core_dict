package com.bivgroup.common.utils.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * Created by bush on 15.08.2016.
 * процессор конвертер даты Float - Date
 */
public class FloadToDate implements ObjectConverter {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /**
     * конвертер
     *
     * @param object - исходный объект
     * @param <T>    - тип конвертирования
     * @return -  дата
     */
    public <T> Object convertToObject(T object) {
        return convertDate((BigDecimal) object);
    }

    /**
     * конвертер даты
     *
     * @param time - дата
     * @return - дата
     */
    public Date convertDate(BigDecimal time) {
        return new Date(time.subtract(new BigDecimal(DataConverter.START_DATE)).multiply(new BigDecimal(DataConverter.DELTA_CONSTANT)).round(new MathContext(DataConverter.ROUND_DIGIT)).longValue());
    }
}
