package com.bivgroup.core.dictionary.dao;

import com.bivgroup.core.dictionary.dao.hierarchy.ActionOnMap;
import com.bivgroup.core.dictionary.dao.hierarchy.EntityMap;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.*;

/**
 * Created by andreykus on 12.09.2016.
 * Базовые методы работы с сущностью
 */

public abstract class AbstractGenericDAO implements ExtendGenericDAO {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * метамодель БД
     */
    protected Metamodel metamodel;
    /**
     * сессия
     */
    protected Session session;
    /**
     * граф сущностей для обработки
     */
    protected List<EntityMap> listGraf;
    /**
     * прокси Дао
     */
    protected Object proxy;

    /**
     * установить прокси
     *
     * @param proxy
     */
    @Override
    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    /**
     * получить прокси, если нет , то он this
     *
     * @return
     */
    @Override
    public Object getProxy() {
        if (proxy == null) proxy = this;
        return proxy;
    }

    /**
     * Конструктор базового Дао
     * @param session -  сессия
     */
    public AbstractGenericDAO(Session session) {
        this.session = session;
        this.metamodel = this.getSession().getSessionFactory().getMetamodel();
        init();
    }

    /**
     * инициализация аспектов
     */
    void initAspect() {
        Map<String, EntityPersister> mapPers = ((MetamodelImpl) metamodel).entityPersisters();
        for (Map.Entry<String, EntityPersister> persisten : mapPers.entrySet()) {
            persisten.getValue();
        }
    }

    /**
     * инициализация
     */
    void init() {
        initAspect();
    }

    /**
     * Получить сессию
     *
     * @return - сессия
     */
    @Override
    public Session getSession() {
        return session;
    }


    /**
     * Тип сущности для поля в родительской сущности
     *
     * @param entity - сущность
     * @param field  - поле
     * @return - тип сущногсти
     * @throws DictionaryException
     */
    public EntityType getType(EntityMap entity, String field) throws DictionaryException {
        return (EntityType) getEntityByName(entity.getEntityName()).getSingularAttribute(field).getType();
    }

    /**
     * Тип сущности по названию сущности
     *
     * @param entityName - наименование сущности
     * @return - тип сущногсти
     * @throws DictionaryException
     */
    public EntityType getEntityByName(String entityName) throws DictionaryException {
        EntityType et = metamodel.entity(entityName);
        if (et == null) throw new DictionaryException(String.format("Not found entity  %1s from metadata", entityName));
        return et;
    }

    /**
     * Аттрибуты сущности
     *
     * @param entityName - наименование сущности
     * @return - список аттрибутов (полей)
     * @throws DictionaryException
     */
    public Set<Attribute> getEntityAttributes(String entityName) throws DictionaryException {
        return getEntityByName(entityName).getAttributes();
    }

    //методы обработки объекта по иерархии setProcessList,setProcessMap

    /**
     * иерархия: обработка листа
     *
     * @param tmp_opj - текущий обрабатываемый объект
     * @param lpm     - накопитель объектов, для прооверки ссылок на объект
     * @param action  - процессор выполняемый на элементе структуры
     * @throws DictionaryException
     */
    public void setProcessList(List<Map> tmp_opj, List<Map> lpm, ActionOnMap action) throws DictionaryException {
        if (tmp_opj == null) return;
        List<Map> empty_objects = new ArrayList<Map>();
        for (Object entt : tmp_opj) {
            if (entt instanceof Map) {
                if (((Map) entt).isEmpty()) {
                    empty_objects.add(((Map) entt));
                    continue;
                }
                setProcessMap((Map) entt, lpm, action);
            }
            if (entt instanceof List) {
                if (!((List) entt).isEmpty()) setProcessList((List) entt, lpm, action);
            }
        }
        //удалить пустые объекты
        for (Map empty_object : empty_objects) {
            try {
                tmp_opj.remove(empty_object);
            } catch (Exception ex) {
                logger.warn(ex);
            }
        }

    }

    /**
     * иерархия: обработка мапы
     *
     * @param tmp_opj - текущий обрабатываемый объект
     * @param lpm     - накопитель объектов, для прооверки ссылок на объект
     * @param action  - процессор выполняемый на элементе структуры
     * @throws DictionaryException
     */
    public void setProcessMap(Map<String, Object> tmp_opj, List<Map> lpm, ActionOnMap action) throws DictionaryException {
        setProcessMap(tmp_opj, lpm, action, false);
    }

