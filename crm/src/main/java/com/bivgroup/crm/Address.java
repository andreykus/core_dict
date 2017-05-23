package com.bivgroup.crm;
// Generated 21.03.2017 16:36:12 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Address Generated 21.03.2017 16:36:12 unknow unknow 
 */
@Entity
@Table(name="SD_Address"
)
public class Address  implements java.io.Serializable {


     private long ID;
     private String Address2;
     private String Address;
     private String Postcode;
     private String Flat;
     private String House;
     private String IsEmptyStreet;
     private String StreetCode;
     private String Street;
     private String VillageCode;
     private String Village;
     private String CityCode;
     private String City;
     private String DistrictCode;
     private String District;
     private String RegionCode;
     private String Region;
     private Integer FillMethod;
     private long EID;
     private KindCountry CountryID_EN;
     private KindAddress TypeID_EN;

    public Address() {
    }

    public Address(String Address2, String Address, String Postcode, String Flat, String House, String IsEmptyStreet, String StreetCode, String Street, String VillageCode, String Village, String CityCode, String City, String DistrictCode, String District, String RegionCode, String Region, Integer FillMethod, long EID, KindCountry CountryID_EN, KindAddress TypeID_EN) {
       this.Address2 = Address2;
       this.Address = Address;
       this.Postcode = Postcode;
       this.Flat = Flat;
       this.House = House;
       this.IsEmptyStreet = IsEmptyStreet;
       this.StreetCode = StreetCode;
       this.Street = Street;
       this.VillageCode = VillageCode;
       this.Village = Village;
       this.CityCode = CityCode;
       this.City = City;
       this.DistrictCode = DistrictCode;
       this.District = District;
       this.RegionCode = RegionCode;
       this.Region = Region;
       this.FillMethod = FillMethod;
       this.EID = EID;
       this.CountryID_EN = CountryID_EN;
       this.TypeID_EN = TypeID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_Address"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="Address2")
    public String getAddress2() {
        return this.Address2;
    }
    
    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    
    @Column(name="Address")
    public String getAddress() {
        return this.Address;
    }
    
    public void setAddress(String Address) {
        this.Address = Address;
    }

    
    @Column(name="Postcode")
    public String getPostcode() {
        return this.Postcode;
    }
    
    public void setPostcode(String Postcode) {
        this.Postcode = Postcode;
    }

    
    @Column(name="Flat")
    public String getFlat() {
        return this.Flat;
    }
    
    public void setFlat(String Flat) {
        this.Flat = Flat;
    }

    
    @Column(name="House")
    public String getHouse() {
        return this.House;
    }
    
    public void setHouse(String House) {
        this.House = House;
    }

    
    @Column(name="IsEmptyStreet")
    public String getIsEmptyStreet() {
        return this.IsEmptyStreet;
    }
    
    public void setIsEmptyStreet(String IsEmptyStreet) {
        this.IsEmptyStreet = IsEmptyStreet;
    }

    
    @Column(name="StreetCode")
    public String getStreetCode() {
        return this.StreetCode;
    }
    
    public void setStreetCode(String StreetCode) {
        this.StreetCode = StreetCode;
    }

    
    @Column(name="Street")
    public String getStreet() {
        return this.Street;
    }
    
    public void setStreet(String Street) {
        this.Street = Street;
    }

    
    @Column(name="VillageCode")
    public String getVillageCode() {
        return this.VillageCode;
    }
    
    public void setVillageCode(String VillageCode) {
        this.VillageCode = VillageCode;
    }

    
    @Column(name="Village")
    public String getVillage() {
        return this.Village;
    }
    
    public void setVillage(String Village) {
        this.Village = Village;
    }

    
    @Column(name="CityCode")
    public String getCityCode() {
        return this.CityCode;
    }
    
    public void setCityCode(String CityCode) {
        this.CityCode = CityCode;
    }

    
    @Column(name="City")
    public String getCity() {
        return this.City;
    }
    
    public void setCity(String City) {
        this.City = City;
    }

    
    @Column(name="DistrictCode")
    public String getDistrictCode() {
        return this.DistrictCode;
    }
    
    public void setDistrictCode(String DistrictCode) {
        this.DistrictCode = DistrictCode;
    }

    
    @Column(name="District")
    public String getDistrict() {
        return this.District;
    }
    
    public void setDistrict(String District) {
        this.District = District;
    }

    
    @Column(name="RegionCode")
    public String getRegionCode() {
        return this.RegionCode;
    }
    
    public void setRegionCode(String RegionCode) {
        this.RegionCode = RegionCode;
    }

    
    @Column(name="Region")
    public String getRegion() {
        return this.Region;
    }
    
    public void setRegion(String Region) {
        this.Region = Region;
    }

    
    @Column(name="FillMethod")
    public Integer getFillMethod() {
        return this.FillMethod;
    }
    
    public void setFillMethod(Integer FillMethod) {
        this.FillMethod = FillMethod;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
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
    @JoinColumn(name="TypeID")
    public KindAddress getTypeID_EN() {
        return this.TypeID_EN;
    }
    
    public void setTypeID_EN(KindAddress TypeID_EN) {
        this.TypeID_EN = TypeID_EN;
    }




}


