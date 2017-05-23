package com.bivgroup.core.dictionary.generator;

import com.bivgroup.common.orm.interfaces.ConsumerConfig;
import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.core.dictionary.dao.DictionaryDAO;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.exporter.Exporter;
import com.bivgroup.core.dictionary.generator.visitors.AttributeVisitor;
import com.bivgroup.core.dictionary.generator.visitors.EntityVisitor;
import com.bivgroup.core.dictionary.generator.visitors.ExportObjectType;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import com.bivgroup.core.dictionary.interceptor.GenerateEntityInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.reveng.DatabaseCollector;
import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2x.Cfg2JavaTool;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

/**
 * Created by andreykus on 12.09.2016.
 * Реализация генератора метаданных
 */

//TODO MEtadataSource from ServiceRegister - Metadata from ms.matadatabuilder- build : attempt add hbm
public class GenerateMetadataImpl implements GenerateMetadata {
    /** логгер*/
    protected Logger logger = LogManager.getLogger(this.getClass());
    /** генератор наименованиий элементов сущности*/
    private NameGenerator ng;
    /**
     * PERSISTENT UNIT для модкля генератора
     */
    public static final String PERSISTENT_UNIT = "Generator";
    /** провайдер работы с БД*/
    OrmProviderImpl orm;
    /** типовые операции с метаданными в hibernate*/
    DatabaseCollector collector;
    /** утилита для работы с сущностями описания словарной системы*/
    DictionaryDAO dm;
    /** получатели сгененрированных объектов*/
    List<ExternalConsumerConfig> consumers = new ArrayList<ExternalConsumerConfig>();
    /** метаданные Бд в hibernate*/
    MetadataImplementor metadata;
    /** тип экспорта*/
    TypeExport type = TypeExport.XML;

    /**
     * конструктор генератора метаданных
     * @param ds - DataSource
     */
    public GenerateMetadataImpl(DataSource ds) {
        this.orm = new OrmProviderImpl(ds, PERSISTENT_UNIT);
        //default set auto load generic object to session
        new RuntimeConfig(this.orm);
        this.orm.getConfig().register(this.orm);
        this.collector = new DefaultDatabaseCollector(null);
        this.dm = new DictionaryDAO(this.orm);
        setMetadata(orm);
        ng = new NameGenerator();
    }
    /**
     * конструктор генератора метаданных
     * @param orm - провайдер
     */
    public GenerateMetadataImpl(OrmProviderImpl orm) {
        this.orm = orm;
        //default set auto load generic object to session
        new RuntimeConfig(this.orm);
        this.orm.getConfig().register(this.orm);
        this.collector = new DefaultDatabaseCollector(null);
        this.dm = new DictionaryDAO(this.orm);
        setMetadata(orm);
        ng = new NameGenerator();
    }

    /**
     * Установить тип экуспорта
     *
     * @param type - тип экспорта
     */
    @Override
    public void setType(TypeExport type) {
        this.type = type;
    }

    /**
     * Получить и установить метаданные привязанные к сесси
     *
     * @param orm - провайдер обращения к БД
     */
    private void setMetadata(OrmProvider orm) {
        StandardServiceRegistry serviceRegistry = orm.getSession().getSessionFactory().getSessionFactoryOptions().getServiceRegistry();
        MetadataSources metadataSources = new MetadataSources(new BootstrapServiceRegistryBuilder().build());
        Metadata metadata = metadataSources.buildMetadata(serviceRegistry);
        this.metadata = (MetadataImplementor) metadata;
    }

    /**
     * Получить список всех сущностей из словарной системы
     *
     * @return -  список сущностей
     * @throws OrmException - исключение ORM
     */
    private List<SadEntity> getAllEntities() throws OrmException {
        return dm.getAllEntities();
    }

    /**
     * Получить список всех сущностей для модуля с зависимостями из словарной системы
     *
     * @param module - модуль
     * @return -  список сущностей
     * @throws OrmException - исключение ORM
     */
    private List<SadEntity> getAllEntitiesByModule(SadModule module) throws OrmException {
        return dm.getAllEntityByModule(module);
    }

