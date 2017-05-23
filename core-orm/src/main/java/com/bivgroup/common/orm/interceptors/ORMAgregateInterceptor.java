package com.bivgroup.common.orm.interceptors;

import java.util.List;

/**
 * Created by bush on 19.08.2016.
 * Интефейс расширения перехватчика (список перехватчиков)
 */
public interface ORMAgregateInterceptor extends ORMInterceptor {

    /**
     * добавить перехватчик к агрегатному перехватчику
     * @param interceptor -  перехватчик
     */
    void addInterceptor(ORMInterceptor interceptor);

    /**
     * Получить перехватчик
     *
     * @return - перехватчик
     */
    List<ORMInterceptor> getInterceptors();
}
