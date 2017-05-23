package com.bivgroup.common.orm.interceptors.strategy;

/**
 * Created by andreykus on 25.08.2016.
 * Стратегии обработки sql
 */
public enum StrategyVariant {
    /**стратегия модификация sql c директивами 'chain','chunk'*/
    INJECT_MUTANT_SQL(new MutantSQLStrategy());
    /**
     * Обработчик Стратегии
     */
    private ProcessQueryStrategy strategy;

    /**
     * онструктор Стратегии обработки sql
     * @param strategy - Обработчик Стратегии
     */
    StrategyVariant(ProcessQueryStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * получить обработчик стратегии
     * @return - обработчик стратегии
     */
    public ProcessQueryStrategy getStrategy() {
        return strategy;
    }

    /**
     * обработать sql
     * @param sql - sql
     * @param args - параметры
     * @return -sql
     */
    public String process(String sql, Object... args) {
        sql = strategy.process(sql, args);
        return sql;
    }
}
