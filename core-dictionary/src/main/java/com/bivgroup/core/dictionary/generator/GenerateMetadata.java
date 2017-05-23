package com.bivgroup.core.dictionary.generator;

import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.common.orm.OrmException;
import org.hibernate.Session;

/**
 * Created by andreykus on 19.09.2016.
 * Интерфейс гененратор метаданных
 */
public interface GenerateMetadata {

    /**
     * Сгененрировать метаданные по всем сущностям
     *
     * @throws DictionaryException - исключение словарной системы
     * @throws OrmException - исключение ORM
     */
    void generateAll() throws DictionaryException, OrmException;

    /**
     * Сгененрировать метаданные по сущностям модуля
     *
     * @param module - модуль
     * @throws DictionaryException - исключение словарной системы
     * @throws OrmException - исключение ORM
     */
    void generateByModule(SadModule module) throws DictionaryException, OrmException;

    /**
     * Сгененрировать метаданные по сущностям модуля с зависимостями
     *
     * @param module - модуль
     * @throws DictionaryException - исключение словарной системы
     * @throws OrmException - исключение ORM
     */
    void generateByModuleWithDependency(SadModule module) throws DictionaryException, OrmException;

    /**
     * Установить подписчика на метаданные
     *
     * @param consumer потребитель
     */
    void setConsumer(ExternalConsumerConfig consumer);

    /**
     * Установить тип экспорта
     *
     * @param type - тип экспорта
     */
    void setType(TypeExport type);

    /**
     * Получить сессию
     *
     * @return - сессия
     */
    Session getSession();
}
