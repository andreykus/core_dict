package com.bivgroup.core.dictionary.generator.visitors;

import com.bivgroup.common.orm.sequence.SequenceContainer;
import com.bivgroup.core.dictionary.common.AbstractStructuraVisitor;
import com.bivgroup.core.dictionary.common.StructuraVisitor;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.enums.TypeEntity;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.MetadataCollection;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import com.bivgroup.core.dictionary.generator.visitors.util.TypeGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.MappingException;
import org.hibernate.cfg.BinderHelper;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.mapping.*;

import java.util.*;

/**
 * Created by bush on 11.10.2016.
 * Шаблонн для создания свойст таблицы
 */
//TODO почему то не работет в XML @PrimaryKeyJoinColumn
abstract class AbstractAttributeVisitor extends AbstractStructuraVisitor<MetadataCollection> {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * генератор наименованиий элементов сущности
     */
    private NameGenerator ng;
    /**
     * флаг создания двойний связки M20, 02M
     */
    static final boolean NOT_DUBLE_BIND = true;

    /**
     * Конструктор Шаблона для создания свойст таблицы
     *
     * @param next - следующий обработчик элемента структуры
     * @throws DictionaryException - исключение словарной системы
     */
    public AbstractAttributeVisitor(StructuraVisitor next) throws DictionaryException {
        super(next);
        this.ng = new NameGenerator();
    }

    /**
     * генератор поля идентификатора на сущностях
     * TODO сделать фабрику, чтобы можно было менть процесс , на данный момент нет описания в структуре БД - где можно менять тип идентификатора
     * TODO т.к. есть еще старый генератор из diasoft
     *
     * @param attr  - аттрибут
     * @param value - величина свойства, к ней привязывается идентификатор
     */
    protected void generatorId(SadAttribute attr,
                               Value value) {
        //идентификаторы генерятся из пула полученного от последовательности
        ((SimpleValue) value).setIdentifierGeneratorStrategy("enhanced-sequence");
        Properties params = new Properties();
        params.put(SequenceStyleGenerator.JPA_ENTITY_NAME,
                ng.getTableName(attr.getEntity().getTablename()));
        params.put(SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY,
                "true");
        params.setProperty(
                SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX,
                SequenceContainer.DEFAULT_SEQUENCE_SUFFIX);
        params.setProperty(
                SequenceStyleGenerator.INCREMENT_PARAM,
                SequenceContainer.INCREMENT);
        params.setProperty(
                SequenceStyleGenerator.OPT_PARAM,
                "pooled");
        ((SimpleValue) value).setIdentifierGeneratorProperties(params);
        ((SimpleValue) value).setNullValue(null);

    }

    /**
     * получить свойство по наименованию
     *
     * @param clazzRef     - описание ссылки
     * @param propertyName - название свойства
     * @return -  свойство
     */
    protected Property getRefProperty(PersistentClass clazzRef, String propertyName) {
        Property refProp = null;
        //для композитного идентификатора
        if (clazzRef.getIdentifier() instanceof Component) { //Composite id
            try {
                refProp = ((Component) clazzRef.getIdentifier()).getProperty(propertyName);
            } catch (Exception e) {
                logger.debug("Property " + propertyName + " not found in identifier", e);
            }
        }
        if (refProp == null) {
            refProp = clazzRef.getProperty(propertyName);
        }
        assert refProp != null;
        return refProp;
    }

    /**
     * создать вторичный ключ ссылку для O2M
     *
     * @param clazzRef          - класс ссылки
     * @param clazzOwner        - класс влыделец
     * @param colFromName       - с колонки
     * @param colToPropertyName - на колонку
     * @return - зависимое значение свойства
     */
    protected DependantValue createOneFk(PersistentClass clazzRef,
                                         PersistentClass clazzOwner, String colFromName, String colToPropertyName) {
        DependantValue dv = null;
        Property refProp;
        refProp = getRefProperty(clazzRef, colToPropertyName);
        dv = new DependantValue(source.metadata, clazzOwner.getTable(),
                refProp.getPersistentClass().getKey());
        dv.setNullable(true);
        dv.setUpdateable(true);
        Iterator it = clazzRef.getTable().getColumnIterator();
        while (it.hasNext()) {
            Column col = (Column) it.next();
            if (col.getName().equals(colFromName)) {
                dv.addColumn(col);
                break;
            }
        }
        return dv;
    }

