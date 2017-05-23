package com.bivgroup.crm;
// Generated 21.03.2017 16:36:11 unknow unknow 


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
 * ClientProfile Generated 21.03.2017 16:36:11 unknow unknow 
 */
@Entity
@Table(name="SD_ClientProfile"
)
public class ClientProfile  implements java.io.Serializable {


     private long ID;
     private String Tel;
     private long EID;
     private Client ClientID_EN;
     private Collection<ClientProfileToken> Tokens = new ArrayList<ClientProfileToken>(0);
     private Collection<ClientProfileEvent> Events = new ArrayList<ClientProfileEvent>(0);

    public ClientProfile() {
    }

    public ClientProfile(String Tel, long EID, Client ClientID_EN, Collection<ClientProfileToken> Tokens, Collection<ClientProfileEvent> Events) {
       this.Tel = Tel;
       this.EID = EID;
       this.ClientID_EN = ClientID_EN;
       this.Tokens = Tokens;
       this.Events = Events;
    }
   
     @GenericGenerator(name="generator", strategy="enhanced-sequence", parameters={@Parameter(name="optimizer", value="pooled"), @Parameter(name="prefer_sequence_per_entity", value="true"), @Parameter(name="jpa_entity_name", value="SD_ClientProfile"), @Parameter(name="increment_size", value="10"), @Parameter(name="sequence_per_entity_suffix", value="_SEQ")})@Id @GeneratedValue(generator="generator")

    
    @Column(name="ID", nullable=false, insertable=false, updatable=false)
    public long getID() {
        return this.ID;
    }
    
    public void setID(long ID) {
        this.ID = ID;
    }

    
    @Column(name="Tel")
    public String getTel() {
        return this.Tel;
    }
    
    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    
    @Column(name="EID")
    public long getEID() {
        return this.EID;
    }
    
    public void setEID(long EID) {
        this.EID = EID;
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
    @JoinColumn(name="ClientProfileID", updatable=false)
    public Collection<ClientProfileToken> getTokens() {
        return this.Tokens;
    }
    
    public void setTokens(Collection<ClientProfileToken> Tokens) {
        this.Tokens = Tokens;
    }

@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name="ClientProfileID", updatable=false)
    public Collection<ClientProfileEvent> getEvents() {
        return this.Events;
    }
    
    public void setEvents(Collection<ClientProfileEvent> Events) {
        this.Events = Events;
    }




}


