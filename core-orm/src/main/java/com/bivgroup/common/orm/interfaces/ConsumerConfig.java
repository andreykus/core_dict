package com.bivgroup.common.orm.interfaces;

import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.utils.observer.Subject;

import java.util.List;

/**
 * Created by andreykus on 02.11.2016.
 * интерфейс потребителя кофигурации
 */
public interface ConsumerConfig extends Subject {

    /**
     * обработать конфигурацию
     * @param configs - конфигурация
     * @throws OrmException
     */
    public void processConfig(List<ExportObject> configs) throws OrmException;
}
