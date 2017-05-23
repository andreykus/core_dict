package com.bivgroup.crm;
// Generated 21.03.2017 16:36:44 unknow unknow 


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
 * ClientProfileToken Generated 21.03.2017 16:36:44 unknow unknow 
 */
@Entity
@Table(name="SD_ClientProfile_Token"
)
public class ClientProfileToken  implements java.io.Serializable {


     private long ID;
     private Integer IsNotUsed;
     private String IDDevice;
     private String Hash;
     private Date CreateDate;
     private Date DateOfExpiry;
     private long EID;
     private ClientProfile ClientProfileID_EN;

    public ClientProfileToken() {
    }

    public ClientProfileToken(Integer IsNotUsed, String IDDevice, String Hash, Date CreateDate, Date DateOfExpiry, long EID, ClientProfile ClientProfileID_EN) {
       this.IsNotUsed = IsNotUsed;
       this.IDDevice = IDDevice;
       this.Hash = Hash;
       this.CreateDate = CreateDate;
       this.DateOfExpiry = DateOfExpiry;
       this.EID = EID;
       this.ClientProfileID_EN = ClientProfileID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_ClientProfile_Token"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="IsNotUsed")
    public Integer getIsNotUsed() {
        return this.IsNotUsed;
    }
    
    public void setIsNotUsed(Integer IsNotUsed) {
        this.IsNotUsed = IsNotUsed;
    }

    
    @Column(name="IDDevice")
    public String getIDDevice() {
        return this.IDDevice;
    }
    
    public void setIDDevice(String IDDevice) {
        this.IDDevice = IDDevice;
    }

    
    @Column(name="Hash")
    public String getHash() {
        return this.Hash;
    }
    
    public void setHash(String Hash) {
        this.Hash = Hash;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CreateDate")
    public Date getCreateDate() {
        return this.CreateDate;
    }
    
    public void setCreateDate(Date CreateDate) {
        this.CreateDate = CreateDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfExpiry")
    public Date getDateOfExpiry() {
        return this.DateOfExpiry;
    }
    
    public void setDateOfExpiry(Date DateOfExpiry) {
        this.DateOfExpiry = DateOfExpiry;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ClientProfileID")
    public ClientProfile getClientProfileID_EN() {
        return this.ClientProfileID_EN;
    }
    
    public void setClientProfileID_EN(ClientProfile ClientProfileID_EN) {
        this.ClientProfileID_EN = ClientProfileID_EN;
    }




}