    /**
     * Получить список всех сущностей для модуля из словарной системы
     *
     * @param module - модуль
     * @return -  список сущностей
     * @throws OrmException - исключение ORM
     */
    private List<SadEntity> getEntitiesByModule(SadModule module) throws OrmException {
        return dm.getEntityByModule(module);
    }

    /**
     * Обработать сущности из словарной системы
     *
     * @param entitys - список сущностей
     * @return - внутрення метамодель (это уже сгененренные объекты)
     * @throws DictionaryException
     * @throws OrmException - исключение ORM
     */
    private MetadataCollection processEnt(List<SadEntity> entitys) throws DictionaryException, OrmException {
        MetadataCollection source = new MetadataCollection();
        source.setMetadata(metadata);
        source.setExportType(this.type);
        for (SadEntity ent : entitys) {
            if (source.getClass(ng.getClassName(ent)) == null) {
                new EntityVisitor(new AttributeVisitor(null)).visit(ent, source);
            }
        }
        //source.persistentsClassOnModules
        return source;
    }

    /**
     * Получить сессию
     *
     * @return - сессия
     */
    @Override
    public Session getSession() {
        return orm.getSession();
    }

    /**
     * Добавить в список объектов кофигурации hbm метаданные
     *
     * @param listCfg    - список объектов кофигурации
     * @param moduleName - название модуля
     * @param inClass    - список PersistentClass, по ним генерятся метаданные
     * @throws OrmException - исключение ORM
     */
    private void addExtConfigXML(List<ExportObject> listCfg, String moduleName, List<PersistentClass> inClass) throws OrmException {
        listCfg.add(new ExportObject(moduleName, null, getConfig(moduleName, inClass), ExportObjectType.XML));
    }

    /**
     * Добавить в список объектов кофигурации Классы
     *
     * @param listCfg    - список объектов кофигурации
     * @param moduleName - название модуля
     * @param inClass    - список PersistentClass, по ним генерятся метаданные
     * @throws OrmException - исключение ORM
     */
    private void addExtConfigClass(List<ExportObject> listCfg, String moduleName, List<PersistentClass> inClass) throws OrmException {
        Map<String, StringBuffer> clazzs = getConfigClass(moduleName, inClass);
        for (Map.Entry<String, StringBuffer> element : clazzs.entrySet()) {
            listCfg.add(new ExportObject(moduleName, element.getKey(), element.getValue(), ExportObjectType.CLASS));
        }
    }

    /**
     * Добавить в список объектов кофигурации Аспекты на модуле
     *
     * @param listCfg    - список объектов кофигурации
     * @param moduleName - название модуля
     * @param entitys    - список сущностей
     * @throws OrmException - исключение ORM
     */
    private void addExtConfigAspect(List<ExportObject> listCfg, String moduleName, List<SadEntity> entitys) throws OrmException {
        listCfg.add(new ExportObject(moduleName, null, getConfigAspect(moduleName, entitys), ExportObjectType.ASPECT));
    }

    /**
     * Добавить в список объектов кофигурации Аспекты - все
     *
     * @param listCfg - список объектов кофигурации
     * @param entitys - список сущностей
     * @throws OrmException - исключение ORM
     */
    private void addExtConfigAspectAll(List<ExportObject> listCfg, Map<String, List<SadEntity>> entitys) throws OrmException {
        listCfg.add(new ExportObject("all", null, getConfigAspectAll(entitys), ExportObjectType.ASPECT));
    }

    /**
     * группировка метаданных по модуляим и типу экспорта, и отсылка потребителю
     *
     * @param listClass - список объектов кофигурации
     * @throws OrmException - исключение ORM
     */
    private void groupPersistentByModuleAndNotify(Map<String, List<PersistentClass>> listClass) throws OrmException {
        List<ExportObject> listCfg = new ArrayList<ExportObject>();
        for (Map.Entry<String, List<PersistentClass>> list : listClass.entrySet()) {
            if (list.getKey() == null || list.getValue() == null || list.getValue().isEmpty()) return;
            switch (type) {
                case CLASS:
                    addExtConfigClass(listCfg, list.getKey(), list.getValue());
                    break;
                case XML:
                    addExtConfigXML(listCfg, list.getKey(), list.getValue());
                    break;
                default:
                    break;
            }
        }
        if (!listCfg.isEmpty()) {
            processConfig(listCfg, type);
        }
    }

