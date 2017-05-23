package com.bivgroup.core.dictionary.common;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;

/**
 * Created by andreykus on 18.09.2016.
 * Интерфейс посетителя структуры
 *
 * @param <T> - сковзная коллекция
 */
public interface StructuraVisitor<T> {
    /**
     * обработать элемент структуры
     *
     * @param element - элемент структуры
     * @param sources - глобальный объект лоя передачи в цепочке
     * @return - обработанный элемент структуры
     * @throws DictionaryException
     */
    T visit(Object element, T sources) throws DictionaryException;
}
