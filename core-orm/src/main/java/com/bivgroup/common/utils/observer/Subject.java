package com.bivgroup.common.utils.observer;

import com.bivgroup.common.orm.OrmException;

/**
 * Created by andreykus on 25.08.2016.
 * издатель
 */
public interface Subject {
    /**
     * регистрировать подписчика
     * @param Observer - подписчик
     */
    void register(Observer Observer) ;

    /**
     * снять регистрацию подписчика
     * @param Observer - подписчик
     */
    void unRegistrer(Observer Observer) ;

    /**
     * уведомит подписчиков
     * @param arg - аргументы передаваемых параметров подписчику
     * @throws OrmException - исключение Orm
     */
    void notifyObservers(Object... arg) throws OrmException;
}
