package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.common.utils.ToolCore;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadEntityAspect;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import org.hibernate.engine.OptimisticLockStyle;
import org.hibernate.mapping.*;
import org.hibernate.type.StringType;

import java.util.*;

/**
 * Created by andreykus on 27.10.2016.
 * Стратегия создания PersistentClass
 */
public enum CreateClassStrategy {
    /**
     * Родительский класс
     */
    ROOT_CLASS {
        @Override
        public PersistentClass createClass(SadEntity ent, Table table, MetadataCollection source) throws DictionaryException {
            NameGenerator ng = new NameGenerator();
            PersistentClass pc = source.getClass(ng.getClassName(ent));
            if (pc == null) {
                pc = new RootClass(null);
                pc.setEntityName(ng.getEntityName(ent));
                pc.setClassName(ng.getClassName(ent));
                pc.setJpaEntityName(ng.getEntityName(ent));
                pc.setProxyInterfaceName(ng.getClassName(ent));
                pc.setLazy(true);
                ((RootClass) pc).setTable(table);
            }
            addClass(pc, ent, source);
            return pc;
        }
    },
    /**
     * Подкласс наследования , присоединение таблицы по идентификатору
     */
    JOIN_SUB_CLASS {
        @Override
        public PersistentClass createClass(SadEntity ent, Table table, MetadataCollection source) throws DictionaryException {
            NameGenerator ng = new NameGenerator();
            PersistentClass pc = source.getClass(ng.getClassName(ent));
            if (pc == null) {
                Table mainTable = source.getTable(ng.getTableName(ent.getParent().getTablename()));
                mainTable.setComment(null);
                PersistentClass mainClass = CLASS.createClass(ent.getParent(), mainTable, source);
                if (!mainClass.getPropertyIterator().hasNext()) {
                    new AttributeVisitor(null).visit(ent.getParent(), source);
                }
                pc = new JoinedSubclass(mainClass, null);
                pc.setEntityName(ng.getEntityName(ent));
                pc.setJpaEntityName(ng.getEntityName(ent));
                pc.setClassName(ng.getClassName(ent));
                pc.setProxyInterfaceName(ng.getClassName(ent));
                pc.setLazy(true);
                pc.setOptimisticLockStyle(OptimisticLockStyle.VERSION);
                mainClass.addSubclass((Subclass) pc);
                mainClass.setOptimisticLockStyle(OptimisticLockStyle.VERSION);

                Column col = new Column();
                RootClass root = mainClass.getRootClass();
                col.setName(ng.getDiscriminatorColumnName(root.getEntityName()));
                SimpleValue discriminatorValue = new SimpleValue(source.metadata, root.getTable());
                discriminatorValue.addColumn(col);
                discriminatorValue.setTypeName(StringType.INSTANCE.getName());
                root.setDiscriminator(discriminatorValue);

                ((JoinedSubclass) pc).setTable(table);
            }
            addClass(pc, ent, source);
            return pc;
        }
    },
    //TODO непонятно как в него помещать связи , т.к. классы колекций работают только с PersistentClass
    /**
     * Подкласс наследования , присоединение таблицы по идентификатору с дискриминатора
     */
    JOIN_SUB_CLASS_DISCRIMINATOR {
        @Override
        public PersistentClass createClass(SadEntity ent, Table table, MetadataCollection source) throws DictionaryException {
            NameGenerator ng = new NameGenerator();
            PersistentClass pc = source.getClass(ng.getClassName(ent));
            if (pc == null) {
                Table mainTable = source.getTable(ng.getTableName(ent.getParent().getTablename()));
                mainTable.setComment(null);
                PersistentClass mainClass = CLASS.createClass(ent.getParent(), mainTable, source);
                if (!mainClass.getPropertyIterator().hasNext()) {
                    new AttributeVisitor(null).visit(ent.getParent(), source);
                }
                RootClass root = mainClass.getRootClass();
                pc = new Subclass(mainClass, null);
                mainClass.addSubclass((Subclass) pc);
                pc.setEntityName(ng.getEntityName(ent));
                pc.setJpaEntityName(ng.getEntityName(ent));
                pc.setClassName(ng.getClassName(ent));
                pc.setProxyInterfaceName(ng.getClassName(ent));
                pc.setLazy(true);
                Column col = new Column();
                col.setName(ng.getDiscriminatorColumnName(root.getEntityName()));
                SimpleValue discriminatorValue = new SimpleValue(source.metadata, root.getTable());
                discriminatorValue.addColumn(col);
                discriminatorValue.setTypeName(StringType.INSTANCE.getName());
                root.setDiscriminator(discriminatorValue);
                pc.setDiscriminatorValue(ng.getDiscriminatorName(ent.getSysname()));
                pc.setOptimisticLockStyle(OptimisticLockStyle.VERSION);
                Join join = new Join();
                join.setTable(table);
                discriminatorValue.addColumn(col);
                KeyValue kv = (KeyValue) col.getValue();
                join.setKey(kv);
                pc.addJoin(join);
            }
            addClass(pc, ent, source);
            return pc;
        }
    },
    /**
     * общий класс создания
     */
    CLASS {
        @Override
        public PersistentClass createClass(SadEntity ent, Table table, MetadataCollection source) throws DictionaryException {
            PersistentClass pc = source.getClass(new NameGenerator().getClassName(ent));
            if (pc == null) {
                if (!ent.hasParent()) {
                    pc = ROOT_CLASS.createClass(ent, table, source);
                } else {
                    pc = JOIN_SUB_CLASS.createClass(ent, table, source);
                }
            }
            return pc;
        }

    };

    /**
     * Добавить мета тег на классе
     * TODO не используется , т.к. PersistentClass можно получить только на этапе создания  SessionFactory, на сессии его уже не получить
     *
     * @param ent - сущность
     * @param pc  - описание сущности
     * @throws DictionaryException - исключение словарной системы
     */
    void addMetaTag(SadEntity ent, PersistentClass pc) throws DictionaryException {
        java.util.List<SadEntityAspect> aspects = ent.getAspects();
        for (SadEntityAspect aspect : aspects) {
            java.util.Map<String, MetaAttribute> m = new HashMap<String, MetaAttribute>();
            MetaAttribute mapMeta = new MetaAttribute(aspect.getAspect().getSysname());
            try {
                mapMeta.addValue(new ToolCore().convertStreamToString(aspect.getConfig().getAsciiStream()));
            } catch (Exception ex) {
                throw new DictionaryException(ex);
            }
            m.put(mapMeta.getName(), mapMeta);
            pc.setMetaAttributes(m);
        }
    }

    /**
     * шаблонный метод создания PersistentClass
     *
     * @param ent    - сущность
     * @param table  -  таблица
     * @param source - хранилище метаданных
     * @return - PersistentClass
     * @throws DictionaryException - исключение словарной системы
     */
    public abstract PersistentClass createClass(SadEntity ent, Table table, MetadataCollection source) throws DictionaryException;

    /**
     * добавть PersistentClass в хранилище метаданных
     *
     * @param pc     - класс
     * @param ent    - сущность
     * @param source - хранилище метаданны
     * @throws DictionaryException - исключение словарной системы
     */
    void addClass(PersistentClass pc, SadEntity ent, MetadataCollection source) throws DictionaryException {
        //not insert meta in PersistenceClass addMetaTag(ent,pc);
        NameGenerator ng = new NameGenerator();
        source.addClassWithModules(ng.getModulePackage(ent.getModule()), pc);
        source.addEntityWithModules(ng.getModulePackage(ent.getModule()), ent);
    }
}
