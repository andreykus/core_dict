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
 * <p>Java class for BinaryFile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryFile"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://bivgroup.aspect.com/}Aspect"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="objEntityName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="objEntityPKFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryFile", propOrder = {
    "objEntityName",
    "objEntityPKFieldName"
})
public class BinaryFile
    extends Aspect
{

    @XmlElement(required = true)
    protected String objEntityName;
    @XmlElement(required = true)
    protected String objEntityPKFieldName;

    /**
     * Gets the value of the objEntityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjEntityName() {
        return objEntityName;
    }

    /**
     * Sets the value of the objEntityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjEntityName(String value) {
        this.objEntityName = value;
    }

    /**
     * Gets the value of the objEntityPKFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjEntityPKFieldName() {
        return objEntityPKFieldName;
    }

    /**
     * Sets the value of the objEntityPKFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjEntityPKFieldName(String value) {
        this.objEntityPKFieldName = value;
    }

}