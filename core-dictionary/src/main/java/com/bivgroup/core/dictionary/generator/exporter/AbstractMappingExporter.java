package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.Cfg2JavaTool;
import org.hibernate.tool.hbm2x.ExporterException;
import org.hibernate.tool.hbm2x.ExporterSettings;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by bush on 15.09.2016.
 * шаблон мапинга экспорта
 */
abstract class AbstractMappingExporter implements Exporter {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * выходной поток
     */
    private StringBuffer outputStream;
    /**
     * пути к шаблонам
     */
    protected String[] templatePaths = new String[0];
    /**
     * построитель по шаблону
     */
    private TemplateHelper vh;
    /**
     * передаваемые в шаблон свойства
     */
    private Properties properties = new Properties();
    /**
     * выходные артефакты
     */
    private ArtifactCollector collector = new ArtifactCollector();
    /**
     * источник данных для гененрации
     */
    private Iterator<Map.Entry<Object, Object>> iterator;
    /**
     * утилита для экспорта xml
     */
    private Db2HbmTool c2h;
    /**
     * утилита для экспорта class
     */
    private Cfg2JavaTool c2j;
    /**
     * название модуля
     */
    private String moduleName;

    /**
     * Конструктор шаблон мапинга экспорта
     *
     * @param outputStream -  выходной поток данных
     */
    public AbstractMappingExporter(StringBuffer outputStream) {
        this.c2h = new Db2HbmTool();
        this.c2j = new Cfg2JavaTool();
        this.outputStream = outputStream;
    }

    /**
     * получить куда выводим
     *
     * @return - StringBuffer
     */
    public StringBuffer getOutputStream() {
        return this.outputStream;
    }

    /**
     * установить название модуля
     *
     * @param nameModule -  название модуля
     */
    @Override
    public void setModuleName(String nameModule) {
        this.moduleName = moduleName;
    }

    /**
     * полчить название модуля
     *
     * @return -  модуль
     */
    @Override
    public String getModuleName() {
        return moduleName;
    }

    /**
     * генерация
     *
     * @throws DictionaryException - исключение словарной системы
     */
    @Override
    public void start() throws DictionaryException {
        //генератор
        setTemplateHelper(new TemplateHelper());
        //шаблон
        setupTemplates();
        //контекст -  параметры
        setupContext();
        //генерация
        doStart();
        //очистим параметры
        cleanUpContext();
        //генератор может смениться
        setTemplateHelper(null);
        getArtifactCollector().formatFiles();
    }

    /**
     * шаблонный метод частного процессора
     *
     * @throws DictionaryException - исключение словарной системы
     */
    abstract protected void doStart() throws DictionaryException;

    /**
     * получить пути к шаблонам
     *
     * @return - массив аутей к шаблонам
     */
    public String[] getTemplatePaths() {
        return templatePaths;
    }

    /**
     * преобразование к строке
     *
     * @param a - объект
     * @return - строка
     */
    static String toString(Object[] a) {
        if (a == null)
            return "null";
        if (a.length == 0)
            return "[]";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            if (i == 0)
                buf.append('[');
            else
                buf.append(", ");

            buf.append(String.valueOf(a[i]));
        }
        buf.append("]");
        return buf.toString();
    }

    /**
     * установит для генератора параметры назначения, и путь к шаблонам
     */
    protected void setupTemplates() {
        if (logger.isDebugEnabled()) {
            logger.debug(getClass().getName() + " path: " + toString(templatePaths));
        }
        getTemplateHelper().init(getOutputStream(), templatePaths);
    }

    /**
     * установка контекста для шаблонного обработчика
     */
    protected void setupContext() {
        getTemplateHelper().setupContext();
        getTemplateHelper().putInContext("exporter", this);
        getTemplateHelper().putInContext("c2h", getCfg2HbmTool());
        getTemplateHelper().putInContext("c2j", getCfg2JavaTool());
        if (getOutputStream() != null) getTemplateHelper().putInContext("outputdir", getOutputStream());
        if (getTemplatePaths() != null) getTemplateHelper().putInContext("template_path", getTemplatePaths());
        if (getProperties() != null) {
            iterator = getProperties().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> element = iterator.next();
                String key = element.getKey().toString();
                Object value = transformValue(element.getValue());
                getTemplateHelper().putInContext(key, value);
                if (key.startsWith(ExporterSettings.PREFIX_KEY)) {
                    getTemplateHelper().putInContext(key.substring(ExporterSettings.PREFIX_KEY.length()), value);
                    if (key.endsWith(".toolclass")) {
                        try {
                            Class<?> toolClass = ReflectHelper.classForName(value.toString(), this.getClass());
                            Object object = toolClass.newInstance();
                            getTemplateHelper().putInContext(key.substring(ExporterSettings.PREFIX_KEY.length(), key.length() - ".toolclass".length()), object);
                        } catch (Exception e) {
                            throw new ExporterException("Exception when instantiating tool " + element.getKey() + " with " + value, e);
                        }
                    }
                }
            }
        }
        getTemplateHelper().putInContext("artifacts", collector);
    }

    /**
     * преобразователь текста в bool
     *
     * @param value - объект
     * @return
     */
    private Object transformValue(Object value) {
        if ("true".equals(value)) {
            return Boolean.TRUE;
        }
        if ("false".equals(value)) {
            return Boolean.FALSE;
        }
        return value;
    }

    /**
     * очистить контекст передаваемый в шаблон генератор
     */
    protected void cleanUpContext() {
        if (getProperties() != null) {
            iterator = getProperties().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> element = iterator.next();
                Object value = transformValue(element.getValue());
                String key = element.getKey().toString();
                if (key.startsWith(ExporterSettings.PREFIX_KEY)) {
                    getTemplateHelper().removeFromContext(key.substring(ExporterSettings.PREFIX_KEY.length()), value);
                }
                getTemplateHelper().removeFromContext(key, value);
            }
        }
        if (getOutputStream() != null) getTemplateHelper().removeFromContext("outputdir", getOutputStream());
        if (getTemplatePaths() != null) getTemplateHelper().removeFromContext("template_path", getTemplatePaths());
        getTemplateHelper().removeFromContext("exporter", this);
        getTemplateHelper().removeFromContext("artifacts", collector);
        getTemplateHelper().removeFromContext("c2h", getCfg2HbmTool());
        getTemplateHelper().removeFromContext("c2j", getCfg2HbmTool());
    }

    protected void setTemplateHelper(TemplateHelper vh) {
        this.vh = vh;
    }

    protected TemplateHelper getTemplateHelper() {
        return vh;
    }

    public ArtifactCollector getArtifactCollector() {
        return collector;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * название класса
     *
     * @return - название класса
     */
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * утилита для обработки в xml
     *
     * @return - утилита
     */
    public Db2HbmTool getCfg2HbmTool() {
        return c2h;
    }

    /**
     * утилита для обработки в class
     *
     * @return - утилита
     */
    public Cfg2JavaTool getCfg2JavaTool() {
        return c2j;
    }
}


