package com.bivgroup.common.orm.audit;

import com.bivgroup.common.orm.interceptors.ORMInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bush on 15.08.2016.
 * Перехватчик для аудита событий с объектами persistence
 */
public class AuditLogEntityInterceptor extends EmptyInterceptor implements ORMInterceptor {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * сессия
     */
    private Session session;
    /**
     * список действий - вставить
     */
    private Set inserts = new HashSet();
    /**
     * список действий - обновить
     */
    private Set updates = new HashSet();
    /**
     * список действий - удалить
     */
    private Set deletes = new HashSet();


    /**
     * сесия
     * @param session - сессия
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * при сохранении
     * @param entity -  сущность
     * @param id -  идентификатор
     * @param state -  сосотояние
     * @param propertyNames -  название свойства
     * @param types -  тип
     * @return - false
     * @throws CallbackException - исключение
     */
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types)
            throws CallbackException {

        if (entity instanceof AuditObject) {
            inserts.add(entity);
        }
        return false;
    }

    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types)
            throws CallbackException {

        if (entity instanceof AuditObject) {
            updates.add(entity);
        }
        return false;

    }

    /**
     * при удалении
     * @param entity - сущности
     * @param id -  идентификатор
     * @param state -  состояние
     * @param propertyNames -  название свойства
     * @param types -  тип
     */
    public void onDelete(Object entity, Serializable id,
                         Object[] state, String[] propertyNames,
                         Type[] types) {

        if (entity instanceof AuditObject) {
            deletes.add(entity);
        }
    }

    /**до коммита в бд*/
    public void preFlush(Iterator iterator) {

    }

    /**после коммита в БД*/
    public void postFlush(Iterator iterator) {
        AuditLogUtil util = new AuditLogUtil();
        try {
            for (Iterator it = inserts.iterator(); it.hasNext(); ) {
                AuditObject ent = (AuditObject) it.next();
                util.LogIt("Saved", ent, session);
            }

            for (Iterator it = updates.iterator(); it.hasNext(); ) {
                AuditObject ent = (AuditObject) it.next();
                util.LogIt("Updated", ent, session);
            }

            for (Iterator it = deletes.iterator(); it.hasNext(); ) {
                AuditObject ent = (AuditObject) it.next();
                util.LogIt("Deleted", ent, session);
            }

        } finally {
            inserts.clear();
            updates.clear();
            deletes.clear();
        }
    }

}