package com.bivgroup.core.dictionary.entity;

import com.bivgroup.common.orm.sequence.SequenceContainer;
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
 * Entity - Аспект
 */
@Entity
@Table(name = "SAD_ASPECT")
public class SadAspect implements java.io.Serializable {

    @Id
    @GeneratedValue(generator = "SadAspectTableIdGenerator")
    @GenericGenerator(name = "SadAspectTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ASPECT"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })

    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //наименование
    @Column(name = "NAME")
    private String name;
    //системное наименование
    @Column(name = "SYSNAME")
    private String sysname;
    //пометка
    @Column(name = "NOTE")
    private String note;

    //связи с entity
    @OneToMany(mappedBy = "aspect", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SadEntityAspect> attributes = new ArrayList<SadEntityAspect>();

    //ссылка на модуль
    @ManyToOne
    @JoinColumn(name = "MODULEID")
    private SadModule module;

    public List<SadEntityAspect> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SadEntityAspect> attributes) {
        this.attributes = attributes;
    }

    public SadModule getModule() {
        return module;
    }

    public void setModule(SadModule module) {
        this.module = module;
    }

    public SadAspect() {
    }

    public SadAspect(Long id) {
        this.id = id;
    }

    public SadAspect(Long id, String name, String sysname, String note) {
        this.id = id;
        this.name = name;
        this.sysname = sysname;
        this.note = note;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
