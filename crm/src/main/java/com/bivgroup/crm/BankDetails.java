package com.bivgroup.crm;
// Generated 21.03.2017 16:36:37 unknow unknow 


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * BankDetails Generated 21.03.2017 16:36:37 unknow unknow 
 */
@Entity
@Table(name="SD_BankDetails"
)
public class BankDetails  implements java.io.Serializable {


     private long ID;
     private String PurposeOfPayment;
     private String Account;
     private String BankAccount;
     private String BankINN;
     private String BankBIK;
     private String BankName;
     private long EID;

    public BankDetails() {
    }

    public BankDetails(String PurposeOfPayment, String Account, String BankAccount, String BankINN, String BankBIK, String BankName, long EID) {
       this.PurposeOfPayment = PurposeOfPayment;
       this.Account = Account;
       this.BankAccount = BankAccount;
       this.BankINN = BankINN;
       this.BankBIK = BankBIK;
       this.BankName = BankName;
       this.EID = EID;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_BankDetails"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="PurposeOfPayment")
    public String getPurposeOfPayment() {
        return this.PurposeOfPayment;
    }
    
    public void setPurposeOfPayment(String PurposeOfPayment) {
        this.PurposeOfPayment = PurposeOfPayment;
    }

    
    @Column(name="Account")
    public String getAccount() {
        return this.Account;
    }
    
    public void setAccount(String Account) {
        this.Account = Account;
    }

    
    @Column(name="BankAccount")
    public String getBankAccount() {
        return this.BankAccount;
    }
    
    public void setBankAccount(String BankAccount) {
        this.BankAccount = BankAccount;
    }

    
    @Column(name="BankINN")
    public String getBankINN() {
        return this.BankINN;
    }
    
    public void setBankINN(String BankINN) {
        this.BankINN = BankINN;
    }

    
    @Column(name="BankBIK")
    public String getBankBIK() {
        return this.BankBIK;
    }
    
    public void setBankBIK(String BankBIK) {
        this.BankBIK = BankBIK;
    }

    
    @Column(name="BankName")
    public String getBankName() {
        return this.BankName;
    }
    
    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
    }




}


