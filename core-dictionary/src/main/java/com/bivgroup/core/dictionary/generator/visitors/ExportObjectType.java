package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.hibernate.boot.MetadataSources;

import java.io.ByteArrayInputStream;

/**
 * Created by andreykus on 12.11.2016.
 * Тип экспорта
 */
public enum ExportObjectType {
    /**
     * xml файл -- hbm
     */
    XML {
        @Override
        protected void export(MetadataSources metadata, ExportObject object) throws DictionaryException {
            if (object.body == null || object.body.toString().equals("")) {
                return;
            }
            metadata.addInputStream(new ByteArrayInputStream(object.body.toString().getBytes()));
        }
    },
    /**
     * класс
     */
    CLASS {
        @Override
        protected void export(MetadataSources metadata, ExportObject object) throws DictionaryException {
            //TODO сделать копиляцию классов
            throw new DictionaryException("this type export not support runtime");
        }
    },
    /**
     * аспект
     */
    ASPECT {
        @Override
        protected void export(MetadataSources metadata, ExportObject object) throws DictionaryException {
            //do nothing
        }
    };

    /**
     * шаблонный метод экспорта
     *
     * @param metadata - хранилище метаданных
     * @param object   - объект экспорта
     * @throws DictionaryException - исключение словарной системы
     */
    protected abstract void export(MetadataSources metadata, ExportObject object) throws DictionaryException;

    /**
     * Получить тип экспорта по наименованию
     *
     * @param name - наименование типа экуспорта
     * @return - тип экспорта
     * @throws DictionaryException - исключение словарной системы
     */
    public static ExportObjectType getByName(String name) throws DictionaryException {
        for (ExportObjectType el : ExportObjectType.values()) {
            if (name.equals(el.name())) return el;
        }
        throw new DictionaryException(String.format("element with name %1s not found in ExportObjectType", name));
    }

    /**
     * Экспортировать объект в метаданные
     *
     * @param metadata - хранилище метаданных
     * @param object   - объект экспорта
     * @throws DictionaryException - исключение словарной системы
     */
    public static void exportObject(MetadataSources metadata, ExportObject object) throws DictionaryException {
        ExportObjectType el = getByName(object.type.name());
        el.export(metadata, object);
    }
}
