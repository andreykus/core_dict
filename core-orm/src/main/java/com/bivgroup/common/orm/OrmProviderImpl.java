package com.bivgroup.common.orm;

import com.bivgroup.common.Constants;
import com.bivgroup.common.orm.audit.AuditLogEntityInterceptor;
import com.bivgroup.common.orm.interceptors.AgregateInterceptor;
import com.bivgroup.common.orm.interceptors.ORMAgregateInterceptor;
import com.bivgroup.common.orm.interceptors.ORMInterceptor;
import com.bivgroup.common.orm.interceptors.MutantQuerySQLInterceptor;
import com.bivgroup.common.orm.interfaces.ConsumerConfig;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.common.orm.page.ScrollableResultsCollectionOnList;
import com.bivgroup.common.utils.observer.Observer;
import com.bivgroup.common.utils.observer.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.transform.Transformers;

import javax.persistence.*;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bush on 05.08.2016.
 * Провайдер работы с БД, реализует интерфейс OrmProvider {@link OrmProvider}
 */
public class OrmProviderImpl implements OrmProvider, Subject, Observer {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private DataSource dataSource;
    private EntityManager em;
    private Session session;
    private String PERSISTENCE_NAME;
    private Integer DEFAULT_PAGE_NUM = 1;
    private Boolean withInnerTransaction = Boolean.TRUE;
    private ORMAgregateInterceptor interceptor;
    private List<Observer> observersInjectParamInterceptor;
    private MutantQuerySQLInterceptor mutantInterceptor = new MutantQuerySQLInterceptor();
    private ConsumerConfig extCfg;
    private EntityManagerFactoryBuilder builder;


    public OrmProviderImpl(DataSource ds, String persistenceName) {
        this.dataSource = ds;
        this.PERSISTENCE_NAME = persistenceName;
        this.observersInjectParamInterceptor = new CopyOnWriteArrayList<Observer>();
        this.observersInjectParamInterceptor.add(this.mutantInterceptor);
       // this.extCfg = new RuntimeConfig(this);
    }


    /**
     * В озвращает EntityManager для работы с БД
     *
     * @return - EntityManager
     */
    public EntityManager getEntityManager() {
        initInternalEntityManager(this.dataSource, this.PERSISTENCE_NAME);
        return this.em;
    }


    /**
     * регистрация наблюдателя на модуле
     *
     * @param Observer -  наблюдатель
     */
    public void register(Observer Observer) {
        observersInjectParamInterceptor.add(Observer);
    }

    /**
     * удаление наблюдателя на модуле
     *
     * @param Observer - наблюдатель
     */
    public void unRegistrer(Observer Observer) {
        observersInjectParamInterceptor.remove(Observer);
    }

    /**
     * уведомление всех наблюдателей модуля
     *
     * @param args -  аргументы
     */
    public void notifyObservers(Object... args) {
        Iterator<Observer> observer = observersInjectParamInterceptor.iterator();
        while (observer.hasNext()) {
            observer.next().update(args);
        }
    }

    public void useInnerTransaction(Boolean withInnerTransaction) {
        this.withInnerTransaction = withInnerTransaction;
    }

    /**
     * Инициирует работу с БД и возвращается Session
     *
     * @return - сессия
     */
    public Session getSession() {
        initInternalEntityManager(this.dataSource, this.PERSISTENCE_NAME);
        return (Session) this.session;
    }

    /**
     * получить DATA SOURCE
     *
     * @return - DataSource
     */
    public DataSource getDataSource() {
        return  this.dataSource;
    }

    /**
     * Открывает тразакцию
     */
    private void openTransaction() {
        EntityTransaction transaction = getEntityManager().getTransaction();
        if (transaction.isActive()) {
            getEntityManager().joinTransaction();
        } else {
            transaction.begin();
        }
    }

