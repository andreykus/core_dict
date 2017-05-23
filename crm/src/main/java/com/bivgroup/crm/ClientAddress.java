package com.bivgroup.crm;
// Generated 21.03.2017 16:36:24 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ClientAddress Generated 21.03.2017 16:36:24 unknow unknow 
 */
@Entity
@Table(name="CDM_Client_Address"
)
public class ClientAddress extends com.bivgroup.crm.Address implements java.io.Serializable {


     private Integer IsPrimary;
     private KindStatus StateID_EN;
     private Client ClientID_EN;

    public ClientAddress() {
    }

    public ClientAddress(String Address2, String Address, String Postcode, String Flat, String House, String IsEmptyStreet, String StreetCode, String Street, String VillageCode, String Village, String CityCode, String City, String DistrictCode, String District, String RegionCode, String Region, Integer FillMethod, long EID, KindCountry CountryID_EN, KindAddress TypeID_EN, Integer IsPrimary, KindStatus StateID_EN, Client ClientID_EN) {
        super(Address2, Address, Postcode, Flat, House, IsEmptyStreet, StreetCode, Street, VillageCode, Village, CityCode, City, DistrictCode, District, RegionCode, Region, FillMethod, EID, CountryID_EN, TypeID_EN);        
       this.IsPrimary = IsPrimary;
       this.StateID_EN = StateID_EN;
       this.ClientID_EN = ClientID_EN;
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
    @JoinColumn(name="ClientID")
    public Client getClientID_EN() {
        return this.ClientID_EN;
    }
    
    public void setClientID_EN(Client ClientID_EN) {
        this.ClientID_EN = ClientID_EN;
    }




}


