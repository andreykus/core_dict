package com.bivgroup.crm;
// Generated 21.03.2017 16:37:56 unknow unknow 


import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ContractDriver Generated 21.03.2017 16:37:56 unknow unknow 
 */
@Entity
@Table(name="PD_Contract_Driver"
)
public class ContractDriver extends com.bivgroup.crm.ContractPMember implements java.io.Serializable {


     private Date DateOfExp;

    public ContractDriver() {
    }

    public ContractDriver(long EID, KindStatus StateID_EN, KindCountry CountryID_EN, BankDetails BankDetailsID_EN, Client ClientID_EN, Collection<PersonDocument> Documents, Collection<PersonAddress> Addresses, Collection<PersonContact> Contacts, Integer IsMarried, String INN, Integer Sex, String PlaceOfBirth, Date DateOfBirth, Integer IsEmptyPatronymic, String Patronymic2, String Patronymic, String Name2, String Name, String Surname2, String Surname, Collection<PPersonChild> Childs, Date EndDate, Date StartDate, long ContractID, KindContractMember TypeID_EN, Date DateOfExp) {
        super(EID, StateID_EN, CountryID_EN, BankDetailsID_EN, ClientID_EN, Documents, Addresses, Contacts, IsMarried, INN, Sex, PlaceOfBirth, DateOfBirth, IsEmptyPatronymic, Patronymic2, Patronymic, Name2, Name, Surname2, Surname, Childs, EndDate, StartDate, ContractID, TypeID_EN);        
       this.DateOfExp = DateOfExp;
    }
   

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfExp")
    public Date getDateOfExp() {
        return this.DateOfExp;
    }
    
    public void setDateOfExp(Date DateOfExp) {
        this.DateOfExp = DateOfExp;
    }




}


