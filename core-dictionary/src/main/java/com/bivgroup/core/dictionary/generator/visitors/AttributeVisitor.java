package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

/**
 * Created by andreykus on 18.09.2016.
 * Посетитель структуры для создания свойств сущности: колонка, ссылка, ссылка на множество
 */
public class AttributeVisitor extends AbstractAttributeVisitor {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Конструктор Посетитель структуры для создания свойств сущности: колонка, ссылка, ссылка на множество
     * @param next -  следующий обработчик
     * @throws DictionaryException -  исключение словарной системы
     */
    public AttributeVisitor(StructuraVisitor next) throws DictionaryException {
        super(next);
    }

    /**
     * Создать объект - свойство таблицы
     *
     * @param element - элемент структуры
     * @param sources - глобальный объект лоя передачи в цепочке
     * @return - хранилище метаданны
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public MetadataCollection visit(Object element, MetadataCollection sources) throws DictionaryException {
        this.source = sources;
        if (element instanceof SadEntity) {
            SadEntity ent = (SadEntity) element;
            List<SadAttribute> listAttr = ent.getAttributes();
            //первые создаются простые поля
            listAttr.sort(new Comparator<SadAttribute>() {
                @Override
                public int compare(SadAttribute o1, SadAttribute o2) {
                    if (o1.getCategory().ordinal() > o2.getCategory().ordinal()) {
                        return 1;
                    } else if (o1.getCategory().ordinal() < o2.getCategory().ordinal()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            for (SadAttribute attr : listAttr) {
                attr.getCategory().getVisitor().visit(attr, sources);
            }
        }
        processNext(element);
        return source;
    }
}
