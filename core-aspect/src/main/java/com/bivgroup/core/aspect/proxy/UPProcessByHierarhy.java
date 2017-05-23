package com.bivgroup.core.aspect.proxy;

import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ActionOnMap;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.tuple.DynamicMapInstantiator;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by bush on 12.01.2017.
 * Обработчик структуры - используется при обходе структуры объекта , доболняет вложенные объекты данными из аспектов обработки по типу объекта (сущности)
 */
public class UPProcessByHierarhy implements ActionOnMap {
    /** прокси над Дао*/
    AspectProxy proxy;
    /**метод*/
    Method method;
    /**аргументы метода*/
    Object[] args;
    /**логгер*/
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Конструктор Обработчик структуры - используется при обходе структуры объекта , доболняет вложенные объекты данными из аспектов обработки по типу объекта (сущности)
     * @param proxy - прокси над Дао
     * @param method - метод
     * @param args -аргументы метода
     */
    public UPProcessByHierarhy(AspectProxy proxy, Method method, Object[] args) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    /**
     * Название сущности
     *
     * @param in - объект
     * @return - название сущности
     * берется по полю $type$
     */
    private String getEntityName(Map<String, Object> in) {
        if (in.get(DynamicMapInstantiator.KEY) == null) return null;
        return in.get(DynamicMapInstantiator.KEY).toString();
    }

    /**
     * После посещения элемента структуры
     * работает только для чтения
     *
     * @param visitor - аспект обработки
     * @param method  - метод
     * @param args    - аргументы метода
     * @param rez     - результат метода
     * @throws DictionaryException
     */
    private void after(AspectVisitor visitor, Method method, Object[] args, Object rez) throws DictionaryException {
        AspectMethod annotation = method.getDeclaredAnnotation(AspectMethod.class);
        if (annotation != null && visitor != null) {
            logger.info("element object process");
            if (Action.READ.equals(annotation.action())) {
                try {
                    visitor.visitPost(annotation.action(), method, args, rez, null);
                } catch (AspectException ex) {
                    throw new DictionaryException(ex);
                }
            }
        }
    }

    /**
     * обработчик элемента структуры
     *
     * @param in - элемент структуры
     * @throws DictionaryException
     */
    @Override
    public void process(Map<String, Object> in) throws DictionaryException {
        if (in != null) {
            String entityName = getEntityName(in);
            if (entityName != null) {
                //аспект обработки на вложенной сущности
                AspectVisitor visitor = proxy.visitorOnModule.get(getEntityName(in));
                after(visitor, method, args, in);
            }
        }
    }
}
