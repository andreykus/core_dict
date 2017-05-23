package com.bivgroup.core.aspect.visitors.impl.version;

import com.bivgroup.core.aspect.bean.Aspect;
import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectClass;
import com.bivgroup.core.aspect.bean.Field;
import com.bivgroup.core.aspect.bean.VersionAspect;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AbstractAspectVisitor;
import com.bivgroup.core.aspect.visitors.AspectVisitor;

import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.metamodel.internal.MetamodelImpl;

import org.hibernate.tuple.DynamicMapInstantiator;

import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by bush on 21.12.2016.
 * Обработчик сущности - расширение , аспект версионности
 */
@AspectClass(name = VersionVisitor.VERSION_ASPECT_NAME, delegator = VersionDelegator.class, callOrder = 1000000)
public class VersionVisitor extends AbstractAspectVisitor<Object> implements VersionDelegator {
    /**название аспекта*/
    public static final String VERSION_ASPECT_NAME = "VersionAspect";
    /**колонка ноды в БД*/
    public static final String NODEVERSION_ASPECT_NAME = "NodeVersion";
    /**параметр результата в методе*/
    private static final String RESULT = "Result";
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /** генератор нименования элементов сущности */
    private NameGenerator nameGenerator;

    /**
     * Конструктор Обработчик сущности - расширение , аспект версионности
     * @param next - следующий обработчик
     * @param externaldao - дао
     * @param aspect -  аспект
     * @throws AspectException -  исключение Аспекта
     */
    public VersionVisitor(AspectVisitor next, ExtendGenericDAO externaldao, Aspect aspect) throws AspectException {
        super(next, externaldao, aspect);
        this.nameGenerator = new NameGenerator();
    }

    /**
     * До исполнения метода
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументвы метода
     * @param sources - сквозной результат
     * @return - сквозной результат
     * @throws AspectException - Исключение для модуля аспекты
     */
    @Override
    public Object visitPred(Action action, Method method, Object[] args, Object sources) throws AspectException {
        this.source = sources;
        innerBeforeCommand(action, method, args, sources);
        processNextPred(action, method, args);
        return this.source;
    }


    /**
     * После исполнения метода
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументвы метода
     * @param element - результат метода
     * @param sources - сквозной результат
     * @return - сквозной результат
     * @throws AspectException - Исключение для модуля аспекты
     */
    @Override
    public Object visitPost(Action action, Method method, Object[] args, Object element, Object sources) throws AspectException {
        this.source = sources;
        innerAfterCommand(action, method, args, element, sources);
        processNextPost(action, method, args, element);
        return this.source;
    }

    /**
     * действие чтение -  после выполнения
     * дополняем текущий результат версионными данными, с последней версии
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументвы метода
     * @param element - результат метода
     * @param sources - сквозной результат
     * @throws DictionaryException - Исключение для модуля словарная система
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void readAfterCommand(Action action, Method method, Object[] args, Object element, Object sources) throws DictionaryException, AspectException {
        if (element instanceof Map) {
            Map rez = (Map) element;
            Long idLastVersion = (Long) rez.get(((VersionAspect) aspect).getLastVersionIdFieldName());
            if (idLastVersion == null)
                throw new AspectException(String.format("Data not contains refer on version, field %1s is empty", ((VersionAspect) aspect).getLastVersionIdFieldName()));
            Map lastVersion = externaldao.findById(((VersionAspect) aspect).getVersionEntityName(), idLastVersion);
            if (lastVersion == null)
                throw new AspectException(String.format("Last Version entity %1s id %2s not found ", ((VersionAspect) aspect).getVersionEntityName(), idLastVersion));
            lastVersion.remove(DynamicMapInstantiator.KEY);
            rez.putAll(lastVersion);
        }
    }

    /**
     * Получение списка версионных свойств сущности , из аспекта
     *
     * @return - список полей
     */
    private List<String> getFields() {
        List<String> outFields = new ArrayList<String>();
        List<Field> fields = aspect.getField();
        if (fields != null) {
            for (Field f : fields) {
                outFields.add(f.getSysname());
            }
        }
        return outFields;
        // return Arrays.asList("Name", "Name2", "Surname", "Surname2", "Patronymic", "Patronymic2", "INN", "Sex", "PlaceOfBirth", "DateOfBirth", "IsMarried", "IsEmptyPatronymic");
    }

