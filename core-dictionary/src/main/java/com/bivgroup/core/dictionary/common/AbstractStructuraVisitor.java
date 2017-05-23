package com.bivgroup.core.dictionary.common;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by andreykus on 18.09.2016.
 * Шаблонный обработчик структуры
 *
 * @param <T> - сквозная коллекция
 */
public abstract class AbstractStructuraVisitor<T extends AbstractCollection> implements StructuraVisitor<T> {
    /**
     * следующий обработчик
     */
    private final StructuraVisitor next;
    /**
     * сквозное хранилище
     */
    public T source;

    /**
     * ининциировать посетителя
     *
     * @throws DictionaryException
     */
    void initInnerCollection() throws DictionaryException {
        //если нет глобального объекта, то создать (так мы можем опрокидывать независимой объект в посетителях)
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
            throw new DictionaryException(ex);
        }
    }

    /**
     * Конструктор Шаблонный обработчик структуры
     *
     * @param next - следующий обработчик
     * @throws DictionaryException -  исключение словарной системы
     */
    public AbstractStructuraVisitor(StructuraVisitor next) throws DictionaryException {
        this.next = next;
        initInnerCollection();
    }

    /**
     * обработать следующий посетитель
     *
     * @param element
     * @throws DictionaryException
     */
    protected void processNext(Object element) throws DictionaryException {
        if (next != null) {
            next.visit(element, source);
        }
    }

}
