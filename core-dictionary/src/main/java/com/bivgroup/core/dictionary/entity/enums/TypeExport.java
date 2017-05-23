package com.bivgroup.core.dictionary.entity.enums;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.exporter.AspectClassExporter;
import com.bivgroup.core.dictionary.generator.exporter.DataToMappingExporter;
import com.bivgroup.core.dictionary.generator.exporter.Exporter;
import com.bivgroup.core.dictionary.generator.exporter.PojoClassExporter;
import org.hibernate.boot.Metadata;

/**
 * Created by bush on 26.10.2016.
 * Тип Экспорта
 */
public enum TypeExport {
    /**
     * метаданные как XML
     */
    XML("templates/hbm/hibernate-mapping.hbm.ftl", "XML") {
        @Override
        public Exporter getExporter(Object lpc, Metadata md, StringBuffer outputStream) {
            return new DataToMappingExporter(lpc, outputStream);
        }
    },
    /**
     * метаданные как java класс
     */
    CLASS("templates/pojo/Pojo.ftl", "CLASS") {
        @Override
        public Exporter getExporter(Object pojo, Metadata md, StringBuffer outputStream) {
            return new PojoClassExporter(pojo, md, outputStream);
        }
    },
    /**
     * аспекты
     */
    ASPECT("", "ASPECT") {
        @Override
        public Exporter getExporter(Object lent, Metadata md, StringBuffer outputStream) {
            return new AspectClassExporter(lent, outputStream);
        }
    };

    /**
     * файл шаблон
     */
    private String nameTamplate;
    /**
     * тип экспорта
     */
    private String nameExport;

    /**
     * Конструктор Тип Экспорта
     *
     * @param nameTamplate - айл шаблон
     * @param nameExport   - тип экспорта
     */
    TypeExport(String nameTamplate, String nameExport) {
        this.nameTamplate = nameTamplate;
        this.nameExport = nameExport;
    }

    /**
     * Фабрика обработчика - экспорта объекта
     *
     * @param obj          - входные параметры, наша метамодель
     * @param md           - метаданные
     * @param outputStream - выходной поток
     * @return - обработчик
     */
    public abstract Exporter getExporter(Object obj, Metadata md, StringBuffer outputStream);

    /**
     * наименование шаблона экспорта
     *
     * @return - шаблон экспорта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public String getNameTemplate() throws DictionaryException {
        return this.nameTamplate;
    }

    /**
     * наименование типа экспорта
     *
     * @return - наименование типа экспорта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public String getNameExport() throws DictionaryException {
        return this.nameExport;
    }

    /**
     * тип экспорта по наименованию
     *
     * @param value - наименование типа экспорта
     * @return - тип экспорта
     */
    public static TypeExport getTypeExportByValue(String value) {
        switch (value) {
            case "class":
                return CLASS;
            case "xml":
                return XML;
            case "aspect":
                return ASPECT;
            default:
                return XML;
        }
    }

}
