package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;

import java.util.Map;

/**
 * Created by bush on 01.12.2016.
 * Процессор установки статуса обработки на элементах структуры
 */
public class SetRowStatus implements ActionOnMap {
    RowStatus rowStatus;

    public SetRowStatus(RowStatus rowStatus) {
        this.rowStatus = rowStatus;
    }

    /**
     * обработать элемент
     *
     * @param in - элемент
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @Override
    public void process(Map<String, Object> in) throws DictionaryException {
        if (in != null) {
            if (rowStatus == null) throw new DictionaryException("not set param ROWSTATUS");
            in.put(RowStatus.ROWSTATUS_PARAM_NAME, rowStatus.getId());
        }
    }

}
