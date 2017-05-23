package com.bivgroup.crm;
// Generated 21.03.2017 16:37:23 unknow unknow 


import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PClient Generated 21.03.2017 16:37:23 unknow unknow 
 */
@Entity
@Table(name="CDM_PClient"
)
public class PClient extends com.bivgroup.crm.Client implements java.io.Serializable {


     private long VerLock;
     private PClient_VER VerLastID_EN;

    public PClient() {
    }

    public PClient(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons, long VerLock, PClient_VER VerLastID_EN) {
        super(EID, Addresses, Documents, Contacts, Persons);        
       this.VerLock = VerLock;
       this.VerLastID_EN = VerLastID_EN;
    }
   

    
    @Column(name="VerLock")
    public long getVerLock() {
        return this.VerLock;
    }
    
    public void setVerLock(long VerLock) {
        this.VerLock = VerLock;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="VerLastID")
    public PClient_VER getVerLastID_EN() {
        return this.VerLastID_EN;
    }
    
    public void setVerLastID_EN(PClient_VER VerLastID_EN) {
        this.VerLastID_EN = VerLastID_EN;
    }




}


