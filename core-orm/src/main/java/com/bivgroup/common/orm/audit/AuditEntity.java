package com.bivgroup.common.orm.audit;

import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by andreykus on 10.09.2016.
 * сущность сохранения аудита
 */
@Entity
@Table(name = "ORM_AUDIT")
public class AuditEntity implements java.io.Serializable {

    public AuditEntity() {
    }

    public AuditEntity(Long id) {
        this.id = id;
    }

    public AuditEntity(Long idObject, String action, String entityName, String detail) {
        this.idObject = idObject;
        this.action = action;
        this.detail = detail;
        this.entityName = entityName;
    }

    @Id
    @GeneratedValue(generator = "SadAspectTableIdGenerator")
    @GenericGenerator(name = "SadAspectTableIdGenerator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.JPA_ENTITY_NAME, value = "ORM_AUDIT"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY, value = "true"),
            @Parameter(name = SequenceStyleGenerator.CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, value = SequenceContainer.DEFAULT_SEQUENCE_SUFFIX),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = SequenceContainer.INCREMENT),
            @Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled")
    })
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "ACTION")
    private String action;

    @Column(name = "DETAIL")
    private String detail;

    @Column(name = "ID_OBJECT")
    private Long idObject;

    @Column(name = "LOGDATE")
    private Timestamp logDate;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Timestamp getLogdate() {
        return logDate;
    }

    public void setLogdate(Timestamp logdate) {
        this.logDate = logdate;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
