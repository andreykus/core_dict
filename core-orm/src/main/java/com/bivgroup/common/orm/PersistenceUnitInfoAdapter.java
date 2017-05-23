package com.bivgroup.common.orm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by bush on 02.08.2016.
 * Адаптер для тестирования
 * {@link PersistenceUnitInfo}
 */
public class PersistenceUnitInfoAdapter implements PersistenceUnitInfo {
    /*логгер**/
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * название адаптера
     */
    private final String name = "persistenceUnitInfoAdapter@" + System.identityHashCode(this);
    /**
     * свойства PersistenceUnit
     */
    private Properties properties;

    /**
     * получить имя PersistenceUnit
     * @return - имя PersistenceUnit
     */
    public String getPersistenceUnitName() {
        return "jpaPersistenceTest";
    }

    /**
     * получить название провайдера PersistenceUnit
     * @return - название провайдера PersistenceUnit
     */
    public String getPersistenceProviderClassName() {
        return HibernatePersistenceProvider.class.getName();
    }

    /**
     * получить  тип транзакции
     * @return - тип транзакции
     */
    public PersistenceUnitTransactionType getTransactionType() {
        return null;
    }

    /**
     * получить JTA источник данных
     * @return - DataSource
     */
    public DataSource getJtaDataSource() {
        return null;
    }
    /**
     * получить не JTA источник данных
     * @return - DataSource
     */
    public DataSource getNonJtaDataSource() {
//        JdbcDataSource dataSource = new JdbcDataSource();
//        dataSource.setURL("jdbc:h2:˜/test");
//        dataSource.setUser("sa");
//        dataSource.setPassword("sa");
        return null;
    }

    /**
     * файлы метаданных
     * @return - список файлов
     */
    public List<String> getMappingFileNames() {
        List<String> files = new ArrayList<String>();
        files.add("com/bivgroup/map/Admin.xml");
        return files;
    }

    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    public List<String> getManagedClassNames() {
        return Collections.emptyList();
    }

    public boolean excludeUnlistedClasses() {
        return false;
    }

    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    /**
     * получить режим валидации
     * @return - режим валидации
     */
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    /**
     * получить сойства PersistenceUnit
     * @return -сойства PersistenceUnit
     */
    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        return properties;
    }

    public String getPersistenceXMLSchemaVersion() {
        return null;
    }
    /**
     * получить загрузчик классов
     * @return - загрузчик классов
     */
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public void addTransformer(ClassTransformer transformer) {
    }

    /**
     * получить загрузчик классов
     * @return - загрузчик классов
     */
    public ClassLoader getNewTempClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
