package com.bivgroup.core.aspect.builder;

import com.bivgroup.core.aspect.annotations.AspectClass;
import com.bivgroup.core.aspect.bean.Aspect;
import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Metamodel;

import javax.persistence.metamodel.EntityType;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by bush on 07.12.2016.
 * Создатель цепочки обработчиков
 */
public class ChainBuilder {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * коллекция модуль-сущность:список аспектов
     */
    private Map<String, Map<String, List<AspectCfg>>> aspects;

    /**
     * Конструктор Создатель цепочки обработчиков
     * @param aspects -  аспекты
     */
    public ChainBuilder(Map<String, Map<String, List<AspectCfg>>> aspects) {
        this.aspects = aspects;
    }

    /**
     * Аспекты в цепочке должны быть отсортированы по порядку указанному на классе аспекта
     *
     * @param aspectsFactory - список содателей аспектов
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void sortChainByFactory(List<AspectFactory> aspectsFactory) throws AspectException {
        aspectsFactory.sort(new Comparator<AspectFactory>() {
            @Override
            public int compare(AspectFactory o1, AspectFactory o2) {
                AspectClass t_class_o1 = o1.getClass().getDeclaredAnnotation(AspectClass.class);
                if (t_class_o1 == null) {
                    return -1;
                }
                AspectClass t_class_o2 = o2.getClass().getDeclaredAnnotation(AspectClass.class);
                if (t_class_o2 == null) {
                    return -1;
                }
                if (t_class_o1.callOrder() > t_class_o2.callOrder()) {
                    return 1;
                } else if (t_class_o1.callOrder() < t_class_o2.callOrder()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * Аспекты должны быть отсортированы по порядку указанному на аспекте
     *
     * @param aspectCfg - список аспектов
     * @throws AspectException - Исключение для модуля аспекты
     */
    private void sortChainByAspect(List<AspectCfg> aspectCfg) throws AspectException {
        aspectCfg.sort(new Comparator<AspectCfg>() {
            @Override
            public int compare(AspectCfg o1, AspectCfg o2) {
                BigInteger order_o1 = o1.getCallOrder();
                if (order_o1 == null) {
                    return -1;
                }
                BigInteger order_o2 = o2.getCallOrder();
                if (order_o2 == null) {
                    return -1;
                }
                return order_o1.compareTo(order_o2);
            }
        });
    }

    /**
     * Построитель цепочки обработчиков
     * из списка аспектов получаем цепочку обработчиков сущности
     *
     * @param aspects     - список конфигураций аспектов
     * @param externaldao - дао
     * @return - цепочка обработки сущности через аспект
     * @throws AspectException - Исключение для модуля аспекты
     */
    private AspectVisitor buildChain(List<AspectCfg> aspects, ExtendGenericDAO externaldao) throws AspectException {
        AspectVisitor visitor = null;
        Map<AspectFactory, Aspect> factorysMap = new EnumMap<AspectFactory, Aspect>(AspectFactory.class);
        if (aspects == null || aspects.isEmpty()) return null;
        for (AspectCfg aspect : aspects) {
            factorysMap.put(AspectFactory.getByAspect(aspect.getAspect().getClass()), aspect.getAspect());
        }
        if (factorysMap.keySet().isEmpty()) return null;
        List<AspectFactory> factorys = new ArrayList<AspectFactory>(factorysMap.keySet());
        //упорядочим по порядку на классе аспекта обработчика
        sortChainByFactory(factorys);
        for (AspectFactory factory : factorys) {
            visitor = factory.getVisitor(visitor, externaldao, factorysMap.get(factory));
        }
        return visitor;
    }

    /**
     * Позиционируем родительские аспекты отбработчиков , на дочернюю сущность
     *
     * @param all         - все аспекты
     * @param in          - аспекты на сущности
     * @param nameEntity  - название сущности
     * @param externaldao - дао
     */
    private void addRootAspectCfg(Map<String, List<AspectCfg>> all, List<AspectCfg> in, String nameEntity, ExtendGenericDAO externaldao) {
        if (in == null && in.isEmpty()) {
            in = new ArrayList<AspectCfg>();
        }
        Metamodel metamodel = externaldao.getSession().getSessionFactory().getMetamodel();
        EntityType type = (EntityType) metamodel.entity(nameEntity).getSupertype();
        if (type != null) {
            List<AspectCfg> rootCgf = all.get(type.getName());
            if (rootCgf != null && !rootCgf.isEmpty()) in.addAll(rootCgf);
            addRootAspectCfg(all, in, type.getName(), externaldao);
        }
    }

    /**
     * Построим цепочки
     *
     * @param externaldao - дао
     * @return - коллекция (модуль-(сущность:цепочка обработки))
     * @throws AspectException - Исключение для модуля аспекты
     */
    public Map<String, Map<String, AspectVisitor>> build(ExtendGenericDAO externaldao) throws AspectException {
        Map<String, Map<String, AspectVisitor>> out = new HashMap<>();
        for (Map.Entry<String, Map<String, List<AspectCfg>>> onModule : aspects.entrySet()) {
            String nameModule = onModule.getKey();
            if (onModule.getValue() == null || onModule.getValue().isEmpty()) continue;
            for (Map.Entry<String, List<AspectCfg>> onEntity : onModule.getValue().entrySet()) {
                String nameEntity = onEntity.getKey();
                List<AspectCfg> anycfg = onEntity.getValue();
                // добавим обработчиков с родителей
                addRootAspectCfg(onModule.getValue(), anycfg, nameEntity, externaldao);
                if (anycfg == null || anycfg.isEmpty()) continue;
                AspectVisitor visitor = buildChain(anycfg, externaldao);
                Map visitorOnEntity = new HashMap<String, AspectVisitor>();
                visitorOnEntity.put(nameEntity, visitor);
                logger.debug(String.format("for entity %1s chain:%2s", nameEntity, visitor.getDescription()));
                out.put(nameModule, visitorOnEntity);
            }
        }
        return out;
    }

}
