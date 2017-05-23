package com.bivgroup.crm;
// Generated 21.03.2017 16:37:23 unknow unknow 


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
 * PClient_VER Generated 21.03.2017 16:37:23 unknow unknow 
 */
@Entity
@Table(name="CDM_PClient_VER"
)
public class PClient_VER extends com.bivgroup.crm.PClient implements java.io.Serializable {


     private Integer IsMarried;
     private String INN;
     private Integer Sex;
     private String PlaceOfBirth;
     private Date DateOfBirth;
     private Integer IsEmptyPatronymic;
     private String Patronymic2;
     private String Patronymic;
     private String Name2;
     private String Name;
     private String Surname2;
     private String Surname;
     private Date VerDate;
     private long VerNumber;
     private KindCountry CountryID_EN;

    public PClient_VER() {
    }

    public PClient_VER(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons, long VerLock, PClient_VER VerLastID_EN, Integer IsMarried, String INN, Integer Sex, String PlaceOfBirth, Date DateOfBirth, Integer IsEmptyPatronymic, String Patronymic2, String Patronymic, String Name2, String Name, String Surname2, String Surname, Date VerDate, long VerNumber, KindCountry CountryID_EN) {
        super(EID, Addresses, Documents, Contacts, Persons, VerLock, VerLastID_EN);        
       this.IsMarried = IsMarried;
       this.INN = INN;
       this.Sex = Sex;
       this.PlaceOfBirth = PlaceOfBirth;
       this.DateOfBirth = DateOfBirth;
       this.IsEmptyPatronymic = IsEmptyPatronymic;
       this.Patronymic2 = Patronymic2;
       this.Patronymic = Patronymic;
       this.Name2 = Name2;
       this.Name = Name;
       this.Surname2 = Surname2;
       this.Surname = Surname;
       this.VerDate = VerDate;
       this.VerNumber = VerNumber;
       this.CountryID_EN = CountryID_EN;
    }
   

    
    @Column(name="IsMarried")
    public Integer getIsMarried() {
        return this.IsMarried;
    }
    
    public void setIsMarried(Integer IsMarried) {
        this.IsMarried = IsMarried;
    }

    
    @Column(name="INN")
    public String getINN() {
        return this.INN;
    }
    
    public void setINN(String INN) {
        this.INN = INN;
    }

    
    @Column(name="Sex")
    public Integer getSex() {
        return this.Sex;
    }
    
    public void setSex(Integer Sex) {
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

    
    @Column(name="Patronymic2")
    public String getPatronymic2() {
        return this.Patronymic2;
    }
    
    public void setPatronymic2(String Patronymic2) {
        this.Patronymic2 = Patronymic2;
    }

    
    @Column(name="Patronymic")
    public String getPatronymic() {
        return this.Patronymic;
    }
    
    public void setPatronymic(String Patronymic) {
        this.Patronymic = Patronymic;
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

    
    @Column(name="Surname2")
    public String getSurname2() {
        return this.Surname2;
    }
    
    public void setSurname2(String Surname2) {
        this.Surname2 = Surname2;
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




}


