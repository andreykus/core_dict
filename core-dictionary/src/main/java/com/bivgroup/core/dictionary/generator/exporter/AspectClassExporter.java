package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.core.aspect.bean.*;
import com.bivgroup.core.aspect.xml.XMLProcessorImpl;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadEntityAspect;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 09.12.2016.
 * Процессор экспорта аспектов
 */
public class AspectClassExporter implements Exporter {
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /** выходной поток сгененрированного описания аспектов */
    private StringBuffer outputStream;
    /** список сущностей */
    private Object listEnt;
    /** наименование  модуля*/
    private String moduleName;

    /**
     * Конструктор процессора экспорта аспектов
     * @param lent - список сущностей
     * @param outputStream - выходной поток сгененрированного описания аспектов
     */
    public AspectClassExporter(Object lent, StringBuffer outputStream) {
        this.outputStream = outputStream;
        this.listEnt = lent;
    }

    /**
     * установить имя модуля
     *
     * @param moduleName -  модуль
     */
    @Override
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * получить имя модуля
     *
     * @return - модуль
     */
    @Override
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Аспекты на модуле
     *
     * @return - аспекты
     * @throws DictionaryException - исключение словарной системы
     */
    private Aspects getAspectsOnModule() throws DictionaryException {
        if (listEnt instanceof List) return getAspectsOnModuleList(new Aspects(), moduleName, listEnt);
        else return getAspectsOnModuleMap();
    }

    /**
     * Аспекты, если listEnt это Map (название модуля, список сущностей)
     *
     * @return - аспекты
     * @throws DictionaryException - исключение словарной системы
     */
    private Aspects getAspectsOnModuleMap() throws DictionaryException {
        Map<String, List<SadEntity>> mapList = (Map<String, List<SadEntity>>) listEnt;
        Aspects out = new Aspects();
        for (Map.Entry<String, List<SadEntity>> entry : mapList.entrySet()) {
            out = getAspectsOnModuleList(out, entry.getKey(), entry.getValue());
        }
        return out;
    }

    /**
     * Аспекты на модуле , если listEnt это listEnt
     *
     * @param out        - аспекты, к ним добавляются найденные
     * @param moduleName - модуль
     * @param listEnt    - список сущностей
     * @return - аспекты
     * @throws DictionaryException - исключение словарной системы
     */
    private Aspects getAspectsOnModuleList(Aspects out, String moduleName, Object listEnt) throws DictionaryException {
        List<SadEntity> list = (List<SadEntity>) listEnt;
        AspectsOnModule col = new AspectsOnModule();
        col.setNameModule(moduleName);
        List<AspectsOnEntity> listAspect = col.getAspectsOnEntity();
        for (SadEntity entity : list) {
            AspectsOnEntity aspects = getAspectsOnEntity(entity);
            if (aspects == null || aspects.getAspectCfg() == null || aspects.getAspectCfg().isEmpty()) continue;
            listAspect.add(aspects);
        }
        if (!col.getAspectsOnEntity().isEmpty()) {
            out.getAspectsOnModule().add(col);
        }
        return out;
    }

    /**
     * Поля на аспекте, добавляются на конфигураторе аспекта
     *
     * @param cfg    - конфигурация аспекта
     * @param aspect - сущность аспекта
     */
    private void addfields(AspectCfg cfg, SadEntityAspect aspect) {
        if (cfg == null || aspect.getEntity() == null) return;
        List<Field> fields = cfg.getAspect().getField();
        for (SadAttribute attr : aspect.getEntity().getAttributes()) {
            if (attr.getAspect() != null && aspect.getId().equals(attr.getAspect().getId())) {
                Field field = new Field();
                field.setName(attr.getName());
                field.setFieldname(attr.getFieldname());
                field.setSysname(attr.getSysname());
                field.setIsVersiont(attr.getIsversiont());
                fields.add(field);
            }
        }
    }

    /**
     * Аспекты на сущности
     *
     * @param entity - сущность
     * @return -  аспекты на сужности
     * @throws DictionaryException - исключение словарной системы
     */
    private AspectsOnEntity getAspectsOnEntity(SadEntity entity) throws DictionaryException {
        AspectsOnEntity aspectsEnt = new AspectsOnEntity();
        aspectsEnt.setEntityName(entity.getSysname());
        List<AspectCfg> apectsCfg = aspectsEnt.getAspectCfg();
        for (SadEntityAspect aspect : entity.getAspects()) {
            if (aspect.getConfig() == null) continue;
            try {
                InputStream xml = aspect.getConfig().getAsciiStream();
                AspectCfg cfg = new XMLProcessorImpl().unmarshalingConfig(xml);
                addfields(cfg, aspect);
                apectsCfg.add(cfg);
            } catch (Exception ex) {
                logger.error(ex);
                //throw new DictionaryException(ex);
            }
        }
        return aspectsEnt;
    }

    /**
     * получить сгернеренную информацию
     *
     * @return - xml
     */
    public StringBuffer getOutputStream() {
        return this.outputStream;
    }

    /**
     * гененрация
     *
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public void start() throws DictionaryException {
        Aspects col = getAspectsOnModule();
        try {
            outputStream.append(new XMLProcessorImpl().marshalingAspects(col));
        } catch (Exception ex) {
            throw new DictionaryException(ex);
        }
    }

    @Override
    public void setGlobalSettings(HibernateMappingGlobalSettings hgs) {
        //do nothing
    }
}
