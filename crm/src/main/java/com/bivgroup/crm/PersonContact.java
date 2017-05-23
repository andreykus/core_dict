package com.bivgroup.crm;
// Generated 21.03.2017 16:36:44 unknow unknow 


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PersonContact Generated 21.03.2017 16:36:44 unknow unknow 
 */
@Entity
@Table(name="CDM_Person_Contact"
)
public class PersonContact extends com.bivgroup.crm.Contact implements java.io.Serializable {


     private KindStatus StateID_EN;
     private Person PersonID_EN;

    public PersonContact() {
    }

    public PersonContact(String Value, long EID, KindContact TypeID_EN, KindStatus StateID_EN, Person PersonID_EN) {
        super(Value, EID, TypeID_EN);        
       this.StateID_EN = StateID_EN;
       this.PersonID_EN = PersonID_EN;
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


