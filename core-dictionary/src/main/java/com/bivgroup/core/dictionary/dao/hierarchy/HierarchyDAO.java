package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.core.dictionary.dao.AbstractGenericDAO;
import com.bivgroup.core.dictionary.dao.enums.Action;
import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.metamodel.internal.EntityTypeImpl;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.collection.OneToManyPersister;
import org.hibernate.tuple.DynamicMapInstantiator;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;

/**
 * Created by bush on 01.11.2016.
 * ДАО обработки иерархической структуры
 */
public class HierarchyDAO extends AbstractGenericDAO {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * провайдер работы с БД
     */
    private OrmProvider provider;
    /**
     * список рутовых элементов не используется - удалить
     */
    private Map<String, EntityMap> listRoot;
    /**
     * флаг разворачивать ссылки
     */
    private Boolean isInsertRootInO2M = false;
    /**
     * генератор наименования элементов сущности
     */
    private NameGenerator ng;

    /**
     * Параметр - разворачивать ссылки (json), получить
     *
     * @return
     */
    public Boolean getIsInsertRootInO2M() {
        return isInsertRootInO2M;
    }

    /**
     * Параметр - разворачивать ссылки (json), установить
     *
     * @param isInsertRootInO2M
     */
    public void setIsInsertRootInO2M(Boolean isInsertRootInO2M) {
        this.isInsertRootInO2M = isInsertRootInO2M;
    }

    public HierarchyDAO(OrmProvider provider) {
        super(provider.getSession());
        this.provider = provider;
        this.ng = new NameGenerator();
    }

    /**
     * Конструктор  ДАО обработки иерархической структуры
     *
     * @param session - сессия
     */
    public HierarchyDAO(Session session) {
        super(session);
    }

    /**
     * Получить DataSource
     *
     * @return DataSource
     */
    @Override
    public DataSource getDataSource() {
        return provider.getDataSource();
    }


    /**
     * Получить название вложенной сужности для отношения One-To-Many
     *
     * @param listName - наименования поля OneToMany
     * @param et       - тип сущности
     * @return - аттрибут
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private PluralAttribute getSuperDeclaredCollection(EntityTypeImpl et, String listName) throws DictionaryException {
        try {
            return et.getDeclaredCollection(listName);
        } catch (IllegalArgumentException ex) {
            if (et.getSupertype() == null)
                throw new DictionaryException(String.format("Not found attribute %1s on entity %2s", listName, et.getName()));
            return getSuperDeclaredCollection((EntityTypeImpl) et.getSupertype(), listName);
        }
    }

    /**
     * получение хранилища сущности из графа обработки
     *
     * @param map - входная сущность
     * @return - хранилище
     */
    private EntityMap getReference(Map map) {
        int index = listGraf.lastIndexOf(map);
        if (index == -1) return null;
        return listGraf.get(index);
    }

    /**
     * Получение типа списка OneToMany
     *
     * @param listName   - наименование OneToMany
     * @param entityName - наименование сущности
     * @return - тип Many
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private String processPcOneToMany(String listName, String entityName) throws DictionaryException {
        if (entityName == null || listName == null) return null;
        EntityType et = getEntityByName(entityName);
        if (et == null) {
            throw new DictionaryException(String.format("this link list %1s for entity %2s not correct", listName, entityName));
        }
        PluralAttribute bindAttr = getSuperDeclaredCollection((EntityTypeImpl) et, listName);
        EntityTypeImpl type = (EntityTypeImpl) bindAttr.getElementType();
        return type.getName();
    }

    /**
     * Обработать элемент сущности - иерархический обход
     *
     * @param parentEntity      - родительская сущность
     * @param entityName        - наименование сущности
     * @param transientInstance - обрабатываемый объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void processObject(EntityMap parentEntity, String entityName, Object transientInstance, String listFiels) throws DictionaryException {
        if (transientInstance instanceof Map) {
            processMap(parentEntity, entityName, (Map) transientInstance, listFiels);
        } else if (transientInstance instanceof Collection) {
            processCollection(parentEntity, entityName, (Collection) transientInstance, listFiels);
        }
    }

    /**
     * Подняться вверх по типам и отдать верний тип аттрибута
     *
     * @param rootEntity - наименование родительской сущности
     * @param type       - тип сужности
     * @param attr       - аттрибут
     * @return - тип сущности
     * @throws DictionaryException - исключение словарной сиситемы
     */
    String getPropertyBySuperType(String rootEntity, EntityType type, SingularAttribute attr) throws DictionaryException {
        EntityType type_en = getEntityByName(rootEntity);
        if (type.getName().equals(rootEntity) || (type_en.getSupertype() != null && type.getName().equals(((EntityType) type_en.getSupertype()).getName())))
            return attr.getName();
        else if (type_en.getSupertype() != null) {
            return getPropertyBySuperType(((EntityType) type_en.getSupertype()).getName(), type, attr);
        }
        return null;
    }

