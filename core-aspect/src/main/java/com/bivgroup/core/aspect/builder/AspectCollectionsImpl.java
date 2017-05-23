package com.bivgroup.core.aspect.builder;

import com.bivgroup.core.aspect.Constants;
import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.bean.Aspects;
import com.bivgroup.core.aspect.bean.AspectsOnEntity;
import com.bivgroup.core.aspect.bean.AspectsOnModule;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.aspect.xml.XMLProcessorImpl;

import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.*;

/**
 * Created by bush on 14.12.2016.
 * Хранилише аспектов
 * TODO Сделать Singleton , с обновлением содержимого
 */
public class AspectCollectionsImpl {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * аспекты
     */
    private Aspects aspects;
    /**
     * хранилище конфигураций аспектов
     */
    private Map<String, Map<String, List<AspectCfg>>> aspectsMap;
    /**
     * хранилище обработчиков сужностей  по аспектам
     */
    private Map<String, Map<String, AspectVisitor>> aspectsBuilderChain;
    /**
     * построитель цепочки обработки сущности
     */
    ChainBuilder builder;

    /**
     * Загрузка аспектов из XML aspects.xml
     *
     * @param obj - dao (соответственно файл берется из classpath модуля)
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void load(Object obj) throws AspectException {
        aspectsMap = new HashMap<>();
        InputStream aspects_stream = obj.getClass().getClassLoader().getResourceAsStream(Constants.ASPECT_CONFIG);
        if (aspects_stream == null) {
            logger.info("config resource aspects.xml not found");
            return;
        }
        aspects = new XMLProcessorImpl().unmarshalingAspects(aspects_stream);
    }

    /**
     * Преложим аспекты по коллекциям
     * модуль - (сущность : аспекты)
     */
    private void initAspectsCollection() {
        for (AspectsOnModule onModule : aspects.getAspectsOnModule()) {
            for (AspectsOnEntity onEntity : onModule.getAspectsOnEntity()) {
                if (aspectsMap.get(onModule.getNameModule()) == null)
                    aspectsMap.put(onModule.getNameModule(), new HashMap<String, List<AspectCfg>>());
                Map<String, List<AspectCfg>> moduleMap = aspectsMap.get(onModule.getNameModule());
                if (moduleMap.get(onEntity.getEntityName()) == null)
                    moduleMap.put(onEntity.getEntityName(), new ArrayList<AspectCfg>());
                List<AspectCfg> aspectsOnEntity = moduleMap.get(onEntity.getEntityName());
                aspectsOnEntity.addAll(onEntity.getAspectCfg());
            }
        }
    }

    /**
     * построим цепочки обработки по аспектам
     *
     * @param externaldao - дао
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void initVisitorsCollection(ExtendGenericDAO externaldao) throws AspectException {
        builder = new ChainBuilder(aspectsMap);
        aspectsBuilderChain = builder.build(externaldao);
    }

    /**
     * Инициализация коллекции аспектов
     *
     * @param obj         - объект аспекта
     * @param externaldao - дао
     * @throws AspectException - Исключение для модуля аспекты
     */
    public synchronized void initAspects(Object obj, ExtendGenericDAO externaldao) throws AspectException {
        load(obj);
        if (aspects != null) {
            initAspectsCollection();
            initVisitorsCollection(externaldao);
        }
    }

    /**
     * Все аспекты
     *
     * @return - сущность:аспекты
     */
    public Map<String, List<AspectCfg>> getAspectsAll() {
        Map<String, List<AspectCfg>> all = new HashMap<String, List<AspectCfg>>();
        for (Map<String, List<AspectCfg>> cfg : aspectsMap.values()) {
            all.putAll(cfg);
        }
        return all;
    }

    /**
     * Аспекты на модуле
     *
     * @param nameModule - название модуля
     * @return - сущность:аспекты
     */
    public Map<String, List<AspectCfg>> getAspectsOnModule(String nameModule) {
        Map<String, List<AspectCfg>> onModule = aspectsMap.get(nameModule);
        return onModule;
    }

    /**
     * Обработчик на сущности и модуле
     *
     * @param nameModule - название модуля
     * @param entity     - название сущности
     * @return - цепочка обработки
     * @throws AspectException - Исключение для модуля аспекты
     */
    public AspectVisitor getVisitorByModuleAndEntity(String nameModule, String entity) throws AspectException {
        Map<String, AspectVisitor> onModule = aspectsBuilderChain.get(nameModule);
        if (onModule == null) return null;
        return onModule.get(entity);
    }

    /**
     * Обработчики на модуле
     *
     * @param nameModule - название модуля
     * @return - коллекция сущность:цепочка обработки
     * @throws AspectException - Исключение для модуля аспекты
     */
    public Map<String, AspectVisitor> getVisitorOnModule(String nameModule) throws AspectException {
        Map<String, AspectVisitor> onModule = aspectsBuilderChain.get(nameModule);
        if (onModule == null) return null;
        return onModule;
    }

    /**
     * Все Обработчики
     *
     * @return - коллекция сущность:цепочка обработки
     * @throws AspectException - Исключение для модуля аспекты
     */
    public Map<String, AspectVisitor> getVisitorAll() throws AspectException {
        Map<String, AspectVisitor> all = new HashMap<String, AspectVisitor>();
        for (Map<String, AspectVisitor> aspect : aspectsBuilderChain.values()) {
            all.putAll(aspect);
        }
        return all;
    }

    /**
     * @param nameModule
     * @param entity
     * @return
     */
    public List<AspectCfg> getAspectsByModuleAndEntity(String nameModule, String entity) {
        return null;
    }

}
