package com.bivgroup.core.dictionary.entity;


import com.bivgroup.core.dictionary.entity.enums.Category;
import com.bivgroup.common.orm.sequence.SequenceContainer;
import com.bivgroup.core.dictionary.interceptor.GenerateEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;


/**
 * Created by bush on 16.09.2016.
 * Аттрибуты сущности
 */
@GenerateEntity
@Entity
@Table(name = "SAD_ATTRIBUTE")

public class SadAttribute implements java.io.Serializable {
    @Id
    @GeneratedValue(generator = "SadAttributeTableIdGenerator")
    @GenericGenerator(name = "SadAttributeTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ATTRIBUTE"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //ссылка на  entity
    @ManyToOne
    @JoinColumn(name = "ENTITYID")
    private SadEntity entity;
    //наименование
    @Column(name = "NAME")
    private String name;
    //системное наименование
    @Column(name = "SYSNAME")
    private String sysname;
    //комментарии
    @Column(name = "NOTE")
    private String note;
    //категория - ссылка, поле, множество
    @Column(name = "CATEGORY")
    @Enumerated(EnumType.ORDINAL)
    private Category category;
    //тип поля
    @Column(name = "DATATYPE")
    private String datatype;
    //наименование поля
    @Column(name = "FIELDNAME")
    private String fieldname;
    //первичный ключ
    @Column(name = "ISPRIMARYKEY")
    private Long isprimarykey;
    //ссылка на другую entity
    @ManyToOne
    @JoinColumn(name = "REFENTITYID")
    private SadEntity refentity;
    //ссылка на другое поле
    @ManyToOne
    @JoinColumn(name = "REFATTRIBUTEID")
    private SadAttribute refattribute;
    //ссылка на аспект
    @ManyToOne
    @JoinColumn(name = "ASPECTID")
    private SadAspect aspect;


    public void setEntity(SadEntity entity) {
        this.entity = entity;
    }
    //версионное поле
    @Column(name = "ISVERSIONT")
    private Long isversiont;
    @Column(name = "ISCUSTOM")
    private Long iscustom;
    //ссылка на модуль
    @ManyToOne
    @JoinColumn(name = "MODULEID")
    private SadModule module;

    public SadModule getModule() {
        return module;
    }

    public void setModule(SadModule module) {
        this.module = module;
    }

    public SadAttribute() {
    }

    public SadAttribute(Long id) {
        this.id = id;
    }

    public SadAttribute(Long id, SadEntity entity, String name, String sysname, String note, Category category, String datatype, String fieldname, Long isprimarykey, SadEntity refentity, SadAttribute refattribute, SadAspect aspect, Long isversiont, Long iscustom) {
        this.id = id;
        this.entity = entity;
        this.name = name;
        this.sysname = sysname;
        this.note = note;
        this.category = category;
        this.datatype = datatype;
        this.fieldname = fieldname;
        this.isprimarykey = isprimarykey;
        this.refentity = refentity;
        this.refattribute = refattribute;
        this.aspect = aspect;
        this.isversiont = isversiont;
        this.iscustom = iscustom;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SadEntity getEntity() {
        return this.entity;
    }

    public void setEntityid(SadEntity entity) {
        this.entity = entity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysname() {
        return this.sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDatatype() {
        return this.datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getFieldname() {
        return this.fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Long getIsprimarykey() {
        return this.isprimarykey;
    }

    public void setIsprimarykey(Long isprimarykey) {
        this.isprimarykey = isprimarykey;
    }

    public SadEntity getRefentity() {
        return this.refentity;
    }

    public void setRefentity(SadEntity refentity) {
        this.refentity = refentity;
    }

    public SadAttribute getRefattribute() {
        return this.refattribute;
    }

    public void setRefattribute(SadAttribute refattribute) {
        this.refattribute = refattribute;
    }

    public SadAspect getAspect() {
        return this.aspect;
    }

    public void setAspect(SadAspect aspect) {
        this.aspect = aspect;
    }

    public Long getIsversiont() {
        return this.isversiont;
    }

    public void setIsversiont(Long isversiont) {
        this.isversiont = isversiont;
    }

    public Long getIscustom() {
        return this.iscustom;
    }

    public void setIscustom(Long iscustom) {
        this.iscustom = iscustom;
    }

}
