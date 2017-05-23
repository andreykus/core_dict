package com.bivgroup.core.dictionary.interceptor;

import com.bivgroup.common.orm.interceptors.ORMInterceptor;
import com.bivgroup.core.dictionary.generator.GenerateMetadataImpl;
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
 * Created by andreykus on 15.08.2016.
 * Перехватчик измененияв в Словарной системе
 * Обновляет метаданные ORM
 */
public class GenerateEntityInterceptor extends EmptyInterceptor implements ORMInterceptor {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * сессия
     */
    private Session session;
    /**
     * генератор
     */
    private GenerateMetadataImpl generator;
    /**
     * коллекция объектов с действием - вставить
     */
    private Set inserts = new HashSet();
    /**
     * коллекция объектов с действием - обновить
     */
    private Set updates = new HashSet();
    /**
     * коллекция объектов с действием - удалить
     */
    private Set deletes = new HashSet();

    /**
     * Конструктор Перехватчик измененияв в Словарной системе
     * @param generator -  генератор метаданных
     */
    public GenerateEntityInterceptor(GenerateMetadataImpl generator) {
        this.generator = generator;
    }

    /**
     * установить сессию
     * @param session - сессия
     */
    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * При сохранениии
     *
     * @param entity        - сущность
     * @param id            - идентификатор сущности
     * @param state         - состояния
     * @param propertyNames -  наименования свойства
     * @param types         - типы
     * @return - false
     * @throws CallbackException -  исключение
     */
    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types)
            throws CallbackException {

        if (entity.getClass().isAnnotationPresent(GenerateEntity.class)) {
            inserts.add(entity);
        }
        return false;
    }

    /**
     * При обновлении
     *
     * @param entity        - сущность
     * @param id            - идентификатор
     * @param currentState  -  текущее сосотояние
     * @param previousState - пред сосотояние
     * @param propertyNames - название свойства
     * @param types         - типы
     * @return - false
     * @throws CallbackException -  исключение
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types)
            throws CallbackException {

        if (entity.getClass().isAnnotationPresent(GenerateEntity.class)) {
            updates.add(entity);
        }
        return false;

    }

    /**
     * При удалении
     *
     * @param entity        - сущность
     * @param id            -  идентификатор
     * @param state         - сосотояние
     * @param propertyNames - название свойства
     * @param types         - типы
     */
    @Override
    public void onDelete(Object entity, Serializable id,
                         Object[] state, String[] propertyNames,
                         Type[] types) {

        if (entity.getClass().isAnnotationPresent(GenerateEntity.class)) {
            deletes.add(entity);
        }
    }

    //called before commit into database
    @Override
    public void preFlush(Iterator iterator) {

    }

    /**
     * Если есть объекты на изменение , то перегенерим метаданные и очистим метки обновляемых объектов
     * генерация породит содание новорго SessionFactory и EntityManager --- новая сессия
     *
     * @param iterator - итератор операций над сущностью
     */
    @Override
    public void postFlush(Iterator iterator) {
        try {

            if (!inserts.isEmpty() || !updates.isEmpty() || !deletes.isEmpty()) {
                try {
                    //TODO Пока перегенерим все, СДЕЛАТЬ ПО МОДУЛЮ
                    generator.generateAll();
                } catch (Exception e) {
                    logger.error(e);
                }
            }

        } finally {
            inserts.clear();
            updates.clear();
            deletes.clear();
        }
    }

}