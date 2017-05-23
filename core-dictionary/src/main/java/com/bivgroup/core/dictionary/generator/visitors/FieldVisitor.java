package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 11.10.2016.
 * Посетитель структуры для создания поля - колонки
 */
public class FieldVisitor extends AbstractAttributeVisitor {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Контруктор Посетитель структуры для создания поля - колонки
     * @param next - следующий обработчик
     * @throws DictionaryException - исключение словарной системы
     */
    public FieldVisitor(StructuraVisitor next) throws DictionaryException {
        super(next);
    }

    /**
     * Проверка первичного ключа
     *
     * @param attr - аттрибут
     * @return - true если аттрибут это первичный ключ
     */
    public boolean isPrimaryKey(SadAttribute attr) {
        if (attr.getIsprimarykey() != null && attr.getIsprimarykey() > 0) return true;
        return false;
    }

    /**
     * Создать колонку по аттрибуту
     *
     * @param attr - аттрибут
     * @throws DictionaryException - исключение словарной системы
     */
    private void createField(SadAttribute attr) throws DictionaryException {

        if (isPrimaryKey(attr)) {
            createOnePk(attr);
        } else {
            createSimpleColumn(attr);
        }
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
        createField((SadAttribute) element);
        return null;
    }

}
