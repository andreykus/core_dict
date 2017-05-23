package com.bivgroup.core.dictionary.entity;


import com.bivgroup.common.orm.audit.AuditObject;
import com.bivgroup.common.orm.sequence.SequenceContainer;
import com.bivgroup.core.dictionary.entity.enums.TypeEntity;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.interceptor.GenerateEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bush on 16.09.2016.
 * Сущность
 */
@GenerateEntity
@Entity
@Table(name = "SAD_ENTITY")
public class SadEntity implements java.io.Serializable, AuditObject {

    @Id
    @GeneratedValue(generator = "SadEntityTableIdGenerator")
    @GenericGenerator(name = "SadEntityTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ENTITY"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификато
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //ссылка на дочерний (наследование)
    @ManyToOne
    @JoinColumn(name = "PARENTID", nullable = true)
    private SadEntity parent;

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    //наименование
    @Column(name = "NAME")
    private String name;
    //системное наименование
    @Column(name = "SYSNAME")
    private String sysname;
    //название таблицы
    @Column(name = "TABLENAME")
    private String tablename;
    //комментарий
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ISMETAENTITY")
    private Long ismetaentity;

    public TypeExport getTypeExport() {
        return typeExport;
    }

    public void setTypeExport(TypeExport typeExport) {
        this.typeExport = typeExport;
    }

    public void setAttributes(List<SadAttribute> attributes) {
        this.attributes = attributes;
    }
    //тип сущности: простая, справочник
    @Column(name = "TYPEENTITY")
    @Enumerated(EnumType.ORDINAL)
    private TypeEntity typeEntity;

    public TypeEntity getTypeEntity() {
        return typeEntity;
    }

    public void setTypeEntity(TypeEntity typeEntity) {
        this.typeEntity = typeEntity;
    }

    public List<SadEntityAspect> getAspects() {
        return aspects;
    }

    public void setAspects(List<SadEntityAspect> aspects) {
        this.aspects = aspects;
    }
    //тип экспорта
    @Column(name = "TYPEEXPORT")
    @Enumerated(EnumType.ORDINAL)
    private TypeExport typeExport;
    //аттрибуты
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SadAttribute> attributes = new ArrayList<SadAttribute>();
    //аспекты
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SadEntityAspect> aspects = new ArrayList<SadEntityAspect>();

    public SadModule getModule() {
        return module;
    }

    public void setModule(SadModule module) {
        this.module = module;
    }
    //ссылка на модуль
    @ManyToOne
    @JoinColumn(name = "MODULEID")
    private SadModule module;

    public SadEntity() {
    }

    public SadEntity(Long id) {
        this.id = id;
    }

    public SadEntity(Long id, SadEntity parent, String name, String sysname, String note, Long ismetaentity) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.sysname = sysname;
        this.note = note;
        this.ismetaentity = ismetaentity;
    }

    public List<SadAttribute> getAttributes() {
        return attributes;
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return String.format("Object:name %1s sysname %2s", name, sysname);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SadEntity getParent() {
        return this.parent;
    }

    public void setParent(SadEntity parent) {
        this.parent = parent;
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

    public Long getIsmetaentity() {
        return this.ismetaentity;
    }

    public void setIsmetaentity(Long ismetaentity) {
        this.ismetaentity = ismetaentity;
    }

    public boolean hasParent() {
        return parent != null;
    }

}
