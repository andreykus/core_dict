package com.bivgroup.common.orm.interceptors;

import org.hibernate.EmptyInterceptor;

/**
 * Created by bush on 19.08.2016.
 * шаблон перехватчика для обработки sql запроса
 */
public abstract class AbstractInjectQueryInterceptor extends EmptyInterceptor {

    /**
     * обработать sql запрос
     * @param sql - sql запрос
     * @return - sql запрос
     */
    public abstract String processSql(String sql);

    /**
     * при формировании PrepareStatement обработать запрос
     * @param sql  - sql запрос
     * @return  - sql запрос
     */
    @Override
    public String onPrepareStatement(String sql) {
        return processSql(sql);
    }

}
