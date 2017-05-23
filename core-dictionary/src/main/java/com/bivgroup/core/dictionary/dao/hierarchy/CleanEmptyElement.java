package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;

import java.util.*;

/**
 * Created by bush on 01.12.2016.
 * Процессор удаления пустых объектов структуры
 */
public class CleanEmptyElement implements ActionOnMap {
    /**
     * Очистить пустые элементы Мапы
     * @param in - элемент
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public void process(Map<String, Object> in) throws DictionaryException {
        if (in != null) {
            List<Object> remoteMap = new ArrayList<Object>();
            //пометим
            for (Map.Entry<String, Object> entry : in.entrySet()) {
                if (entry.getValue() == null) {
                    remoteMap.add(entry.getKey());
                }
            }
            //удалим
            for (Object rmKey : remoteMap) {
                in.remove(rmKey);
            }
        }
    }
}
