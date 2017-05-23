package com.bivgroup.common.utils.observer;

/**
 * Created by andreykus on 25.08.2016.
 * подписчик
 */
public interface Observer {
    /**
     * действие вполнеяемое при уведомлении
     * @param arg -  аркументы от издаиеля
     */
    void update(Object... arg);
}
