package com.bivgroup.core.aspect.xml;

import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.bean.Aspects;
import com.bivgroup.core.aspect.bean.AspectsOnEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * Created by bush on 13.12.2016.
 * Фабрика создания объектов аспекта
 */
@XmlRegistry
public class XMLObjectFactory {
    /**логгер*/
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**название поля для aspectCfg*/
    private QName CFG_QNAME ;
    /**название поля для aspectsOnEntity*/
    private QName ENTITY_QNAME ;

    /**
     * Конструктор Фабрика создания объектов аспекта
     */
    public XMLObjectFactory() {
        this.CFG_QNAME = new QName("", "aspectCfg");
        this.ENTITY_QNAME = new QName("", "aspectsOnEntity");
    }

    /**
     * создать пустой аспект
     *
     * @return - аспект
     */
    public Aspects createAspects() {
        return new Aspects();
    }

    /**
     * создать пустой конфиг аспекта
     *
     * @return - конфигурация аспекта
     */
    public AspectCfg createAspectCfg() {
        return new AspectCfg();
    }

    /**
     * создать пустой аспект на сущности
     *
     * @return - аспект на сущности
     */
    public AspectsOnEntity createAspectsOnEntity() {
        return new AspectsOnEntity();
    }

    /**
     * созать xml элемент конфига аспекта
     *
     * @param value - конфигурация аспекта
     * @return - конфигурация аспекта
     */
    @XmlElementDecl(namespace = "", name = "aspectCfg")
    public JAXBElement<AspectCfg> createCfg(AspectCfg value) {
        return new JAXBElement<AspectCfg>(CFG_QNAME, AspectCfg.class, null, value);
    }

    /**
     * созать xml элемент аспекта на сущности
     *
     * @param value - аспект на сущности
     * @return  - аспект на сущности
     */
    @XmlElementDecl(namespace = "", name = "aspectsOnEntity")
    public JAXBElement<AspectsOnEntity> createEntity(AspectsOnEntity value) {
        return new JAXBElement<AspectsOnEntity>(CFG_QNAME, AspectsOnEntity.class, null, value);
    }


}
