package com.bivgroup.crm;
// Generated 21.03.2017 16:37:37 unknow unknow 


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
 * EClient_VER Generated 21.03.2017 16:37:37 unknow unknow 
 */
@Entity
@Table(name="CDM_EClient_VER"
)
public class EClient_VER extends com.bivgroup.crm.EClient implements java.io.Serializable {


     private String OGRNIP;
     private String INN;
     private long Sex;
     private String PlaceOfBirth;
     private Date DateOfBirth;
     private Integer IsEmptyPatronymic;
     private String Patronymic;
     private String Name;
     private String Surname;
     private Date VerDate;
     private long VerNumber;
     private KindCountry CountryID_EN;
     private LegalFormsOfBusiness LegalFormsOfBusinessID_EN;

    public EClient_VER() {
    }

    public EClient_VER(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons, long VerLock, EClient_VER VerLastID_EN, String OGRNIP, String INN, long Sex, String PlaceOfBirth, Date DateOfBirth, Integer IsEmptyPatronymic, String Patronymic, String Name, String Surname, Date VerDate, long VerNumber, KindCountry CountryID_EN, LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        super(EID, Addresses, Documents, Contacts, Persons, VerLock, VerLastID_EN);        
       this.OGRNIP = OGRNIP;
       this.INN = INN;
       this.Sex = Sex;
       this.PlaceOfBirth = PlaceOfBirth;
       this.DateOfBirth = DateOfBirth;
       this.IsEmptyPatronymic = IsEmptyPatronymic;
       this.Patronymic = Patronymic;
       this.Name = Name;
       this.Surname = Surname;
       this.VerDate = VerDate;
       this.VerNumber = VerNumber;
       this.CountryID_EN = CountryID_EN;
       this.LegalFormsOfBusinessID_EN = LegalFormsOfBusinessID_EN;
    }
   

    
    @Column(name="OGRNIP")
    public String getOGRNIP() {
        return this.OGRNIP;
    }
    
    public void setOGRNIP(String OGRNIP) {
        this.OGRNIP = OGRNIP;
    }

    
    @Column(name="INN")
    public String getINN() {
        return this.INN;
    }
    
    public void setINN(String INN) {
        this.INN = INN;
    }

    
    @Column(name="Sex")
    public long getSex() {
        return this.Sex;
    }
    
    public void setSex(long Sex) {
        this.Sex = Sex;
    }

    
    @Column(name="PlaceOfBirth")
    public String getPlaceOfBirth() {
        return this.PlaceOfBirth;
    }
    
    public void setPlaceOfBirth(String PlaceOfBirth) {
        this.PlaceOfBirth = PlaceOfBirth;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfBirth")
    public Date getDateOfBirth() {
        return this.DateOfBirth;
    }
    
    public void setDateOfBirth(Date DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    
    @Column(name="IsEmptyPatronymic")
    public Integer getIsEmptyPatronymic() {
        return this.IsEmptyPatronymic;
    }
    
    public void setIsEmptyPatronymic(Integer IsEmptyPatronymic) {
        this.IsEmptyPatronymic = IsEmptyPatronymic;
    }

    
    @Column(name="Patronymic")
    public String getPatronymic() {
        return this.Patronymic;
    }
    
    public void setPatronymic(String Patronymic) {
        this.Patronymic = Patronymic;
    }

    
    @Column(name="Name")
    public String getName() {
        return this.Name;
    }
    
    public void setName(String Name) {
        this.Name = Name;
    }

    
    @Column(name="Surname")
    public String getSurname() {
        return this.Surname;
    }
    
    public void setSurname(String Surname) {
        this.Surname = Surname;
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


