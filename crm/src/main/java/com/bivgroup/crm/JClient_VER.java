package com.bivgroup.crm;
// Generated 21.03.2017 16:37:33 unknow unknow 


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
 * JClient_VER Generated 21.03.2017 16:37:33 unknow unknow 
 */
@Entity
@Table(name="CDM_JClient_VER"
)
public class JClient_VER extends com.bivgroup.crm.JClient implements java.io.Serializable {


     private String OGRN;
     private String KPP;
     private String INN;
     private String Name2;
     private String Name;
     private Date VerDate;
     private long VerNumber;
     private PPerson FirstPersonID_EN;
     private KindCountry CountryID_EN;
     private LegalFormsOfBusiness LegalFormsOfBusinessID_EN;

    public JClient_VER() {
    }

    public JClient_VER(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons, long VerLock, JClient_VER VerLastID_EN, String OGRN, String KPP, String INN, String Name2, String Name, Date VerDate, long VerNumber, PPerson FirstPersonID_EN, KindCountry CountryID_EN, LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        super(EID, Addresses, Documents, Contacts, Persons, VerLock, VerLastID_EN);        
       this.OGRN = OGRN;
       this.KPP = KPP;
       this.INN = INN;
       this.Name2 = Name2;
       this.Name = Name;
       this.VerDate = VerDate;
       this.VerNumber = VerNumber;
       this.FirstPersonID_EN = FirstPersonID_EN;
       this.CountryID_EN = CountryID_EN;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="VerDate")
    public Date getVerDate() {
        return this.VerDate;
    }
    
    public void setVerDate(Date VerDate) {
        this.VerDate = VerDate;
    }

    
    @Column(name="VerNumber")
    public long getVerNumber() {
        return this.VerNumber;
    }
    
    public void setVerNumber(long VerNumber) {
        this.VerNumber = VerNumber;
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
    @JoinColumn(name="CountryID")
    public KindCountry getCountryID_EN() {
        return this.CountryID_EN;
    }
    
    public void setCountryID_EN(KindCountry CountryID_EN) {
        this.CountryID_EN = CountryID_EN;
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


