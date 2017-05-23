package com.bivgroup.crm;
// Generated 21.03.2017 16:36:26 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * KindStatus Generated 21.03.2017 16:36:26 unknow unknow 
 */
@Entity
@Table(name="HB_KindStatus"
)
public class KindStatus  implements java.io.Serializable {


     private long ID;
     private String Sysname;
     private String Name;

    public KindStatus() {
    }

    public KindStatus(String Sysname, String Name) {
       this.Sysname = Sysname;
       this.Name = Name;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="HB_KindStatus"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="Sysname")
    public String getSysname() {
        return this.Sysname;
    }
    
    public void setSysname(String Sysname) {
        this.Sysname = Sysname;
    }

    
    @Column(name="Name")
    public String getName() {
        return this.Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }




}


