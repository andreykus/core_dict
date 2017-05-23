package com.bivgroup.crm;
// Generated 21.03.2017 16:36:37 unknow unknow 


import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Person Generated 21.03.2017 16:36:37 unknow unknow 
 */
@Entity
@Table(name="CDM_Person"
)
public class Person  implements java.io.Serializable {


     private long ID;
     private long EID;
     private KindStatus StateID_EN;
     private KindCountry CountryID_EN;
     private BankDetails BankDetailsID_EN;
     private Client ClientID_EN;
     private Collection<PersonDocument> Documents = new ArrayList<PersonDocument>(0);
     private Collection<PersonAddress> Addresses = new ArrayList<PersonAddress>(0);
     private Collection<PersonContact> Contacts = new ArrayList<PersonContact>(0);

    public Person() {
    }

    public Person(long EID, KindStatus StateID_EN, KindCountry CountryID_EN, BankDetails BankDetailsID_EN, Client ClientID_EN, Collection<PersonDocument> Documents, Collection<PersonAddress> Addresses, Collection<PersonContact> Contacts) {
       this.EID = EID;
       this.StateID_EN = StateID_EN;
       this.CountryID_EN = CountryID_EN;
       this.BankDetailsID_EN = BankDetailsID_EN;
       this.ClientID_EN = ClientID_EN;
       this.Documents = Documents;
       this.Addresses = Addresses;
       this.Contacts = Contacts;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="CDM_Person"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
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
    @JoinColumn(name="CountryID")
    public KindCountry getCountryID_EN() {
        return this.CountryID_EN;
    }
    
    public void setCountryID_EN(KindCountry CountryID_EN) {
        this.CountryID_EN = CountryID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="BankDetailsID")
    public BankDetails getBankDetailsID_EN() {
        return this.BankDetailsID_EN;
    }
    
    public void setBankDetailsID_EN(BankDetails BankDetailsID_EN) {
        this.BankDetailsID_EN = BankDetailsID_EN;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID")
    public Client getClientID_EN() {
        return this.ClientID_EN;
    }
    
    public void setClientID_EN(Client ClientID_EN) {
        this.ClientID_EN = ClientID_EN;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="PersonID", updatable=false)
    public Collection<PersonDocument> getDocuments() {
        return this.Documents;
    }
    
    public void setDocuments(Collection<PersonDocument> Documents) {
        this.Documents = Documents;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="PersonID", updatable=false)
    public Collection<PersonAddress> getAddresses() {
        return this.Addresses;
    }
    
    public void setAddresses(Collection<PersonAddress> Addresses) {
        this.Addresses = Addresses;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="PersonID", updatable=false)
    public Collection<PersonContact> getContacts() {
        return this.Contacts;
    }
    
    public void setContacts(Collection<PersonContact> Contacts) {
        this.Contacts = Contacts;
    }




}


