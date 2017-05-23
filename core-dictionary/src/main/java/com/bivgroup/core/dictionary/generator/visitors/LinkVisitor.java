package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 11.10.2016.
 * Посетитель структуры для создания ссылки по аттрибуту
 */
public class LinkVisitor extends AbstractAttributeVisitor {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Конструктор Посетитель структуры для создания ссылки по аттрибуту
     * @param next - следующий обработчик
     * @throws DictionaryException - исключение словарной системы
     */
    public LinkVisitor(StructuraVisitor next) throws DictionaryException {
        super(next);
    }

    /**
     * Создать ссылку по аттрибуту
     *
     * @param attr - аттрибут
     * @throws DictionaryException - исключение словарной системы
     */
    private void createRefernce(SadAttribute attr) throws DictionaryException {
        createRefernceM2O(attr);
    }

    /**
     * Вополняется при обходе аттрибутов
     *
     * @param element - элемент структуры
     * @param sources - глобальный объект лоя передачи в цепочке
     * @return - хранилище метаданных
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public MetadataCollection visit(Object element, MetadataCollection sources) throws DictionaryException {
        this.source = sources;
        createRefernce((SadAttribute) element);
        return null;
    }
}
