package com.bivgroup.core.dictionary.dao;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 12.10.2016.
 * Шаблон - Базовые методы работы с сущностью
 * В отличии от AbstractGenericDAO - уже привязана к Entity
 */
public abstract class TemplateGenericDAO extends AbstractGenericDAO {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * название сущности
     */
    String entity;
    /**
     * сессия
     */
    Session session;

    /**
     * Конструктор Шаблон - Базовые методы работы с сущностью
     *
     * @param entityName -  название сущности
     * @param session    -  сессия
     */
    public TemplateGenericDAO(String entityName, Session session) {
        super(session);
        this.entity = entityName;
        this.session = session;

    }

    /**
     * создать критерий
     *
     * @return -  критерий
     */
    public Criteria createCriteria() {
        return createCriteria(entity);
    }

    /**
     * поиск по идентификатору
     *
     * @param id - идентификатор
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    public Map findById(Serializable id) throws DictionaryException {
        return super.findById(entity, id);
    }

    /**
     * поиск по параметрам
     *
     * @param instance - сущность
     * @return - список сущностей
     */
    public List<Map> findByExample(Map instance) {
        return findByCriterion(entity, Example.create(instance));
    }

    /**
     * поиск по параметрам
     *
     * @param instance - сущность
     * @return - сущность
     */
    public Map findOneExample(Map instance) {
        return findOneExample(entity, instance);
    }

    /**
     * поиск по критериям
     *
     * @param criterion -  критерии
     * @return - список сущностей
     */
    public List<Map> findByCriterion(Criterion... criterion) {
        return findByCriterion(entity, criterion);
    }

    /**
     * поиск по критериям
     *
     * @param criterion - критерии
     * @return - сущность
     */
    public Map findOneByCriterion(Criterion... criterion) {
        return (Map) findOneByCriterion(entity, criterion);
    }

    /**
     * поиск по критериям
     *
     * @param criterion -  критерии
     * @param orders    - порядок
     * @return - список сущностей
     */
    public List<Map> findByCriterion(Collection<Criterion> criterion, Collection<Order> orders) {
        return findByCriterion(entity, criterion, orders);
    }

    /**
     * поиск по критериям
     *
     * @param criterion -  критерии
     * @return - сущность
     */
    public Map findOneByCriterion(Collection<Criterion> criterion) {
        return findOneByCriterion(entity, criterion);
    }

    /**
     * обновить и связать
     *
     * @param detachedInstance - сущность
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    public Map merge(Map detachedInstance) throws DictionaryException {
        return merge(entity, detachedInstance);
    }

    /**
     * связь
     *
     * @param instance - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    public void attachDirty(Map instance) throws DictionaryException {
        attachDirty(entity, instance);
    }

    /**
     * очистить связь
     *
     * @param instance - сущность
     */
    public void attachClean(Map instance) {
        attachClean(entity, instance);
    }

    /**
     * сохранить сущность
     *
     * @param transientInstance
     * @return - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    public Map save(Map transientInstance) throws DictionaryException {
        save(entity, transientInstance);
        return transientInstance;
    }

    /**
     * удалить сущность
     *
     * @param persistentInstance - сущность
     * @throws DictionaryException - исключение словарной системы
     */
    public void delete(Map persistentInstance) throws DictionaryException {
        delete(entity, persistentInstance);
    }

}
