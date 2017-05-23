package com.bivgroup.core.aspect.proxy;

import com.bivgroup.core.aspect.Constants;
import com.bivgroup.core.aspect.annotations.AspectClass;
import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.aspect.bean.AspectCfg;

import com.bivgroup.core.aspect.builder.AspectCollectionsImpl;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AspectDelegator;
import com.bivgroup.core.aspect.builder.AspectFactory;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ActionOnMap;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import com.bivgroup.core.dictionary.dao.hierarchy.HierarchyDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Metamodel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 06.12.2016.
 * Прокси налагаемый на ДАО модулей
 * - Добавляет методы с делегаторов аспекта
 * - Обрабатывает сущности
 */
public class AspectProxy implements InvocationHandler {
    /** логгер*/
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**объект на который накладывается прокси*/
    private Object obj;
    /** хранилище аспектов*/
    private AspectCollectionsImpl apectCollection;
    /** приведение хранилища аспектов к  сущность:обработчик*/
    protected Map<String, AspectVisitor> visitorOnModule;
    /** приведение хранилища аспектов к  сущность:список аспектов*/
    private Map<String, List<AspectCfg>> aspectsByModule;
    /**Дао*/
    private ExtendGenericDAO externaldao;
    /**название модуля*/
    private String nameModule;

    /**
     * копирование массива
     *
     * @param one - массив
     * @param too - массив
     * @return
     */
    public Class[] copyArrays(Class[] one, Class[] too) {
        if (one == null) {
            return too;
        }
        if (too == null) {
            return one;
        }
        Class[] res = new Class[one.length + too.length];
        System.arraycopy(one, 0, res, 0, one.length);
        System.arraycopy(too, 0, res, one.length, too.length);
        return res;
    }

    /**
     * Получает класс Делегатора привязанный к аспекту
     *
     * @param factory - обработчик
     * @return - класс расширения
     * @throws AspectException - Исключение для модуля аспекты
     */
    private  Class getDelegatorClass(AspectFactory factory) throws AspectException {
        Class visitorClass = factory.getVisitorClass();
        AspectClass aspect = (AspectClass) visitorClass.getDeclaredAnnotation(AspectClass.class);
        if (aspect.delegator() == null) {
            return null;
        }
        Class clazz = aspect.delegator();
        return clazz;
    }

    /**
     * Добавляет все возможный интерфейс делегаторов от аспектов
     *
     * @param interfaces - входные интерфейсы (обычно ExtendGenericDAO )
     * @return - выходные интерфейсы
     * @throws AspectException - Исключение для модуля аспекты
     */
    private Class[] addAllAspectDelegatorOnProxy(Class[] interfaces) throws AspectException {
        AspectFactory[] factorys = AspectFactory.values();
        Class[] delegators = new Class[factorys.length];
        for (int i = 0; i < factorys.length; i++) {
            Class delegatorClass = getDelegatorClass(factorys[i]);
            if (delegatorClass == null) continue;
            delegators[i] = delegatorClass;
        }
        Class[] outInterfaces = copyArrays(interfaces, delegators);
        return outInterfaces;
    }

