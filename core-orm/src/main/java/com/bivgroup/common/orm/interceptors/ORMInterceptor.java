package com.bivgroup.common.orm.interceptors;

import org.hibernate.Interceptor;
import org.hibernate.Session;

/**
 * Created by bush on 19.08.2016.
 * Интефейс перехватчика с сессией Hibernet
 */
public interface ORMInterceptor extends Interceptor {

    /**
     * Установить сессию
     *
     * @param session - сессия
     */
    void setSession(Session session);
}
