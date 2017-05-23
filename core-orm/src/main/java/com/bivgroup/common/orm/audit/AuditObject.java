package com.bivgroup.common.orm.audit;

/**
 * Created by andreykus on 10.09.2016.
 * интерфейс объекта аудита , данным интерфейсом маркируются сущности
 */
public interface AuditObject {
    /**
     * получить идентификатор
     * @return - идентификатор
     */
    Long getId();

    /**
     * получить описание
     * @return -  описание
     */
    String getDescription();
}
