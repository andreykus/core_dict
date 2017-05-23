package com.bivgroup.common.orm.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by bush on 15.08.2016.
 * Ацдит в БД
 */
public class AuditLogUtil {
    /**
     * логгер
     */
    private static Logger logger = LogManager.getLogger(AuditLogUtil.class);

    /**
     * логировать
     *
     * @param action - действие
     * @param entity - сущность
     * @param session -  сессия
     */
    public void LogIt(final String action
            , final AuditObject entity, Session session) {
        final Session tempSession = session.getSessionFactory().openSession();

        try {
            tempSession.getTransaction().begin();
            tempSession.doWork(
                    new Work() {
                        public void execute(Connection connection) throws SQLException {

                            logger.debug(String.format("orm: audit perisitence - action %1s on entity %2s connection %3s", action, entity.getClass(), connection));
                            //AuditEntity auditEntity = new AuditEntity(entity.getId(), action, entity.getClass().toString(), entity.getDescription());
                            //tempSession.save(auditEntity);
                        }
                    }
            );
            tempSession.flush();
            tempSession.getTransaction().commit();
        } finally {
            tempSession.close();
        }
    }
}