    /**
     * группировка аспектов по модуляим  и отсылка потребителю
     *
     * @param moduleEntity - список сущностей метамодели
     * @throws OrmException - исключение ORM
     */
    private void groupAspectByModuleAndNotify(Map<String, List<SadEntity>> moduleEntity) throws OrmException {
        List<ExportObject> listCfg = new ArrayList<ExportObject>();
        for (Map.Entry<String, List<SadEntity>> list : moduleEntity.entrySet()) {
            if (list.getKey() == null || list.getValue() == null || list.getValue().isEmpty()) continue;
            addExtConfigAspect(listCfg, list.getKey(), list.getValue());
        }
        if (!listCfg.isEmpty()) {
            processConfig(listCfg, TypeExport.ASPECT);
        }
    }

    /**
     * аспектоы отсылка потребителю (без группировки))
     *
     * @param moduleEntity - список сущностей метамодели
     * @throws OrmException - исключение ORM
     */
    private void aspectAllAndNotify(Map<String, List<SadEntity>> moduleEntity) throws OrmException {
        List<ExportObject> listCfg = new ArrayList<ExportObject>();
        Map<String, List<SadEntity>> allModules = new HashMap<String, List<SadEntity>>();
        for (Map.Entry<String, List<SadEntity>> list : moduleEntity.entrySet()) {
            if (list.getKey() == null || list.getValue() == null || list.getValue().isEmpty()) continue;
            allModules.put(list.getKey(), list.getValue());
        }
        addExtConfigAspectAll(listCfg, allModules);
        if (!listCfg.isEmpty()) {
            processConfig(listCfg, TypeExport.ASPECT);
        }
    }

    /**
     * Получить RunTime подписчика
     *
     * @return - потребитель конфигурации
     */
    private ConsumerConfig getRunTimeConsumer() {
        return orm.getConfig();
    }

    /**
     * Добавить подписчика на конфигурацию метаданных
     *
     * @param consumer потребитель
     */
    @Override
    public void setConsumer(ExternalConsumerConfig consumer) {
        this.consumers.add(consumer);
    }

    //TODO Использовать CDI

    /**
     * Отошлем все конфигйрации подписчикам
     *
     * @param sb   - список объектов конфигурации
     * @param type - тип экспорта
     * @throws OrmException - исключение ORM
     */
    private void processConfig(List<ExportObject> sb, TypeExport type) throws OrmException {
        //если нет потребителей , клиент получает изменения в RunTime
        if (consumers.isEmpty()) {
            getRunTimeConsumer().processConfig(sb);
        }
        for (ExternalConsumerConfig consumer : consumers) {
            if (type.equals(consumer.getTypeObject())) {
                consumer.processConfig(sb);
            }
        }
    }

    /**
     * генерация метаобъектов
     *
     * @param entitys - сущности
     * @throws DictionaryException - исключение словарной системы
     * @throws OrmException - исключение ORM
     */
    private void generate(List<SadEntity> entitys) throws DictionaryException, OrmException {
        MetadataCollection collection = processEnt(entitys);
        aspectAllAndNotify(collection.entityOnModules);
        groupPersistentByModuleAndNotify(collection.persistentsClassOnModules);
    }

    /**
     * генерация метаобъектов для всех сущностей
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сиситемы
     */
    @Override
    public void generateAll() throws DictionaryException, OrmException {
        generate(getAllEntities());
    }

    /**
     * генерация метаобъектов для всех сущностей модуля
     * 
     * @param module - модуль
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сиситемы
     */
    @Override
    public void generateByModule(SadModule module) throws DictionaryException, OrmException {
        generate(getEntitiesByModule(module));
    }

