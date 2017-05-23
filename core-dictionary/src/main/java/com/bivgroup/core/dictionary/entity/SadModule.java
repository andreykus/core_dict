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
 * Created by bush on 04.10.2016.
 * Модуль
 */
@Entity
@Table(name = "SAD_MODULE")
public class SadModule implements java.io.Serializable {

    @Id
    @GeneratedValue(generator = "SadModuleTableIdGenerator")
    @GenericGenerator(name = "SadModuleTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_MODULE"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //группа
    @Column(name = "GROUPID")
    private String groupId;
    //артефакт
    @Column(name = "ARTIFACTID")
    private String artifactId;
    //пакет
    @Column(name = "PACKAGING")
    private String packaging;
    //зависимые модули
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SadDependency> dependencyes = new ArrayList<SadDependency>();
    //версии
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SAD_MODULE_VERSION", joinColumns = {
            @JoinColumn(name = "MODULEID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "VERSIONID",
                    nullable = false, updatable = false)})

    private List<SadVersion> versions = new ArrayList<SadVersion>();

    public List<SadVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<SadVersion> versions) {
        this.versions = versions;
    }

    public SadModule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public List<SadDependency> getDependencyes() {
        return dependencyes;
    }

    public void setDependencyes(List<SadDependency> dependencyes) {
        this.dependencyes = dependencyes;
    }
}