    /**
     * создать первичный ключ
     *
     * @param attr - аттрибут
     * @throws DictionaryException - исключение словарной системы
     */
    protected void createOnePk(SadAttribute attr) throws DictionaryException {
        PersistentClass pc = source.getClass(ng.getClassName(attr.getEntity()));
        Table table = pc.getTable();

        final PrimaryKey primaryKey = new PrimaryKey(table);
        primaryKey.setName(ng.getPkName(attr.getSysname()));
        primaryKey.setTable(table);
        table.setPrimaryKey(primaryKey);

        Column col = createColumn(attr);
        SimpleValue id = (SimpleValue) col.getValue();
        id.setNullValue("undefined");

        table.getPrimaryKey().addColumn(col);

        Property prop = createProperty(attr, id);
        prop.setUpdateable(false);
        prop.setInsertable(false);

        generatorId(attr, id);

        table.setIdentifierValue(id);

        AttributeContainer pc_atr = getSubclass(pc, attr);
        //для разных типов класса
        if (pc_atr instanceof Join) {
            KeyValue kv = (KeyValue) col.getValue();
            ((Join) pc_atr).setKey(kv);
        } else if (pc_atr instanceof JoinedSubclass) {
            KeyValue kv = (KeyValue) col.getValue();
            ((JoinedSubclass) pc_atr).setKey(kv);
        } else {
            ((RootClass) pc).setIdentifier(id);
            ((RootClass) pc).setIdentifierProperty(prop);
        }
    }

    /**
     * создание колонки без всязи с классов
     * псевдо колонка
     *
     * @param attr - аттрибут
     * @return - свойство
     * @throws DictionaryException - исключение словарной системы
     */
    protected Property createSimpleColumnWOBind(SadAttribute attr) throws DictionaryException {
        //PersistentClass pc = source.getClass(nameGenerator.getClassName(attr.getEntity().getSysname()));
        Column col = createColumn(attr);
        Property prop = createProperty(attr, (SimpleValue) col.getValue());
        //      pc.addProperty(prop);
        TypeGenerator.setType((SimpleValue) col.getValue(), attr.getDatatype());
        return prop;
    }

    /**
     * вторичный ключ
     *
     * @param clazzFrom   - с класса
     * @param clazzTo     - на класс
     * @param colFromName - с колонки
     * @param colToName   - на колонку
     * @return - вторичный ключ
     */
    protected ForeignKey createFk(PersistentClass clazzFrom, PersistentClass clazzTo, String colFromName, String colToName) {
        ForeignKey fk = null;

        fk = new ForeignKey();
        fk.addColumn(clazzFrom.getTable().getColumn(new Column(colFromName)));
        fk.setReferencedTable(clazzTo.getTable());
        fk.setTable(clazzFrom.getTable());
        fk.setName(ng.getFkName(colFromName));
        try {
            clazzTo.getTable().getColumn(new Column(colToName));
        } catch (Exception ex) {
        }

        fk.addReferencedColumns(Arrays.asList(clazzTo.getTable().getColumn(new Column(colToName))).iterator());

        fk = clazzFrom.getTable().createForeignKey(
                fk.getName(),
                fk.getColumns(),
                clazzTo.getEntityName(),
                fk.getKeyDefinition(),
                fk.getReferencedColumns());


        return fk;
    }

    /**
     * полкласс класса
     *
     * @param pc   - класс
     * @param attr - аттрибут
     * @return - подкласс
     */
    protected AttributeContainer getSubclass(PersistentClass pc, SadAttribute attr) {
        if (pc instanceof Subclass) {
            Iterator<Join> iter = ((Subclass) pc).getJoinClosureIterator();
            while (iter.hasNext()) {
                Join join = iter.next();
                if (ng.getTableName(attr.getEntity().getTablename()).equals(join.getTable().getName())) {
                    return join;
                }
            }
        }
        return pc;
    }

    /**
     * создать колонку (в свойстве)
     *
     * @param attr - аттрибут
     * @throws DictionaryException - исключение словарной системы
     */
    protected void createSimpleColumn(SadAttribute attr) throws DictionaryException {
        PersistentClass pc = source.getClass(ng.getClassName(attr.getEntity()));
        try {
            pc.getProperty(ng.getPropertyName(attr.getSysname()));
        } catch (MappingException ex) {
            Column col = createColumn(attr);
            Property prop = createProperty(attr, (SimpleValue) col.getValue());
            AttributeContainer pc_atr = getSubclass(pc, attr);
            pc_atr.addProperty(prop);
            TypeGenerator.setType((SimpleValue) col.getValue(), attr.getDatatype());
        }
    }

