package com.bivgroup.core.dictionary.entity;

import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;

/**
 * Created by bush on 10.10.2016.
 * зависимость модуля
 * для начитки зависимых модулей используется запрос GET_ALL_DEPEND_MODULES
 */
@Entity
@Table(name = "SAD_DEPENDENCY")
@NamedNativeQueries(

        {@NamedNativeQuery(name = "GET_ALL_DEPEND_MODULES",
                query = "WITH ids ( MODULEID, DEPENDENCYID, levels ) AS ( " +
                        "   SELECT f.MODULEID , f.DEPENDENCYID , 1 AS levels " +
                        "       FROM  SAD_DEPENDENCY f " +
                        "       WHERE 1=1 " +
                        "       #chunk($IDMODULE) AND f.MODULEID = $IDMODULE #end" +
                        "   UNION ALL " +
                        "   SELECT dep.MODULEID , dep.DEPENDENCYID , levels + 1 AS levels  " +
                        "       FROM   SAD_DEPENDENCY dep " +
                        "       INNER JOIN ids ON ids.DEPENDENCYID = dep.MODULEID " +
                        "       WHERE 1=1 " +
                        "       #chunk($LEVELDEP) AND (levels + 1) < $LEVELDEP  #end ) " +
                        " SELECT DISTINCT ids.DEPENDENCYID FROM ids "
        )}
)
public class SadDependency {
    @Id
    @GeneratedValue(generator = "SadDependencyTableIdGenerator")
    @GenericGenerator(name = "SadDependencyTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "SAD_DEPENDENCY"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    //идентификатор
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    //ссылка на модуль
    @ManyToOne
    @JoinColumn(name = "MODULEID")
    private SadModule module;

    public SadModule getDependency() {
        return dependency;
    }

    public void setDependency(SadModule dependency) {
        this.dependency = dependency;
    }
    //ссылка на модуль зависимости
    @OneToOne
    @JoinColumn(name = "DEPENDENCYID")
    private SadModule dependency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SadModule getModule() {
        return module;
    }

    public void setModule(SadModule module) {
        this.module = module;
    }


}
