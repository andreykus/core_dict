package com.bivgroup.core.dictionary.generator.exporter;

import com.bivgroup.common.utils.ToolCore;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.tool.hbm2x.ExporterException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bush on 15.09.2016.
 * freeMarkerEngine генератор
 */
public class TemplateHelper {
    /** логгер */
    protected Logger logger = LogManager.getLogger(this.getClass());
    private String templatePrefix;
    /** выход сгененренного объекта*/
    private StringBuffer outputStream;
    /** конфигуратор freeMarkerEngine*/
    protected Configuration freeMarkerEngine;
    /** контекст*/
    protected SimpleHash context;
    /** утилита - методы для генератора */
    private ToolCore tool;

    /**
     * Конструктор обработчика шаблона
     */
    public TemplateHelper() {
        this.tool = new ToolCore();
    }

    /**
     * инициализация
     *
     * @param outputStream  -  куда выводим
     * @param templatePaths - путь к шаблону
     */
    public void init(StringBuffer outputStream, String[] templatePaths) {
        this.outputStream = outputStream;
        context = new SimpleHash(ObjectWrapper.BEANS_WRAPPER);
        freeMarkerEngine = new Configuration();
        List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
        //загрузим шаблоны
        for (int i = 0; i < templatePaths.length; i++) {
            File file = new File(templatePaths[i]);
            if (file.exists() && file.isDirectory()) {
                try {
                    loaders.add(new FileTemplateLoader(file));
                } catch (IOException e) {
                    throw new ExporterException("Problems with templatepath " + file, e);
                }
            } else {
                logger.warn("template path" + file + " either does not exist or is not a directory");
            }
        }
        loaders.add(new ClassTemplateLoader(this.getClass(), "/")); // the template names are like pojo/Somewhere so have to be a rooted classpathloader
        //установить шаблоны в freeMarkerEngine
        freeMarkerEngine.setTemplateLoader(new MultiTemplateLoader((TemplateLoader[]) loaders.toArray(new TemplateLoader[loaders.size()])));
    }

    /**
     * класс шаблонов
     */
    public class Templates {
    }

    /**
     * добавим параметр
     *
     * @param key   - ключ
     * @param value - значение
     */
    public void putInContext(String key, Object value) {
        logger.trace("putInContext " + key + "=" + value);
        if (value == null) throw new IllegalStateException("value must not be null for " + key);
        Object replaced = internalPutInContext(key, value);
        if (replaced != null) {
            logger.warn("Overwriting " + replaced + " when setting " + key + " to " + value + ".");
        }
    }

    /**
     * удалим параметр
     *
     * @param key      - ключ
     * @param expected - значение
     */
    public void removeFromContext(String key, Object expected) {
        logger.trace("removeFromContext " + key + "=" + expected);
        Object replaced = internalRemoveFromContext(key);
        //if (replaced == null) throw new IllegalStateException(key + " did not exist in template context.");
        /*if(replaced!=expected) { //FREEMARKER-TODO: how can i validate this ? or maybe not needed to validate since mutation is considered bad ?
            throw new IllegalStateException("expected " + key + " to be bound to " + expected + " but was to " + replaced);
        }*/
    }

    /**
     * Получить контекст
     *
     * @return контекст
     */
    protected SimpleHash getContext() {
        return context;
    }

    /**
     * Назначить контекст
     */
    public void setupContext() {
        getContext().put("module", tool.getModule(this.getClass()));
        getContext().put("version", tool.getVersion(this.getClass()));
        getContext().put("ctx", getContext()); //TODO: I would like to remove this, but don't know another way to actually get the list possible "root" keys for debugging.
        getContext().put("templates", new Templates());
        getContext().put("date", new SimpleDate(new Date(), TemplateDateModel.DATETIME));
    }

    /**
     * Положить в конеткст
     *
     * @param key   - ключ
     * @param value - значение
     * @return - модель для шаблона
     */
    protected Object internalPutInContext(String key, Object value) {
        TemplateModel model = null;
        try {
            model = getContext().get(key);
        } catch (TemplateModelException e) {
            throw new ExporterException("Could not get key " + key, e);
        }
        getContext().put(key, value);
        return model;
    }

    /**
     * Удалить из контекста
     *
     * @param key - ключ
     * @return - модель для шаблона
     */
    protected Object internalRemoveFromContext(String key) {
        TemplateModel model = null;
        try {
            model = getContext().get(key);
        } catch (TemplateModelException e) {
            throw new ExporterException("Could not get key " + key, e);
        }
        getContext().remove(key);
        return model;
    }

    /**
     * look up the template named templateName via the paths and print the content to the output
     * А вот и конечная генерация
     *
     * @param templateName -  название шаблона
     * @param output - назначение гененрации
     * @param rootContext - контекст
     */

    public void processTemplate(String templateName, Writer output, String rootContext) {
        if (rootContext == null) {
            rootContext = "Unknown context";
        }
        try {
            Template template = freeMarkerEngine.getTemplate(templateName);
            template.process(getContext(), output);
        } catch (IOException e) {
            throw new ExporterException("Error while processing " + rootContext + " with template " + templateName, e);
        } catch (TemplateException te) {
            throw new ExporterException("Error while processing " + rootContext + " with template " + templateName, te);
        } catch (Exception e) {
            throw new ExporterException("Error while processing " + rootContext + " with template " + templateName, e);
        }
    }


}