    /**
     * создать свойство
     *
     * @param attr  - аттрибут
     * @param value - значение
     * @return - свойство
     * @throws DictionaryException - исключение словарной системы
     */
    protected Property createProperty(SadAttribute attr,
                                      Value value) throws DictionaryException {
        Property prop = new Property();
        prop.setName(ng.getPropertyName(attr.getSysname()));
        prop.setValue(value);
        TypeGenerator.setType((SimpleValue) value, attr.getDatatype());
        return prop;
    }

    /**
     * простое создание кололнки
     *
     * @param attr - аттрибут
     * @return - колонка
     * @throws DictionaryException - исключение словарной системы
     */
    protected Column createColumn(SadAttribute attr) throws DictionaryException {
        PersistentClass pc = source.getClass(ng.getClassName(attr.getEntity()));
        Table table = pc.getTable();
        Column col = table.getColumn(new Column(ng.getColumnName(attr.getSysname())));
        if (col != null) return col;
        col = new Column();
        col.setName(ng.getColumnName(attr.getSysname()));
        col.setComment(attr.getNote());
        SimpleValue simpleValue = new SimpleValue(source.metadata, table);
        simpleValue.addColumn(col);
        table.addColumn(col);
        return col;
    }

    /**
     * создать синтетическое, псевдо свойство. используется при создании ссылки не на первичный ключ
     *
     * @param manyToOne    - manyToOne
     * @param relationship - вторичный ключ
     * @param pcMany       - класс Many
     * @param pcOne        - класс One
     */
    protected void addSyntheticProperty(
            ManyToOne manyToOne, ForeignKey relationship,
            PersistentClass pcMany, PersistentClass pcOne) {

        if (relationship != null) {
            String firstPropertyName = ((Column) relationship.getReferencedColumns().get(0)).getName();
            String syntheticPropertyName = "syn_" + ng.getPropertyName(firstPropertyName);
            PersistentClass referencedClass = pcMany;
            Component embeddedComp = new Component(source.metadata, referencedClass);
            embeddedComp.setEmbedded(true);
            embeddedComp.setComponentClassName(embeddedComp.getOwner().getClassName());
            java.util.List<Column> columns = relationship.getReferencedColumns();
            for (Column ref : columns) {
                String propertyName = ref.getName();
                Property property;
                if (referencedClass.getIdentifier() instanceof Component) {
                    //Composite id
                    property = ((Component) referencedClass.getIdentifier()).getProperty(propertyName);
                } else {
                    property = referencedClass.getProperty(propertyName);
                }
                Property clone = BinderHelper.shallowCopy(property);
                clone.setInsertable(false);
                clone.setUpdateable(false);
                clone.setNaturalIdentifier(false);
                clone.setValueGenerationStrategy(property.getValueGenerationStrategy());
                embeddedComp.addProperty(clone);
            }

            SyntheticProperty synthProp = new SyntheticProperty();
            synthProp.setName(syntheticPropertyName);
            synthProp.setPersistentClass(referencedClass);
            synthProp.setUpdateable(false);
            synthProp.setInsertable(false);
            synthProp.setValue(embeddedComp);
            synthProp.setPropertyAccessorName("embedded");
            referencedClass.addProperty(synthProp);

            manyToOne.setReferencedPropertyName(syntheticPropertyName);

        }
    }

    /**
     * получение M2O из класса, по ссвлке
     *
     * @param pcOne   - класс One
     * @param pcMany  - класс Many
     * @param refName - наименование ссылки
     * @return - ManyToOne
     */
    private ManyToOne getM2OInClass(PersistentClass pcOne, PersistentClass pcMany, String refName) {
        try {
            Property prop = pcMany.getProperty(ng.getOneToManyPropertyName(refName)); //pcOne.getEntityName()
            return (ManyToOne) prop.getValue();
        } catch (MappingException ex) {
            return null;
        }
    }

