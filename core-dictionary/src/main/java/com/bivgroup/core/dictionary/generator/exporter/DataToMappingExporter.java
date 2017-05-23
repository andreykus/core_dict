package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.tool.hbm2x.HibernateMappingGlobalSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bush on 15.09.2016.
 * Процессор экспорта xml представления метаданных словарной системы
 */
public class DataToMappingExporter extends AbstractMappingExporter {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * название шаблона
     */
    private String templateName;
    /**
     * входные классы
     */
    private Object lpc;
    //List<PersistentClass>
    /**
     * глобальные параметры
     */
    protected HibernateMappingGlobalSettings globalSettings = new HibernateMappingGlobalSettings();

    /**
     * контекст по умолчанию - параметры конфигурации
     */
    @Override
    protected void setupContext() {
        super.setupContext();
        getTemplateHelper().putInContext("hmgs", globalSettings);
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
     * Конструктор Процессор экспорта xml представления метаданных словарной системы
     * @param lpc - входные объекты
     * @param outputStream -  выходной поток
     */
    public DataToMappingExporter(Object lpc, StringBuffer outputStream) {
        super(outputStream);
        this.lpc = lpc;
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
     * иницализация
     * установим началтный шаблон для генерации hbm файлов
     *
     * @throws DictionaryException - исключение словарной системы
     */
    protected void init() throws DictionaryException {
        setTemplateName(TypeExport.XML.getNameTemplate());
    }

    /**
     * генерация
     *
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public void doStart() throws DictionaryException {
        init();
        exportGeneralSettings();
    }

    /**
     * запуск производителя выходной информации
     */
    private void exportGeneralSettings() {
        Map<String, Object> addition = new HashMap<String, Object>();
        addition.put("clazzlist", lpc);
        //lpc - это входной список  PersistentClass
        TemplateProducer producer = new TemplateProducer(getTemplateHelper(), getArtifactCollector());
        producer.produce(addition, getTemplateName(), getOutputStream(), getTemplateName(), "General Settings");
    }

    /**
     * используется в шаблонах
     *
     * @return - имя генератора
     */
    @Override
    public String getName() {
        return "hbm2hbmxml";
    }

}
