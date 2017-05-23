package com.bivgroup.common.utils.converter;

/**
 * Created by bush on 15.08.2016.
 * конвертер
 */
public interface ObjectConverter {
    /**
     * ковертировать объект
     *
     * @param object - исходный объект
     * @return - выходной объект
     */
    <T> Object convertToObject(T object);
}
