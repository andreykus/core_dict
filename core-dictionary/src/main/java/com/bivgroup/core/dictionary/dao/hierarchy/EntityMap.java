package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import org.hibernate.tuple.DynamicMapInstantiator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andreykus on 06.11.2016.
 * Хранилище - Элемент генерируемый при обходе иерархии структуры
 * Содержит наименование сущности, сужность, действие над сущностью, цепочку пред действий, родительскую сущность, параметры восстановления - используется при отсроченном обновлении
 */
public class EntityMap {
    /**
     * сущность
     */
    Map entity;
    /**
     * родительская сущность
     */
    public EntityMap prevEntity;
    /**
     * первичный ключ сущности
     */
    public String primaryKeyName;
    /**
     * действия над всемит родительскими сущностями
     */
    public List<RowStatus> parentAction;
    /**
     * действие над сущностью
     */
    public RowStatus rowStatus;
    public Boolean isRoot = false;
    /**
     * сущность для восстановления всех ссылок
     */
    public Map<Object, Object> restoresRef = new HashMap<>();

    /**
     * восстановление объекта, для отсроченного обновления
     */
    public void restore() {
        for (Map.Entry ref : restoresRef.entrySet()) {
            entity.put(ref.getKey(), ref.getValue());
        }
    }

    /**
     * Конструктор  Хранилище - Элемент генерируемый при обходе иерархии структуры
     *
     * @param prevMap        -  родительска сущность
     * @param map            -  сущность
     * @param primaryKeyName - первичный ключ
     */
    public EntityMap(EntityMap prevMap, Map map, String primaryKeyName) {
        if (prevMap == null) this.isRoot = true;
        this.parentAction = new ArrayList<>();
        this.entity = map;
        this.prevEntity = prevMap;
        this.primaryKeyName = primaryKeyName;
        this.rowStatus = RowStatus.getRowStatusByMap(entity);
        if (this.prevEntity != null) this.parentAction.addAll(this.prevEntity.parentAction);
        this.parentAction.add(RowStatus.getRowStatusByMap(entity));
    }

    /**
     * Получить название сущности
     *
     * @return -  наименование сущности
     */
    public String getEntityName() {
        return entity.get(DynamicMapInstantiator.KEY).toString();
    }

    /**
     * Получить сущность
     *
     * @return - сущность
     */
    public Map getEntityMap() {
        return entity;
    }

    /**
     * Равенство
     *
     * @param obj - объект сравнения
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Map) {
            return (this.getEntityMap() == obj || equalsByID(obj));
        } else if (obj instanceof EntityMap) {
            return (this.getEntityMap() == ((EntityMap) obj).getEntityMap() || equalsByID(obj));
        } else return false;

    }

    /**
     * Равенство по идентификатору
     *
     * @param obj - объект сравнения
     * @return
     */
    public boolean equalsByID(Object obj) {
        if (this.getEntityMap().get(primaryKeyName) == null) return false;
        if (obj instanceof Map) {
            Map m = ((Map) obj);
            if (m.get(primaryKeyName) == null) return false;
            return (this.getEntityName().equals(m.get(DynamicMapInstantiator.KEY))) && (m.get(primaryKeyName).equals(this.getEntityMap().get(primaryKeyName)));
        } else if (obj instanceof EntityMap) {
            Map m = ((EntityMap) obj).getEntityMap();
            if (m.get(primaryKeyName) == null) return false;
            return this.getEntityName().equals(m.get(DynamicMapInstantiator.KEY)) && (m.get(primaryKeyName).equals(this.getEntityMap().get(primaryKeyName)));
        } else return false;
    }
}
