package com.bivgroup.crm;
// Generated 21.03.2017 16:36:32 unknow unknow 


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ClientDocument Generated 21.03.2017 16:36:32 unknow unknow 
 */
@Entity
@Table(name="CDM_Client_Document"
)
public class ClientDocument extends com.bivgroup.crm.Document implements java.io.Serializable {


     private Integer IsPrimary;
     private KindStatus StateID_EN;
     private Client ClientID_EN;

    public ClientDocument() {
    }

    public ClientDocument(Date DateOfExpiry, String IssuerCode, String Authority, Date DateOfIssue, String No, String Series, long EID, KindDocument TypeID_EN, Integer IsPrimary, KindStatus StateID_EN, Client ClientID_EN) {
        super(DateOfExpiry, IssuerCode, Authority, DateOfIssue, No, Series, EID, TypeID_EN);        
       this.IsPrimary = IsPrimary;
       this.StateID_EN = StateID_EN;
       this.ClientID_EN = ClientID_EN;
    }
   

    
    @Column(name="IsPrimary")
    public Integer getIsPrimary() {
        return this.IsPrimary;
    }
    
    public void setIsPrimary(Integer IsPrimary) {
        this.IsPrimary = IsPrimary;
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
    @JoinColumn(name="ClientID")
    public Client getClientID_EN() {
        return this.ClientID_EN;
    }
    
    public void setClientID_EN(Client ClientID_EN) {
        this.ClientID_EN = ClientID_EN;
    }




}


