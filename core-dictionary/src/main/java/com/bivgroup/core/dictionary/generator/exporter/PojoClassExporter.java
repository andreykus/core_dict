package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.boot.Metadata;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bush on 11.11.2016.
 * Процессор экспорта классов представления метаданных словарной системы
 */
public class PojoClassExporter extends AbstractMappingExporter {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * название шаблона
     */
    private String templateName;
    /**
     * входной класс
     */
    private Object pojo;
    /**
     * метаданные БД
     */
    private Metadata md;
    /**
     * глобальные параметры
     */
    protected HibernateMappingGlobalSettings globalSettings = new HibernateMappingGlobalSettings();

    /**
     * Конструктор   Процессора экспорта классов представления метаданных словарной системы
     * @param lpc -  входные классы
     * @param md - метаданные БД
     * @param outputStream - выходной поток конфигурации
     */
    public PojoClassExporter(Object lpc, Metadata md, StringBuffer outputStream) {
        super(outputStream);
        this.pojo = lpc;
        this.md = md;
    }

    /**
     * используется в шаблонах
     *
     * @return - имя генератора
     */
    @Override
    public String getName() {
        return "hbm2java";
    }


    /**
     * установка контекста
     */
    @Override
    protected void setupContext() {
        getTemplateHelper().putInContext("hmgs", globalSettings);
        //TODO: this safe guard should be in the root templates instead for each variable they depend on.
        if (!getProperties().containsKey("ejb3")) {
            getProperties().put("ejb3", false);
        }
        if (!getProperties().containsKey("jdk5")) {
            getProperties().put("jdk5", false);
        }
        super.setupContext();
    }


    /**
     * глобалные параметры для генератора
     *
     * @param hgs - глобальные настройки экспорта
     */
    @Override
    public void setGlobalSettings(HibernateMappingGlobalSettings hgs) {
        this.globalSettings = hgs;
    }

    /**
     * получим имя шаблона
     *
     * @return - шаблон
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * установим имя шаблона
     *
     * @param templateName - имя шаблона
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * инициализация, установим имя шаблона
     *
     * @throws DictionaryException - исключение словарной системы
     */
    protected void init() throws DictionaryException {
        setTemplateName(TypeExport.CLASS.getNameTemplate());
    }

    /**
     * старт процесса генерации
     *
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public void doStart() throws DictionaryException {
        init();
        exportGeneralSettings();
    }

    /**
     * генерация
     */
    private void exportGeneralSettings() {
        Map<String, Object> addition = new HashMap<String, Object>();
        addition.put("pojo", pojo);
        //если нужны аннотации
        addition.put("ejb3", true);
        addition.put("jdk5", true);
        addition.put("md", md);
        addition.put("clazz", ((POJOClass) pojo).getDecoratedObject());
        TemplateProducer producer = new TemplateProducer(getTemplateHelper(), getArtifactCollector());
        producer.produce(addition, getTemplateName(), getOutputStream(), getTemplateName(), pojo.toString());
    }


}
