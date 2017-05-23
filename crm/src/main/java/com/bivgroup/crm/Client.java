package com.bivgroup.crm;
// Generated 21.03.2017 16:36:12 unknow unknow 


import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Client Generated 21.03.2017 16:36:12 unknow unknow 
 */
@Entity
@Table(name="CDM_Client"
)
public class Client  implements java.io.Serializable {


     private long ID;
     private long EID;
     private Collection<ClientAddress> Addresses = new ArrayList<ClientAddress>(0);
     private Collection<ClientDocument> Documents = new ArrayList<ClientDocument>(0);
     private Collection<ClientContact> Contacts = new ArrayList<ClientContact>(0);
     private Collection<Person> Persons = new ArrayList<Person>(0);

    public Client() {
    }

    public Client(long EID, Collection<ClientAddress> Addresses, Collection<ClientDocument> Documents, Collection<ClientContact> Contacts, Collection<Person> Persons) {
       this.EID = EID;
       this.Addresses = Addresses;
       this.Documents = Documents;
       this.Contacts = Contacts;
       this.Persons = Persons;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="CDM_Client"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
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

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID", updatable=false)
    public Collection<ClientAddress> getAddresses() {
        return this.Addresses;
    }
    
    public void setAddresses(Collection<ClientAddress> Addresses) {
        this.Addresses = Addresses;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID", updatable=false)
    public Collection<ClientDocument> getDocuments() {
        return this.Documents;
    }
    
    public void setDocuments(Collection<ClientDocument> Documents) {
        this.Documents = Documents;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID", updatable=false)
    public Collection<ClientContact> getContacts() {
        return this.Contacts;
    }
    
    public void setContacts(Collection<ClientContact> Contacts) {
        this.Contacts = Contacts;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="ClientID", updatable=false)
    public Collection<Person> getPersons() {
        return this.Persons;
    }
    
    public void setPersons(Collection<Person> Persons) {
        this.Persons = Persons;
    }




}


