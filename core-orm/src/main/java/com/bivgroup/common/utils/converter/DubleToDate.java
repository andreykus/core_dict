package com.bivgroup.common.utils.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * Created by bush on 15.08.2016.
 * процессор конвертер даты Dunle - Date
 */
public class DubleToDate implements ObjectConverter {
    /**логгер*/
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /**
     * конвертер
     * @param object - исходный объект
     * @param <T> - тип конвертирования
     * @return -  дата
     */
    public <T> Object convertToObject(T object) {
        return convertDate((Double) object);
    }

    /**
     * конвертер даты
     * @param time - дата
     * @return - дата
     */
    public Date convertDate(Double time) {
        return new Date((long) ((time.doubleValue() - (new Double(DataConverter.START_DATE)).doubleValue()) * DataConverter.DELTA_CONSTANT.doubleValue()));
    }
}
