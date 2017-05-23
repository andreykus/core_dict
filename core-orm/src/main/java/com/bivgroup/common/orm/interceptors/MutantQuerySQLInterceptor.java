package com.bivgroup.common.orm.interceptors;

import com.bivgroup.common.orm.interceptors.strategy.StrategyVariant;
import com.bivgroup.common.utils.observer.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Map;


/**
 * Created by bush on 19.08.2016.
 * Перехватчик обрабатывающий chunk, chain. Аналогичная реализация Cayyene
 * {@link AbstractInjectQueryInterceptor}
 */
public class MutantQuerySQLInterceptor extends AbstractInjectQueryInterceptor implements ORMInterceptor, Observer {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * сессия
     */
    private Session session;
    /**
     * параметры
     */
    private Map params;
    /**
     * запрос
     */
    private Query query;

    /**
     * получить сессию
     *
     * @param session - сессия
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * обработать
     *
     * @param sql - sql запрос
     * @return - sql запрос
     */
    @Override
    public String processSql(String sql) {
        ///JdbcCoordinator coord = ((SessionImplementor) session).getJdbcCoordinator();
        if (params != null) {
            //Set<Parameter<?>> paramsInSql = query.getParameters();
            sql = StrategyVariant.INJECT_MUTANT_SQL.process(sql, query, params);
        }
        params = null;
        return sql;
    }

    /**
     * TODO удалить
     *
     * @param args
     */
    public void update(Object... args) {
        if (args.length > 1) {
            this.params = ((Map) args[0]);
            this.query = ((Query) args[1]);
        }
    }
}
