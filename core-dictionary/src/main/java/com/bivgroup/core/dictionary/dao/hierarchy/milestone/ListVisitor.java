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
public class ListVisitor extends AbstractPersistVisitor {

    protected Logger logger = LogManager.getLogger(this.getClass());
    public ListVisitor(StructuraVisitor parent) throws DictionaryException {
        super(parent);
    }

    /**
     * Обработать элемент сущности - как коллекцию
     *
     * @param action
     * @param parentEntity
     * @param entityName
     * @param transientInstance
     * @throws DictionaryException
     */
    @Override
    public void process(Action action, Map parentEntity, String entityName, Object transientInstance , boolean first) throws DictionaryException {
        Iterator it = ((Collection)transientInstance).iterator();
        while (it.hasNext()) {
            TypeCollectionElement.MAP.getVisitor(this).process(action, parentEntity, entityName, it.next(), true);
        }
    }

    @Override
    public PersistCollection visit(Object element, PersistCollection sources) throws DictionaryException {
        return null;
    }
}
