package com.bivgroup.core.aspect.xml;


import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.bean.Aspects;
import com.bivgroup.core.aspect.bean.AspectsOnEntity;
import com.bivgroup.core.aspect.bean.AspectsOnModule;
import com.bivgroup.core.aspect.exceptions.AspectException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.*;


/**
 * Обработчик XML аспектов
 * Created by bush on 08.12.2016.
 */
public class XMLProcessorImpl {
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Записать аспекты
     *
     * @param aspects - аспекты
     * @return - xml аспекта
     * @throws AspectException - Исключение для модуля аспекты
     */
    public OutputStream marshalingAspects(Aspects aspects) throws AspectException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(aspects.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(aspects, out);
            return out;
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * получить аспекты
     *
     * @param in - xml для разбора
     * @return - аспекты
     * @throws AspectException - Исключение для модуля аспекты
     */
    public Aspects unmarshalingAspects(InputStream in) throws AspectException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Aspects.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Aspects) jaxbUnmarshaller.unmarshal(new StreamSource(in));
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Записать аспекты на модуле
     *
     * @param aspects - аспекты на модуле
     * @return - xml аспекта
     * @throws AspectException - Исключение для модуля аспекты
     */
    public OutputStream marshalingCollections(AspectsOnModule aspects) throws AspectException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Aspects.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(aspects, out);
            return out;
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Получить аспекты на модуле
     *
     * @param in - xml для разбора
     * @return - аспекты на модуле
     * @throws AspectException - Исключение для модуля аспекты
     */
    public AspectsOnModule unmarshalingCollections(InputStream in) throws AspectException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AspectsOnModule.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<AspectsOnModule> el = (JAXBElement<AspectsOnModule>) jaxbUnmarshaller.unmarshal(new StreamSource(in), AspectsOnModule.class);
            return el.getValue();
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Записать конфиг аспекта
     *
     * @param aspects - конфиг аспекта
     * @return - xml аспекта
     * @throws AspectException - Исключение для модуля аспекты
     */
    public OutputStream marshalingConfig(AspectCfg aspects) throws AspectException {
        try {
            JAXBElement<AspectCfg> jaxbElement = (new XMLObjectFactory()).createCfg(aspects);
            JAXBContext jaxbContext = JAXBContext.newInstance(Aspects.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(jaxbElement, out);
            return out;
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

    /**
     * Получить конфиг аспекта
     *
     * @param in - xml для разбора
     * @return - конфиг аспекта
     * @throws AspectException - Исключение для модуля аспекты
     */
    public AspectCfg unmarshalingConfig(InputStream in) throws AspectException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AspectsOnEntity.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<AspectCfg> el = (JAXBElement<AspectCfg>) jaxbUnmarshaller.unmarshal(new StreamSource(in), AspectCfg.class);
            return el.getValue();
        } catch (JAXBException ex) {
            throw new AspectException(ex);
        }
    }

}