    /**
     * Извлечь из корневых параметров , объект версионности
     *
     * @param params - параметроы
     * @return - версионный объект
     */
    private Map extractVersionObject(Map<String, Object> params) {
        Map versionObject = new HashMap<>();
        List<String> versionFields = getFields();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (versionFields.contains(param.getKey())) versionObject.put(param.getKey(), param.getValue());
        }
        return versionObject;
    }

    /**
     * Добавим запись в версионную таблицу
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументвы метода
     * @param element - результат метода
     * @param sources - сквозной результат
     * @throws DictionaryException - Исключение для модуля словарная система
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void updateAfterCommand(Action action, Method method, Object[] args, Object element, Object sources) throws DictionaryException, AspectException {
        Map params = getInParam(method, args);
        if (params.get(((VersionAspect) aspect).getVersionNumberParamName()) == null) {
            logger.warn(String.format("Optimistic locking error. Not send param %1s", ((VersionAspect) aspect).getVersionNumberParamName()));
            return;
        }

        String entityName = getEntityName(method, args);
        if (element == null) element = new HashMap<String, Object>();
        if (element instanceof Map) {
            Map versionObject = extractVersionObject(params);
            if (versionObject.isEmpty()) {
                logger.warn(String.format("Object type %1s not contains main version field", ((VersionAspect) aspect).getVersionEntityName()));
                return;
            }
            if (((VersionAspect) aspect).getLastVersionTimeFieldName() != null)
                versionObject.put(((VersionAspect) aspect).getLastVersionTimeFieldName(), Calendar.getInstance().getTime());
            versionObject.put(((VersionAspect) aspect).getVersionEntityNumberFieldName(), params.get(((VersionAspect) aspect).getLockVersionFieldName()));

            params.put(nameGenerator.getOneToManyPropertyName(((VersionAspect) aspect).getLastVersionIdFieldName()), versionObject);
            params.put(DynamicMapInstantiator.KEY, ((VersionAspect) aspect).getVersionEntityName());
            params.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
            element = externaldao.crudByHierarchy(entityName, params, true);
        }
    }

    /**
     * Действие создать - до выполнения
     * выставим начальное значение версии
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументы метода
     * @param sources - сквозной результат
     */
    private void createBeforeCommand(Action action, Method method, Object[] args, Object sources) {
        // отключим исполнение основного метода
        this.source = Boolean.FALSE;
        Map params = getInParam(method, args);
        params.put(((VersionAspect) aspect).getLockVersionFieldName(), 0L);
    }

    /**
     * Действие создать - после выполнения
     *
     * @param action  - действие
     * @param method  - метод
     * @param args    - аргументвы метода
     * @param element - результат метода
     * @param sources - сквозной результат
     * @throws DictionaryException - Исключение для словарная сиситема
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void createAfterCommand(Action action, Method method, Object[] args, Object element, Object sources) throws DictionaryException, AspectException {
        // отключим исполнение основного метода
        this.source = Boolean.FALSE;
        Map params = getInParam(method, args);
        String entityName = getEntityName(method, args);
        if (element == null) element = new HashMap<String, Object>();
        if (element instanceof Map) {
            Map versionObject = extractVersionObject(params);
            if (versionObject.isEmpty()) {
                throw new AspectException(String.format("Object type %1s not contains main version field", ((VersionAspect) aspect).getVersionEntityName()));
            }
            if (((VersionAspect) aspect).getLastVersionTimeFieldName() != null)
                versionObject.put(((VersionAspect) aspect).getLastVersionTimeFieldName(), Calendar.getInstance().getTime());
            versionObject.put(((VersionAspect) aspect).getVersionEntityNumberFieldName(), params.get(((VersionAspect) aspect).getLockVersionFieldName()));

            params.put(nameGenerator.getOneToManyPropertyName(((VersionAspect) aspect).getLastVersionIdFieldName()), versionObject);
            params.put(DynamicMapInstantiator.KEY, ((VersionAspect) aspect).getVersionEntityName());
            params.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
            element = externaldao.crudByHierarchy(entityName, params, true);
        }
    }

    /**
     * Получение поля первичного ключа на сущности
     *
     * @param entityName - название сущности
     * @return -  название первичного ключа
     */
    private String getPkOnEntity(String entityName) {
        EntityType type = externaldao.getSession().getSessionFactory().getMetamodel().entity(entityName);
        String idName = ((MetamodelImpl) externaldao.getSession().getSessionFactory().getMetamodel()).entityPersister(entityName).getIdentifierPropertyName();
        return idName;
    }

    /**
     * Обновим основную таблицу
     *
     * @param action  - действие
     * @param method - метод
     * @param args - аргументвы метода
     * @param sources - сквозной результат
     * @throws DictionaryException - Исключение для словарная система
     * @throws AspectException - Исключение для модуля аспекты
     */

    private void updateBeforeCommand(Action action, Method method, Object[] args, Object sources) throws DictionaryException, AspectException {
        // отключим исполнение основного метода
        this.source = Boolean.FALSE;
        CriteriaBuilder cb = externaldao.getSession().getCriteriaBuilder();
        Map params = getInParam(method, args);
        String entityName = getEntityName(method, args);
        Map versionObject = extractVersionObject(params);
        if (versionObject.isEmpty()) {
            logger.warn(String.format("Object type %1s not contains main version field", ((VersionAspect) aspect).getVersionEntityName()));
            return;
        }
        //свой запрос на обновление основной таблицы (node)
        String hqlUpdate = String.format("update %1s node set node.%2s = :newVersion where node.%3s = :oldVersion and node.%4s = :idObject", entityName, ((VersionAspect) aspect).getLockVersionFieldName(), ((VersionAspect) aspect).getLockVersionFieldName(), getPkOnEntity(entityName));
        if (params.get(((VersionAspect) aspect).getVersionNumberParamName()) == null) {
            logger.warn(String.format("Optimistic locking error. Not send param %1s", ((VersionAspect) aspect).getVersionNumberParamName()));
            return;
        }
        Long actualVersion = (Long) params.get(((VersionAspect) aspect).getVersionNumberParamName()) + 1;
        int updatedEntities = externaldao.getSession().createQuery(hqlUpdate)
                .setLong("newVersion", actualVersion)
                .setLong("oldVersion", (Long) params.get(((VersionAspect) aspect).getVersionNumberParamName()))
                .setLong("idObject", (Long) params.get(getPkOnEntity(entityName)))
                .executeUpdate();

        if (updatedEntities == 0) throw new AspectException("Optimistic locking error. Try to save new version again");
        params.put(((VersionAspect) aspect).getLockVersionFieldName(), actualVersion);

    }

    /**
     * Обработка до выполнения метода
     *
     * @param action  - действие
     * @param method - метод
     * @param args - аргументвы метода
     * @param sources - сквозной результат
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void innerBeforeCommand(Action action, Method method, Object[] args, Object sources) throws AspectException {
        try {
            switch (action) {
                case CREATE:
                    createBeforeCommand(action, method, args, sources);
                    break;
                case UPDATE:
                    updateBeforeCommand(action, method, args, sources);
                    break;
                default:
                    break;
            }
        } catch (DictionaryException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Обработка после выполнения метода
     *
     * @param action  - действие
     * @param method - метод
     * @param args - аргументвы метода
     * @param element - результат метода
     * @param sources - сквозной результат
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void innerAfterCommand(Action action, Method method, Object[] args, Object element, Object sources) throws AspectException {
        try {
            switch (action) {
                case CREATE:
                    createAfterCommand(action, method, args, element, sources);
                    break;
                case READ:
                    readAfterCommand(action, method, args, element, sources);
                    break;
                case UPDATE:
                    updateAfterCommand(action, method, args, element, sources);
                    break;
                default:
                    break;
            }
        } catch (DictionaryException ex) {
            throw new AspectException(ex);
        }
    }

}
