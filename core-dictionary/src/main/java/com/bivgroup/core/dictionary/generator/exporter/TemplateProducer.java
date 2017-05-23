package com.bivgroup.core.dictionary.generator.exporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.ExporterException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by bush on 15.09.2016.
 * Непосредственный производитель генеренной информации
 */
public class TemplateProducer {
    /**
     * логгер
     */
    protected Logger logger = LogManager.getLogger(this.getClass());
    /**freeMarkerEngine генератор*/
    private final TemplateHelper th;
    /** выходные артефакты*/
    private ArtifactCollector ac;

    /**
     * Конструктор Непосредственный производитель генеренной информации
     * @param th - freeMarkerEngine генератор
     * @param ac - выходные артефакты
     */
    public TemplateProducer(TemplateHelper th, ArtifactCollector ac) {
        this.th = th;
        this.ac = ac;
    }

    /**
     * сгенерим и отправим назначению
     *
     * @param additionalContext - входные данные
     * @param templateName      - шаблон
     * @param destination       - назначение
     * @param identifier
     * @param rootContext       - контекст
     */
    public void produce(Map<String, Object> additionalContext, String templateName, StringBuffer destination, String identifier, String rootContext) {
        String tempResult = produceToString(additionalContext, templateName, rootContext);
        if (tempResult.trim().length() == 0) {
            logger.warn("Generated output is empty. Skipped creation for file " + destination);
            return;
        }
        try {
            destination.append(tempResult);
        } catch (Exception e) {
            throw new ExporterException("Error while writing result to file", e);
        }
    }

    /**
     * генерация
     *
     * @param additionalContext - входные данные
     * @param templateName      -  шаблон
     * @param rootContext       - контекст
     * @return - сгенерированная строка
     */
    private String produceToString(Map<String, Object> additionalContext, String templateName, String rootContext) {
        Map<String, Object> contextForFirstPass = additionalContext;
        //наши данные
        putInContext(th, contextForFirstPass);
        StringWriter tempWriter = new StringWriter();
        BufferedWriter bw = new BufferedWriter(tempWriter);
        // First run - writes to in-memory string
        //вызовем freeMarkerEngine генератор
        th.processTemplate(templateName, bw, rootContext);
        removeFromContext(th, contextForFirstPass);
        try {
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while flushing to string", e);
        }
        return tempWriter.toString();
    }

    /**
     * очистим контекс
     *
     * @param templateHelper -  freeMarkerEngine генератор
     * @param context        - контекст
     */
    private void removeFromContext(TemplateHelper templateHelper, Map<String, Object> context) {
        Iterator<Entry<String, Object>> iterator = context.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> element = iterator.next();
            templateHelper.removeFromContext((String) element.getKey(), element.getValue());
        }
    }

    /**
     * положим в контекст
     *
     * @param templateHelper - freeMarkerEngine генератор
     * @param context        - контекст
     */
    private void putInContext(TemplateHelper templateHelper, Map<String, Object> context) {
        Iterator<Entry<String, Object>> iterator = context.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> element = iterator.next();
            templateHelper.putInContext((String) element.getKey(), element.getValue());
        }
    }

}
