package com.bivgroup.core.dictionary.exceptions;

import com.bivgroup.common.orm.OrmException;

/**
 * Created by andreykus on 19.09.2016.
 * Исключение для модуля - Словарная система
 */
public class DictionaryException extends OrmException {
    /** префикс сообщения*/
    final static String PREFIX_MESSAGE = "dictionary:";
    public Throwable detail;

    /**
     * конструктор Исключение для модуля - Словарная система
     */
    public DictionaryException() {
        initCause(null);
    }

    /**
     * конструктор Исключение для модуля - Словарная система
     * @param s - сообщения
     */
    public DictionaryException(String s) {
        super(s);
        //initCause(null);
    }

    /**
     * конструктор Исключение для модуля - Словарная система
     * @param cause - причина
     */
    public DictionaryException(Throwable cause) {
        super(cause);
        this.detail = cause;
    }

    /**
     * конструктор Исключение для модуля - Словарная система
     * @param s - сообщение
     * @param cause - причина
     */
    public DictionaryException(String s, Throwable cause) {
        super(s);
        initCause(null);
        detail = cause;
    }

    /**
     * Формирование сообщения при ошибке
     * @return - строка сообщения
     */
    @Override
    public String getMessage() {
        if (detail == null) {
            return PREFIX_MESSAGE + super.getMessage();
        } else {
            return PREFIX_MESSAGE + super.getMessage() + "; nested exception is: \n\t" +
                    detail.toString();
        }
    }

    /**
     * получить сообщение
     * @return
     */
    @Override
    public Throwable getCause() {
        return detail;
    }
}
