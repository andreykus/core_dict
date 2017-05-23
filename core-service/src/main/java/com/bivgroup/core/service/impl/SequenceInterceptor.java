package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.interceptors.AbstractInjectQueryInterceptor;
import com.bivgroup.common.orm.interceptors.ORMInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by andreykus on 19.08.2016.
 */
public class SequenceInterceptor extends AbstractInjectQueryInterceptor implements ORMInterceptor {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String processSql(String sql) {
        sql.replace("$NAME_SEQUENCE", "1");
        return sql;
    }
}
