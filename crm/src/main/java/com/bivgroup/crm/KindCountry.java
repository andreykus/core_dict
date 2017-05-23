package com.bivgroup.crm;
// Generated 21.03.2017 16:35:53 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * KindCountry Generated 21.03.2017 16:35:53 unknow unknow 
 */
@Entity
@Table(name="B2B_COUNTRY"
)
public class KindCountry  implements java.io.Serializable {


     private long COUNTRYID;
     private String PHONECODE;
     private String NATIVENAME;
     private long ISNOTUSE;
     private String FLAG;
     private String ENGNAME;
     private String DIGITCODE;
     private String COUNTRYNAME;
     private String BRIEFNAME;
     private String ALPHACODE3;
     private String ALPHACODE2;

    public KindCountry() {
    }

    public KindCountry(String PHONECODE, String NATIVENAME, long ISNOTUSE, String FLAG, String ENGNAME, String DIGITCODE, String COUNTRYNAME, String BRIEFNAME, String ALPHACODE3, String ALPHACODE2) {
       this.PHONECODE = PHONECODE;
       this.NATIVENAME = NATIVENAME;
       this.ISNOTUSE = ISNOTUSE;
       this.FLAG = FLAG;
       this.ENGNAME = ENGNAME;
       this.DIGITCODE = DIGITCODE;
       this.COUNTRYNAME = COUNTRYNAME;
       this.BRIEFNAME = BRIEFNAME;
       this.ALPHACODE3 = ALPHACODE3;
       this.ALPHACODE2 = ALPHACODE2;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="B2B_COUNTRY"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="COUNTRYID", nullable=false, insertable=false, updatable=false)
    public long getCOUNTRYID() {
        return this.COUNTRYID;
    }
    
    public void setCOUNTRYID(long COUNTRYID) {
        this.COUNTRYID = COUNTRYID;
    }

    
    @Column(name="PHONECODE")
    public String getPHONECODE() {
        return this.PHONECODE;
    }
    
    public void setPHONECODE(String PHONECODE) {
        this.PHONECODE = PHONECODE;
    }

    
    @Column(name="NATIVENAME")
    public String getNATIVENAME() {
        return this.NATIVENAME;
    }
    
    public void setNATIVENAME(String NATIVENAME) {
        this.NATIVENAME = NATIVENAME;
    }

    
    @Column(name="ISNOTUSE")
    public long getISNOTUSE() {
        return this.ISNOTUSE;
    }
    
    public void setISNOTUSE(long ISNOTUSE) {
        this.ISNOTUSE = ISNOTUSE;
    }

    
    @Column(name="FLAG")
    public String getFLAG() {
        return this.FLAG;
    }
    
    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    
    @Column(name="ENGNAME")
    public String getENGNAME() {
        return this.ENGNAME;
    }
    
    public void setENGNAME(String ENGNAME) {
        this.ENGNAME = ENGNAME;
    }

    
    @Column(name="DIGITCODE")
    public String getDIGITCODE() {
        return this.DIGITCODE;
    }
    
    public void setDIGITCODE(String DIGITCODE) {
        this.DIGITCODE = DIGITCODE;
    }

    
    @Column(name="COUNTRYNAME")
    public String getCOUNTRYNAME() {
        return this.COUNTRYNAME;
    }
    
    public void setCOUNTRYNAME(String COUNTRYNAME) {
        this.COUNTRYNAME = COUNTRYNAME;
    }

    
    @Column(name="BRIEFNAME")
    public String getBRIEFNAME() {
        return this.BRIEFNAME;
    }
    
    public void setBRIEFNAME(String BRIEFNAME) {
        this.BRIEFNAME = BRIEFNAME;
    }

    
    @Column(name="ALPHACODE3")
    public String getALPHACODE3() {
        return this.ALPHACODE3;
    }
    
    public void setALPHACODE3(String ALPHACODE3) {
        this.ALPHACODE3 = ALPHACODE3;
    }

    
    @Column(name="ALPHACODE2")
    public String getALPHACODE2() {
        return this.ALPHACODE2;
    }
    
    public void setALPHACODE2(String ALPHACODE2) {
        this.ALPHACODE2 = ALPHACODE2;
    }




}


