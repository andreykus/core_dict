package com.bivgroup.core.dictionary.generator;

import com.bivgroup.common.orm.interfaces.ConsumerConfig;
import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.utils.observer.Observer;
import com.bivgroup.common.utils.ToolCore;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.ExportObjectType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.internal.SessionFactoryBuilderImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by andreykus on 15.09.2016.
 * Потребитель сгененренной конфигурации RunTime
 * получает и перегружает SessionFactory, EntityManager , Session c новыми метаданными
 */
public class RuntimeConfig implements ConsumerConfig {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * подписчики
     */
    private List<Observer> observers = new ArrayList<Observer>();
    /**
     * список экспортируемых объектов
     */
    private List<ExportObject> observersConfig;
    /**
     * провайдер orm
     */
    private OrmProviderImpl orm;
    /**
     * пакет для генерируемых сущностей
     */
    private final String ENTITY_PACKAGE = "com.bivgroup.core.dictionary.entity";

    /**
     * конструктор Потребителя сгененренной конфигурации RunTime
     *
     * @param orm - orm
     */
    public RuntimeConfig(OrmProviderImpl orm) {
        this.orm = orm;
        this.orm.setConfig(this);
        this.observersConfig = new CopyOnWriteArrayList<ExportObject>();
    }

    /**
     * Загрузим новые метаданные
     *
     * @return - SessionFactory
     * @throws DictionaryException - исключение словарной системы
     */
    private SessionFactory redeploy() throws DictionaryException {
        EntityManagerFactoryBuilderImpl emb = (EntityManagerFactoryBuilderImpl) orm.getBuilder();
        SessionFactoryBuilderImpl sb = (SessionFactoryBuilderImpl) emb.getMetadata().getSessionFactoryBuilder();
        StandardServiceRegistry sr = sb.getServiceRegistry();
        MetadataSources sources = new MetadataSources(sr);

        //почемуто теряются внуттренние Entity, поэтому мы их загрузим
        List<Class> listEnClass = new ToolCore().getAllClassOnPackage(ENTITY_PACKAGE);
        for (Class clazz : listEnClass) {
            sources.addAnnotatedClass(clazz);
        }

        //JPAConfiguration configuration = new JPAConfiguration((EntityManagerFactoryBuilderImpl) builder);
        //add external Entity
        for (ExportObject config : observersConfig) {
            logger.debug(config.toString());
            ExportObjectType.exportObject(sources, config);
        }

        Metadata metadata = sources.buildMetadata();
        SessionFactory sf = metadata.buildSessionFactory();
        return sf;
    }

    /**
     * регистрация наблядателя (у нас это DAO)
     *
     * @param Observer - наблюдатель
     */
    @Override
    public void register(Observer Observer) {
        if (!observers.contains(Observer)) {
            observers.add(Observer);
        }
    }

    /**
     * удалим наблядателя (у нас это DAO)
     *
     * @param Observer - наблюдатель
     */
    @Override
    public void unRegistrer(Observer Observer) {
        observers.remove(Observer);
    }

    /**
     * Так наши DAO получают новые SessionFactory, Session
     *
     * @param configs - список конфигураций
     * @throws OrmException - исключение ORM
     */
    @Override
    public void processConfig(List<ExportObject> configs) throws OrmException {
        notifyObservers(configs);
    }

    /**
     * Загрузим конфигурацию и Передадим всем потребителя новый SessionFactory
     *
     * @param args - список конфигураций
     * @throws OrmException - исключение ORM
     */
    @Override
    public void notifyObservers(Object... args) throws OrmException {
        if (args == null || observers == null) return;
        Iterator<Observer> it = observers.iterator();
        while (it.hasNext()) {
            for (Object obj : (List) args[0]) {
                observersConfig.add((ExportObject) obj);
            }
            SessionFactory sf = redeploy();
            it.next().update(sf);
        }
    }
}