    /**
     * Экземпля Прокси объекта
     * Раширяет DAo объект, и накладывает обработчики методов
     *
     * @param obj        - входной объект (обычно DAO)
     * @param interfaces - входные интерфейсы
     * @return - прокси
     * @throws AspectException - Исключение для модуля аспекты
     */
    public static Object newInstance(Object obj, Class[] interfaces) throws AspectException {
        List<Class> outInterfaces = new ArrayList<Class>();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].isInterface()) {
                outInterfaces.add(interfaces[i]);
            } else {
                Class[] inter = interfaces[i].getInterfaces();
                if (inter != null && inter.length > 0) {
                    outInterfaces.addAll(Arrays.asList(interfaces[i].getInterfaces()));
                }
            }
        }
        AspectProxy ob = new AspectProxy(obj);
        Class[] out = ob.addAllAspectDelegatorOnProxy(outInterfaces.toArray(new Class[outInterfaces.size()]));
        Object tmpObj = Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                out,
                ob);
        if (obj instanceof ExtendGenericDAO) {
            ((ExtendGenericDAO) obj).setProxy(tmpObj);
        }
        return tmpObj;
    }

    /**
     * Конструктор Прокси
     * @param obj -  объекто DAO
     * @throws AspectException - исключение аспектов
     */
    public AspectProxy(Object obj) throws AspectException {
        this.obj = obj;
        if (obj instanceof ExtendGenericDAO) {
            if (((ExtendGenericDAO) obj).getDataSource() == null)
                throw new AspectException("For create dao Aspect need DataSource");
            this.externaldao = (ExtendGenericDAO) obj;
            this.nameModule = ((ExtendGenericDAO) obj).getModuleName();
        } else {
            throw new AspectException("This module not have interface ExtendGenericDAO");
        }
        initAspect(obj);
    }

    /**
     * обработчик вложенных объектов результата
     *
     * @param method - метод
     * @param args   - аргументы метода
     * @param rez    - результат исполнения метода
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void upByHierarchy(Method method, Object[] args, Object rez) throws AspectException {
        try {
            if (rez != null) {
                //обработем вложенные объекты результата, согласно аспектов
                ActionOnMap act = new UPProcessByHierarhy(this, method, args);
                if (rez instanceof Map) {
                    ((HierarchyDAO) externaldao).setProcessMap((Map<String, Object>) rez, null, act, true);
                }
                if (rez instanceof List) {
                    ((HierarchyDAO) externaldao).setProcessList((List<Map>) rez, null, act);
                }
            }
        } catch (DictionaryException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Вызов нативного метода
     *
     * @param visitor - обработчик
     * @param proxy   - прокси
     * @param method  - метод
     * @param args    - аргументы метода
     * @return - результат выполнения и обработки
     * @throws AspectException - Исключение для модуля аспекты
     */
    private Object ownerMethod(AspectVisitor visitor, Object proxy, Method method, Object[] args) throws
            AspectException {
        //до выполенния метода
        Boolean isExe = before(visitor, method, args);
        Object rez = null;
        try {
            //так отключается исполненние нативного метода
            if (isExe) {
                rez = method.invoke(obj, args);
            } else {
                logger.debug(String.format("Before aspect exclude execution main method %1s", method.getName()));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new AspectException(String.format("method %1s class %2s error process, messeage %3s", method.getName(), method.getDeclaringClass().getName(), ex.getCause().getMessage()), ex.getCause());
        }
        //после выполенния метода
        after(visitor, method, args, rez);
        //обработчик вложенных объектов результата
        upByHierarchy(method, args, rez);
        return rez;
    }

    /**
     * Получить раширение DAo объекта по сущности, цепочике обработке, интерфейсу расширения   из аспектной модели
     *
     * @param entityName     - название сущности
     * @param visitor        - обработчик
     * @param delegatorClass -  расширение dao объекта
     * @return - раширение
     * @throws AspectException - Исключение для модуля аспекты
     */
    private AspectDelegator getDelegator(String entityName, AspectVisitor visitor, Class delegatorClass) throws AspectException {
        do {
            if (isDelegatorConcretClass(visitor.getClass(), delegatorClass)) return (AspectDelegator) visitor;
            visitor = visitor.getNext();
        }
        while (visitor != null);
        throw new AspectException(String.format("Not found delegator %1s on entity %2s", delegatorClass.getName(), entityName));
    }

    /**
     * Вызов метода внешнего расширения
     *
     * @param entityName - название сущности
     * @param visitor    - обработчик
     * @param proxy      - прокси
     * @param method     - метод
     * @param args       - аргументы метода
     * @return - результат метода
     * @throws Throwable
     */
    private Object delegatorMethod(String entityName, AspectVisitor visitor, Object proxy, Method method, Object[] args) throws
            Throwable {
        if (visitor == null) {
            throw new AspectException(String.format("Not found delegator %1s on entity %2s", method.getDeclaringClass().getName(), entityName));
        }
        AspectDelegator delegator = getDelegator(entityName, visitor, method.getDeclaringClass());
        Object rez = method.invoke(delegator, args);
        return rez;
    }

    /**
     * Является ли данный метод из расширениея делегатора (расширением dao через аспект)
     *
     * @param method
     * @return
     */
    private Boolean isDelegatorMethod(Method method) {
        return isDelegatorClass(method.getDeclaringClass());
    }

    /**
     * Является ли данный класс расширением делегатора (расширением dao через аспект)
     *
     * @param clazz - класс
     * @return
     */
    private Boolean isDelegatorClass(Class clazz) {
        return isDelegatorConcretClass(clazz, AspectDelegator.class);
    }

    /**
     * Содержит ли сlazz интерфейс оговоренный face
     *
     * @param clazz - класс
     * @param face  - интерфейс
     * @return
     */
    private Boolean isDelegatorConcretClass(Class clazz, Class face) {
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            return Arrays.asList(interfaces).contains(face);
        }
        return Boolean.FALSE;
    }

    /**
     * Получение наименование сущности по параметрам метода
     *
     * @param method - метод
     * @param args   - аргументы метода
     * @return - наименование сущности
     * @throws AspectException - Исключение для модуля аспекты
     */
    private String getEntityName(Method method, Object[] args) throws AspectException {
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
                    //первый параметр это название сущности
                    entityName = args[i].toString();
                    //TODO переделать на entityName = metaParam.nameParam()
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
     * обработка вызова метода
     *
     * @param proxy  - прокси объект
     * @param method - метод
     * @param args   - аргументы метода
     * @return - результат исполнения
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws
            Throwable {
        method.setAccessible(true);
        //   logger.debug(String.format("Method %1s from class %2s", method.getName(), Reflection.getCallerClass()));
        String entityName = getEntityName(method, args);
        // если нет аспекта то сразу исполняем нативный метод
        if (visitorOnModule == null || entityName == null) {
            return ownerMethod(null, proxy, method, args);
        }
        AspectVisitor visitor = visitorOnModule.get(entityName);
        if (isDelegatorMethod(method)) {
            // если это метод расширения dao описанный аспектом
            return delegatorMethod(entityName, visitor, proxy, method, args);
        } else {
            // если это нативный метод dao
            return ownerMethod(visitor, proxy, method, args);
        }
    }

    /**
     * до нативного исмолнения метода
     *
     * @param visitor - обработчик
     * @param method  - метод
     * @param args    - аргументы метода
     * @return - результаты нативного исполнения метода
     * @throws AspectException - Исключение для модуля аспекты
     */
    private Boolean before(AspectVisitor visitor, Method method, Object[] args) throws AspectException {
        AspectMethod annotation = method.getDeclaredAnnotation(AspectMethod.class);
        Boolean rez = true;
        if (annotation != null && visitor != null) {
            logger.info("Start before invoke");
            rez = (Boolean) visitor.visitPred(annotation.action(), method, args, rez);
        }
        return rez;
    }

    /**
     * после нативного исмолнения метода
     *
     * @param visitor - обработчик
     * @param method  - метод
     * @param args    - аргументы метода
     * @param rez     - результаты выполнения метода
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void after(AspectVisitor visitor, Method method, Object[] args, Object rez) throws AspectException {
        AspectMethod annotation = method.getDeclaredAnnotation(AspectMethod.class);
        if (annotation != null && visitor != null) {
            logger.info("Start after invoke");
            visitor.visitPost(annotation.action(), method, args, rez, null);
        }
    }

    /**
     * получим  и заполним аспекты
     *
     * @param obj - текущий dao объект модуля
     * @throws AspectException - Исключение для модуля аспекты
     */
    void initAspect(Object obj) throws AspectException {
        logger.info("init aspect");
        apectCollection = new AspectCollectionsImpl();
        apectCollection.initAspects(obj, externaldao);
        //aspectsByModule = apectCollection.getAspectsOnModule(nameModule);
        //visitorOnModule = apectCollection.getVisitorOnModule(nameModule);
        aspectsByModule = apectCollection.getAspectsAll();
        visitorOnModule = apectCollection.getVisitorAll();
    }
}
