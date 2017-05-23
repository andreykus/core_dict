package com.bivgroup.core.dictionary.entity;


import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;

/**
 * Created by bush on 16.09.2016.
 */
@Entity
@Table(name = "SAD_ATTRIBUTE_CFG")
public class SadAttributeCfg implements java.io.Serializable {
    @Id
    @GeneratedValue(generator = "SadAttributeCfgTableIdGenerator")
    @GenericGenerator(name = "SadAttributeCfgTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ATTRIBUTE_CFG"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ATTRIBUTEID")
    private SadAttribute attribute;
    @Column(name = "ISVISIBLE")
    private Long isvisible;
    @Column(name = "ISENABLED")
    private Long isenabled;
    @Column(name = "ISREQUIRED")
    private Long isrequired;
    @Column(name = "DISCRIMINATORID")
    private Long discriminatorid;
    @Column(name = "BOM_ETASK_STATE_CFGID")
    private Long bomEtaskStateCfgid;

    public SadAttributeCfg() {
    }

    public SadAttributeCfg(Long id) {
        this.id = id;
    }

    public SadAttributeCfg(Long id, SadAttribute attribute, Long isvisible, Long isenabled, Long isrequired, Long discriminatorid, Long bomEtaskStateCfgid) {
        this.id = id;
        this.attribute = attribute;
        this.isvisible = isvisible;
        this.isenabled = isenabled;
        this.isrequired = isrequired;
        this.discriminatorid = discriminatorid;
        this.bomEtaskStateCfgid = bomEtaskStateCfgid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SadAttribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(SadAttribute attribute) {
        this.attribute = attribute;
    }

    public Long getIsvisible() {
        return this.isvisible;
    }

    public void setIsvisible(Long isvisible) {
        this.isvisible = isvisible;
    }

    public Long getIsenabled() {
        return this.isenabled;
    }

    public void setIsenabled(Long isenabled) {
        this.isenabled = isenabled;
    }

    public Long getIsrequired() {
        return this.isrequired;
    }

    public void setIsrequired(Long isrequired) {
        this.isrequired = isrequired;
    }

    public Long getDiscriminatorid() {
        return this.discriminatorid;
    }

    public void setDiscriminatorid(Long discriminatorid) {
        this.discriminatorid = discriminatorid;
    }

    public Long getBomEtaskStateCfgid() {
        return this.bomEtaskStateCfgid;
    }

    public void setBomEtaskStateCfgid(Long bomEtaskStateCfgid) {
        this.bomEtaskStateCfgid = bomEtaskStateCfgid;
    }

}
