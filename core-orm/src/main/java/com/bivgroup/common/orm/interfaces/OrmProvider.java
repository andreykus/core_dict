package com.bivgroup.common.orm.interfaces;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interceptors.ORMAgregateInterceptor;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bush on 16.08.2016.
 * Интерфейс провайдера работы с БД, реализует интерфейс Provider
 *
 */
public interface OrmProvider {
    /**
     * сессия JPA
     * @return - EntityManager
     */
    EntityManager getEntityManager();

    /**
     * получить сессию
     * @return - сессия
     */
    Session getSession();

    /**
     * Источник данных
     * @return - Источник данных
     */
    DataSource getDataSource();

    /**
     * Возвращает данные запроса представлениные в виде List of Map
     *
     * @param nameQuery - имя именнованог запроса
     * @param <T>
     * @return - ответ из БД
     * @throws OrmException
     */
    <T> List<Map<String, T>> getCommonListRezult(String nameQuery) throws OrmException;

    /**
     * Возвращает данные запроса представлениные в виде List of Map
     *
     * @param nameQuery - имя именнованог запроса
     * @param realParam - входные параметры
     * @param <T>
     * @return - List of Map of String, T ответ из БД
     * @throws OrmException
     */
    <T> List<Map<String, T>> getCommonListRezult(String nameQuery, Map<String, ?> realParam) throws OrmException;

    /**
     * Возвращает данные запроса представлениные в виде List of Map с педжинацией
     *
     * @param nameQueryAll - имя именнованог запроса (запрос количества записей = nameQueryAll + 'Count')
     * @param realParam    - входные параметры
     * @param <T>
     * @return -  Map of String, T  ответ из БД
     * @throws OrmException
     */
    <T> Map<String, T> getCommonListPageRezult(String nameQueryAll, Map<String, ?> realParam) throws OrmException;

    /**
     * Возвращает данные запроса представлениные в виде List of Map с педжинацией
     *
     * @param nameQueryAll   - имя именнованог запроса
     * @param nameQueryCount - имя именнованог запроса с подсчетом количества записей
     * @param realParam      - входные параметры
     * @param <T>
     * @return -  Map of String, T ответ из БД
     * @throws OrmException
     */
    <T> Map<String, T> getCommonListPageRezult(String nameQueryAll, String nameQueryCount, Map<String, ?> realParam) throws OrmException;

    /**
     * Возвращает данные запроса row представлениные в виде Map
     *
     * @param nameQuery - имя именнованог запроса
     * @param realParam - входные параметры
     * @param <T>
     * @return - T Map(String, T) ответ из БД
     * @throws OrmException
     */
    <T> Map<String, T> getCommonRecordRezult(String nameQuery, Map<String, ?> realParam) throws OrmException;

    /**
     * Возвращает данные запроса представлениные в виде дискретног значения
     *
     * @param nameQuery - имя именнованог запроса
     * @param realParam - входные параметры
     * @param <T>
     * @return - ответ из БД
     * @throws OrmException
     */
    <T> T getCommonRezult(String nameQuery, Map<String, ?> realParam) throws OrmException;

    /**
     * Исполнение CUD запроса (CREATE,UPDATE,DELETE)
     *
     * @param nameQuery - имя именнованог запроса
     * @param realParam - входные параметры
     * @param <T>
     * @throws OrmException
     */
    <T> void performNonSelectingQuery(String nameQuery, Map<String, ?> realParam) throws OrmException;

    /**
     * Вернуть перехватчика
     *
     * @return
     */
    ORMAgregateInterceptor getInterceptor();

    /**
     * Возвращает список идентификатор
     *
     * @param nameQuery - имя именнованог запроса
     * @param realParam - входные параметры
     * @param fieldName - наименование поля
     * @return - Set of Long ответ из БД
     * @throws OrmException
     */
    Set<Long> getCommonListIds(String nameQuery, Map<String, ?> realParam, String fieldName) throws OrmException;
}
