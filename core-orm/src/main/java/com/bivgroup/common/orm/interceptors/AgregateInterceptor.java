package com.bivgroup.common.orm.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bush on 19.08.2016.
 * Перехватчик расширитель стандартного для Hibernet
 */
public class AgregateInterceptor extends EmptyInterceptor implements ORMAgregateInterceptor {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * сессия
     */
    private Session session;
    /**
     * список внутренних перехватчиков
     */
    private List<ORMInterceptor> interceptors = new ArrayList<ORMInterceptor>();

    /**
     * установить сессию
     *
     * @param session - сессия
     */
    public void setSession(Session session) {
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            iter.next().setSession(session);
        }
        this.session = session;
    }

    /**
     * добавить перехватчик
     *
     * @param interceptor -  перехватчик
     */
    public void addInterceptor(ORMInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    /**
     * получить перехватчик
     *
     * @return - перехватчик
     */
    public List<ORMInterceptor> getInterceptors() {
        return this.interceptors;
    }

    /**
     * выполняется после опреации Flush
     *
     * @param iterator - итератор
     */
    @Override
    public void postFlush(Iterator iterator) {
        //на всех перехватчиках выполнить операцию postFlush
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            iter.next().postFlush(iterator);
        }
    }

    /**
     * выполняется перед опреацией  Flush
     *
     * @param iterator
     */
    @Override
    public void preFlush(Iterator iterator) {
        //на всех перехватчиках выполнить операцию preFlush
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            iter.next().preFlush(iterator);
        }
    }

    /**
     * выполняется при операции Delete
     *
     * @param entity        -  сущность
     * @param id            - идентификиатор
     * @param state         -  состояние
     * @param propertyNames -  параметры
     * @param types         -  типы
     */
    @Override
    public void onDelete(Object entity, Serializable id,
                         Object[] state, String[] propertyNames,
                         Type[] types) {
        //на всех перехватчиках выполнить операцию onDelete
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            iter.next().onDelete(entity, id, state, propertyNames, types);
        }

    }

    /**
     * выполняется при операции FlushDirty
     *
     * @param entity        -  сущность
     * @param id            - идентификиатор
     * @param currentState  - текущие сосогтояния
     * @param previousState - пред сосотояния
     * @param propertyNames -  параметры
     * @param types         -  типы
     * @return
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        //на всех перехватчиках выполнить операцию onFlushDirty
        boolean rez = false;
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            rez = rez | iter.next().onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }
        return rez;
    }

    /**
     * выполняется при операции Save
     *
     * @param entity        -  сущность
     * @param id            - идентификиатор
     * @param state         -  состояние
     * @param propertyNames -  параметры
     * @param types         -  типы
     * @return
     */
    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) {
        //на всех перехватчиках выполнить операцию Save
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        boolean rez = false;
        while (iter.hasNext()) {
            rez = rez | iter.next().onSave(entity, id, state, propertyNames, types);
        }
        return rez;
    }

    /**
     * сформировался PrepareStatement
     *
     * @param sql -  sql запрос
     * @return -  sql запрос
     */
    @Override
    public String onPrepareStatement(String sql) {
        Iterator<ORMInterceptor> iter = interceptors.iterator();
        while (iter.hasNext()) {
            sql = iter.next().onPrepareStatement(sql);
        }
        return sql;
    }


}