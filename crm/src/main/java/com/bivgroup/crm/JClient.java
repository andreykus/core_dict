package com.bivgroup.crm;
// Generated 21.03.2017 16:37:33 unknow unknow 


import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JClient Generated 21.03.2017 16:37:33 unknow unknow 
 */
@Entity
@Table(name="CDM_JClient"
)
public class JClient extends com.bivgroup.crm.Client implements java.io.Serializable {


     private long VerLock;
     private JClient_VER VerLastID_EN;

    public JClient() {
    }

    public JClient(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons, long VerLock, JClient_VER VerLastID_EN) {
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
    public JClient_VER getVerLastID_EN() {
        return this.VerLastID_EN;
    }
    
    public void setVerLastID_EN(JClient_VER VerLastID_EN) {
        this.VerLastID_EN = VerLastID_EN;
    }




}


