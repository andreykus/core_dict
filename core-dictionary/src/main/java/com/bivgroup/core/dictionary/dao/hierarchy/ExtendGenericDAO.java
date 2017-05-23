package com.bivgroup.core.dictionary.dao.hierarchy;

import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.dictionary.dao.GenericDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.aspect.annotations.Action;
import org.hibernate.Criteria;

import java.util.*;

/**
 * Created by bush on 16.12.2016.
 * Интерфейс - расширение для работы с иерархической структурой
 */
public interface ExtendGenericDAO extends GenericDAO<Map> {
    /**
     * Установить прокси
     *
     * @param proxy
     */
    void setProxy(Object proxy);

    /**
     * Получить прокси
     *
     * @return
     */
    Object getProxy();

    /**
     * Обработать иерархию
     *
     * @param transientInstance - иерархическая структура
     * @return - иерархическая структура
     * @throws DictionaryException - исключение словарной сиситемы
     */
    Map crudByHierarchy(Map transientInstance) throws DictionaryException;

    /**
     * Обработать иерархию
     *
     * @param entityName - наименование сущности
     * @param transientInstance - иерархическая структура
     * @return - иерархическая структура
     * @throws DictionaryException - исключение словарной сиситемы
     */
    Map crudByHierarchy(@AspectMethodParam(nameParam = "entityName") String entityName, Map transientInstance) throws DictionaryException;

    /**
     * Обработать иерархию
     *
     * @param entityName - наименование сущности
     * @param transientInstance - иерархическая структура
     * @return - иерархическая структура
     * @throws DictionaryException - исключение словарной сиситемы
     */
    List crudByHierarchy(@AspectMethodParam(nameParam = "entityName") String entityName, List<Map> transientInstance) throws DictionaryException;

    /**
     * Обработать иерархию
     *
     * @param entityName - наименование сущности
     * @param transientInstance - иерархическая структура
     * @param isUseProxy - использовать прокси (для повторной обработки аспектов)
     * @return - иерархическая структура
     * @throws DictionaryException - исключение словарной сиситемы
     */
    Map crudByHierarchy(String entityName, Map transientInstance, boolean isUseProxy) throws DictionaryException;

    /**
     * Удалить
     *
     * @param entityName - наименование сущности
     * @param persistentInstance - сущность
     * @throws DictionaryException - исключение словарной сиситемы
     */
    @AspectMethod(action = Action.DELETE)
    @Override
    void delete(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") Map persistentInstance) throws DictionaryException;

    /**
     * Поиск по критерю
     *
     * @param entityName - наименование сущности
     * @param criteria - критерий
     * @return - список сущностей
     */
    @AspectMethod(action = Action.READ)
    List<Map> findByCriteria(@AspectMethodParam(nameParam = "entityName") String entityName , Criteria criteria);
}
