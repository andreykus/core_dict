package com.bivgroup.core.dictionary.dao.enums;

import com.bivgroup.core.dictionary.dao.GenericDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.metamodel.internal.EntityTypeImpl;
import org.hibernate.tuple.DynamicMapInstantiator;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 11.10.2016.
 * CRUD опреации над сущностью
 */
public enum RowStatus {
    /**
     * не изменяем
     */
    UNMODIFIED(0) {
        @Override
        public void processEntity(GenericDAO dao, Map element) throws DictionaryException {
            if (isNotEntity(element)) return;
            //TODO Возможно нужно ее переписывать
            String idName = getIdField(dao, element).getName();
            if (element.get(idName) == null) {
                logger.warn(String.format("value %1s not found in object %2s", idName, element.get(DynamicMapInstantiator.KEY).toString()));
                return;
            }
            Map rez = (Map) dao.findById(element.get(DynamicMapInstantiator.KEY).toString(), (Long) element.get(idName));
            if (rez == null || rez.get(getIdField(dao, element).getName()) == null) {
                logger.warn(String.format("entity %1s not found in database", element.get(DynamicMapInstantiator.KEY)));
            }
        }

        @Override
        public List<RowStatus> getAllowStatus() {
            return Arrays.asList(values());
        }
    },
    /**
     * вставка
     */
    INSERTED(1) {
        @Override
        public void processEntity(GenericDAO dao, Map element) throws DictionaryException {
            if (isNotEntity(element)) return;
            String idName = getIdField(dao, element).getName();
            element.remove(idName);
            dao.onlySave((String) element.get(DynamicMapInstantiator.KEY), element);
        }

        @Override
        public List<RowStatus> getAllowStatus() {
            return Arrays.asList(values());
        }
    },
    /**
     * модификация
     */
    MODIFIED(2) {
        @Override
        public void processEntity(GenericDAO dao, Map element) throws DictionaryException {
            if (isNotEntity(element)) return;
            dao.merge((String) element.get(DynamicMapInstantiator.KEY), element);
        }

        @Override
        public List<RowStatus> getAllowStatus() {
            return Arrays.asList(values());
        }
    },
    /**
     * удаление
     */
    DELETED(3) {
        @Override
        public void processEntity(GenericDAO dao, Map element) throws DictionaryException {
            if (isNotEntity(element)) return;
            //dao.getSession().persist((String) element.get(DynamicMapInstantiator.KEY), element);
            dao.delete((String) element.get(DynamicMapInstantiator.KEY), element);
            element.clear();
        }

        @Override
        public List<RowStatus> getAllowStatus() {
            return Arrays.asList(MODIFIED, UNMODIFIED, DELETED);
        }
    };
    /**
     *идентификатор действия
     */
    private int id = 0;
    /**
     * имя параметра в мапе
     */
    public static final String ROWSTATUS_PARAM_NAME = "ROWSTATUS";
    /**
     * значение по умолчанию
     */
    public static final RowStatus DEFAULT_ROW_STAUS = INSERTED;

    /**
     * Конструктор CRUD опреации над сущностью
     *
     * @param newId - идентификатор действия
     */
    RowStatus(int newId) {
        this.id = newId;
    }

    /**
     * получить идентификатор статуса
     *
     * @return - идентоификатор
     */
    public int getId() {
        return this.id;
    }

    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * действие над сущностью
     *
     * @param value - сущность
     * @return - действие
     */
    public static RowStatus getRowStatusByMap(Map value) {
        return getRowStatusById((Integer) value.get(ROWSTATUS_PARAM_NAME));
    }

    /**
     * Проверка элемента как сущности
     *
     * @param element - сущность
     * @return - true если нет такой сущности
     */
    private static boolean isNotEntity(Map element) {
        return element.get(DynamicMapInstantiator.KEY) == null;
    }

    /**
     * Название поля идентификатора для сущности
     *
     * @param dao     - дао
     * @param element - сущность
     * @return - аттрибут
     */
    private static Attribute getIdField(GenericDAO dao, Map element) {
        EntityType et = dao.getSession().getSessionFactory().getMetamodel().entity((String) element.get(DynamicMapInstantiator.KEY));
        return ((EntityTypeImpl) et).getId(Long.class);
    }

    /**
     * Статус по значению
     *
     * @param value - значение, для БД
     * @return - статус
     */
    public static RowStatus getRowStatusById(Integer value) {
        RowStatus result = null;
        if (value == null) return DEFAULT_ROW_STAUS;
        switch (value) {
            case 0: {
                result = UNMODIFIED;
                break;
            }
            case 1: {
                result = INSERTED;
                break;
            }
            case 2: {
                result = MODIFIED;
                break;
            }
            case 3: {
                result = DELETED;
                break;
            }
            default:
                result = DEFAULT_ROW_STAUS;
        }
        return result;
    }

    /**
     * Проведение операции CRUD
     *
     * @param dao     - дао
     * @param element - сущность
     * @throws DictionaryException
     */
    public abstract void processEntity(GenericDAO dao, Map element) throws DictionaryException;

    /**
     * Доступные действия
     *
     * @return - список доступных действий
     */
    public abstract List<RowStatus> getAllowStatus();

}