    /**
     * генерация метаобъектов для всех сущностей модуля с зависимостями
     * 
     * @param module - модуль
     * @throws com.bivgroup.core.dictionary.exceptions.DictionaryException - исключение словарной сиситемы
     * @throws com.bivgroup.common.orm.OrmException - исключение ORM
     */
    @Override
    public void generateByModuleWithDependency(SadModule module) throws DictionaryException, OrmException {
        //getAllDependModules(module)
        generate(getAllEntitiesByModule(module));
    }

    /**
     * глобальная конфигурация для генератор
     *
     * @param module - модуль
     * @return - конфигурация
     */
    private HibernateMappingGlobalSettings getHConfig(String module){
        //глобальная конфигурация для генератор
        HibernateMappingGlobalSettings hgs = new HibernateMappingGlobalSettings();
        //пакет
        hgs.setDefaultPackage(module);
        //умолчание  для Lazy
        hgs.setDefaultLazy(false);
        hgs.setAutoImport(true);
        return hgs;
    }

    /**
     * генерация тела конфигурации (xml) - для модуля
     *
     * @param module - название модуля
     * @param listClass - список описаний сущностей
     * @return - сгенерированная модель
     * @throws DictionaryException - исключение словарной системы
     */
    private StringBuffer getConfig(String module, List<PersistentClass> listClass) throws DictionaryException {
        //глобальная конфигурация для генератор
        HibernateMappingGlobalSettings hgs = getHConfig(module);
        StringBuffer stream = new StringBuffer();
        Exporter exporter = TypeExport.XML.getExporter(listClass, metadata, stream);
        exporter.setGlobalSettings(hgs);
        exporter.setModuleName(module);
        exporter.start();
        logger.debug(String.format("Generator:%1s ", stream));
        return stream;
    }

    /**
     * генерация тела конфигурации (class)
     *
     * @param module - название модуля
     * @param listClass - список описаний сущностей
     * @return - сгенерированная модель
     * @throws DictionaryException - исключение словарной системы
     */
    private Map<String, StringBuffer> getConfigClass(String module, List<PersistentClass> listClass) throws DictionaryException {
        //глобальная конфигурация для генератор
        HibernateMappingGlobalSettings hgs = getHConfig(module);
        Map<String, StringBuffer> streams = new HashMap<String, StringBuffer>();
        //получим POJOClass
        Iterator<POJOClass> pojo = new Cfg2JavaTool().getPOJOIterator(listClass.iterator());
        while (pojo.hasNext()) {
            StringBuffer stream = new StringBuffer();
            POJOClass pc = pojo.next();
            Exporter exporter = TypeExport.CLASS.getExporter(pc, metadata, stream);
            exporter.setGlobalSettings(hgs);
            exporter.setModuleName(module);
            exporter.start();
            logger.debug(String.format("Generator class %1s  :%2s ", pc.getShortName(), stream));
            streams.put(pc.getShortName(), stream);
        }
        return streams;
    }

    /**
     * генерация тела конфигурации (aspect) - для модуля
     *
     * @param module -  наименование модуля
     * @return - конфигурация
     * @throws DictionaryException - исключение словарной системы
     */
    private StringBuffer getConfigAspect(String module, List<SadEntity> entitys) throws DictionaryException {
        StringBuffer stream = new StringBuffer();
        Exporter exporter = TypeExport.ASPECT.getExporter(entitys, metadata, stream);
        exporter.setModuleName(module);
        exporter.start();
        logger.debug(String.format("Generator:%1s ", stream));
        return stream;
    }

    /**
     * генерация тела конфигурации (aspect) - все
     * 
     * @param entitys - коллекция модуль, сущности
     * @return - конфигурация
     * @throws DictionaryException - исключение словарной системы
     */
    private StringBuffer getConfigAspectAll(Map<String, List<SadEntity>> entitys) throws DictionaryException {
        StringBuffer stream = new StringBuffer();
        Exporter exporter = TypeExport.ASPECT.getExporter(entitys, metadata, stream);
        exporter.setModuleName("all");
        exporter.start();
        logger.debug(String.format("Generator:%1s ", stream));
        return stream;
    }

    /**
     * Пост конструктор
     * добавляем получателей сгенеренной метадаты
     */
    @PostConstruct
    public void init() {
        this.orm.addInterceptor(new GenerateEntityInterceptor(this));
    }

}

