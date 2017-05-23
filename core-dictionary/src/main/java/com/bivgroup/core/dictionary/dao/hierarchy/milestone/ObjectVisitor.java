package com.bivgroup.core.dictionary.dao.hierarchy.milestone;

import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.dao.enums.Action;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.hibernate.tuple.DynamicMapInstantiator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bush on 01.11.2016.
 *  TODO удалить
 */
public class ObjectVisitor extends AbstractPersistVisitor {
    public ObjectVisitor(StructuraVisitor parent) throws DictionaryException {
        super(parent);
    }
    /**
     * Обработать элемент сущности
     *
     * @param action
     * @param parentEntity
     * @param entityName
     * @param transientInstance
     * @param first
     * @throws DictionaryException
     */
    @Override
    public void process(Action action, Map parentEntity, String entityName, Object transientInstance, boolean first) throws DictionaryException {
        if (transientInstance instanceof Map) {
            if (entityName == null && source.listGraf.isEmpty()) source.listGraf.add(new HashMap((Map) transientInstance));
            if (entityName != null) ((Map) transientInstance).put(DynamicMapInstantiator.KEY, entityName);
            if (first) {
                //action.run(dao, parentEntity, entityName, transientInstance);
            }
            TypeCollectionElement.MAP.getVisitor(this).process(action, (Map) transientInstance, entityName, (Map) transientInstance, false);
        } else if (transientInstance instanceof Collection) {
            TypeCollectionElement.LIST.getVisitor(this).process(action, parentEntity, entityName, (Collection) transientInstance,false);
        }
    }

    @Override
    public PersistCollection visit(Object element, PersistCollection sources) throws DictionaryException {
        return null;
    }
}
