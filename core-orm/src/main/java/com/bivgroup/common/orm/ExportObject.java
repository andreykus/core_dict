package com.bivgroup.common.orm;

import java.util.Enumeration;

/**
 * Created by andreykus on 12.11.2016.
 * Экспортируемый объект
 */
public class ExportObject {
    /**
     * название модуля
     */
    public String moduleName;
    /**
     * класс
     */
    public String className;
    /**
     * тело
     */
    public StringBuffer body;
    /**
     * тип
     */
    public Enum type;

    /**
     * Конструктор Экспортируемый объект
     */
    public ExportObject() {
    }

    /**
     * Конструктор Экспортируемый объект
     *
     * @param moduleName - название модуля
     * @param className  - имя класса
     * @param body       - тело класса
     * @param type       -  тип
     */
    public ExportObject(String moduleName, String className, StringBuffer body, Enum type) {
        this.moduleName = moduleName;
        this.className = className;
        this.body = body;
        this.type = type;
    }
}
