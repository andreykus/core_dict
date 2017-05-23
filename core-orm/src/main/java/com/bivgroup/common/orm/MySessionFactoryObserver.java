package com.bivgroup.common.orm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;

/**
 * Created by bush on 02.08.2016.
 * Наблюдатель сессии
 */
public class MySessionFactoryObserver implements SessionFactoryObserver {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /**
     * при создании сессии
     *
     * @param factory
     */
    public void sessionFactoryCreated(SessionFactory factory) {
        logger.debug("orm: create session Factory");
    }

    /**
     * при закрытии сессии
     * @param factory
     */
    public void sessionFactoryClosed(SessionFactory factory) {
        logger.debug("orm: closed session Factory");
    }
    /**
     * при закрытой сессии
     * @param factory
     */
    public void sessionFactoryClosing(SessionFactory factory) {
        logger.debug("orm: closing session Factory");
    }
}

