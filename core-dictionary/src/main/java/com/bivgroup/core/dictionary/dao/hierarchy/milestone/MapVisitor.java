package com.bivgroup.core.dictionary.dao.hierarchy.milestone;

import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.dao.enums.Action;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bush on 01.11.2016.
 *  TODO удалить
 */
public class MapVisitor extends AbstractPersistVisitor {
    protected Logger logger = LogManager.getLogger(this.getClass());
    public MapVisitor(StructuraVisitor parent) throws DictionaryException {
        super(parent);
    }
    /**
     * Обработать элемент сущности - как Map
     *
     * @param action
     * @param parentEntity
     * @param entityName
     * @param transientInstance
     * @throws DictionaryException
     */
    @Override
    public void process(Action action, Map parentEntity, String entityName, Object transientInstance, boolean first) throws DictionaryException {
        Iterator<Map.Entry> it = ((Map)transientInstance).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry el = it.next();
            String entityAttr = el.getKey().toString();
            if (el.getValue() instanceof Map) {

                //action.run(dao, parentEntity, entityAttr, el.getValue());
                TypeCollectionElement.OBJECT.getVisitor(this).process(action, parentEntity, entityAttr, el.getValue(), false);

            } else if (el.getValue() instanceof Collection) {
//                if (RowStatus.PARENT_ROW_STATUS.equals(entityAttr)) {
//                    return;
//                }
                String listEntityName = processPcOTM(entityAttr, entityName);
                TypeCollectionElement.OBJECT.getVisitor(this).process(action, parentEntity, listEntityName, el.getValue(), true);
            }
        }
    }


    @Override
    public PersistCollection visit(Object element, PersistCollection sources) throws DictionaryException {
        return null;
    }
}
