package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.core.dictionary.common.AbstractStructuraVisitor;
import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import com.bivgroup.core.dictionary.entity.SadEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.Table;


/**
 * Created by andreykus on 18.09.2016.
 * Посетитель структуры для создания сущности
 */
public class EntityVisitor extends AbstractStructuraVisitor<MetadataCollection> {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * генератор наименования элементов сущности
     */
    private NameGenerator ng;

    /**
     * Конструктор Посетителя структуры для создания сущности
     *
     * @param next
     * @throws DictionaryException
     */
    public EntityVisitor(StructuraVisitor next) throws DictionaryException {
        super(next);
        this.ng = new NameGenerator();
    }

    /**
     * Создать объект - таблица
     *
     * @param ent - сущность таблиы
     * @return - таблица
     */
    private Table createTable(SadEntity ent) {
        Table table = source.getTable(ng.getTableName(ent.getTablename()));
        if (table == null) {
            table = new Table(ng.getTableName(ent.getTablename()));
            table.setComment(ng.getComment(ent.getNote()));
            source.addTable(table);
        }
        if (ent.hasParent()) {
            createTable(ent.getParent());
        }
        return table;
    }

    /**
     * Вополняется при обходе таблиц
     *
     * @param element - элемент структуры
     * @param sources - глобальный объект лоя передачи в цепочке
     * @return - хранилище метаданных
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public MetadataCollection visit(Object element, MetadataCollection sources) throws DictionaryException {
        this.source = sources;
        if (element instanceof SadEntity) {
            SadEntity ent = (SadEntity) element;
            CreateClassStrategy.CLASS.createClass(ent, createTable(ent), sources);

        }
        processNext(element);
        return source;
    }
}
