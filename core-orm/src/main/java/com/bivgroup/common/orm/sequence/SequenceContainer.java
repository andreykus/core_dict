package com.bivgroup.common.orm.sequence;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.internal.MetadataBuilderImpl;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.enhanced.StandardOptimizerDescriptor;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.StandardBasicTypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by andreykus on 23.08.2016.
 * Генератор Идентификаторов.
 */

public class SequenceContainer {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * начало  имени последовательности
     */
    public final static String DEFAULT_SEQUENCE_SUFFIX = "_SEQ";
    /**
     * пул выделяемых идентификаторов
     */
    public final static String INCREMENT = "10";
    /**
     * хранилише генераторов
     */
    private ConcurrentHashMap<String, PersistentIdentifierGenerator> generatorStrategyToClassNameMap = new ConcurrentHashMap<String, PersistentIdentifierGenerator>();
    /**
     * провайдер для работы с БД
     */
    private OrmProvider orm;

    /**
     * Конструктор Генератор Идентификаторов
     *
     * @param orm -провайдер для работы с БД
     */
    public SequenceContainer(OrmProvider orm) {
        this.orm = orm;
    }


    /**
     * положить в харнилище генератор
     *
     * @param tablename -  имя таблицы
     * @param generator - генератор
     */
    private void addSequence(String tablename, PersistentIdentifierGenerator generator) {
        generatorStrategyToClassNameMap.put(tablename, generator);
    }

    /**
     * Получить следующий идентификатор
     *
     * @param tablename - имя сущности
     * @return - идентификатор
     * @throws OrmException - исключение  ORM
     */
    public Long getNextId(String tablename) throws OrmException {
        SharedSessionContractImplementor session = (SharedSessionContractImplementor) ((OrmProviderImpl) orm).getSession();
        PersistentIdentifierGenerator generator = getGenerator(tablename);
        Serializable ser = getGenerator(tablename).generate(session, null);
        logger.debug(String.format("orm: sequence %1s return id for table %2s = %3s", generator.generatorKey(), tablename, ser.toString()));
        return (Long) ser;
    }

    /**
     * создать генератор
     *
     * @param tablename - имя сущности
     * @return - генератор
     */
    private PersistentIdentifierGenerator createGenerator(String tablename) {
        final SessionFactory ses = ((OrmProviderImpl) orm).getSession().getSessionFactory();
        final StandardServiceRegistry serviceRegistry = ses.getSessionFactoryOptions().getServiceRegistry();
        SequenceStyleGenerator fGenerator = new SequenceStyleGenerator();
        Properties params = new Properties();
        params.setProperty(SequenceStyleGenerator.JPA_ENTITY_NAME, tablename);
        params.setProperty(SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, "true");
        params.setProperty(SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, DEFAULT_SEQUENCE_SUFFIX);
        params.setProperty(SequenceStyleGenerator.INCREMENT_PARAM, INCREMENT);
        params.setProperty(SequenceStyleGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED.getExternalName());
        Database db = new Database(new MetadataBuilderImpl.MetadataBuildingOptionsImpl(serviceRegistry));
        fGenerator.configure(StandardBasicTypes.LONG, params, serviceRegistry);
        fGenerator.registerExportables(db);
        return fGenerator;
    }

    /**
     * создать последовательность в  БД
     *
     * @param generator - генератор
     * @throws OrmException - исключение  ORM
     */
    private void createSequence(PersistentIdentifierGenerator generator) throws OrmException {
        try {
            SessionImpl session = (SessionImpl) ((OrmProviderImpl) orm).getSession();
            final SessionFactoryImplementor ses = (SessionFactoryImplementor) ((OrmProviderImpl) orm).getSession().getSessionFactory();
            String[] sql = generator.sqlCreateStrings(ses.getDialect());
            for (String expression : sql) {
                PreparedStatement st = session.getJdbcCoordinator().getStatementPreparer().prepareStatement(expression);
                st.execute();
            }
        } catch (SQLException ex) {
            //TODO if contain sequence
            logger.error(String.format("orm: sequence %1s", ex.getMessage()), ex);
        }
    }

    /**
     * получить генератор
     *
     * @param tablename - имя сущности
     * @return - генератор
     * @throws OrmException - исключение  ORM
     */
    private PersistentIdentifierGenerator getGenerator(String tablename) throws OrmException {
        if (!generatorStrategyToClassNameMap.containsKey(tablename)) {
            PersistentIdentifierGenerator generator = createGenerator(tablename);
            generatorStrategyToClassNameMap.put(tablename, generator);
            createSequence(generator);
            return generator;
        } else {
            return generatorStrategyToClassNameMap.get(tablename);
        }
    }

}
