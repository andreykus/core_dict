package com.bivgroup.core.dictionary.dao;

import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by bush on 13.10.2016.
 * Интерфейс для работы с Сущностями
 *
 * @param <MAP> - параметизированный дао
 */
public interface GenericDAO<MAP> {
    /**
     * получить сесию
     *
     * @return -  сессия
     */
    Session getSession();

    /**
     * получить источник данных
     *
     * @return - источник данных
     */
    DataSource getDataSource();

    /**
     * название модуля
     *
     * @return - название модуля
     */
    String getModuleName();

    /**
     * создать критерий
     *
     * @param entityName - название сущности
     * @return - критерий
     */
    Criteria createCriteria(String entityName);

    /**
     * поиск по идентификатору
     *
     * @param entityName -  название сущности
     * @param id         -  идентификатор
     * @return - сущность
     * @throws DictionaryException -  исключение словарной системы
     */
    @AspectMethod(action = Action.READ)
    MAP findById(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") Serializable id) throws DictionaryException;

    /**
     * поиск по параметрам
     *
     * @param entityName -  название сущности
     * @param instance   -  параметры
     * @return - список сущностей
     */
    @AspectMethod(action = Action.READ)
    List<MAP> findByExample(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP instance);

    /**
     * поиск по параметрам
     *
     * @param entityName -  название сущности
     * @param instance   -  параметры
     * @return - сущность
     */
    @AspectMethod(action = Action.READ)
    MAP findOneExample(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP instance);

    /**
     * поиск по критериям
     *
     * @param entityName -  название сущности
     * @param criterion  -  критерий
     * @return - список сущностей
     */
    @AspectMethod(action = Action.READ)
    List<MAP> findByCriterion(@AspectMethodParam(nameParam = "entityName") String entityName, Criterion... criterion);

    /**
     * поиск по критериям
     *
     * @param entityName -  название сущности
     * @param criterion  -  критерий
     * @return - сущность
     */
    @AspectMethod(action = Action.READ)
    MAP findOneByCriterion(@AspectMethodParam(nameParam = "entityName") String entityName, Criterion... criterion);

    /**
     * поиск по критериям по порядку
     *
     * @param entityName -  название сущности
     * @param criterion  -  критерий
     * @param orders     - порядок
     * @return - список сущностей
     */
    @AspectMethod(action = Action.READ)
    List<MAP> findByCriterion(@AspectMethodParam(nameParam = "entityName") String entityName, Collection<Criterion> criterion, Collection<Order> orders);

    /**
     * поиск по критериям
     *
     * @param entityName -  название сущности
     * @param criterion  -  критерий
     * @return - сущность
     */
    @AspectMethod(action = Action.READ)
    MAP findOneByCriterion(@AspectMethodParam(nameParam = "entityName") String entityName, Collection<Criterion> criterion);

    /**
     * обновить сущность (с связью если нет)
     *
     * @param entityName       -  название сущности
     * @param detachedInstance - сущность
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.UPDATE)
    MAP merge(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP detachedInstance) throws DictionaryException;

    void attachDirty(String entityName, MAP instance) throws DictionaryException;

    void attachClean(String entityName, MAP instance);

    /**
     * сохранить сущность (если есть обновить)
     *
     * @param entityName        -  название сущности
     * @param transientInstance - сущность
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.CREATE)
    MAP save(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP transientInstance) throws DictionaryException;

    /**
     * удалить сущность
     *
     * @param entityName         -  название сущности
     * @param persistentInstance - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.DELETE)
    void delete(@AspectMethodParam(nameParam = "entityName") String entityName, MAP persistentInstance) throws DictionaryException;

    /**
     * обновить сущность
     *
     * @param entityName        -  название сущности
     * @param transientInstance - сущность
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.UPDATE)
    MAP update(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP transientInstance) throws DictionaryException;

    /**
     * сохранить сущность
     *
     * @param entityName        -  название сущности
     * @param transientInstance - сущность
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.CREATE)
    MAP onlySave(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") MAP transientInstance) throws DictionaryException;

    /**
     * количество записей
     *
     * @param entityName -  название сущности
     * @return - счетчик
     * @throws DictionaryException - исключение словарной системы
     */
    @AspectMethod(action = Action.READ)
    long getCount(@AspectMethodParam(nameParam = "entityName") String entityName) throws DictionaryException;

}
