package com.bivgroup.common.orm;

/**
 * Created by bush on 17.08.2016.
 * исключение модуля  ORM
 */
public class OrmException extends Exception {
    /**
     * префикс сообщения
     */
    final static String PREFIX_MESSAGE = "orm:";
    public Throwable detail;

    /**
     * Констуктор исключение модуля  ORM
     */
    public OrmException() {
        initCause(null);
    }

    /**
     * Констуктор исключение модуля  ORM
     * @param s - сообщение
     */
    public OrmException(String s) {
        super(s);
        initCause(null);
    }

    /**
     * Констуктор исключение модуля  ORM
     * @param cause - причина
     */
    public OrmException(Throwable cause) {
        super(cause);
        this.detail = cause;

    }

    /**
     * Констуктор исключение модуля  ORM
     * @param s - сообщение
     * @param cause  - причина
     */
    public OrmException(String s, Throwable cause) {
        super(s, cause);
        //initCause(cause);
        detail = cause;
    }

    /**
     * получить сообщение
     *
     * @return -  сообщение
     */
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
     *
     * @return
     */
    public Throwable getCause() {
        return detail;
    }

}
