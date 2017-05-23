package com.bivgroup.core.dictionary.generator;

import com.bivgroup.common.orm.interfaces.ConsumerConfig;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;

/**
 * Created by bush on 12.12.2016.
 * Интерфейс Внешнего потребителя метаданных
 */
public interface ExternalConsumerConfig extends ConsumerConfig {
    /**
     * Получить тип экспорта
     *
     * @return - тип экспорта
     */
    TypeExport getTypeObject();
}
