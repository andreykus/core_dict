package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;

/**
 * Created by bush on 11.11.2016.
 * Интерфейс процессора экспорта
 */
public interface Exporter {
    /**
     * старт экспорта
     *
     * @throws DictionaryException - исключение словарной системы
     */
    void start() throws DictionaryException;

    /**
     * установить глобальные настройки экспорта
     *
     * @param hgs - глобальные настройки экспорта
     */
    void setGlobalSettings(HibernateMappingGlobalSettings hgs);

    /**
     * установить название модуля
     *
     * @param nameModule - модуль
     */
    void setModuleName(String nameModule);

    /**
     * получить название модуля
     *
     * @return - модуль
     */
    String getModuleName();
}
