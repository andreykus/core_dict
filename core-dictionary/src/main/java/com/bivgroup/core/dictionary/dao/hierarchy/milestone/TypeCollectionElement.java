package com.bivgroup.core.dictionary.dao.hierarchy.milestone;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;

/**
 * Created by bush on 01.11.2016.
 * TODO удалить
 */
public enum TypeCollectionElement {
    LIST {
        public AbstractPersistVisitor getVisitor(AbstractPersistVisitor visitor) throws DictionaryException {
            return new ListVisitor(visitor);
        }
    },
    MAP {
        public AbstractPersistVisitor getVisitor(AbstractPersistVisitor visitor) throws DictionaryException {
            return new MapVisitor(visitor);
        }
    },
    OBJECT{
            public AbstractPersistVisitor getVisitor(AbstractPersistVisitor visitor) throws DictionaryException {
                return new ObjectVisitor(visitor);
            }
        }
    ;

    public abstract AbstractPersistVisitor getVisitor(AbstractPersistVisitor visitor) throws DictionaryException;
}
