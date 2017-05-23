package com.bivgroup.core.dictionary.dao;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by bush on 07.10.2016.
 * Менеджер для получения данных для генерации метаданных
 */
public class DictionaryDAO extends AbstractGenericDAO {
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /** сессия */
    private Session session;
    /** провайдер работы с БД */
    private OrmProvider provider;
    /** максимальный уровень рекурсии */
    final static Integer DEFAULT_MAX_LEVEL = 5;

    /**
     * конструкторо  ДАО получения данных для генерации метаданных
     * @param provider - провайдер работы с БД
     */
    public DictionaryDAO(OrmProvider provider) {
        super(provider.getSession());
        this.provider = provider;
        this.session = provider.getSession();
    }

    /**
     * получить DataSource
     * @return - DataSource
     */
    @Override
    public DataSource getDataSource() {
        return provider.getDataSource();
    }

    /**
     * Все сущности
     *
     * @return - список сущностей
     */
    public List<SadEntity> getAllEntities() {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SadEntity> query_cr = cb.createQuery(SadEntity.class);
        Root<SadEntity> root = query_cr.from(SadEntity.class);
        TypedQuery<SadEntity> query = session.createQuery(query_cr);
        List<SadEntity> results = query.getResultList();
        return results;
    }

    /**
     * Все идентификаторы, зависимости модуля
     *
     * @param idModule - идентификатор модуля
     * @return - идентификаторы зависимостей
     * @throws OrmException
     */
    private Set<Long> getIdsAllDepModules(Long idModule) throws OrmException {
        Map param = new HashMap();
        param.put("IDMODULE", idModule);
        param.put("LEVELDEP", DEFAULT_MAX_LEVEL);
        Set<Long> listIds = provider.getCommonListIds("GET_ALL_DEPEND_MODULES", param, "DEPENDENCYID");
        listIds.add(idModule);
        return listIds;
    }

    /**
     * Все модули, зависимости модуля
     *
     * @param module - модуль
     * @return - список модулей, зависимостей
     * @throws OrmException
     */
    public List<SadModule> getAllDependModules(SadModule module) throws OrmException {
        if (module.getId() == null) {
            module = getModule(module);
            if (module.getId() == null) return Collections.emptyList();
        }
        Set<Long> listIds = getIdsAllDepModules(module.getId());
        MultiIdentifierLoadAccess<SadModule> multiLoadAccess = session.byMultipleIds(SadModule.class);
        List<SadModule> modules = multiLoadAccess.multiLoad(new ArrayList<Long>(listIds));
        return modules;
    }

    /**
     * Модуль по параметрам
     *
     * @param module - модуль
     * @return -  модуль
     * @throws OrmException
     */
    public SadModule getModule(SadModule module) throws OrmException {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SadModule> query_cr = cb.createQuery(SadModule.class);
        Root<SadModule> root = query_cr.from(SadModule.class);
        query_cr.select(root).
                where(cb.and(cb.equal(root.get("artifactId"), module.getArtifactId()), cb.equal(root.get("groupId"), module.getGroupId())));
        TypedQuery<SadModule> query = session.createQuery(query_cr);
        SadModule mod = query.getSingleResult();
        return mod;
    }

    /**
     * Все сущности для модуля с зависимостями
     *
     * @param module - модуль
     * @return  - список сущностей
     * @throws OrmException
     */
    public List<SadEntity> getEntityByModule(SadModule module) throws OrmException {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SadEntity> query_cr = cb.createQuery(SadEntity.class);
        Root<SadEntity> root = query_cr.from(SadEntity.class);
        //Join<SadEntity,SadModule> moduleJoin = root.join( SadEntity_.module );
        query_cr.select(root).
                where(cb.and(cb.equal(root.get("module"), module.getId()), cb.equal(root.get("module").get("artifactId"), module.getArtifactId()), cb.equal(root.get("module").get("groupId"), module.getGroupId())));
        TypedQuery<SadEntity> query = session.createQuery(query_cr);
        List<SadEntity> results = query.getResultList();
        return results;
    }

    /**
     * Все сущности для модуля с зависимостями
     *
     * @param module - модуль
     * @return - список сущностей
     * @throws OrmException
     */
    public List<SadEntity> getAllEntityByModule(SadModule module) throws OrmException {
        if (module == null) return getAllEntities();
        if (module.getId() == null) {
            module = getModule(module);
            if (module.getId() == null) return Collections.emptyList();
        }
        Set<Long> listIds = getIdsAllDepModules(module.getId());
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<SadEntity> query_cr = cb.createQuery(SadEntity.class);
        Root<SadEntity> root = query_cr.from(SadEntity.class);
        query_cr.select(root);
        CriteriaBuilder.In<Object> in = cb.in(root.get("module"));
        for (Long value : listIds) {
            in = in.value(value);
        }
        query_cr.where(in);
        TypedQuery<SadEntity> query = session.createQuery(query_cr);
        List<SadEntity> results = query.getResultList();
        return results;
    }

    /**
     * Название модуля
     * @return - название модуля
     */
    @Override
    public String getModuleName() {
        return "com.bivgroup.core.core-dictionary";
    }

    @Override
    public Map crudByHierarchy(Map transientInstance) throws DictionaryException {
        return null;
    }

    @Override
    public Map crudByHierarchy(String entityName, Map transientInstance) throws DictionaryException {
        return null;
    }

    @Override
    public Map crudByHierarchy(String entityName, Map transientInstance, boolean isUseProxy) throws DictionaryException {
        return null;
    }

    @Override
    public List crudByHierarchy(String entityName, List transientInstance) throws DictionaryException {
        return null;
    }
}