    /**
     * создание ссылки
     *
     * @param attr - аттрибут
     * @return - ManyToOne
     * @throws DictionaryException - исключение словарной системы
     */
    protected ManyToOne createRefernceM2O(SadAttribute attr) throws DictionaryException {

        PersistentClass pcMany = source.getClass(ng.getClassName(attr.getEntity()));

        SadEntity refEnt = attr.getRefentity();
        SadAttribute refAttr = attr.getRefattribute();

        String refEntityName = ng.getColumnName(refEnt.getSysname());
        String refClassName = ng.getClassName(refEnt);
        String mainEntityName = ng.getColumnName(attr.getEntity().getSysname());
        if (refEnt == null || refAttr == null) {
            return null;
        }
        String refColumnName = ng.getColumnName(refAttr.getSysname());
        String mainColumnName = ng.getColumnName(attr.getSysname());

        PersistentClass depClass = source.getClass(refClassName);
        Property prop = new Property();
        prop.setName(ng.getOneToManyPropertyName(attr.getSysname())); //pcOne.getEntityName()

        //если нет зависимой сущности создадим ее, и ее свойства
        if (depClass == null) {
            ManyToOne manyToOne1 = new ManyToOne(source.metadata, pcMany.getTable());
            prop.setValue(manyToOne1);
            pcMany.addProperty(prop);
            new EntityVisitor(new AttributeVisitor(null)).visit(refEnt, source);
        }

        PersistentClass pcOne = source.getClass(refClassName);

        //если уже есть ссылка (возможно реверсивное создание) - то выходим
        ManyToOne m20 = getM2OInClass(pcOne, pcMany, attr.getSysname());
        if (m20 != null && m20.getReferencedEntityName() != null) return m20;
        // создадим  псевдо колоннку не привязанную к свойств
        if (pcMany.getTable().getColumn(new Column(mainColumnName)) == null) {
            Column in = createColumn(attr);
        }
        //создадим ссылки для таблицы Многие
        ForeignKey fk = createFk(pcMany, pcOne, mainColumnName, refColumnName);

        ManyToOne manyToOne = new ManyToOne(source.metadata, pcMany.getTable());
        manyToOne.setLazy(false);
        manyToOne.setReferencedEntityName(pcOne.getEntityName());

        //manyToOne.setReferencedPropertyName("ID");
        // manyToOne.setReferenced(pcOne.getClassName());

        java.util.Collection<ForeignKey> fkeys = pcMany.getTable().getForeignKeys().values();

        Iterator refIt = fkeys.iterator();

        java.util.Set<Column> referencedColumns = new HashSet<>();

        ForeignKey relationship = null;
        //привязываем колонки
        while (refIt.hasNext()) {
            ForeignKey fk1 = (ForeignKey) refIt.next();
            java.util.List<Column> cols = fk.getColumns();
            for (Column col : cols) {
                if (cols.contains(new Column(mainColumnName))) {
                    manyToOne.addColumn(col);
                }
            }
            java.util.List<Column> refcols = fk.getReferencedColumns();
            for (Column col : refcols) {
                if (refcols.contains(new Column(refColumnName))) {
                    manyToOne.addColumn(fk.getColumn(0));
                    referencedColumns.add(col);
                    relationship = fk;
                }
            }
        }


//        if (pcOne.getTable().getPrimaryKey().containsColumn(new Column(NameGenerator.getColumnName(depAttr.getSysname()))))) {
//            referencedColumns.add(pcOne.getTable().getColumn(new Column(NameGenerator.getColumnName(depAttr.getSysname()))));
//        }

        java.util.List<Column> pkColumns = Collections.emptyList();
        if (pcOne.getTable().getPrimaryKey() != null) {
            pkColumns = pcOne.getTable().getPrimaryKey().getColumns();
        }

        //установим ссылку
        boolean referenceToPrimaryKey =
                pkColumns.containsAll(referencedColumns) && pkColumns.size() == referencedColumns.size();
        manyToOne.setReferenceToPrimaryKey(referenceToPrimaryKey);

        if (!referenceToPrimaryKey) {
            addSyntheticProperty(manyToOne, relationship, pcMany, pcOne);
        }

        manyToOne.setFetchMode(FetchMode.DEFAULT);

        //      manyToOne.createForeignKey();
        // такой обман необходим если M20 пересекается с O2M, и O2М создается раньше
        try {
            Property tmpPr = pcMany.getProperty(ng.getOneToManyPropertyName(attr.getSysname()));
            prop = tmpPr;
            prop.setValue(manyToOne);
            prop.setCascade("none");
            prop.setUpdateable(true);
            prop.setInsertable(true);
        } catch (MappingException ex) {
            prop.setValue(manyToOne);
            prop.setCascade("none");
            prop.setUpdateable(true);
            prop.setInsertable(true);
            pcMany.addProperty(prop);

        }

        // Добавляем транзитивное свойство для удобства сохранения json объектов, потом по данному id будем restore объект, только для Map TODO для классов нужно сделать аннотацию
        if (TypeExport.XML.equals(source.getExportType())) {
            //TODO Добавить для свойства колонку
            Property transient_prop = new Property();
            SimpleValue value = new SimpleValue(source.metadata);

            TypeGenerator.setType(value, attr.getDatatype());
            transient_prop.setValue(value);
            transient_prop.setName(attr.getSysname());
            //TODO удалить
            //<meta attribute="annotations">@javax.persistence.Transient</meta>
            //            Map<String, MetaAttribute> m = new HashMap<String, MetaAttribute>();
            //            MetaAttribute mapMeta = new MetaAttribute("class-code");
            //            mapMeta.addValue(String.format("\n@javax.persistence.Transient\n public Long %1$s;\n  public Long get%1$s(){return %1$s;}\n public void set%1$s(){%1$s=%1$s;}\n",attr.getSysname()));
            //            m.put(mapMeta.getName(), mapMeta);
            //            pcMany.setMetaAttributes(m);
            transient_prop.setInsertable(false);
            transient_prop.setUpdateable(false);
            pcMany.addProperty(transient_prop);
        }

        return manyToOne;
    }

