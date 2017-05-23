package com.bivgroup.crm;
// Generated 21.03.2017 16:37:21 unknow unknow 


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
 * ShareContractIns Generated 21.03.2017 16:37:21 unknow unknow 
 */
@Entity
@Table(name="SD_Share_Contract"
)
public class ShareContractIns  implements java.io.Serializable {


     private long ID;
     private long ContractID;
     private String Tel;
     private long EID;
     private KindStatus StateID_EN;
     private KindShareRole ShareRoleID_EN;
     private Client ClientID_EN;

    public ShareContractIns() {
    }

    public ShareContractIns(long ContractID, String Tel, long EID, KindStatus StateID_EN, KindShareRole ShareRoleID_EN, Client ClientID_EN) {
       this.ContractID = ContractID;
       this.Tel = Tel;
       this.EID = EID;
       this.StateID_EN = StateID_EN;
       this.ShareRoleID_EN = ShareRoleID_EN;
       this.ClientID_EN = ClientID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_Share_Contract"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="ContractID")
    public long getContractID() {
        return this.ContractID;
    }
    
    public void setContractID(long ContractID) {
        this.ContractID = ContractID;
    }

    
    @Column(name="Tel")
    public String getTel() {
        return this.Tel;
    }
    
    public void setTel(String Tel) {
        this.Tel = Tel;
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
    @JoinColumn(name="ShareRoleID")
    public KindShareRole getShareRoleID_EN() {
        return this.ShareRoleID_EN;
    }
    
    public void setShareRoleID_EN(KindShareRole ShareRoleID_EN) {
        this.ShareRoleID_EN = ShareRoleID_EN;
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


