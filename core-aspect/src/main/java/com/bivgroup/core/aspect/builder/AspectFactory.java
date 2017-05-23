package com.bivgroup.core.aspect.builder;

import com.bivgroup.core.aspect.bean.Aspect;
import com.bivgroup.core.aspect.bean.AuthAspect;
import com.bivgroup.core.aspect.bean.BinaryFile;
import com.bivgroup.core.aspect.bean.VersionAspect;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.aspect.visitors.impl.auth.AuthVisitor;
import com.bivgroup.core.aspect.visitors.impl.binaryfiles.BinaryFileVisitor;
import com.bivgroup.core.aspect.visitors.impl.version.VersionVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 07.12.2016.
 * Фабрика создания аспектов обработки сущности
 */
public enum AspectFactory {
    /**
     * бинарный файл
     */
    BINARY_FILES(BinaryFileVisitor.BINARYFILE_ASPECT_NAME, BinaryFileVisitor.class, BinaryFile.class) {
        @Override
        public AspectVisitor getVisitor(AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException {
            return new BinaryFileVisitor(visitor, externaldao, param);
        }
    },
    /**
     * авторизация
     */
    AUTH(AuthVisitor.AUTH_ASPECT_NAME, AuthVisitor.class, AuthAspect.class) {
        @Override
        public AspectVisitor getVisitor(AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException {
            return new AuthVisitor(visitor, externaldao, param);
        }
    },
    /**
     * версионность
     */
    VERSION_ASPECT(VersionVisitor.VERSION_ASPECT_NAME, VersionVisitor.class, VersionAspect.class) {
        @Override
        public AspectVisitor getVisitor(AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException {
            return new VersionVisitor(visitor, externaldao, param);
        }
    };
    /** класс обработчик аспекта */
    private Class visitor;
    /** класс аспекта */
    private Class aspect;
    /** название аспекта */
    private String name;
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Конструктор фабрики аспектов
     *
     * @param name - название аспекта
     * @param visitor -  класс обработчик аспекта
     * @param aspect - класс аспекта
     */
    AspectFactory(String name, Class visitor, Class aspect) {
        this.visitor = visitor;
        this.aspect = aspect;
        this.name = name;
    }

    /**
     * Наименование аспекта
     *
     * @return - название аспекта
     */
    public String getName() {
        return this.name;
    }

    /**
     * Получить класс аспекта обработки
     *
     * @return - класс обработчика
     * @throws AspectException
     */
    public Class getVisitorClass() throws AspectException {
        return this.visitor;
    }

    public abstract AspectVisitor getVisitor(AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException;

    /**
     * Получить аспект обработки по имени
     *
     * @param name - наименование аспекта
     * @return - обработчик
     * @throws AspectException - Исключение для модуля аспекты
     */
    public static AspectFactory getByName(String name) throws AspectException {
        for (AspectFactory fact : AspectFactory.values()) {
            if (fact.name.equals(name)) {
                return fact;
            }
        }
        throw new AspectException(String.format("this aspect %1s not found", name));
    }

    /**
     * Получить аспект обработки по классу
     *
     * @param aspect - класс аспекта
     * @return - обработчик
     * @throws AspectException - Исключение для модуля аспекты
     */
    public static AspectFactory getByAspect(Class aspect) throws AspectException {
        for (AspectFactory fact : AspectFactory.values()) {
            if (fact.aspect.getName().equals(aspect.getName())) {
                return fact;
            }
        }
        throw new AspectException(String.format("this aspect %1s not found", aspect.getName()));
    }

    /**
     * Посторить обработчик по параметрам
     *
     * @param visitor     - обработчик (вложенный)
     * @param externaldao - дао
     * @param param       - аспект
     * @return - обработчик
     * @throws AspectException - Исключение для модуля аспекты
     */
    public AspectVisitor build(AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException {
        return this.getVisitor(visitor, externaldao, param);
    }

    /**
     * Посторить обработчик по параметрам
     *
     * @param visitorName - наименования создаваемого обработчика
     * @param visitor     - обработчик (вложенный)
     * @param externaldao - дао
     * @param param       - аспект
     * @return - обработчик
     * @throws AspectException - Исключение для модуля аспекты
     */
    public AspectVisitor build(String visitorName, AspectVisitor visitor, ExtendGenericDAO externaldao, Aspect param) throws AspectException {
        return getByName(visitorName).getVisitor(visitor, externaldao, param);
    }

}
