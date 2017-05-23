package com.bivgroup.crm;
// Generated 21.03.2017 16:36:34 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Contact Generated 21.03.2017 16:36:34 unknow unknow 
 */
@Entity
@Table(name="SD_Contact"
)
public class Contact  implements java.io.Serializable {


     private long ID;
     private String Value;
     private long EID;
     private KindContact TypeID_EN;

    public Contact() {
    }

    public Contact(String Value, long EID, KindContact TypeID_EN) {
       this.Value = Value;
       this.EID = EID;
       this.TypeID_EN = TypeID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_Contact"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="Value")
    public String getValue() {
        return this.Value;
    }
    
    public void setValue(String Value) {
        this.Value = Value;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="TypeID")
    public KindContact getTypeID_EN() {
        return this.TypeID_EN;
    }
    
    public void setTypeID_EN(KindContact TypeID_EN) {
        this.TypeID_EN = TypeID_EN;
    }




}


