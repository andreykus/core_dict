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
@Table(name = "SAD_ATTRIBUTE_STRUCTURE")
public class SadAttributeStructure implements java.io.Serializable {

    @Id
    @GeneratedValue(generator = "SadAttributeStructureTableIdGenerator")
    @GenericGenerator(name = "SadAttributeStructureTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_ATTRIBUTE_STRUCTURE"),
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
    @Column(name = "DISCRIMINATORID")
    private Long discriminatorid;
    @Column(name = "TASKSTATECONFIGID")
    private Long taskstateconfigid;

    public SadAttributeStructure() {
    }

    public SadAttributeStructure(Long id) {
        this.id = id;
    }

    public SadAttributeStructure(Long id, SadAttribute attribute, Long discriminatorid, Long taskstateconfigid) {
        this.id = id;
        this.attribute = attribute;
        this.discriminatorid = discriminatorid;
        this.taskstateconfigid = taskstateconfigid;
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

    public Long getDiscriminatorid() {
        return this.discriminatorid;
    }

    public void setDiscriminatorid(Long discriminatorid) {
        this.discriminatorid = discriminatorid;
    }

    public Long getTaskstateconfigid() {
        return this.taskstateconfigid;
    }

    public void setTaskstateconfigid(Long taskstateconfigid) {
        this.taskstateconfigid = taskstateconfigid;
    }

}
