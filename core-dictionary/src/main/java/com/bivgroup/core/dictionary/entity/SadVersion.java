package com.bivgroup.core.dictionary.entity;

import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bush on 17.10.2016.
 * Версии
 */
@Entity
@Table(name = "SAD_VERSION")
public class SadVersion implements java.io.Serializable {

    @Id
    @GeneratedValue(generator = "SadVersionTableIdGenerator")
    @GenericGenerator(name = "SadVersionTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_VERSION"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //версия
    @Column(name = "VERSION")
    private String version;

    public List<SadModule> getModules() {
        return modules;
    }

    public void setModules(List<SadModule> modules) {
        this.modules = modules;
    }
    //модули
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "versions")
    private List<SadModule> modules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
