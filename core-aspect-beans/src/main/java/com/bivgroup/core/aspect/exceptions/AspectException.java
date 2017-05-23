package com.bivgroup.core.aspect.exceptions;

/**
 * Created by andreykus on 19.09.2016.
 * Исключение для модуля аспекты
 */
public class AspectException extends Exception {
    /**
     * префикс сообщения
     */
    final static String PREFIX_MESSAGE = "aspect:";
    public Throwable detail;

    /**
     * конструктор Исключение для модуля аспекты
     */
    public AspectException() {
        initCause(null);
    }

    /**
     * конструктор Исключение для модуля аспекты
     *
     * @param s - сообщение
     */
    public AspectException(String s) {
        super(s);
        //initCause(null);
    }

    /**
     * конструктор Исключение для модуля аспекты
     *
     * @param cause - причина
     */
    public AspectException(Throwable cause) {
        super(cause);
        this.detail = cause;

    }

    /**
     * конструктор Исключение для модуля аспекты
     *
     * @param s - сообщение
     * @param cause -  причина
     */
    public AspectException(String s, Throwable cause) {
        super(s);
        // initCause(cause);
        detail = cause;
    }

    /**
     * получить сообщение
     * @return - сообщение
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
     * получить причину
     * @return -  причина
     */
    @Override
    public Throwable getCause() {
        return detail;
    }
}
