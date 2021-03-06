//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.15 at 03:12:33 PM GMT+03:00 
//


package com.bivgroup.core.aspect.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VersionAspect complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VersionAspect"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://bivgroup.aspect.com/}Aspect"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="versionEntityName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="versionEntityIdFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="versionEntityNumberFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lockVersionFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lastVersionIdFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="lastVersionTimeFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="versionNumberParamName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VersionAspect", propOrder = {
    "versionEntityName",
    "versionEntityIdFieldName",
    "versionEntityNumberFieldName",
    "lockVersionFieldName",
    "lastVersionIdFieldName",
    "lastVersionTimeFieldName",
    "versionNumberParamName"
})
public class VersionAspect
    extends Aspect
{

    @XmlElement(required = true)
    protected String versionEntityName;
    @XmlElement(required = true)
    protected String versionEntityIdFieldName;
    @XmlElement(required = true)
    protected String versionEntityNumberFieldName;
    @XmlElement(required = true)
    protected String lockVersionFieldName;
    @XmlElement(required = true)
    protected String lastVersionIdFieldName;
    @XmlElement(required = true)
    protected String lastVersionTimeFieldName;
    @XmlElement(required = true)
    protected String versionNumberParamName;

    /**
     * Gets the value of the versionEntityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionEntityName() {
        return versionEntityName;
    }

    /**
     * Sets the value of the versionEntityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionEntityName(String value) {
        this.versionEntityName = value;
    }

    /**
     * Gets the value of the versionEntityIdFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionEntityIdFieldName() {
        return versionEntityIdFieldName;
    }

    /**
     * Sets the value of the versionEntityIdFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionEntityIdFieldName(String value) {
        this.versionEntityIdFieldName = value;
    }

    /**
     * Gets the value of the versionEntityNumberFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionEntityNumberFieldName() {
        return versionEntityNumberFieldName;
    }

    /**
     * Sets the value of the versionEntityNumberFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionEntityNumberFieldName(String value) {
        this.versionEntityNumberFieldName = value;
    }

    /**
     * Gets the value of the lockVersionFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLockVersionFieldName() {
        return lockVersionFieldName;
    }

    /**
     * Sets the value of the lockVersionFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLockVersionFieldName(String value) {
        this.lockVersionFieldName = value;
    }

    /**
     * Gets the value of the lastVersionIdFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastVersionIdFieldName() {
        return lastVersionIdFieldName;
    }

    /**
     * Sets the value of the lastVersionIdFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastVersionIdFieldName(String value) {
        this.lastVersionIdFieldName = value;
    }

    /**
     * Gets the value of the lastVersionTimeFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastVersionTimeFieldName() {
        return lastVersionTimeFieldName;
    }

    /**
     * Sets the value of the lastVersionTimeFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastVersionTimeFieldName(String value) {
        this.lastVersionTimeFieldName = value;
    }

    /**
     * Gets the value of the versionNumberParamName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNumberParamName() {
        return versionNumberParamName;
    }

    /**
     * Sets the value of the versionNumberParamName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNumberParamName(String value) {
        this.versionNumberParamName = value;
    }

}
