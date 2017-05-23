package com.bivgroup.crm;
// Generated 21.03.2017 16:37:11 unknow unknow 


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
 * PPersonChild Generated 21.03.2017 16:37:11 unknow unknow 
 */
@Entity
@Table(name="CDM_PPerson_Child"
)
public class PPersonChild  implements java.io.Serializable {


     private long ID;
     private Date DateOfBirth;
     private String Name;
     private long EID;
     private PPerson PersonID_EN;

    public PPersonChild() {
    }

    public PPersonChild(Date DateOfBirth, String Name, long EID, PPerson PersonID_EN) {
       this.DateOfBirth = DateOfBirth;
       this.Name = Name;
       this.EID = EID;
       this.PersonID_EN = PersonID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="CDM_PPerson_Child"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfBirth")
    public Date getDateOfBirth() {
        return this.DateOfBirth;
    }
    
    public void setDateOfBirth(Date DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
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

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="PersonID")
    public PPerson getPersonID_EN() {
        return this.PersonID_EN;
    }
    
    public void setPersonID_EN(PPerson PersonID_EN) {
        this.PersonID_EN = PersonID_EN;
    }




}


