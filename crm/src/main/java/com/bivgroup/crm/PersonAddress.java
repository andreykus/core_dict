package com.bivgroup.crm;
// Generated 21.03.2017 16:36:41 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PersonAddress Generated 21.03.2017 16:36:41 unknow unknow 
 */
@Entity
@Table(name="CDM_Person_Address"
)
public class PersonAddress extends com.bivgroup.crm.Address implements java.io.Serializable {


     private Integer IsPrimary;
     private KindStatus StateID_EN;
     private Person PersonID_EN;

    public PersonAddress() {
    }

    public PersonAddress(String Address2, String Address, String Postcode, String Flat, String House, String IsEmptyStreet, String StreetCode, String Street, String VillageCode, String Village, String CityCode, String City, String DistrictCode, String District, String RegionCode, String Region, Integer FillMethod, long EID, KindCountry CountryID_EN, KindAddress TypeID_EN, Integer IsPrimary, KindStatus StateID_EN, Person PersonID_EN) {
        super(Address2, Address, Postcode, Flat, House, IsEmptyStreet, StreetCode, Street, VillageCode, Village, CityCode, City, DistrictCode, District, RegionCode, Region, FillMethod, EID, CountryID_EN, TypeID_EN);        
       this.IsPrimary = IsPrimary;
       this.StateID_EN = StateID_EN;
       this.PersonID_EN = PersonID_EN;
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
    @JoinColumn(name="PersonID")
    public Person getPersonID_EN() {
        return this.PersonID_EN;
    }
    
    public void setPersonID_EN(Person PersonID_EN) {
        this.PersonID_EN = PersonID_EN;
    }




}


