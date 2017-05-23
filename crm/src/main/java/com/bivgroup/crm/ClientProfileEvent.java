package com.bivgroup.crm;
// Generated 21.03.2017 16:36:48 unknow unknow 


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
 * ClientProfileEvent Generated 21.03.2017 16:36:48 unknow unknow 
 */
@Entity
@Table(name="SD_ClientProfile_Event"
)
public class ClientProfileEvent  implements java.io.Serializable {


     private long ID;
     private long RefObjectID;
     private Integer ParamDate10;
     private Integer ParamDate9;
     private Integer ParamDate8;
     private Integer ParamDate7;
     private Integer ParamDate6;
     private Integer ParamDate5;
     private Integer ParamDate4;
     private Integer ParamDate3;
     private Integer ParamDate2;
     private Integer ParamDate1;
     private Integer ParamBoolean10;
     private Integer ParamBoolean9;
     private Integer ParamBoolean8;
     private Integer ParamBoolean7;
     private Integer ParamBoolean6;
     private Integer ParamBoolean5;
     private Integer ParamBoolean4;
     private Integer ParamBoolean3;
     private Integer ParamBoolean2;
     private Integer ParamBoolean1;
     private float ParamFloat10;
     private float ParamFloat9;
     private float ParamFloat8;
     private float ParamFloat7;
     private float ParamFloat6;
     private float ParamFloat5;
     private float ParamFloat4;
     private float ParamFloat3;
     private float ParamFloat2;
     private float ParamFloat1;
     private long ParamInt10;
     private long ParamInt9;
     private long ParamInt8;
     private long ParamInt7;
     private long ParamInt6;
     private long ParamInt5;
     private long ParamInt4;
     private long ParamInt3;
     private long ParamInt2;
     private long ParamInt1;
     private String ParamStr10;
     private String ParamStr9;
     private String ParamStr8;
     private String ParamStr7;
     private String ParamStr6;
     private String ParamStr5;
     private String ParamStr4;
     private String ParamStr3;
     private String ParamStr2;
     private String ParamStr1;
     private String ErrorTest;
     private String ErrorCode;
     private String Signature;
     private long ModeApp;
     private Date EventDate;
     private long EID;
     private KindEventClientProfile EventTypeID_EN;
     private ClientProfile ClientProfileID_EN;
     private ClientProfileToken TokenID_EN;

    public ClientProfileEvent() {
    }

