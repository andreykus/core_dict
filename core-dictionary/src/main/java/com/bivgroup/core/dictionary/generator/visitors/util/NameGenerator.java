package com.bivgroup.core.dictionary.generator.visitors.util;

import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.entity.SadEntity;
import org.hibernate.mapping.PersistentClass;

/**
 * Created by andreykus on 18.09.2016.
 * Генератор имен для объектов метаданных
 */
public class NameGenerator {
    /**
     * имя пакета
     */
    public static final String DEFAULT_PACKAGE_NAME = "com.bivgroup";
    /**
     * постфикс ссылки
     */
    public static final String REF_TAG_M2O = "_EN";
    /**
     * дискриминатор
     */
    public static final String DISCRIMINATOR = "DISCRIMINATOR";

    /**
     * Имя сущности
     *
     * @param ent - сущности
     * @return - имя сущности
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public String getEntityName(SadEntity ent) throws DictionaryException {
        if (ent == null || ent.getSysname() == null)
            throw new DictionaryException(String.format("On Entity not set requred param entity name %1s", ent.getSysname()));
        char c[] = ent.getSysname().toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        // return DEFAULT_PACKAGE_NAME + '.' + "en_" + new String(c);
        return new String(c);
    }

    /**
     * Имя списка
     *
     * @param name - имя списка
     * @return - имя списка
     */
    public String getEntityNameList(String name) {
        return name;//pc.getEntityName() + "List";
    }

    /**
     * Имя таблицы
     *
     * @param name - имя таблицы в БД
     * @return - имя таблицы
     */
    public String getTableName(String name) {
        return name;
    }

    /**
     * Имя колонки
     *
     * @param name - имя колонки в БД
     * @return - имя колонки
     */
    public String getColumnName(String name) {
        return name;
    }

    /**
     * Имя свойства
     *
     * @param name - имя свойства
     * @return - имя свойства
     */
    public String getPropertyName(String name) {
        return name;
    }

    /**
     * Имя класса
     *
     * @param ent - сущность
     * @return - имя класса
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной системы
     */
    public String getClassName(SadEntity ent) throws DictionaryException {
        if (ent == null || ent.getSysname() == null)
            throw new DictionaryException(String.format("On Entity not set requred param entity name %1s module name %2s", ent.getSysname(), ent.getModule()));
        char tempchar[] = ent.getSysname().toCharArray();
        tempchar[0] = Character.toUpperCase(tempchar[0]);
        return new StringBuffer().append(getModulePackage(ent.getModule())).append('.').append(tempchar).toString();
    }

    /**
     * пакет модуля
     *
     * @param module - модуль
     * @return -  имя пакет модуля
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public String getModulePackage(SadModule module) throws DictionaryException {
        if (module == null) return DEFAULT_PACKAGE_NAME;
        if (module != null && module.getArtifactId() == null)
            throw new DictionaryException(String.format("On Module not set requred param ArtifactId name %1s", module.getArtifactId()));
        return new StringBuffer().append(module.getGroupId() != null ? module.getGroupId() : DEFAULT_PACKAGE_NAME).append('.').append(module.getArtifactId()).toString();
    }

    /**
     * Имя  индекса
     *
     * @param name - имя  индекса в БД
     * @return - имя  индекса
     */
    public String getIndexName(String name) {
        return name;
    }

    /**
     * Имя первичного ключа
     *
     * @param name - имя первичного ключа в БД
     * @return - имя первичного ключа
     */
    public String getPkName(String name) {
        return "pk_" + name;
    }

    /**
     * Имя вторичного ключа
     *
     * @param name - имя вторичного ключа в БД
     * @return - имя вторичного ключа
     */
    public String getFkName(String name) {
        return "fk_" + name;
    }

    /**
     * Имя сойства
     * TODO удалить -  не используется
     *
     * @param pc - описание сущности
     * @param firstPropertyName - нативное название свойства
     * @return - название свойства
     */
    public String getPropertyName(PersistentClass pc, String firstPropertyName) {
        StringBuilder propertyNameBuffer = new StringBuilder("_");
        propertyNameBuffer.append(pc.getEntityName());
        propertyNameBuffer.append("_").append(firstPropertyName);
        propertyNameBuffer.append("_").append(pc.getTable());

        return propertyNameBuffer.toString();
    }

    /**
     * Имя дискриминатора
     *
     * @param name - имя дискриминатора
     * @return - имя дискриминатора
     */
    public String getDiscriminatorName(String name) {
        char c[] = name.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return "ds_" + new String(c);
    }

    /**
     * Имя колонки дискриминатора
     *
     * @param name - имя колнки дискриминатора в БД
     * @return - имя колонки дискриминатора
     */
    public String getDiscriminatorColumnName(String name) {
        return DISCRIMINATOR;
    }

    /**
     * Коммент
     *
     * @param note - коммент в БД
     * @return - коммент
     */
    public String getComment(String note) {
        return "";
        //new String(note.getBytes(), Charset.forName("UTF-8"));
    }

    /**
     * Имя ссылки
     *
     * @param name - имя ссылки в БД
     * @return - имя ссылки
     */
    public String getOneToManyPropertyName(String name) {
        return name + REF_TAG_M2O;
    }
}
