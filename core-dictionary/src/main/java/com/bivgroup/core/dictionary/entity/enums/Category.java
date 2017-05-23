package com.bivgroup.core.dictionary.entity.enums;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.ArrayVisitor;
import com.bivgroup.core.dictionary.generator.visitors.FieldVisitor;
import com.bivgroup.core.dictionary.generator.visitors.LinkVisitor;
import com.bivgroup.core.dictionary.common.StructuraVisitor;

/**
 * Created by bush on 16.09.2016.
 * Категория экспорта аттрибута
 */
public enum Category {
    /**
     * неизвестно
     */
    UNKNOW {
        @Override
        public StructuraVisitor getVisitor() throws DictionaryException {
            return null;
        }
    },
    /**
     * поле
     */
    FIELD {
        @Override
        public StructuraVisitor getVisitor() throws DictionaryException {
            return new FieldVisitor(null);
        }
    },
    /**
     * ссылка - ManyToOne
     */
    REFERENCE {
        @Override
        public StructuraVisitor getVisitor() throws DictionaryException {
            return new LinkVisitor(null);
        }
    },
    /**
     * множество - OneToMany
     */
    ARRAY {
        @Override
        public StructuraVisitor getVisitor() throws DictionaryException {
            return new ArrayVisitor(null);
        }
    };

    /**
     * Фабрика посетителя структуры
     *
     * @return - обработчик структуры
     * @throws DictionaryException - исключение словарной сиситемы
     */
    public abstract StructuraVisitor getVisitor() throws DictionaryException;
}
