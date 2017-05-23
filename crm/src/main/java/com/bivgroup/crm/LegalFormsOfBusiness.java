package com.bivgroup.crm;
// Generated 21.03.2017 16:36:04 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * LegalFormsOfBusiness Generated 21.03.2017 16:36:04 unknow unknow 
 */
@Entity
@Table(name="HB_LegalFormsOfBusiness"
)
public class LegalFormsOfBusiness  implements java.io.Serializable {


     private long ID;
     private String Sysname;
     private Integer PersonType;
     private String Name;
     private long EID;

    public LegalFormsOfBusiness() {
    }

    public LegalFormsOfBusiness(String Sysname, Integer PersonType, String Name, long EID) {
       this.Sysname = Sysname;
       this.PersonType = PersonType;
       this.Name = Name;
       this.EID = EID;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="HB_LegalFormsOfBusiness"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
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

    
    @Column(name="PersonType")
    public Integer getPersonType() {
        return this.PersonType;
    }
    
    public void setPersonType(Integer PersonType) {
        this.PersonType = PersonType;
    }

    
    @Column(name="Name")
    public String getName() {
        return this.Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }




}