    /**
     * @param childEntity
     * @param rootEntity
     * @return
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private String getPropertyByType(String childEntity, String rootEntity) throws DictionaryException {
        Set<SingularAttribute> listField = getEntityByName(childEntity).getSingularAttributes();
        for (SingularAttribute attr : listField) {
            if (attr.getType() instanceof EntityType) {
                EntityType type = (EntityType) attr.getType();
                return getPropertyBySuperType(rootEntity, type, attr);
            }
        }
        return null;
    }

    /**
     * Проверить аттрибут в сущности
     *
     * @param prop - название аттрибута
     * @param obj  - сущность
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void chekContainsRefer(String prop, Map obj) throws DictionaryException {
        for (Object key : obj.keySet()) {
            if (key.equals(prop)) return;
        }
        throw new DictionaryException(String.format("Entity %1s not contains field %2s", obj.get(DynamicMapInstantiator.KEY), prop));

    }

    /**
     * тип списочного параметра, ищет по всем родителям
     *
     * @param entityName - наименование сущности
     * @param listfield  - поле списка
     * @return - Persister - класс
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private CollectionPersister getBySuper(String entityName, String listfield) throws DictionaryException {
        String path = entityName + '.' + listfield;
        CollectionPersister persister = ((MetamodelImpl) metamodel).collectionPersisters().get(path);
        if (persister == null) {
            EntityType type = (EntityType) getEntityByName(entityName).getSupertype();
            if (type == null) return null;
            return getBySuper(type.getName(), listfield);
        }
        return persister;
    }

    /**
     * 1. Если используем параметр для json -isInsertRootInO2M. Поместить в дочернее поле , oneToMany, ссылку на родительсую сущность
     * 2. Если  нет , то проверить данную ссылку
     *
     * @param obj          - дочерняя сущность
     * @param listfield    - поле списка
     * @param parentEntity - хранилище, родительской сущности
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void patchChildEntityByRoot(Object obj, String listfield, EntityMap parentEntity) throws DictionaryException {
        if (obj instanceof Map && parentEntity != null && listfield != null) {
            CollectionPersister persister = getBySuper(parentEntity.getEntityName(), listfield);
            if (persister != null) {
                if (persister.isOneToMany()) {
                    if (((OneToManyPersister) persister).getKeyColumnNames() != null) {
                        String keyColumnName = ((OneToManyPersister) persister).getKeyColumnNames()[0];
                        if (keyColumnName == null) return;
                        if (isInsertRootInO2M) {
                            ((Map) obj).put(ng.getOneToManyPropertyName(keyColumnName), parentEntity.getEntityMap());
                        } else {
                            chekContainsRefer(ng.getOneToManyPropertyName(keyColumnName), (Map) obj);
                        }
                    }
                }
            }
        }
    }

    /**
     * Обработать элемент сущности - как коллекцию
     *
     * @param parentEntity      - родительская сущность
     * @param entityName        - наименование сущности
     * @param transientInstance - обрабатываемый объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void processCollection(EntityMap parentEntity, String entityName, Collection transientInstance, String listFiels) throws DictionaryException {
        Iterator it = transientInstance.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            processObject(parentEntity, entityName, obj, listFiels);
            patchChildEntityByRoot(obj, listFiels, parentEntity);
        }
        listFiels = null;
    }

    /**
     * Обработать нулевые ссылки
     * TODO удалить
     *
     * @param em - хранилище обрабатываемого объекта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void processNullReference(EntityMap em) throws DictionaryException {
        Set<String> fieldFact = em.getEntityMap().keySet();
        Set<String> fieldAll = new HashSet<>();
        Set<javax.persistence.metamodel.Attribute> listField = getEntityAttributes(em.getEntityName());
        for (javax.persistence.metamodel.Attribute attr : listField) {
            fieldAll.add(attr.getName());
        }
    }

    /**
     * Получить соответствие полей справочника  ..ID -- ..ID_EN
     *
     * @param em - хранилище обрабатываемого объекта
     * @return - соответствие полей справочника
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private Map<String, String> getDictionary(EntityMap em) throws DictionaryException {
        Map<String, String> keyDict = new HashMap<String, String>();
        Set<javax.persistence.metamodel.Attribute> listField = getEntityAttributes(em.getEntityName());
        for (javax.persistence.metamodel.Attribute attr : listField) {
            try {
                if (attr.getName().contains(NameGenerator.REF_TAG_M2O)) {
                    javax.persistence.metamodel.Attribute attr_d = getEntityByName(em.getEntityName()).getAttribute(attr.getName().replace(NameGenerator.REF_TAG_M2O, ""));
                    keyDict.put(attr_d.getName(), attr.getName());
                }
            } catch (IllegalArgumentException ex) {
                logger.error(ex);
            }
        }
        return keyDict;
    }

    /**
     * Выставить для иерархии статус Неизменный , используется в начитке данных справочника , при развороте по идентификатору
     *
     * @param tmp_opj - сущность
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void setRowStatusUnmodifMap(Map<String, Object> tmp_opj) throws DictionaryException {
        setProcessMap(tmp_opj, null, new SetRowStatus(RowStatus.UNMODIFIED));
    }

    /**
     * Удалить пустые элементы сущности
     *
     * @param tmp_opj - сущность
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public void cleanMap(Map<String, Object> tmp_opj) throws DictionaryException {
        setProcessMap(tmp_opj, null, new CleanEmptyElement());
    }

    /**
     * Удалим пустые элементы
     *
     * @param tmp_opj - список сущностей
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public void cleanList(List<Map> tmp_opj) throws DictionaryException {
        setProcessList(tmp_opj, null, new CleanEmptyElement());
    }

    /**
     * развернем объект по идентификатору , только для сцщностей с полями ..ID ..ID_EN
     *
     * @param em       - хранилище обрабатываемого объекта
     * @param dicField - отношения полей справочника ..ID:..ID_EN
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void replaceForJson(EntityMap em, Map<String, String> dicField) throws DictionaryException {
        for (Map.Entry<String, String> field : dicField.entrySet()) {
            if (em.getEntityMap().containsKey(field.getKey())) {
                javax.persistence.metamodel.EntityType type = getType(em, field.getValue());
                Object id = em.getEntityMap().get(field.getKey());
                if (id == null) {
                    logger.warn(String.format("Not set id field %1s for row type %2s ", field.getValue(), type.getName()));
                    continue;
                    //throw new DictionaryException(String.format("Not set id field %1s for row type %2s ", field.getValue(), type.getName()));
                }
                if (field.getValue() != null && em.getEntityMap().get(field.getValue()) != null) {
                    logger.warn(String.format("Prioritet field %1s for row type %2s on id field ", field.getValue(), type.getName()));
                    continue;
                }
                Map tmp_opj = findById(type.getName(), (Long) id);
                if (tmp_opj == null) {
                    logger.warn(String.format("Not found row type %1s with id %2s", type.getName(), id));
                    continue;
                }
                setRowStatusUnmodifMap(tmp_opj);
                em.getEntityMap().put(field.getValue(), tmp_opj);
                //em.getEntityMap().remove(field.getKey());
            }
        }
    }

    /**
     * Развернуть справочные данные по иденификаторам,
     * перобразовать типы к типам сущности
     *
     * @param em - хранилище обрабатываемого объекта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void convertMap(EntityMap em) throws DictionaryException {
        convertObjectToTypeModel(em.getEntityName(), em.getEntityMap());
        Map<String, String> dicField = getDictionary(em);
        replaceForJson(em, dicField);
    }

    /**
     * заполнение списка рутовых ссылок
     * TODO удалить
     *
     * @param em - хранилище обрабатываемого объекта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void addRootIfEmpty(EntityMap em) throws DictionaryException {
        Set<SingularAttribute> attrs = getEntityByName(em.getEntityName()).getSingularAttributes();
        for (SingularAttribute attr : attrs) {
            if (attr.getType() instanceof EntityType) {
                EntityType type = (EntityType) attr.getType();
                if (listRoot.containsKey(type.getName())) {
                    em.getEntityMap().put(attr.getName(), listRoot.get(type.getName()).getEntityMap());
                    return;
                }
            }
        }
    }

    /**
     * заполнение списка рутовых ссылок
     * TODO удалить
     *
     * @param entityName - наименование сущности
     * @param em         - хранилище обрабатываемого объекта
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void putToMap(String entityName, EntityMap em) throws DictionaryException {
        EntityType type = (EntityType) getEntityByName(entityName).getSupertype();
        if (type != null && !type.getName().equals(entityName)) listRoot.put(type.getName(), em);
    }

    /**
     * Обработать элемент сущности - как Map
     *
     * @param parentEntity      - родительская сущность
     * @param entityName        - наименование сущности
     * @param transientInstance -  обрабатываемый объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void processMap(EntityMap parentEntity, String entityName, Map transientInstance, String listFiels) throws DictionaryException {
        EntityMap em = null;
        if (entityName != null) {
            if (parentEntity != null) {
                try {
                    if (transientInstance.get(DynamicMapInstantiator.KEY) != null) {
                        entityName = transientInstance.get(DynamicMapInstantiator.KEY).toString();
                    } else {
                        entityName = getType(parentEntity, entityName).getName();
                    }
                } catch (IllegalArgumentException ex) {
                }
            }
            transientInstance.put(DynamicMapInstantiator.KEY, entityName);
            String primaryKeyName = getEntityByName(entityName).getId(Long.class).getName();
            em = new EntityMap(parentEntity, transientInstance, primaryKeyName);
            convertMap(em);
            // addRootIfEmpty(em);
            processNullReference(em);
            boolean isEquals = listGraf.contains(em);
            if (isEquals) {
                logger.warn("This recursion reference");
                return;
            } else {
                if (em.isRoot) {
                    listRoot.put(entityName, em);
                    putToMap(entityName, em);
                }
                listGraf.add(em);
            }
        }
        Iterator<Map.Entry> it = transientInstance.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry el = it.next();
            String entityAttr = el.getKey().toString();
            if (el.getValue() instanceof Map) {
                Map in = (Map) el.getValue();
                processObject(em, entityAttr, el.getValue(), listFiels);
            } else if (el.getValue() instanceof Collection) {
                String listEntityName = processPcOneToMany(entityAttr, entityName);
                processObject(em, listEntityName, el.getValue(), entityAttr);
            }
        }
    }

    /**
     * Список полей на сущности
     *
     * @param entityName - наименование сущности
     * @return - поля
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private Set<String> getFieldsOnEntity(String entityName) throws DictionaryException {
        Set<String> fieldAll = new HashSet<>();
        fieldAll.add(RowStatus.ROWSTATUS_PARAM_NAME);
        fieldAll.add(DynamicMapInstantiator.KEY);
        Set<javax.persistence.metamodel.Attribute> listField = getEntityAttributes(entityName);
        for (javax.persistence.metamodel.Attribute attr : listField) {
            fieldAll.add(attr.getName());
        }
        return fieldAll;
    }

    /**
     * Преобразуем граф. Если есть объект для модифицирования, разбиваем его на 2-а: c сылками - выполняется по окончании, без ссылок - остается на своем месте
     *
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void prepareGraf() throws DictionaryException {
        List<EntityMap> convertGraf = new ArrayList<EntityMap>();
        List<EntityMap> extendGraf = new ArrayList<EntityMap>();
        for (EntityMap map : listGraf) {
            //проверим сущность
            Action.CHECK.run(this, map);
            //если операция модификации , включаем отсроченное исполнение (обновляется  сущность без ссылок , по окончании обновляется с сылками)
            if (RowStatus.MODIFIED.equals(map.rowStatus)) {
                Map<String, Object> ent = map.getEntityMap();
                Map mapTmp = new HashMap<>();
                Set<String> fields = getFieldsOnEntity(map.getEntityName());
                for (Map.Entry realMap : ent.entrySet()) {
                    if (fields.contains(realMap.getKey()) && (realMap.getValue() == null || !(realMap.getValue() instanceof Map))) {
                        mapTmp.put(realMap.getKey(), realMap.getValue());
                    } else {
                        //мап для восстановления ссылок
                        map.restoresRef.put(realMap.getKey(), realMap.getValue());
                    }
                }
                if (mapTmp.size() == ent.size()) {
                    convertGraf.add(map);
                    continue;
                }
                extendGraf.add(0, map);
                EntityMap em = new EntityMap(map.prevEntity, mapTmp, map.primaryKeyName);
                convertGraf.add(em);
            } else {
                convertGraf.add(map);
            }
        }
        convertGraf.addAll(extendGraf);
        listGraf = convertGraf;
    }

    /**
     * Обработать весь иерархический объект с опреациями CRUD
     *
     * @param notUseProxy - не использовать прокси
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void processGrafs(boolean notUseProxy) throws DictionaryException {
        //отсроченное выполенение обновления , проверка, заполнение данными
        prepareGraf();
        //исполнение CRUD операций
        for (EntityMap map : listGraf) {
            //восстановление ссылок
            map.restore();
            ExtendGenericDAO dao = (ExtendGenericDAO) this.getProxy();
            if (notUseProxy) dao = this;
            //исполенение встаки,обновления,удаления ...
            Action.CRUD.run(dao, map);
        }
    }

    /**
     * Создание графа
     *
     * @param transientInstance - входной иерархический объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void createGraf(Object transientInstance) throws DictionaryException {
        processObject(null, null, transientInstance, null);
    }

    /**
     * Создание графа
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - входной иерархический объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private void createGraf(String entityName, Object transientInstance) throws DictionaryException {
        processObject(null, entityName, transientInstance, null);
    }

    /**
     * CRUD операции с мапой иерархической сущности
     *
     * @param transientInstance - входной иерархический объект
     * @return - входной иерархический объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public Map crudByHierarchy(Map transientInstance) throws DictionaryException {
        listGraf = new LinkedList<>();
        listRoot = new HashMap<>();
        createGraf(transientInstance);
        processGrafs(false);
        cleanMap(transientInstance);
        return transientInstance;
    }

    /**
     * CRUD операции с мапой иерархической сущности
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - входной иерархический объект
     * @return - входной иерархический объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public Map crudByHierarchy(String entityName, Map transientInstance) throws DictionaryException {
        return crudByHierarchy(entityName, transientInstance, false);
    }

    /**
     * CRUD операции с мапой иерархической сущности
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - иерархическая структура
     * @param isUseProxy        - использовать прокси (для повторной обработки аспектов)
     * @return - входной иерархический объект
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public Map crudByHierarchy(String entityName, Map transientInstance, boolean isUseProxy) throws DictionaryException {
        listGraf = new ArrayList<>();
        listRoot = new HashMap<>();
        createGraf(entityName, transientInstance);
        processGrafs(isUseProxy);
        cleanMap(transientInstance);
        return transientInstance;
    }

    /**
     * CRUD операции с листов иерархических сущностей
     *
     * @param entityName        - наименование сущности
     * @param transientInstance - иерархическая структура
     * @return -  список обработанных объектов
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public List crudByHierarchy(String entityName, List<Map> transientInstance) throws DictionaryException {
        listGraf = new ArrayList<>();
        listRoot = new HashMap<>();
        createGraf(entityName, transientInstance);
        processGrafs(false);
        cleanList(transientInstance);
        return transientInstance;
    }

    /**
     * Удаление с проверкой существования сущности
     *
     * @param entityName         - наименование сущности
     * @param persistentInstance - иерархическая структура
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public void delete(String entityName, Map persistentInstance) throws DictionaryException {
        String primaryKeyName = getEntityByName(entityName).getId(Long.class).getName();
        if (persistentInstance.get(primaryKeyName) == null)
            throw new DictionaryException(String.format("id %1s not contains in input object", primaryKeyName));
        Map obj = findById(entityName, (Long) persistentInstance.get(primaryKeyName));
        if (obj == null)
            throw new DictionaryException(String.format("object %1s with id %2s not found", entityName, (Long) persistentInstance.get(primaryKeyName)));
        super.delete(entityName, obj);
    }

    /**
     * Название модуля
     *
     * @return модуль с пакетом
     */
    @Override
    public String getModuleName() {
        return "com.bivgroup.core.core-dictionary";
    }

    /**
     * Поиск c установкой статуса НЕ ИЗМЕНЯЕКМ
     *
     * @param entityName - наименование сущности
     * @param id         - идентификатор
     * @return - сущность
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public Map findById(String entityName, Serializable id) throws DictionaryException {
        Map in = (Map) getSession().get(entityName, id);
        setRowStatusUnmodifMap(in);
        return in;
    }

}