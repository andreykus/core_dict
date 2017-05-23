package com.bivgroup.core.aspect.visitors;

import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.exceptions.AspectException;

import java.lang.reflect.Method;


/**
 * Created by bush on 07.12.2016.
 * Интерфейс аспекта обработки сущности
 */
public interface AspectVisitor<T> {
    /**
     * посетитель до вызова метода
     *
     * @param action  -  действие
     * @param method  -  метод
     * @param args    -  аргументы метода
     * @param sources -  сквозная коллекция
     * @return - данные
     * @throws AspectException - исключение аспекта
     */
    T visitPred(Action action, Method method, Object[] args, T sources) throws AspectException;

    /**
     * следующий обработчик
     *
     * @return -  обработчик
     */
    AspectVisitor getNext();

    /**
     * есть следующий обработчик
     *
     * @return - true ,если есть
     */
    boolean hasNext();

    /**
     * посетитель после вызова метода
     *
     * @param action  -  действие
     * @param method  -  метод
     * @param args    -  аргументы метода
     * @param sources -  сквозная коллекция
     * @param element - результат метода
     * @return - данные
     * @throws AspectException - исключение аспекта
     */
    T visitPost(Action action, Method method, Object[] args, Object element, T sources) throws AspectException;

    /**
     * описание
     *
     * @return - описение обработчика
     */
    String getDescription();
}
