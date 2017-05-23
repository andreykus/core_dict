package com.bivgroup.crm;
// Generated 21.03.2017 16:37:21 unknow unknow 


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ClientProperty Generated 21.03.2017 16:37:21 unknow unknow 
 */
@Entity
@Table(name="CDM_Client_Property"
)
public class ClientProperty  implements java.io.Serializable {


     private long ID;
     private Date ValueDate;
     private Integer ValueBoolean;
     private float ValueFloat;
     private long ValueInt;
     private String ValueStr;
     private long EID;
     private KindStatus StateID_EN;
     private KindClientProperty PropertyTypeID_EN;
     private Client ClientID_EN;

    public ClientProperty() {
    }

    public ClientProperty(Date ValueDate, Integer ValueBoolean, float ValueFloat, long ValueInt, String ValueStr, long EID, KindStatus StateID_EN, KindClientProperty PropertyTypeID_EN, Client ClientID_EN) {
       this.ValueDate = ValueDate;
       this.ValueBoolean = ValueBoolean;
       this.ValueFloat = ValueFloat;
       this.ValueInt = ValueInt;
       this.ValueStr = ValueStr;
       this.EID = EID;
       this.StateID_EN = StateID_EN;
       this.PropertyTypeID_EN = PropertyTypeID_EN;
       this.ClientID_EN = ClientID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="CDM_Client_Property"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ValueDate")
    public Date getValueDate() {
        return this.ValueDate;
    }
    
    public void setValueDate(Date ValueDate) {
        this.ValueDate = ValueDate;
    }

    
    @Column(name="ValueBoolean")
    public Integer getValueBoolean() {
        return this.ValueBoolean;
    }
    
    public void setValueBoolean(Integer ValueBoolean) {
        this.ValueBoolean = ValueBoolean;
    }

    
    @Column(name="ValueFloat")
    public float getValueFloat() {
        return this.ValueFloat;
    }
    
    public void setValueFloat(float ValueFloat) {
        this.ValueFloat = ValueFloat;
    }

    
    @Column(name="ValueInt")
    public long getValueInt() {
        return this.ValueInt;
    }
    
    public void setValueInt(long ValueInt) {
        this.ValueInt = ValueInt;
    }

    
    @Column(name="ValueStr")
    public String getValueStr() {
        return this.ValueStr;
    }
    
    public void setValueStr(String ValueStr) {
        this.ValueStr = ValueStr;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="StateID")
    public KindStatus getStateID_EN() {
        return this.StateID_EN;
    }
    
    public void setStateID_EN(KindStatus StateID_EN) {
        this.StateID_EN = StateID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="PropertyTypeID")
    public KindClientProperty getPropertyTypeID_EN() {
        return this.PropertyTypeID_EN;
    }
    
    public void setPropertyTypeID_EN(KindClientProperty PropertyTypeID_EN) {
        this.PropertyTypeID_EN = PropertyTypeID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID")
    public Client getClientID_EN() {
        return this.ClientID_EN;
    }
    
    public void setClientID_EN(Client ClientID_EN) {
        this.ClientID_EN = ClientID_EN;
    }




}