    public ClientProfileEvent(long RefObjectID, Integer ParamDate10, Integer ParamDate9, Integer ParamDate8, Integer ParamDate7, Integer ParamDate6, Integer ParamDate5, Integer ParamDate4, Integer ParamDate3, Integer ParamDate2, Integer ParamDate1, Integer ParamBoolean10, Integer ParamBoolean9, Integer ParamBoolean8, Integer ParamBoolean7, Integer ParamBoolean6, Integer ParamBoolean5, Integer ParamBoolean4, Integer ParamBoolean3, Integer ParamBoolean2, Integer ParamBoolean1, float ParamFloat10, float ParamFloat9, float ParamFloat8, float ParamFloat7, float ParamFloat6, float ParamFloat5, float ParamFloat4, float ParamFloat3, float ParamFloat2, float ParamFloat1, long ParamInt10, long ParamInt9, long ParamInt8, long ParamInt7, long ParamInt6, long ParamInt5, long ParamInt4, long ParamInt3, long ParamInt2, long ParamInt1, String ParamStr10, String ParamStr9, String ParamStr8, String ParamStr7, String ParamStr6, String ParamStr5, String ParamStr4, String ParamStr3, String ParamStr2, String ParamStr1, String ErrorTest, String ErrorCode, String Signature, long ModeApp, Date EventDate, long EID, KindEventClientProfile EventTypeID_EN, ClientProfile ClientProfileID_EN, ClientProfileToken TokenID_EN) {
       this.RefObjectID = RefObjectID;
       this.ParamDate10 = ParamDate10;
       this.ParamDate9 = ParamDate9;
       this.ParamDate8 = ParamDate8;
       this.ParamDate7 = ParamDate7;
       this.ParamDate6 = ParamDate6;
       this.ParamDate5 = ParamDate5;
       this.ParamDate4 = ParamDate4;
       this.ParamDate3 = ParamDate3;
       this.ParamDate2 = ParamDate2;
       this.ParamDate1 = ParamDate1;
       this.ParamBoolean10 = ParamBoolean10;
       this.ParamBoolean9 = ParamBoolean9;
       this.ParamBoolean8 = ParamBoolean8;
       this.ParamBoolean7 = ParamBoolean7;
       this.ParamBoolean6 = ParamBoolean6;
       this.ParamBoolean5 = ParamBoolean5;
       this.ParamBoolean4 = ParamBoolean4;
       this.ParamBoolean3 = ParamBoolean3;
       this.ParamBoolean2 = ParamBoolean2;
       this.ParamBoolean1 = ParamBoolean1;
       this.ParamFloat10 = ParamFloat10;
       this.ParamFloat9 = ParamFloat9;
       this.ParamFloat8 = ParamFloat8;
       this.ParamFloat7 = ParamFloat7;
       this.ParamFloat6 = ParamFloat6;
       this.ParamFloat5 = ParamFloat5;
       this.ParamFloat4 = ParamFloat4;
       this.ParamFloat3 = ParamFloat3;
       this.ParamFloat2 = ParamFloat2;
       this.ParamFloat1 = ParamFloat1;
       this.ParamInt10 = ParamInt10;
       this.ParamInt9 = ParamInt9;
       this.ParamInt8 = ParamInt8;
       this.ParamInt7 = ParamInt7;
       this.ParamInt6 = ParamInt6;
       this.ParamInt5 = ParamInt5;
       this.ParamInt4 = ParamInt4;
       this.ParamInt3 = ParamInt3;
       this.ParamInt2 = ParamInt2;
       this.ParamInt1 = ParamInt1;
       this.ParamStr10 = ParamStr10;
       this.ParamStr9 = ParamStr9;
       this.ParamStr8 = ParamStr8;
       this.ParamStr7 = ParamStr7;
       this.ParamStr6 = ParamStr6;
       this.ParamStr5 = ParamStr5;
       this.ParamStr4 = ParamStr4;
       this.ParamStr3 = ParamStr3;
       this.ParamStr2 = ParamStr2;
       this.ParamStr1 = ParamStr1;
       this.ErrorTest = ErrorTest;
       this.ErrorCode = ErrorCode;
       this.Signature = Signature;
       this.ModeApp = ModeApp;
       this.EventDate = EventDate;
       this.EID = EID;
       this.EventTypeID_EN = EventTypeID_EN;
       this.ClientProfileID_EN = ClientProfileID_EN;
       this.TokenID_EN = TokenID_EN;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_ClientProfile_Event"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="RefObjectID")
    public long getRefObjectID() {
        return this.RefObjectID;
    }
    
    public void setRefObjectID(long RefObjectID) {
        this.RefObjectID = RefObjectID;
    }

    
    @Column(name="ParamDate10")
    public Integer getParamDate10() {
        return this.ParamDate10;
    }
    
    public void setParamDate10(Integer ParamDate10) {
        this.ParamDate10 = ParamDate10;
    }

    
    @Column(name="ParamDate9")
    public Integer getParamDate9() {
        return this.ParamDate9;
    }
    
    public void setParamDate9(Integer ParamDate9) {
        this.ParamDate9 = ParamDate9;
    }

    
    @Column(name="ParamDate8")
    public Integer getParamDate8() {
        return this.ParamDate8;
    }
    
    public void setParamDate8(Integer ParamDate8) {
        this.ParamDate8 = ParamDate8;
    }

    
    @Column(name="ParamDate7")
    public Integer getParamDate7() {
        return this.ParamDate7;
    }
    
    public void setParamDate7(Integer ParamDate7) {
        this.ParamDate7 = ParamDate7;
    }

    
    @Column(name="ParamDate6")
    public Integer getParamDate6() {
        return this.ParamDate6;
    }
    
    public void setParamDate6(Integer ParamDate6) {
        this.ParamDate6 = ParamDate6;
    }

    
    @Column(name="ParamDate5")
    public Integer getParamDate5() {
        return this.ParamDate5;
    }
    
    public void setParamDate5(Integer ParamDate5) {
        this.ParamDate5 = ParamDate5;
    }

    
    @Column(name="ParamDate4")
    public Integer getParamDate4() {
        return this.ParamDate4;
    }
    
    public void setParamDate4(Integer ParamDate4) {
        this.ParamDate4 = ParamDate4;
    }

    
    @Column(name="ParamDate3")
    public Integer getParamDate3() {
        return this.ParamDate3;
    }
    
    public void setParamDate3(Integer ParamDate3) {
        this.ParamDate3 = ParamDate3;
    }

    
    @Column(name="ParamDate2")
    public Integer getParamDate2() {
        return this.ParamDate2;
    }
    
    public void setParamDate2(Integer ParamDate2) {
        this.ParamDate2 = ParamDate2;
    }

    
    @Column(name="ParamDate1")
    public Integer getParamDate1() {
        return this.ParamDate1;
    }
    
    public void setParamDate1(Integer ParamDate1) {
        this.ParamDate1 = ParamDate1;
    }

    
    @Column(name="ParamBoolean10")
    public Integer getParamBoolean10() {
        return this.ParamBoolean10;
    }
    
    public void setParamBoolean10(Integer ParamBoolean10) {
        this.ParamBoolean10 = ParamBoolean10;
    }

    
    @Column(name="ParamBoolean9")
    public Integer getParamBoolean9() {
        return this.ParamBoolean9;
    }
    
    public void setParamBoolean9(Integer ParamBoolean9) {
        this.ParamBoolean9 = ParamBoolean9;
    }

    
    @Column(name="ParamBoolean8")
    public Integer getParamBoolean8() {
        return this.ParamBoolean8;
    }
    
    public void setParamBoolean8(Integer ParamBoolean8) {
        this.ParamBoolean8 = ParamBoolean8;
    }

    
    @Column(name="ParamBoolean7")
    public Integer getParamBoolean7() {
        return this.ParamBoolean7;
    }
    
    public void setParamBoolean7(Integer ParamBoolean7) {
        this.ParamBoolean7 = ParamBoolean7;
    }

    
    @Column(name="ParamBoolean6")
    public Integer getParamBoolean6() {
        return this.ParamBoolean6;
    }
    
    public void setParamBoolean6(Integer ParamBoolean6) {
        this.ParamBoolean6 = ParamBoolean6;
    }

    
    @Column(name="ParamBoolean5")
    public Integer getParamBoolean5() {
        return this.ParamBoolean5;
    }
    
    public void setParamBoolean5(Integer ParamBoolean5) {
        this.ParamBoolean5 = ParamBoolean5;
    }

    
    @Column(name="ParamBoolean4")
    public Integer getParamBoolean4() {
        return this.ParamBoolean4;
    }
    
    public void setParamBoolean4(Integer ParamBoolean4) {
        this.ParamBoolean4 = ParamBoolean4;
    }

    
    @Column(name="ParamBoolean3")
    public Integer getParamBoolean3() {
        return this.ParamBoolean3;
    }
    
    public void setParamBoolean3(Integer ParamBoolean3) {
        this.ParamBoolean3 = ParamBoolean3;
    }

    
    @Column(name="ParamBoolean2")
    public Integer getParamBoolean2() {
        return this.ParamBoolean2;
    }
    
    public void setParamBoolean2(Integer ParamBoolean2) {
        this.ParamBoolean2 = ParamBoolean2;
    }

    
    @Column(name="ParamBoolean1")
    public Integer getParamBoolean1() {
        return this.ParamBoolean1;
    }
    
    public void setParamBoolean1(Integer ParamBoolean1) {
        this.ParamBoolean1 = ParamBoolean1;
    }

    
    @Column(name="ParamFloat10")
    public float getParamFloat10() {
        return this.ParamFloat10;
    }
    
    public void setParamFloat10(float ParamFloat10) {
        this.ParamFloat10 = ParamFloat10;
    }

    
    @Column(name="ParamFloat9")
    public float getParamFloat9() {
        return this.ParamFloat9;
    }
    
    public void setParamFloat9(float ParamFloat9) {
        this.ParamFloat9 = ParamFloat9;
    }

    
    @Column(name="ParamFloat8")
    public float getParamFloat8() {
        return this.ParamFloat8;
    }
    
    public void setParamFloat8(float ParamFloat8) {
        this.ParamFloat8 = ParamFloat8;
    }

    
    @Column(name="ParamFloat7")
    public float getParamFloat7() {
        return this.ParamFloat7;
    }
    
    public void setParamFloat7(float ParamFloat7) {
        this.ParamFloat7 = ParamFloat7;
    }

    
    @Column(name="ParamFloat6")
    public float getParamFloat6() {
        return this.ParamFloat6;
    }
    
    public void setParamFloat6(float ParamFloat6) {
        this.ParamFloat6 = ParamFloat6;
    }

    
    @Column(name="ParamFloat5")
    public float getParamFloat5() {
        return this.ParamFloat5;
    }
    
    public void setParamFloat5(float ParamFloat5) {
        this.ParamFloat5 = ParamFloat5;
    }

    
    @Column(name="ParamFloat4")
    public float getParamFloat4() {
        return this.ParamFloat4;
    }
    
    public void setParamFloat4(float ParamFloat4) {
        this.ParamFloat4 = ParamFloat4;
    }

    
    @Column(name="ParamFloat3")
    public float getParamFloat3() {
        return this.ParamFloat3;
    }
    
    public void setParamFloat3(float ParamFloat3) {
        this.ParamFloat3 = ParamFloat3;
    }

    
    @Column(name="ParamFloat2")
    public float getParamFloat2() {
        return this.ParamFloat2;
    }
    
    public void setParamFloat2(float ParamFloat2) {
        this.ParamFloat2 = ParamFloat2;
    }

    
    @Column(name="ParamFloat1")
    public float getParamFloat1() {
        return this.ParamFloat1;
    }
    
    public void setParamFloat1(float ParamFloat1) {
        this.ParamFloat1 = ParamFloat1;
    }

    
    @Column(name="ParamInt10")
    public long getParamInt10() {
        return this.ParamInt10;
    }
    
    public void setParamInt10(long ParamInt10) {
        this.ParamInt10 = ParamInt10;
    }

    
    @Column(name="ParamInt9")
    public long getParamInt9() {
        return this.ParamInt9;
    }
    
    public void setParamInt9(long ParamInt9) {
        this.ParamInt9 = ParamInt9;
    }

    
    @Column(name="ParamInt8")
    public long getParamInt8() {
        return this.ParamInt8;
    }
    
    public void setParamInt8(long ParamInt8) {
        this.ParamInt8 = ParamInt8;
    }

    
    @Column(name="ParamInt7")
    public long getParamInt7() {
        return this.ParamInt7;
    }
    
    public void setParamInt7(long ParamInt7) {
        this.ParamInt7 = ParamInt7;
    }

    
    @Column(name="ParamInt6")
    public long getParamInt6() {
        return this.ParamInt6;
    }
    
    public void setParamInt6(long ParamInt6) {
        this.ParamInt6 = ParamInt6;
    }

    
    @Column(name="ParamInt5")
    public long getParamInt5() {
        return this.ParamInt5;
    }
    
    public void setParamInt5(long ParamInt5) {
        this.ParamInt5 = ParamInt5;
    }

    
    @Column(name="ParamInt4")
    public long getParamInt4() {
        return this.ParamInt4;
    }
    
    public void setParamInt4(long ParamInt4) {
        this.ParamInt4 = ParamInt4;
    }

    
    @Column(name="ParamInt3")
    public long getParamInt3() {
        return this.ParamInt3;
    }
    
    public void setParamInt3(long ParamInt3) {
        this.ParamInt3 = ParamInt3;
    }

    
    @Column(name="ParamInt2")
    public long getParamInt2() {
        return this.ParamInt2;
    }
    
    public void setParamInt2(long ParamInt2) {
        this.ParamInt2 = ParamInt2;
    }

    
    @Column(name="ParamInt1")
    public long getParamInt1() {
        return this.ParamInt1;
    }
    
    public void setParamInt1(long ParamInt1) {
        this.ParamInt1 = ParamInt1;
    }

    
    @Column(name="ParamStr10")
    public String getParamStr10() {
        return this.ParamStr10;
    }
    
    public void setParamStr10(String ParamStr10) {
        this.ParamStr10 = ParamStr10;
    }

    
    @Column(name="ParamStr9")
    public String getParamStr9() {
        return this.ParamStr9;
    }
    
    public void setParamStr9(String ParamStr9) {
        this.ParamStr9 = ParamStr9;
    }

    
    @Column(name="ParamStr8")
    public String getParamStr8() {
        return this.ParamStr8;
    }
    
    public void setParamStr8(String ParamStr8) {
        this.ParamStr8 = ParamStr8;
    }

    
    @Column(name="ParamStr7")
    public String getParamStr7() {
        return this.ParamStr7;
    }
    
    public void setParamStr7(String ParamStr7) {
        this.ParamStr7 = ParamStr7;
    }

    
    @Column(name="ParamStr6")
    public String getParamStr6() {
        return this.ParamStr6;
    }
    
    public void setParamStr6(String ParamStr6) {
        this.ParamStr6 = ParamStr6;
    }

    
    @Column(name="ParamStr5")
    public String getParamStr5() {
        return this.ParamStr5;
    }
    
    public void setParamStr5(String ParamStr5) {
        this.ParamStr5 = ParamStr5;
    }

    
    @Column(name="ParamStr4")
    public String getParamStr4() {
        return this.ParamStr4;
    }
    
    public void setParamStr4(String ParamStr4) {
        this.ParamStr4 = ParamStr4;
    }

    
    @Column(name="ParamStr3")
    public String getParamStr3() {
        return this.ParamStr3;
    }
    
    public void setParamStr3(String ParamStr3) {
        this.ParamStr3 = ParamStr3;
    }

    
    @Column(name="ParamStr2")
    public String getParamStr2() {
        return this.ParamStr2;
    }
    
    public void setParamStr2(String ParamStr2) {
        this.ParamStr2 = ParamStr2;
    }

    
    @Column(name="ParamStr1")
    public String getParamStr1() {
        return this.ParamStr1;
    }
    
    public void setParamStr1(String ParamStr1) {
        this.ParamStr1 = ParamStr1;
    }

    
    @Column(name="ErrorTest")
    public String getErrorTest() {
        return this.ErrorTest;
    }
    
    public void setErrorTest(String ErrorTest) {
        this.ErrorTest = ErrorTest;
    }

    
    @Column(name="ErrorCode")
    public String getErrorCode() {
        return this.ErrorCode;
    }
    
    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    
    @Column(name="Signature")
    public String getSignature() {
        return this.Signature;
    }
    
    public void setSignature(String Signature) {
        this.Signature = Signature;
    }

    
    @Column(name="ModeApp")
    public long getModeApp() {
        return this.ModeApp;
    }
    
    public void setModeApp(long ModeApp) {
        this.ModeApp = ModeApp;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EventDate")
    public Date getEventDate() {
        return this.EventDate;
    }
    
    public void setEventDate(Date EventDate) {
        this.EventDate = EventDate;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="EventTypeID")
    public KindEventClientProfile getEventTypeID_EN() {
        return this.EventTypeID_EN;
    }
    
    public void setEventTypeID_EN(KindEventClientProfile EventTypeID_EN) {
        this.EventTypeID_EN = EventTypeID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ClientProfileID")
    public ClientProfile getClientProfileID_EN() {
        return this.ClientProfileID_EN;
    }
    
    public void setClientProfileID_EN(ClientProfile ClientProfileID_EN) {
        this.ClientProfileID_EN = ClientProfileID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="TokenID")
    public ClientProfileToken getTokenID_EN() {
        return this.TokenID_EN;
    }
    
    public void setTokenID_EN(ClientProfileToken TokenID_EN) {
        this.TokenID_EN = TokenID_EN;
    }




}