    /**
     * Закрывает тразакцию
     */
    private void closeTransaction() {
        if (withInnerTransaction) {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().commit();
            }
        }
    }

    /**
     * Содержимое persistence.xml в контексте unitName
     *
     * @param unitName - контекст
     * @return - ParsedPersistenceXmlDescriptor
     */
    private ParsedPersistenceXmlDescriptor getDescriptorByName(String unitName) {
        List<ParsedPersistenceXmlDescriptor> listpersisit = PersistenceXmlParser.locatePersistenceUnits(Collections.EMPTY_MAP);
        ParsedPersistenceXmlDescriptor descriptor = null;
        for (ParsedPersistenceXmlDescriptor tmpdescriptor : listpersisit) {
            if (tmpdescriptor.getName().equals(unitName)) {
                descriptor = tmpdescriptor;
            }
        }
        if (descriptor == null) {
            throw new RuntimeException("not find this persist unit in config");
        }
        return descriptor;
    }

    /**
     * добавить перехватчик событий
     * @param interceptor - перехватчик
     */
    public void addInterceptor(ORMInterceptor interceptor){
        this.interceptor.addInterceptor(interceptor);
    }

    /**
     * Инициализация перехватчика. Т.к. Hibernet позволяет привязать к фабрике толко один перехватчик , то быдем использовать свой лист перехватчиков
     */
    private void setInterceptor() {
        ORMAgregateInterceptor interceptor = new AgregateInterceptor();
        interceptor.addInterceptor(new AuditLogEntityInterceptor());
        interceptor.addInterceptor(this.mutantInterceptor);
        //TODO перенести - например сделать addInterceptor
       // interceptor.addInterceptor(new GenerateEntityInterceptor(generator));
        this.interceptor = interceptor;
    }

    /**
     * Инициализация фабрики с параметрами и создание EntityManager и Session
     *
     * @param inDataSource - DataSource
     * @param unitName- контекст
     */
    private void initInternalEntityManager(DataSource inDataSource, String unitName) {
        if (this.em == null) {
            ParsedPersistenceXmlDescriptor descriptor = getDescriptorByName(unitName);
            descriptor.setNonJtaDataSource(inDataSource);
            Map cfg = new HashMap();
            cfg.put(org.hibernate.cfg.AvailableSettings.DEFAULT_ENTITY_MODE,
                    EntityMode.MAP.getExternalName());
            cfg.put(org.hibernate.jpa.AvailableSettings.SESSION_FACTORY_OBSERVER,
                    MySessionFactoryObserver.class.getName());
            setInterceptor();
            if (interceptor != null) {
                cfg.put(AvailableSettings.INTERCEPTOR, interceptor);
            }
            builder = Bootstrap.getEntityManagerFactoryBuilder(
                    descriptor, //or use PersistenceUnitInfoAdapter
                    cfg
            );
            // not mate builder.withDataSource(dataSource);
            // SessionFactoryBuilder sfBuilder = ((EntityManagerFactoryBuilderImpl) builder).getmetadata().getSessionFactoryBuilder();
            // SessionFactoryBuilder sb = ((EntityManagerFactoryBuilderImpl) builder).getMetadata().getSessionFactoryBuilder();
            //sb.applyInterceptor(interceptor);
            EntityManagerFactory entityManagerFactory = builder.build();
            //  ((SessionFactoryImpl)entityManagerFactory).configuredInterceptor(interceptor, ((SessionFactoryImpl)entityManagerFactory).getSessionFactoryOptions());
            this.em = entityManagerFactory.createEntityManager();
            this.session = em.unwrap(Session.class);
            if (interceptor != null) {
                interceptor.setSession(this.session);
            }
            //regenOnInit();
        }
    }

    /**
     *  полчучить потребителя конфигурации
     *
     * @return - потребитель конфигурации
     */
    public ConsumerConfig getConfig() {
        return extCfg;
    }

    /**
     * установить потребителя конфигурации
     *
     * @param config - потребитель конфигурации
     */
    public void setConfig(ConsumerConfig config) {
        this.extCfg = config;
    }

    /**
     * построитель фабрики EntityManager
     *
     * @return
     */
    public EntityManagerFactoryBuilder getBuilder() {
        return builder;
    }

    /**
     * Вернуть перехватчик
     *
     * @return -  агрегатный перехватчик
     */
    public ORMAgregateInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * Заполняем не найденные ( не связанные ) параметры null, иначе не сойдется позиционирование параметров
     *
     * @param query     - запрос
     * @param realParam - входные параметры
     */
    private void setNullValueToParams(Query query, Map<String, ?> realParam) {
        Set<String> errorParams = new HashSet<String>();
        Set<Parameter<?>> paramsInSql = query.getParameters();
        for (Parameter paramTmp : paramsInSql) {
            if (!query.isBound(paramTmp)) {
                query.setParameter(paramTmp.getName(), null);
            }
            if (!realParam.containsKey(paramTmp.getName())) {
                errorParams.add(paramTmp.getName());
            }
        }
        if (!errorParams.isEmpty()) {
            //  throw new RuntimeException (String.format("orm: for this query %1s require params: %2s", ((QueryImplementor)query).getComment(), errorParams));
        }
    }

    /**
     * Т.к запрос Hibernet не работает с Map в JPA (только для сессии в режиме EntityMode.MAP). Нужно переложит параметры из входной Map
     *
     * @param query     - запрос
     * @param realParam - входные параметры
     */
    public void predProcessParam(Query query, Map<String, ?> realParam) {
        Set<String> paramsUnic = new HashSet<String>();
        Set<Parameter<?>> paramsInSql = query.getParameters();
        for (Parameter paramTmp : paramsInSql) {
            paramsUnic.add(paramTmp.getName());
        }
        for (Map.Entry<String, ?> ent : realParam.entrySet()) {
            if (paramsUnic.contains(ent.getKey())) {
                query.setParameter(ent.getKey(), ent.getValue());
            }
        }
        setNullValueToParams(query, realParam);
        //доставим перехватчику реальные параметры (для иньекции)
        notifyObservers(realParam, query);
    }

    /**
     * данные из БД
     * результат как простой список
     * {@link OrmProvider ().getCommonListRezult}
     *
     * @param nameQuery -  именованный запрос
     * @param <T> -  тип выходного списка
     * @return - простой список
     * @throws OrmException
     */
    public <T> List<Map<String, T>> getCommonListRezult(String nameQuery) throws OrmException {
        return getCommonListRezult(nameQuery, new HashMap<String, Object>(1));
    }

    /**
     * данные из БД
     * результат как простой список
     * {@link OrmProvider ().getCommonListRezult}
     *
     * @param nameQuery -  именованный запрос
     * @param realParam - входные параметры
     * @param <T> -  тип выходного списка
     * @return - простой список
     * @throws OrmException
     */
    public <T> List<Map<String, T>> getCommonListRezult(String nameQuery, Map<String, ?> realParam) throws OrmException {
        try {
            openTransaction();
            org.hibernate.query.Query query = getSession().createNamedQuery(nameQuery);
            predProcessParam(query, realParam);
            return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }
    /**
     * данные из БД
     * список идентификаторов
     * {@link OrmProvider ().getCommonListIds}
     *
     * @param nameQuery -  именованный запрос
     * @param realParam - входные параметры
     * @param fieldName - поле идентификатора
     * @return -  список идентификаторов
     * @throws OrmException
     */
    public Set<Long> getCommonListIds(String nameQuery, Map<String, ?> realParam, String fieldName) throws OrmException {
        Set<Long> rez = new HashSet<Long>();
        List<Map<String, Long>> rez_tmp = getCommonListRezult(nameQuery, realParam);
        for (Map m : rez_tmp) {
            Long o = (m.get(fieldName) instanceof String) ? Long.valueOf((String) m.get(fieldName)) : ((Number) m.get(fieldName)).longValue();
            rez.add(o);
        }
        return rez;
    }

    /**
     * данные из БД
     * список с разбивкой по страницам
     * {@link OrmProvider ().getCommonListPageRezult}
     *
     * @param nameQueryAll -  именованный запрос
     * @param realParam - входные параметры
     * @param <T> -  тип выходного объекта
     * @return - список с разбивкой по страницам
     * @throws OrmException
     */
    public <T> Map<String, T> getCommonListPageRezult(String nameQueryAll, Map<String, ?> realParam) throws OrmException {
        try {
            openTransaction();
            Integer pageNum = (Integer) realParam.get(Constants.PAGEABLE_LIST_PAGE_NUM_NAME);
            if (pageNum == null) {
                pageNum = DEFAULT_PAGE_NUM;
            }
            ScrollableResultsCollectionOnList collection = new ScrollableResultsCollectionOnList(this, realParam, nameQueryAll);
            List<Map<String, T>> rezList = collection.iterator().loadPage(pageNum);
            Map rez = new HashMap();
            rez.put(Constants.PAGEABLE_LIST_NAME, rezList);
            rez.put(Constants.PAGEABLE_LIST_COUNT_NAME, collection.size());
            rez.put(Constants.PAGEABLE_LIST_COUNT_PAGES_NAME, collection.iterator().pages());
            rez.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, pageNum);
            rez.put(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME, rezList.size());
            return rez;
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }

    /**
     * данные из БД
     * список с разбивкой по страницам
     * {@link OrmProvider ().getCommonListPageRezult}
     *
     * @param nameQueryAll -  именованный запрос, все записи
     * @param nameQueryCount -  именованный запрос, количество записей
     * @param realParam - входные параметры
     * @param <T>  -  тип выходного объекта
     * @return - список с разбивкой по страницам
     * @throws OrmException
     */
    public <T> Map<String, T> getCommonListPageRezult(String nameQueryAll, String nameQueryCount, Map<String, ?> realParam) throws OrmException {
        try {
            openTransaction();
            Integer pageNum = (Integer) realParam.get(Constants.PAGEABLE_LIST_PAGE_NUM_NAME);
            if (pageNum == null) {
                pageNum = 1;
            }
            ScrollableResultsCollectionOnList collection = new ScrollableResultsCollectionOnList(this, realParam, nameQueryAll, nameQueryCount, null);
            List<Map<String, T>> rezList = collection.iterator().loadPage(pageNum);
            Map rez = new HashMap();
            rez.put(Constants.PAGEABLE_LIST_NAME, rezList);
            rez.put(Constants.PAGEABLE_LIST_COUNT_NAME, collection.size());
            rez.put(Constants.PAGEABLE_LIST_COUNT_PAGES_NAME, collection.iterator().pages());
            rez.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, pageNum);
            rez.put(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME, rezList.size());
            return rez;
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }

    /**
     * данные из БД
     * одна запись
     * {@link OrmProvider ().getCommonRecordRezult}
     *
     * @param nameQuery -  именованный запрос
     * @param realParam - входные параметры
     * @param <T> -  тип выходного объекта
     * @return - одна запись
     * @throws OrmException
     */
    public <T> Map<String, T> getCommonRecordRezult(String nameQuery, Map<String, ?> realParam) throws OrmException {
        try {
            openTransaction();
            org.hibernate.query.Query query = getSession().createNamedQuery(nameQuery);
            predProcessParam(query, realParam);
            Map<String, T> rez = (Map<String, T>) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getSingleResult();
            return rez;
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }

    /**
     * данные из БД
     * одна запись
     * {@link OrmProvider ().getCommonRezult}
     *
     * @param nameQuery -  именованный запрос
     * @param realParam - входные параметры
     * @param <T> -  тип выходного объекта
     * @return -  объект типа  T
     * @throws OrmException
     */
    public <T> T getCommonRezult(String nameQuery, Map<String, ?> realParam) throws OrmException {
        openTransaction();
        try {
            openTransaction();
            Query query = getEntityManager().createNamedQuery(nameQuery);
            predProcessParam(query, realParam);
            return (T) query.getSingleResult();
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }

    /**
     * исполнение модифицырующего запроса
     * {@link OrmProvider ().performNonSelectingQuery}
     *
     * @param nameQuery -  именованный запрос
     * @param realParam - входные параметры
     * @param <T> -  тип входного объекта
     * @throws OrmException
     */
    public <T> void performNonSelectingQuery(String nameQuery, Map<String, ?> realParam) throws OrmException {
        try {
            openTransaction();
            Query query = getEntityManager().createNamedQuery(nameQuery);
            predProcessParam(query, realParam);
            int outSize = query.executeUpdate();
        } catch (Exception ex) {
            getEntityManager().getTransaction().rollback();
            throw new OrmException(ex);
        } finally {
            closeTransaction();
        }
    }

    /**
     * @param query
     */
    private void rebuildQuery(Query query) {
        //
    }

    /**
     * приведение к типу
     *
     * @param type
     * @param tuple
     * @param <T>
     * @return
     */
    private  <T> T map(Class<T> type, Object[] tuple) {
        List<Class<?>> tupleTypes = new ArrayList<Class<?>>();
        for (Object field : tuple) {
            tupleTypes.add(field.getClass());
        }
        try {
            Constructor<T> ctor = null;
            return ctor.newInstance(tuple);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * приведение к типу
     *
     * @param type
     * @param records
     * @param <T>
     * @return
     */
    private  <T> List<T> map(Class<T> type, List<Object[]> records) {
        List<T> result = new LinkedList<T>();
        for (Object[] record : records) {
            result.add(map(type, record));
        }
        return result;
    }

    /**
     * Получение данных из бд с приведением к типу
     *
     * @param query -  именованный запрос
     * @param type  - выходной тип
     * @param <T> - тип
     * @return - список
     */
    private  <T> List<T> getResultList(Query query, Class<T> type) {
        @SuppressWarnings("unchecked")
        List<Object[]> records = query.getResultList();
        Map<String, Object> m = query.getHints();
        return map(type, records);
    }

//    private void regenOnInit() {
//        try {
//            generator.generate();
//        } catch (DictionaryException e) {
//            logger.error(e);
//        }
//    }

    /**
     *  обновление сессии, EntityManager
     * @param arg
     */
    public void update(Object... arg) {
        Object ob = arg[0];
        if (ob instanceof SessionFactory) {
            this.em = ((SessionFactory) ob).createEntityManager();
            this.session = em.unwrap(Session.class);
            if (interceptor != null) {
                interceptor.setSession(this.session);
            }
        }

    }
}
