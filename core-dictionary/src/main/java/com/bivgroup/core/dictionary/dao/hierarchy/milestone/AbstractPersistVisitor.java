package com.bivgroup.core.dictionary.dao.hierarchy.milestone;

import com.bivgroup.core.dictionary.common.AbstractStructuraVisitor;
import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.dao.GenericDAO;
import com.bivgroup.core.dictionary.dao.enums.Action;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.metamodel.internal.EntityTypeImpl;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Map;

/**
 * Created by bush on 01.11.2016.
 *  TODO удалить
 */
abstract class AbstractPersistVisitor extends AbstractStructuraVisitor<PersistCollection> {
    protected Logger logger = LogManager.getLogger(this.getClass());
    AbstractPersistVisitor parentEntity;
    AbstractPersistVisitor previusVisitor;
    GenericDAO dao;
    public AbstractPersistVisitor(StructuraVisitor parent) throws DictionaryException {
        super(parent);
        this.previusVisitor = (AbstractPersistVisitor)parent;
    }

    void checkByRecursion() {
    }

    void isInBD() {
    }

    void isReferExist() {
    }

    public abstract void process(Action action, Map parentEntity, String entityName, Object transientInstance, boolean first) throws DictionaryException;

    /**
     * Если ссылко это подкласс - получить родитель
     * @param et
     * @param listName
     * @return
     * @throws DictionaryException - исключение словарной сиситемы
     */
    private PluralAttribute getSuperDeclaredCollection(EntityTypeImpl et, String listName) throws DictionaryException {
        try {
            return et.getDeclaredCollection(listName);
        } catch (IllegalArgumentException ex) {
            return getSuperDeclaredCollection((EntityTypeImpl) et.getSupertype(), listName);
        }
    }


    /**
     * Получить название вложенной сужности для отношения One-To-Many
     *
     * @param listName
     * @param entityName - 
     * @return
     * @throws DictionaryException - исключение словарной сиситемы
     */
    protected String processPcOTM(String listName, String entityName) throws DictionaryException {
        if (entityName == null || listName == null) return null;
        EntityType et = dao.getSession().getSessionFactory().getMetamodel().entity(entityName);
        if (et == null) {
            throw new DictionaryException(String.format("this link list %1s for entity %2s not correct", listName, entityName));
        }
        PluralAttribute bindAttr = getSuperDeclaredCollection((EntityTypeImpl) et, listName);
        EntityTypeImpl type = (EntityTypeImpl) bindAttr.getElementType();
        return type.getName();
    }


//    Action action, Map parentEntity, String entityName, Collection transientInstance
//    Action action, Map parentEntity, String entityName, Map transientInstance
//    Action action, Map parentEntity, String entityName, Object transientInstance, boolean first


}