    /**
     * это справочник ?
     *
     * @param attr    - аттрибут
     * @param refAttr - ссылка
     * @return - если справочник вернуть true
     */
    private boolean isDictionary(SadAttribute attr, SadAttribute refAttr) {
        if (TypeEntity.DICTIONARY.equals(refAttr.getEntity().getTypeEntity())) return true;
        return false;
    }

    /**
     * отношение O2O
     * нет описание в стуктуре
     *
     * @param attr - аттрибут
     */
    private void createRefernceO2O(SadAttribute attr) {
    }

    //TODO удалить
    private SadAttribute getRefAttrPk(SadEntity refEntity) throws DictionaryException {
        for (SadAttribute attr : refEntity.getAttributes()) {
            if (new FieldVisitor(null).isPrimaryKey(attr)) return attr;
        }
        return null;
    }

    //TODO удалить
    private String patchRefColumn(String columnName, PersistentClass mainClass) {
        if (columnName != null) return columnName;
        PrimaryKey pk = mainClass.getTable().getPrimaryKey();
        if ((pk != null) && (!pk.getColumns().isEmpty())) {
            return pk.getColumns().get(0).getName();
        }
        return null;
    }

    /**
     * получить ссылку M2O
     *
     * @param pcMany  - класс Many
     * @param pcOne   - класс One
     * @param refName - наименование поля ссылки
     * @return - описание ссылки
     */
    protected ToOne getRefernceM2O(PersistentClass pcMany, PersistentClass pcOne, String refName) {
        try {
            Property prop = pcMany.getProperty(ng.getOneToManyPropertyName(refName)); //pcOne.getEntityName()
            if (prop != null && prop.getValue() != null) {
                if (prop.getValue() instanceof ManyToOne) return (ManyToOne) prop.getValue();
            }
        } catch (MappingException ex) {
        }
        return null;
    }

    /**
     * получить ссылку на список
     *
     * @param pcOne    - класс One
     * @param pcMany   - класс Many
     * @param listName - наименование поля списка
     * @return - коллекция Bag
     */
    private Bag getO2MInClass(PersistentClass pcOne, PersistentClass pcMany, String listName) {
        try {
            Property prop = pcOne.getProperty(ng.getEntityNameList(listName));
            return (Bag) prop.getValue();
        } catch (MappingException ex) {
            return null;
        }
    }

