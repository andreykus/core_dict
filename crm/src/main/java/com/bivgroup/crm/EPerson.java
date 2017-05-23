package com.bivgroup.crm;
// Generated 21.03.2017 16:37:44 unknow unknow 


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
 * EPerson Generated 21.03.2017 16:37:44 unknow unknow 
 */
@Entity
@Table(name="CDM_EPerson"
)
public class EPerson extends com.bivgroup.crm.Person implements java.io.Serializable {


     private String OGRNIP;
     private String INN;
     private long Sex;
     private Date PlaceOfBirth;
     private Date DateOfBirth;
     private Integer IsEmptyPatronymic;
     private String Patronymic;
     private String Name;
     private String Surname;
     private LegalFormsOfBusiness LegalFormsOfBusinessID_EN;

    public EPerson() {
    }

    public EPerson(long EID, KindStatus StateID_EN, KindCountry CountryID_EN, BankDetails BankDetailsID_EN, Client ClientID_EN, Collection<PersonDocument> Documents, Collection<PersonAddress> Addresses, Collection<PersonContact> Contacts, String OGRNIP, String INN, long Sex, Date PlaceOfBirth, Date DateOfBirth, Integer IsEmptyPatronymic, String Patronymic, String Name, String Surname, LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        super(EID, StateID_EN, CountryID_EN, BankDetailsID_EN, ClientID_EN, Documents, Addresses, Contacts);        
       this.OGRNIP = OGRNIP;
       this.INN = INN;
       this.Sex = Sex;
       this.PlaceOfBirth = PlaceOfBirth;
       this.DateOfBirth = DateOfBirth;
       this.IsEmptyPatronymic = IsEmptyPatronymic;
       this.Patronymic = Patronymic;
       this.Name = Name;
       this.Surname = Surname;
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

    @Temporal(TemporalType.DATE)
    @Column(name="PlaceOfBirth")
    public Date getPlaceOfBirth() {
        return this.PlaceOfBirth;
    }
    
    public void setPlaceOfBirth(Date PlaceOfBirth) {
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

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="LegalFormsOfBusinessID")
    public LegalFormsOfBusiness getLegalFormsOfBusinessID_EN() {
        return this.LegalFormsOfBusinessID_EN;
    }
    
    public void setLegalFormsOfBusinessID_EN(LegalFormsOfBusiness LegalFormsOfBusinessID_EN) {
        this.LegalFormsOfBusinessID_EN = LegalFormsOfBusinessID_EN;
    }




}


