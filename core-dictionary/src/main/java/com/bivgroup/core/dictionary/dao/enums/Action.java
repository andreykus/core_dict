package com.bivgroup.core.dictionary.dao.enums;

import com.bivgroup.core.dictionary.dao.GenericDAO;
import com.bivgroup.core.dictionary.dao.hierarchy.EntityMap;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.metamodel.internal.EntityTypeImpl;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 13.10.2016.
 * Действия над сущностью
 */
public enum Action {

    /**
     * Действие - проверка, есть ли данная сущность в метаданных
     */
    CHECK {
        @Override
        public void run(GenericDAO dao, EntityMap map) throws DictionaryException {
            chekParentAction(map);
            chekCorrectMap(dao, map);
            chekExistId(dao, map);
        }
    },
    /**
     * Действие - CRUD опреация , определяется параметром RowStatus
     */
    CRUD {
        @Override
        public void run(GenericDAO dao, EntityMap map) throws DictionaryException {
            Map obj = map.getEntityMap();
            RowStatus rowSatus = map.rowStatus;
            rowSatus.processEntity(dao, obj);
        }
    };
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Исполнение действия над сущностью
     *
     * @param dao - менеджер работы с данными
     * @param map - сущность
     * @throws DictionaryException - исключение словарной сисистемы
     */
    public abstract void run(GenericDAO dao, EntityMap map) throws DictionaryException;

    /**
     * Проверка корректности мапы
     *
     * @param dao - Дао
     * @param map - сущность
     * @throws DictionaryException - исключение словарной сисистемы
     */
    protected void chekCorrectMap(GenericDAO dao, EntityMap map) throws DictionaryException {
        if (map.getEntityName() == null) return;
        EntityType et = dao.getSession().getSessionFactory().getMetamodel().entity(map.getEntityName());
        if (et == null) {
            throw new DictionaryException(String.format("this entity %1s not found in metamodel", map.getEntityName()));
        }
    }


    /**
     * Проверка возможности проведения операции CRUD, для дочерней сущности
     *
     * @param map - сущность
     * @throws DictionaryException - исключение словарной сисистемы
     */
    protected void chekParentAction(EntityMap map) throws DictionaryException {
        List anyList = new ArrayList<>(map.parentAction);
        if (map.parentAction.size() > 1 && (map.parentAction.contains(RowStatus.DELETED) && map.parentAction.indexOf(RowStatus.DELETED) != (map.parentAction.size() - 1))) {
            map.getEntityMap().put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
            map.rowStatus = RowStatus.UNMODIFIED;
        }
        anyList.removeAll(map.rowStatus.getAllowStatus());
        if (!anyList.isEmpty()) {
            throw new DictionaryException(String.format("action %1s not allow for root action %2s", map.rowStatus, anyList));
        }
    }

    /**
     * Название поля идентификатора для сущности
     *
     * @param dao - дао
     * @param map - сущность
     * @return - аттрибут
     * @throws DictionaryException - исключение словарной сисистемы
     */
    private static Attribute getIdField(GenericDAO dao, EntityMap map) throws DictionaryException {
        EntityType et = dao.getSession().getSessionFactory().getMetamodel().entity(map.getEntityName());
        if (et == null) {
            throw new DictionaryException(String.format("this entity %1s not found in metamodel", map.getEntityName()));
        }
        return ((EntityTypeImpl) et).getId(Long.class);
    }

    /**
     * Наличие идентификатора в сущности
     *
     * @param dao - дао
     * @param map - сущность
     * @throws DictionaryException - исключение словарной сисистемы
     */
    private static void chekExistId(GenericDAO dao, EntityMap map) throws DictionaryException {
        if (Arrays.asList(RowStatus.MODIFIED, RowStatus.DELETED).contains(map.rowStatus)) {
            if (map.getEntityMap().get(getIdField(dao, map).getName()) == null) {
                throw new DictionaryException(String.format("on entity %1s for this action %2s need identified", map.getEntityName(), map.rowStatus));
            }
        }
    }

}
