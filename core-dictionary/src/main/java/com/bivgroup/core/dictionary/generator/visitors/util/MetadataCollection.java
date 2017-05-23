package com.bivgroup.core.dictionary.generator.visitors.util;

import com.bivgroup.core.dictionary.common.AbstractCollection;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

import java.util.*;

/**
 * Created by andreykus on 18.09.2016.
 * Хранилище PersistentClass,Table,SadEntity  построенных по данным в БД
 */
public class MetadataCollection implements AbstractCollection {
    /**
     * PersistentClass -ы
     */
    public Map<String, PersistentClass> persistentsClass;
    /**
     * PersistentClass -ы  на модуле
     */
    public Map<String, List<PersistentClass>> persistentsClassOnModules;
    /**
     * сущности на модуле
     */
    public Map<String, List<SadEntity>> entityOnModules;
    /**
     * таблицы
     */
    public Map<String, Table> tables;
    /**
     * метаданные БД
     */
    public MetadataImplementor metadata;

    public TypeExport type;

    /**
     * Конструктор Хранилище PersistentClass,Table,SadEntity
     */
    public MetadataCollection() {
        persistentsClass = new HashMap<String, PersistentClass>();
        persistentsClassOnModules = new HashMap<String, List<PersistentClass>>();
        entityOnModules = new HashMap<String, List<SadEntity>>();
        tables = new HashMap<String, Table>();
        this.type = TypeExport.XML;
    }

    /**
     * установить метаданные из SessionFactory
     *
     * @param metadata - метаданные
     */
    public void setMetadata(MetadataImplementor metadata) {
        this.metadata = metadata;
    }

    /**
     * добавить PersistentClass
     *
     * @param clazz - PersistentClass
     */
    public void addClass(PersistentClass clazz) {
        persistentsClass.put(clazz.getClassName(), clazz);
    }

    /**
     * добавить PersistentClass для модуля (используем в группировке)
     *
     * @param modules - название модуля
     * @param clazz   - PersistentClass
     */
    public void addClassWithModules(String modules, PersistentClass clazz) {
        addClass(clazz);
        if (persistentsClassOnModules.get(modules != null ? modules : "unknow") == null) {
            persistentsClassOnModules.put(modules != null ? modules : "unknow", (new ArrayList<PersistentClass>(Arrays.asList(clazz))));
        } else {
            persistentsClassOnModules.get(modules != null ? modules : "unknow").add(clazz);
        }
    }

    /**
     * добавить сущность для модуля (используем в группировке)
     *
     * @param modules - название модуля
     * @param entity  - сущность
     */
    public void addEntityWithModules(String modules, SadEntity entity) {
        if (entityOnModules.get(modules != null ? modules : "unknow") == null) {
            entityOnModules.put(modules != null ? modules : "unknow", (new ArrayList<SadEntity>(Arrays.asList(entity))));
        } else {
            entityOnModules.get(modules != null ? modules : "unknow").add(entity);
        }
    }

    /**
     * получить PersistentClass по имени
     *
     * @param className - имя класса
     * @return - PersistentClass
     */
    public PersistentClass getClass(String className) {
        return persistentsClass.get(className);
    }

    /**
     * получить метаданные ORM
     *
     * @return - метаданные
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * добавить таблицу
     *
     * @param table - таблица
     */
    public void addTable(Table table) {
        tables.put(table.getName(), table);
    }

    /**
     * получить таблицу по имени
     *
     * @param tableName -  название таблицы
     * @return - таблица
     */
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    /**
     * установить тип экспорта
     *
     * @param type - тип экспорта
     */
    public void setExportType(TypeExport type) {
        this.type = type;
    }

    /**
     * получить тип экспорта
     *
     * @return - тип жкспорта
     */
    public TypeExport getExportType() {
        return this.type;
    }
}
