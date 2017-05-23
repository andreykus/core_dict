package com.bivgroup.crm;
// Generated 21.03.2017 16:37:53 unknow unknow 


import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ContractPMember Generated 21.03.2017 16:37:53 unknow unknow 
 */
@Entity
@Table(name="PD_Contract_PMember"
)
public class ContractPMember extends com.bivgroup.crm.PPerson implements java.io.Serializable {


     private Date EndDate;
     private Date StartDate;
     private long ContractID;
     private KindContractMember TypeID_EN;

    public ContractPMember() {
    }

    public ContractPMember(long EID, KindStatus StateID_EN, KindCountry CountryID_EN, BankDetails BankDetailsID_EN, Client ClientID_EN, Collection<PersonDocument> Documents, Collection<PersonAddress> Addresses, Collection<PersonContact> Contacts, Integer IsMarried, String INN, Integer Sex, String PlaceOfBirth, Date DateOfBirth, Integer IsEmptyPatronymic, String Patronymic2, String Patronymic, String Name2, String Name, String Surname2, String Surname, Collection<PPersonChild> Childs, Date EndDate, Date StartDate, long ContractID, KindContractMember TypeID_EN) {
        super(EID, StateID_EN, CountryID_EN, BankDetailsID_EN, ClientID_EN, Documents, Addresses, Contacts, IsMarried, INN, Sex, PlaceOfBirth, DateOfBirth, IsEmptyPatronymic, Patronymic2, Patronymic, Name2, Name, Surname2, Surname, Childs);        
       this.EndDate = EndDate;
       this.StartDate = StartDate;
       this.ContractID = ContractID;
       this.TypeID_EN = TypeID_EN;
    }
   

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EndDate")
    public Date getEndDate() {
        return this.EndDate;
    }
    
    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="StartDate")
    public Date getStartDate() {
        return this.StartDate;
    }
    
    public void setStartDate(Date StartDate) {
        this.StartDate = StartDate;
    }

    
    @Column(name="ContractID")
    public long getContractID() {
        return this.ContractID;
    }
    
    public void setContractID(long ContractID) {
        this.ContractID = ContractID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="TypeID")
    public KindContractMember getTypeID_EN() {
        return this.TypeID_EN;
    }
    
    public void setTypeID_EN(KindContractMember TypeID_EN) {
        this.TypeID_EN = TypeID_EN;
    }




}


