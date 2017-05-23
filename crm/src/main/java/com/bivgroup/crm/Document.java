package com.bivgroup.crm;
// Generated 21.03.2017 16:36:28 unknow unknow 


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Document Generated 21.03.2017 16:36:28 unknow unknow 
 */
@Entity
@Table(name="SD_Document"
)
public class Document  implements java.io.Serializable {


     private long ID;
     private Date DateOfExpiry;
     private String IssuerCode;
     private String Authority;
     private Date DateOfIssue;
     private String No;
     private String Series;
     private long EID;
     private KindDocument TypeID_EN;

    public Document() {
    }

    public Document(Date DateOfExpiry, String IssuerCode, String Authority, Date DateOfIssue, String No, String Series, long EID, KindDocument TypeID_EN) {
       this.DateOfExpiry = DateOfExpiry;
       this.IssuerCode = IssuerCode;
       this.Authority = Authority;
       this.DateOfIssue = DateOfIssue;
       this.No = No;
       this.Series = Series;
       this.EID = EID;
       this.TypeID_EN = TypeID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_Document"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfExpiry")
    public Date getDateOfExpiry() {
        return this.DateOfExpiry;
    }
    
    public void setDateOfExpiry(Date DateOfExpiry) {
        this.DateOfExpiry = DateOfExpiry;
    }

    
    @Column(name="IssuerCode")
    public String getIssuerCode() {
        return this.IssuerCode;
    }
    
    public void setIssuerCode(String IssuerCode) {
        this.IssuerCode = IssuerCode;
    }

    
    @Column(name="Authority")
    public String getAuthority() {
        return this.Authority;
    }
    
    public void setAuthority(String Authority) {
        this.Authority = Authority;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DateOfIssue")
    public Date getDateOfIssue() {
        return this.DateOfIssue;
    }
    
    public void setDateOfIssue(Date DateOfIssue) {
        this.DateOfIssue = DateOfIssue;
    }

    
    @Column(name="No")
    public String getNo() {
        return this.No;
    }
    
    public void setNo(String No) {
        this.No = No;
    }

    
    @Column(name="Series")
    public String getSeries() {
        return this.Series;
    }
    
    public void setSeries(String Series) {
        this.Series = Series;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="TypeID")
    public KindDocument getTypeID_EN() {
        return this.TypeID_EN;
    }
    
    public void setTypeID_EN(KindDocument TypeID_EN) {
        this.TypeID_EN = TypeID_EN;
    }




}


