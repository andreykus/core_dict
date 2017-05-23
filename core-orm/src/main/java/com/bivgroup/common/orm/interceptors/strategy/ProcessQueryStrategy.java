package com.bivgroup.common.orm.interceptors.strategy;

/**
 * Created by andreykus on 25.08.2016.
 * Интерфейс стратегии обработки sql
 */
public interface ProcessQueryStrategy {
    /**
     * обрабоать sql
     *
     * @param sql - sql
     * @param args - параметры
     * @return - sql
     */
    String process(String sql, Object... args);
}
