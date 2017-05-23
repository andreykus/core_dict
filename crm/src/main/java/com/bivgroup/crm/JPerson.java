package com.bivgroup.crm;
// Generated 21.03.2017 16:37:50 unknow unknow 


import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPerson Generated 21.03.2017 16:37:50 unknow unknow 
 */
@Entity
@Table(name="CDM_JPerson"
)
public class JPerson extends com.bivgroup.crm.Person implements java.io.Serializable {


     private String OGRN;
     private String KPP;
     private String INN;
     private String Name2;
     private String Name;
     private PPerson FirstPersonID_EN;
     private LegalFormsOfBusiness LegalFormsOfBusinessID_EN;

    public JPerson() {
    }

    public JPerson(long EID, KindStatus StateID_EN, KindCountry CountryID_EN, BankDetails BankDetailsID_EN, Client ClientID_EN, Collection<PersonDocument> Documents, Collection<PersonAddress> Addresses, Collection<PersonContact> Contacts, String OGRN, String KPP, String INN, String Name2, String Name, PPerson FirstPersonID_EN, LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        super(EID, StateID_EN, CountryID_EN, BankDetailsID_EN, ClientID_EN, Documents, Addresses, Contacts);        
       this.OGRN = OGRN;
       this.KPP = KPP;
       this.INN = INN;
       this.Name2 = Name2;
       this.Name = Name;
       this.FirstPersonID_EN = FirstPersonID_EN;
       this.LegalFormsOfBusinessID_EN = LegalFormsOfBusinessID_EN;
    }
   

    
    @Column(name="OGRN")
    public String getOGRN() {
        return this.OGRN;
    }
    
    public void setOGRN(String OGRN) {
        this.OGRN = OGRN;
    }

    
    @Column(name="KPP")
    public String getKPP() {
        return this.KPP;
    }
    
    public void setKPP(String KPP) {
        this.KPP = KPP;
    }

    
    @Column(name="INN")
    public String getINN() {
        return this.INN;
    }
    
    public void setINN(String INN) {
        this.INN = INN;
    }

    
    @Column(name="Name2")
    public String getName2() {
        return this.Name2;
    }
    
    public void setName2(String Name2) {
        this.Name2 = Name2;
    }

    
    @Column(name="Name")
    public String getName() {
        return this.Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="FirstPersonID")
    public PPerson getFirstPersonID_EN() {
        return this.FirstPersonID_EN;
    }
    
    public void setFirstPersonID_EN(PPerson FirstPersonID_EN) {
        this.FirstPersonID_EN = FirstPersonID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="LegalFormsOfBusinessID")
    public LegalFormsOfBusiness getLegalFormsOfBusinessID_EN() {
        return this.LegalFormsOfBusinessID_EN;
    }
    
    public void setLegalFormsOfBusinessID_EN(LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        this.LegalFormsOfBusinessID_EN = LegalFormsOfBusinessID_EN;
    }




}