    /**
     * иерархия: обработка мапы
     *
     * @param tmp_opj     - текущий обрабатываемый объект
     * @param lpm         - накопитель объектов, для прооверки ссылок на объект
     * @param action      - процессор выполняемый на элементе структуры
     * @param ignoreFirst - не обрабатывать 1-ый элемент (вхождение)
     * @throws DictionaryException
     */
    public void setProcessMap(Map<String, Object> tmp_opj, List<Map> lpm, ActionOnMap action, boolean ignoreFirst) throws DictionaryException {
        if (tmp_opj == null) return;
        //если первый раз создадим пустую коллекцию ссылок
        if (lpm == null) {
            lpm = new ArrayList<Map>();
        }
        //если объект уже обработан
        for (Map tmpMap : lpm) {
            if (tmpMap == (Map) tmp_opj) return;
        }
        if (ignoreFirst && lpm.isEmpty()) {
            //первый раз, сделано для исключения повторной обраболтки с аспектами
            //do nothing
        } else {
            //процессор обработки
            action.process(tmp_opj);
        }
        // tmp_opj.put(RowStatus.ROWSTATUS_PARAM_NAME, rowStatus);
        lpm.add(tmp_opj);
        for (Map.Entry<String, Object> entr : tmp_opj.entrySet()) {
            if (entr.getValue() instanceof Map) {
                setProcessMap((Map) entr.getValue(), lpm, action);
            }
            if (entr.getValue() instanceof List) {
                setProcessList((List) entr.getValue(), lpm, action);
            }
        }
    }