    /**
     * создание O2M на список
     *
     * @param attr - аттрибут
     * @return - коллекция Bag
     * @throws DictionaryException - исключение словарной системы
     */
    protected Bag createRefernceO2M(SadAttribute attr) throws DictionaryException {

        PersistentClass pcOne_t = source.getClass(ng.getClassName(attr.getEntity()));
        PersistentClass pcOne = (PersistentClass) getSubclass(pcOne_t, attr);

        SadEntity refEnt = attr.getRefentity();
        SadAttribute refAttr = attr.getRefattribute();

        String refEntityName = ng.getColumnName(refEnt.getSysname());
        String refClassName = ng.getClassName(refEnt);
        String mainEntityName = ng.getColumnName(attr.getEntity().getSysname());
        if (refEnt == null || refAttr == null) {
            return null;
        }
//        if (refAttr == null) {
//            refAttr = getRefAttrPk(refEnt);
//        }
        String refColumnName = ng.getColumnName(refAttr.getSysname());
        String mainColumnName = ng.getColumnName(attr.getSysname());

        PersistentClass mainClass = source.getClass(refClassName);
        //если нет пересоздадим
        if (mainClass == null) {
            new EntityVisitor(new AttributeVisitor(null)).visit(refEnt, source);
        }

        PersistentClass pcMany_t = source.getClass(refClassName);
        PersistentClass pcMany = (PersistentClass) getSubclass(pcMany_t, refAttr);

        Bag set = getO2MInClass(pcOne, pcMany, attr.getSysname());
        if (set != null) return set;
        //        col.setComment(depAttr.getNote());
        //        ForeignKey fk = new ForeignKey();
        //
        //        if (attr.getIsprimarykey() != null && attr.getIsprimarykey() > 0) {
        //            fk = new ForeignKey();
        //            fk.setReferencedTable(pcMany.getTable());
        //            fk.setTable(pcOne.getTable());
        //            fk.addColumn(col);
        //            fk.setName("fk" + nameGenerator.getColumnName(depAttr.getSysname()));
        //            fk.addReferencedColumns(Arrays.asList(pcMany.getTable().getColumn(new Column(nameGenerator.getColumnName(depAttr.getSysname())))).iterator());
        //        }

        set = new Bag(source.metadata, pcOne);
        //TODO для замена на List
        //TODO Заменить на Index простой
        //TODO set.setIndex(new SimpleValue(source.metadata));//pcMany.getKey()new SimpleValue(source.metadata)
        //TODO set.setBaseIndex(3);
        //TODO Index простой работате не корректно , возвращаются пустые листы
        //TODO выделить колонку , by default __s_ORDER (Documets_ORDER) , проставить всюду данное поле
        //<#if c2h.isOrderBy(property)><#assign orderbycolumn = value.getOrderBy()><index column="${orderbycolumn}"/></#if><#if c2h.isHaveIndex(property)></#if>
        // set.setOrderBy("ID");
        set.setLazy(false);
        set.setCollectionTable(pcMany.getTable());
        set.setSorted(false);
        set.setFetchMode(FetchMode.DEFAULT);

        OneToMany oneToMany = new OneToMany(source.metadata, set.getOwner());
        set.setElement(oneToMany);

        oneToMany.setReferencedEntityName(pcMany.getEntityName());
        oneToMany.setAssociatedClass(pcMany);

        // создадим  псевдо колоннку не привязанную к свойств
        if (pcOne.getTable().getColumn(new Column(mainColumnName)) == null) {
            Column in = createColumn(attr);
        }
        // создадим  псевдо колоннку не привязанную к свойств
        if (pcMany.getTable().getColumn(new Column(refColumnName)) == null) {
            Column in1 = createColumn(refAttr);
        }

        //создадим ссылки на таблице Один для таблицы Многие
        ForeignKey fk = createFk(pcOne, pcMany, mainColumnName, refColumnName);

        //получим так называемо двойное связываение. Т.Е. ссылку
        ToOne manyToOne = getRefernceM2O(pcMany, pcOne, refAttr.getSysname());//createRefernceM2O(attr);

        //ссылка есть
        if (manyToOne != null) {
            //TODO for class use BD metamodel, need add to BD metamodel collections Map<name,PC>
            if (TypeExport.XML.equals(source.getExportType())) {
                set.setInverse(true);
            }
            //это первичный ключ -  зависимое значение
            if (manyToOne.isReferenceToPrimaryKey()) {
                DependantValue dv;
                Table tableMany = pcMany.getTable();
                Table tableOne = pcOne.getTable();
                dv = createOneFk(pcMany, pcOne, refColumnName, ng.getOneToManyPropertyName(refColumnName));//pcOne.getEntityName()
                dv.setForeignKeyName(fk.getName());
                dv.setTypeName(null);
                dv.setNullable(false);
                set.setKey(dv);
            } else {
                //это не первичный ключ -  зависимое значение
                set.setReferencedPropertyName(manyToOne.getReferencedPropertyName());
                KeyValue keyVal = (KeyValue) set.getOwner().getReferencedProperty(manyToOne.getReferencedPropertyName()).getValue();
                set.setKey(keyVal);
            }
        } else {
            //нет отношения ManyToOne
            createSimpleColumn(refAttr);
            KeyValue keyVal = (KeyValue) createSimpleColumnWOBind(refAttr).getValue();
            set.setKey(keyVal);
        }

        Property prop = new Property();
        prop.setName(ng.getEntityNameList(attr.getSysname()));
        prop.setValue(set);
        //для списков - каскадное удаление
        prop.setCascade("delete");
        pcOne.addProperty(prop);
        return set;
    }

}
