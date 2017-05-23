package com.bivgroup.core.aspect.visitors;

import com.bivgroup.core.aspect.Constants;
import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.aspect.bean.Aspect;
import com.bivgroup.core.aspect.bean.Field;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Metamodel;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 07.12.2016.
 * Шаблонный класс для обработчика аспекта
 */
public abstract class AbstractAspectVisitor<T> implements AspectVisitor<T> {
    /**
     * обработчик аспекта
     */
    private final AspectVisitor next;
    /**
     * внутренне хранилище  - сквозное для посетителей
     */
    public T source;
    /**
     * аспект
     */
    protected Aspect aspect;
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * Дао
     */
    protected ExtendGenericDAO externaldao;
    /**
     * Поля
     */
    protected List<Field> fields;

    /**
     * Следующий по цепочке обработчик аспекта
     *
     * @return - обработчик аспекта
     */
    public AspectVisitor getNext() {
        return next;
    }

    /**
     * Описание обработчика аспекта
     *
     * @return - описание
     */
    public String getDescription() {
        return this.getClass().getSimpleName() + ((next != null) ? "->" + next.getDescription() : "");
    }

    /**
     * Есть ли следующий в цепочке обработчиков
     *
     * @return
     */
    public boolean hasNext() {
        if (next != null) return true;
        return false;
    }

    /**
     * Инициализация внктренней коллекции
     *
     * @throws AspectException - Исключение для модуля аспекты
     */
    void initInnerCollection() throws AspectException {
        try {
            if (source == null) {
                Type source = this.getClass().getGenericSuperclass();
                ParameterizedType parametrizedType = null;
                while (parametrizedType == null) {
                    if ((source instanceof ParameterizedType)) {
                        parametrizedType = (ParameterizedType) source;
                    } else {
                        source = ((Class<?>) source).getGenericSuperclass();
                    }
                }
            }
        } catch (Exception ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Конструктор Шаблонный класс для обработчика аспекта
     * @param next -  следующий обработчик
     * @param externaldao - Дао
     * @param aspect - аспект
     * @throws AspectException -  исключение аспекта
     */
    public AbstractAspectVisitor(AspectVisitor next, ExtendGenericDAO externaldao, Aspect aspect) throws AspectException {
        this.next = next;
        this.aspect = aspect;
        this.externaldao = externaldao;
        this.fields = aspect.getField();
        initInnerCollection();
    }

    /**
     * Обрабатываем после исполненениня метода
     *
     * @param action  - действие (созданиеб, удаление ..)
     * @param method  - метод
     * @param args    - аргументы метода
     * @param element - результат исполнения метода
     * @throws AspectException - Исключение для модуля аспекты
     */
    protected void processNextPost(Action action, Method method, Object[] args, Object element) throws AspectException {
        if (next != null) {
            next.visitPost(action, method, args, element, source);
        }
    }

    /**
     * Обрабатываем до исполненениня метода
     *
     * @param action - действие (созданиеб, удаление ..)
     * @param method - метод
     * @param args   - аргументы метода
     * @throws AspectException - Исключение для модуля аспекты
     */
    protected void processNextPred(Action action, Method method, Object[] args) throws AspectException {
        if (next != null) {
            next.visitPred(action, method, args, source);
        }
    }

    /**
     * Является ли поле связанным с аспектом
     *
     * @param fieldName - поле
     * @return - true, если поле под аспектом
     */
    public boolean isAspectField(String fieldName) {
        if (fields == null || fields.isEmpty()) return false;
        for (Field field : fields) {
            if (fieldName.equals(field.getSysname())) return true;
        }
        return false;
    }

    /**
     * Получение название сущности из его аргументов
     *
     * @param method - метод
     * @param args   - аргументы метода
     * @return - название сущности
     * @throws AspectException - Исключение для модуля аспекты
     */
    protected String getEntityName(Method method, Object[] args) throws AspectException {
        if (args != null && args.length > 0) {
            Parameter[] params = method.getParameters();
            String entityName = null;
            String userId = null;
//        if (args != null && args.length > 0 && args[0] != null && args[0] instanceof String) {
//            String entity = args[0].toString();
            for (int i = 0; i < params.length; i++) {
                Parameter param = params[i];
                AspectMethodParam metaParam = param.getAnnotation(AspectMethodParam.class);
                if (metaParam == null) continue;
                if (Constants.ENTITY_NAME.equals(metaParam.nameParam())) {
                    entityName = args[i].toString();
                    Metamodel metamodel = externaldao.getSession().getSessionFactory().getMetamodel();
                    if (metamodel.entity(entityName) != null) {
                        return entityName;
                    }
                }
            }
        }
        logger.warn(String.format("Metod %1s not contains compatible params", method.getName()));
        return null;
    }

    /**
     * Получение параметров метода из его аргументов
     *
     * @param method - метод
     * @param args   - аргументы метода
     * @return  - параметры
     */
    protected Map getInParam(Method method, Object[] args) {
        if (args != null && args.length > 0) {
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                Parameter param = params[i];
                AspectMethodParam metaParam = param.getAnnotation(AspectMethodParam.class);
                if (metaParam == null) continue;
                if (Constants.PARAM_NAME.equals(metaParam.nameParam())) {
                    if (args[i] != null && args[i] instanceof Map)
                        return (Map) args[i];
                }
            }
        }
        return null;
    }
}