    /**
     * Преобразование сущности : приведение типов к метаданным
     * пока фиксированные типы
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - сущность
     * @throws DictionaryException
     */
    public void convertObjectToTypeModel(String entityName, Map transientInstance) throws DictionaryException {
        EntityType type = getEntityByName(entityName);
        Set<Attribute<? super Object, ?>> listField = type.getAttributes();

        if (transientInstance instanceof Map) {
            Map<String, String> mapTypeModel = new HashMap<String, String>();
            //TODO Сделать утилиту приведения по графу
            for (javax.persistence.metamodel.Attribute attr : listField) {
                if ((attr.getJavaType() != null && attr.getName() != null) && (Arrays.asList("java.lang.String", "java.lang.Long", "java.lang.Integer", "java.lang.Float").contains(attr.getJavaType().getName()))) {
                    mapTypeModel.put(attr.getName(), attr.getJavaType().getName());
                }
            }
           Map<String, Object> m = (Map<String, Object>) transientInstance;
            Set<Map.Entry<String, Object>> set = m.entrySet();
            if (set == null) return;
            Iterator<Map.Entry<String, Object>> iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> element = iter.next();
                if (element.getValue() != null && (element.getValue() instanceof String || element.getValue() instanceof Long || element.getValue() instanceof Integer || element.getValue() instanceof Float)) {
                    String real = element.getValue().toString();
                    if (mapTypeModel.get(element.getKey()) != null) {
                        if (mapTypeModel.get(element.getKey()).equals("java.lang.String")) {
                            m.put(element.getKey(), real);
                        }
                        if (mapTypeModel.get(element.getKey()).equals("java.lang.Long")) {
                            m.put(element.getKey(), Long.valueOf(real));
                        }
                        if (mapTypeModel.get(element.getKey()).equals("java.lang.Integer")) {
                            m.put(element.getKey(), Integer.valueOf(real));
                        }
                        if (mapTypeModel.get(element.getKey()).equals("java.lang.Float")) {
                            m.put(element.getKey(), Float.valueOf(real));
                        }
                    }
                }
            }
        }
    }

    /**
     * Создание критерия для сущности
     *
     * @param entityName - наименование сущности
     * @return - Criteria
     */
    public Criteria createCriteria(String entityName) {
        return getSession().createCriteria(entityName);
    }

    //TODO Перенести обработку иерархического CRUD объекта

    /**
     * Поиск по идентификатору
     *
     * @param entityName - наименование сущности
     * @param id         - идентификатор
     * @return - сущность
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    public Map findById(String entityName, Serializable id) throws DictionaryException {
        return (Map) getSession().get(entityName, id);
    }

    /**
     * Поиск по полю
     *
     * @param entityName - наименование сущности
     * @param instance   - параметры поиска, преобразуются к Criterion
     * @return - сущностей список
     */
    @Override
    public List<Map> findByExample(String entityName, Map instance) {
        return findByCriterion(entityName, Example.create(instance));
    }

    /**
     * Поиск по полю
     *
     * @param entityName - наименование сущности
     * @param instance   - параметры поиска, преобразуются к Criterion
     * @return - сущность
     */
    @Override
    public Map findOneExample(String entityName, Map instance) {
        return findOneByCriterion(entityName, Example.create(instance));
    }

    /**
     * Поиск по критериям
     *
     * @param entityName - наименование сущности
     * @param criterion  - условия в пределах сущности
     * @return - сущностей список
     */
    @Override
    public List<Map> findByCriterion(String entityName, Criterion... criterion) {
        Criteria crit = createCriteria(entityName);
        for (Criterion cr : criterion) {
            crit.add(cr);
        }
        return crit.list();
    }

    /**
     * Поиск по критерию
     *
     * @param criteria - Критерий
     * @return - сущностей список
     */
    @Override
    public List<Map> findByCriteria(String entityName, Criteria criteria) {
        return criteria.list();
    }

    /**
     * Поиск по критериям
     *
     * @param entityName - наименование сущности
     * @param criterion  - условия в пределах сущности
     * @return -  сущность
     */
    @Override
    public Map findOneByCriterion(String entityName, Criterion... criterion) {
        Criteria crit = createCriteria(entityName);
        for (Criterion cr : criterion) {
            crit.add(cr);
        }
        return (Map) crit.uniqueResult();
    }

    /**
     * Поиск по критерию
     *
     * @param entityName - наименование сущности
     * @param criterion  - условия в пределах сущности
     * @param orders     - порядок
     * @return - сущностей список
     */
    @Override
    public List<Map> findByCriterion(String entityName, Collection<Criterion> criterion, Collection<Order> orders) {
        Criteria crit = createCriteria(entityName);
        if (criterion != null) {
            for (Criterion cr : criterion) {
                crit.add(cr);
            }
        }
        if (orders != null) {
            for (Order order : orders) {
                crit.addOrder(order);
            }
        }
        return crit.list();
    }

    /**
     * Поиск по критерию
     *
     * @param entityName - наименование сущности
     * @param criterion  - условия в пределах сущности
     * @return -  сущность
     */
    @Override
    public Map findOneByCriterion(String entityName, Collection<Criterion> criterion) {
        Criteria crit = createCriteria(entityName);
        if (criterion != null) {
            for (Criterion cr : criterion) {
                crit.add(cr);
            }
        }
        return (Map) crit.uniqueResult();
    }

    /**
     * Мерж - обновлениене не присоединенной сущности
     *
     * @param entityName       - наименование сущности
     * @param detachedInstance - параметры сущности
     * @return - сущность
     * @throws DictionaryException - исключение словарной сисистемы
     */
    @Override
    public Map merge(String entityName, Map detachedInstance) throws DictionaryException {
        convertObjectToTypeModel(entityName, detachedInstance);
        return (Map) getSession().merge(entityName, detachedInstance);
    }

    /**
     * грязное связывание
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - параметры сущности
     * @throws DictionaryException - исключение словарной сисистемы
     */
    @Override
    public void attachDirty(String entityName, Map transientInstance) throws DictionaryException {
        convertObjectToTypeModel(entityName, transientInstance);
        getSession().saveOrUpdate(entityName, transientInstance);
    }

    /**
     * очистить связь объекта
     * 
     * @param entityName - наименование сущности
     * @param transientInstance - параметры сущности 
     */
    @Override
    public void attachClean(String entityName, Map transientInstance) {
        getSession().lock(entityName, transientInstance, LockMode.NONE);
    }

    /**
     * Сохранение и обновление
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - параметры сущности
     * @return - сущность
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    @Override
    public Map save(String entityName, Map transientInstance) throws DictionaryException {
        convertObjectToTypeModel(entityName, transientInstance);
        getSession().saveOrUpdate(entityName, transientInstance);
        return transientInstance;
    }

    /**
     * Удаление
     *
     * @param entityName         - наименование сущности
     * @param persistentInstance - параметры сущности
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    @Override
    public void delete(String entityName, Map persistentInstance) throws DictionaryException {
        getSession().delete(entityName, persistentInstance);
    }

    /**
     * Только обновление
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - параметры сущности
     * @return - сущность
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    @Override
    public Map update(String entityName, Map transientInstance) throws DictionaryException {
        convertObjectToTypeModel(entityName, transientInstance);
        getSession().update(entityName, transientInstance);
        return transientInstance;
    }

    /**
     * Только сохранение
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - параметры сущности
     * @return - сущность
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    @Override
    public Map onlySave(String entityName, Map transientInstance) throws DictionaryException {
        convertObjectToTypeModel(entityName, transientInstance);
        Serializable rez = getSession().save(entityName, transientInstance);

        return transientInstance;
    }

    /**
     * Количество записей в данного объекта
     *
     * @param entityName - наименование сущности
     * @return - количество записей
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сисистемы
     */
    @Override
    public long getCount(String entityName) throws DictionaryException {
        Criteria criteria = createCriteria(entityName);
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

}

