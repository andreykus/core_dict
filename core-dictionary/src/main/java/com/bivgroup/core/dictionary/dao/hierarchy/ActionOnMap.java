package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;

import java.util.Map;

/**
 * Created by bush on 01.12.2016.
 * Интерфейс процессора обработки иерархической структуры
 */
public interface ActionOnMap {
    /**
     * обработать элемент
     *
     * @param in - элемент
     * @throws DictionaryException - исключение словарной системы
     */
    void process(Map<String, Object> in) throws DictionaryException;
}
