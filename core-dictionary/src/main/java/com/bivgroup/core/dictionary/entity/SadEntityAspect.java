package com.bivgroup.core.dictionary.entity;


import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.sql.Clob;

/**
 * Created by bush on 16.09.2016.
 * связь аспектов с сущностями
 */
@Entity
@Table(name = "SAD_ENTITY_ASPECT")
public class SadEntityAspect implements java.io.Serializable {
    @Id
    @GeneratedValue(generator = "SadEntityAspectTableIdGenerator")
    @GenericGenerator(name = "SadEntityAspectTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ENTITY_ASPECT"),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //ссылка на сущность
    @ManyToOne
    @JoinColumn(name = "ENTITYID")
    private SadEntity entity;
    //ссылка на аспект
    @ManyToOne
    @JoinColumn(name = "ASPECTID")
    private SadAspect aspect;
    //настройка аспекта (xml)
    @Column(name = "CONFIG")
    private Clob config;

    public SadEntityAspect() {
    }

    public SadEntityAspect(Long id) {
        this.id = id;
    }

    public SadEntityAspect(Long id, SadEntity entity, SadAspect aspect, Clob config) {
        this.id = id;
        this.entity = entity;
        this.aspect = aspect;
        this.config = config;
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

    public void setEntity(SadEntity entity) {
        this.entity = entity;
    }

    public SadAspect getAspect() {
        return this.aspect;
    }

    public void setAspect(SadAspect aspect) {
        this.aspect = aspect;
    }

    public Clob getConfig() {
        return this.config;
    }

    public void setConfig(Clob config) {
        this.config = config;
    }

}
